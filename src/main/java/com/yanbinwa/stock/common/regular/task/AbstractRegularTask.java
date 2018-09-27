package com.yanbinwa.stock.common.regular.task;

import com.emotibot.middleware.utils.JsonUtils;
import com.yanbinwa.stock.common.constants.Constants;
import com.yanbinwa.stock.common.type.Period;
import com.yanbinwa.stock.common.type.PeriodType;
import com.yanbinwa.stock.utils.DTOUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractRegularTask implements RegularTask
{
    protected String taskName;
    
    protected String taskClass;
    
    /**
     * 设定执行周期，包括是否是周期，下一次运行的时间（不管是否为周期，都要有运行的时间，这个时间会不断更新），运行的时间窗口（分为每周和每天的窗口），运行的周期
     * 这里需要考虑只执行一次的如何处理？
     */
    protected Period period;
    
    protected int timeout;
    
    /**
     * 正规创建时使用的方法
     * 
     * @param taskName
     */
    public AbstractRegularTask(String taskName)
    {
        this.taskName = taskName;
        this.taskClass = this.getClass().getName();
        init();
    }
    
    /**
     * 这里应该是RegularManager在load task时才会调用的方法，这时需要将period设置为当前时间，并且调用
     * 
     * @param taskName
     * @param taskClass
     * @param period
     */
    public AbstractRegularTask(String taskName, String taskClass, Period period)
    {
        this.taskName = taskName;
        this.taskClass = taskClass;
        this.period = period;
        init();
    }
    
    /**
     * 这里应该是RegularManager在load task时才会调用的方法，这时需要将period设置为当前时间，并且调用
     * 
     * @param taskName
     * @param taskClass
     */
    public AbstractRegularTask(String taskName, String taskClass)
    {
        this.taskName = taskName;
        this.taskClass = taskClass;
        this.period = null;
        init();
    }
    
    @Override
    public boolean shouldExecute()
    {
        if (period == null)
        {
            return false;
        }
        return period.shouldExecute();
    }
    
    @Override
    public void run()
    {
        execute();
    }
    
    @Override
    public RegularTaskWarp getTaskWarp()
    {
        RegularTaskWarp taskWarp = new RegularTaskWarp();
        taskWarp.setTaskClass(this.getTaskClass());
        taskWarp.setTaskName(this.getTaskName());
        taskWarp.setUploadStr(this.createUploadStr());
        return taskWarp;
    }
    
    @Override
    public boolean isPeriodTask()
    {
        return this.period.getPeriodType() == PeriodType.PERIOD;
    }
    
    @Override
    public String createUploadStr()
    {
        return DTOUtils.toJsonStr(this);
    }

    protected void init() {
        if (period != null) {
            period.setNextTimestamp(Constants.PERIOD_DEFAULT_NEXT_TIMESTAMP);
        }
        preparePeriod();
        setTimeout();
    }

    public static <T extends AbstractRegularTask> T buildRegularTask(RegularTaskWarp regularTaskWarp, Class<T> clazz) {
        try {
//            Constructor taskConstructor = clazz.getConstructor(new Class[] {String.class});
            T regularTask = upLoad(clazz, regularTaskWarp.getUploadStr());
            regularTask.init();
            return regularTask;
        } catch (Exception e) {
            return null;
        }
    }

    public static <T extends AbstractRegularTask> T upLoad(Class<T> clazz, String uploadStr)
    {
        return (T) JsonUtils.getObject(uploadStr, clazz);
    }

    private void preparePeriod()
    {
        if (period == null)
        {
            period = this.generatePeriod();
        }
        if (period.getNextTimestamp() == Constants.PERIOD_DEFAULT_NEXT_TIMESTAMP)
        {
            period.setNextTime(true);
        }
        else if (period.getNextTimestamp() < System.currentTimeMillis())
        {
            period.setNextTime();
        }
    }
}
