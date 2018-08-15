package com.yanbinwa.stock.common.regular.manager;

import com.emotibot.middleware.conf.ConfigManager;
import com.emotibot.middleware.utils.JsonUtils;
import com.emotibot.middleware.utils.StringUtils;
import com.google.gson.reflect.TypeToken;
import com.yanbinwa.stock.common.constants.Constants;
import com.yanbinwa.stock.common.regular.task.AbstractRegularTask;
import com.yanbinwa.stock.common.regular.task.RegularTaskWarp;
import com.yanbinwa.stock.common.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 需要标注哪些任务还在进行当中，这样就不会多次触发，在一个任务结束后首先判读是否为period服务，如果不是，就直接从taskMap中丢弃
 * 任务超时如何处理？？？这里要考虑，每种task均有自己的超时处理
 * 
 * 可以通过Future，然后调用Future中的V get(long timeout， TimeUnit unit)，这里的future应该包含了一些处理操作，包括将task加入到runningTaskMap，结束后是否从taskMap中丢弃等
 * 
 * @author emotibot
 *
 */
@Slf4j
public class RegularManagerImpl implements RegularManager
{
    private ExecutorService executorService = Executors.newFixedThreadPool(Constants.REGULAR_MANAGER_THREAD_POOL_SIZE);
    private Map<String, AbstractRegularTask> taskMap = new ConcurrentHashMap<String, AbstractRegularTask>();
    /* key为taskname，value为对于的future task */
    private Map<String, Future<?>> runningTaskMap = new ConcurrentHashMap<String, Future<?>>();
    /* key为taskname, value为该task过期的时间点 */
    private Map<String, Long> runningTaskTimeoutMap = new ConcurrentHashMap<String, Long>();
    private String taskStoreFile = ConfigManager.INSTANCE.getPropertyString(Constants.REGULAR_MANAGER_TASK_FILE_KEY);
    private String regularTaskStoreFile = ConfigManager.INSTANCE.getPropertyString(Constants.REGULAR_MANAGER_INTRINSIC_TASK_FILE_KEY);
    private Thread regularPollThread = null;
    private Thread checkTaskFinishThread = null;
    
    private ReentrantLock storeTaskLock = new ReentrantLock();
    
    public RegularManagerImpl()
    {
        init();
    }
    
    @Override
    public boolean addRegularTask(AbstractRegularTask task)
    {
        String taskKey = generateTaskKey(task);
        if (taskMap.containsKey(taskKey))
        {
            log.error(String.format("Task %s has already added", task.getTaskName()));
            return false;
        }
        log.info(String.format("Task %s is added", task.getTaskName()));
        taskMap.put(taskKey, task);
        storeTask();
        return true;
    }
    
    @Override
    public boolean updateRegularTask(AbstractRegularTask task)
    {
        String taskKey = generateTaskKey(task);
        if (!taskMap.containsKey(taskKey))
        {
            log.info(String.format("Task %s is not added yet, just add it", task.getTaskName()));
            taskMap.put(taskKey, task);
            return true;
        }
        log.info(String.format("Task %s is updated", task.getTaskName()));
        taskMap.put(taskKey, task);
        storeTask();
        return true;
    }

    @Override
    public boolean deleteRegularTask(String taskName, String taskClass)
    {
        String taskKey = generateTaskKey(taskName, taskClass);
        if (!taskMap.containsKey(taskKey))
        {
            log.error(String.format("Task %s is not added yet", taskName));
            return false;
        }
        log.info(String.format("Task %s is removed", taskName));
        taskMap.remove(taskKey);
        storeTask();
        return true;
    }
    
    /**
     * 这里查看taskStoreFile是否存在，如果存在，就从中读取task信息，生成相应的task，添加到taskMap中
     * 
     */
    private void init()
    {
        loadTask();
        
        regularPollThread = new Thread(() -> regularPoll());
        
        checkTaskFinishThread = new Thread(() -> checkTaskFinish());
        
        regularPollThread.start();
        checkTaskFinishThread.start();
    }
    
    private String generateTaskKey(AbstractRegularTask task)
    {
        return task.getTaskName() + "-" + task.getTaskClass();
    }
    
    private String generateTaskKey(String taskName, String taskClass)
    {
        return taskName + "-" + taskClass;
    }
    
    /**
     * 这里有两种task，一种是需要触发添加的，另一种是系统初始就需要的task
     * 
     * 同样具有两个task相关的文件，一个是task.txt，记录系统中所有的task，
     * 另一个是regularTask.txt, 记录系统启动时需要运行的task，所以task.txt中可能会包含regularTask.txt中的任务
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void loadTask()
    {
        taskMap.clear();
        String storeContext = FileUtils.readFile(taskStoreFile);
        if (StringUtils.isEmpty(storeContext))
        {
            log.info("can not load task map from " + taskStoreFile);
            return;
        }
        Type resultType = new TypeToken<List<RegularTaskWarp>>(){}.getType();
        List<RegularTaskWarp> taskWarpList = (List<RegularTaskWarp>) JsonUtils.getObject(storeContext, resultType);
        
        String regularStoreContext = FileUtils.readFile(regularTaskStoreFile);
        if (StringUtils.isEmpty(storeContext))
        {
            log.info("can not load task map from " + regularTaskStoreFile);
            return;
        }
        List<RegularTaskWarp> regularTaskWarpList = (List<RegularTaskWarp>) JsonUtils.getObject(regularStoreContext, resultType);
        for (RegularTaskWarp taskWarp : regularTaskWarpList)
        {
            if (!taskWarpList.contains(taskWarp))
            {
                taskWarpList.add(taskWarp);
            }
        }
        
        for (RegularTaskWarp taskWarp : taskWarpList)
        {
            try
            {
                AbstractRegularTask task = AbstractRegularTask.buildRegularTask(taskWarp, (Class<? extends AbstractRegularTask>) Class.forName(taskWarp.getTaskClass()));
                if (task == null) {
                    log.error("fail to upload task: " + taskWarp);
                    continue;
                }
                taskMap.put(generateTaskKey(task), task);
                log.debug("task [" + generateTaskKey(task) + "] has been add to taskMap");
            } 
            catch (Exception e)
            {
                e.printStackTrace();
                continue;
            } 
        }
    }
    
    private void storeTask()
    {
        storeTaskLock.lock();
        try
        {
            List<AbstractRegularTask> taskList = new ArrayList<AbstractRegularTask>(taskMap.values());
            List<RegularTaskWarp> taskWarpList = new ArrayList<RegularTaskWarp>();
            for (AbstractRegularTask task : taskList)
            {
                taskWarpList.add(task.getTaskWarp());
            }
            String storeContext = JsonUtils.getJsonStr(taskWarpList);
            boolean ret = FileUtils.writeFile(taskStoreFile, storeContext);
            if (!ret)
            {
                log.error("can not store task file");
            }
        }
        finally
        {
            storeTaskLock.unlock();
        }
    }
    
    /**
     * 这里每隔一定的时间会遍历taskMap，触发定时任务，这里对于task如何设置超时机制？可以在task设置起始时间，以及超时，并将正在运行的
     * task进行遍历，一旦超时，可以立即终止，所以RegularManager需要记录task的运行状态（taskName -> timestamp）
     */
    private void regularPoll()
    {
        while(true)
        {
            Map<String, AbstractRegularTask> taskMapTmp = new HashMap<String, AbstractRegularTask>(taskMap);
            for(Map.Entry<String, AbstractRegularTask> entry : taskMapTmp.entrySet())
            {
                String taskKey = entry.getKey();
                AbstractRegularTask task = entry.getValue();
                if (runningTaskMap.containsKey(taskKey))
                {
                    continue;
                }
                if (!task.shouldExecute())
                {
                    continue;
                }
                Future<?> future = executorService.submit(task);
                Long timeout = System.currentTimeMillis() + task.getTimeout();
                runningTaskMap.put(taskKey, future);
                runningTaskTimeoutMap.put(taskKey, timeout);
            }
            try
            {
                Thread.sleep(Constants.REGULAR_POLL_INTERVAL);
            } 
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * 如果执行结束或者timeout后，且是循环的task时，应该需要从runningTaskMap和runningTaskTimeoutMap中删除
     */
    private void checkTaskFinish()
    {
        while(true)
        {
            boolean isTaskRemoved = false;
            List<String> finishKeyList = new ArrayList<>();
            Map<String, Future<?>> runningTaskMapTmp = new HashMap<>(runningTaskMap);
            for(Map.Entry<String, Future<?>> entry : runningTaskMapTmp.entrySet())
            {
                String taskKey = entry.getKey();
                Future<?> future = entry.getValue();
                boolean isFinish = false;
                if (future.isDone() && !future.isCancelled())
                {
                    isFinish = true;
                }
                else
                {
                    long endTime = System.currentTimeMillis();
                    if (endTime > runningTaskTimeoutMap.get(taskKey))
                    {
                        future.cancel(true);
                        isFinish = true;
                    }
                }
                if (isFinish)
                {
                    finishKeyList.add(taskKey);  
                    AbstractRegularTask task = taskMap.get(taskKey);
                    if (task == null)
                    {
                        log.error("task is null: " + taskKey);
                        isTaskRemoved = true;
                    }
                    else if (task.isPeriodTask())
                    {
                        task.getPeriod().setNextTime();
                    }
                    else
                    {
                        taskMap.remove(taskKey);
                        isTaskRemoved = true;
                    }
                    runningTaskMap.remove(taskKey);
                    runningTaskTimeoutMap.remove(taskKey);
                }
            }
            if (isTaskRemoved)
            {
                storeTask();
            }
            try
            {
                Thread.sleep(Constants.REGULAR_CHECK_INTERVAL);
            } 
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}
