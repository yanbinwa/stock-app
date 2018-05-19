package com.yanbinwa.stock.service.collection.task;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.emotibot.middleware.utils.JsonUtils;
import com.yanbinwa.stock.common.collector.AbstractCollector;
import com.yanbinwa.stock.common.http.URLMapper;
import com.yanbinwa.stock.common.regular.task.AbstractRegularTask;
import com.yanbinwa.stock.common.singleton.RegularManagerSingleton;
import com.yanbinwa.stock.common.type.DayWindow;
import com.yanbinwa.stock.common.type.HourWindow;
import com.yanbinwa.stock.common.type.Period;
import com.yanbinwa.stock.common.type.PeriodType;
import com.yanbinwa.stock.common.utils.UnicodeUtils;
import com.yanbinwa.stock.service.collection.element.Industry;
import com.yanbinwa.stock.service.collection.utils.CollectionUtils;

/**
 * 这里的调用周期是每天一次，周一到周五，时间是早上8:00，将行业结果存入到redis中，之后有collector可以使用
 * 
 * 需要进行unicode解码
 * 
 * @author emotibot
 *
 */
public class CommissionIndustryCollectionTask extends AbstractCollector
{
    private static Logger logger = Logger.getLogger(CommissionIndustryCollectionTask.class);
    
    private static final DayWindow[] dayWindowArray = {DayWindow.MONDAY, DayWindow.TUESDAY, DayWindow.WEDNESDAY, DayWindow.THURSDAY, DayWindow.FRIDAY};
    private static final HourWindow[] hourWindowArray = {HourWindow.HOUR8};
    private static final int periodInterval = Period.SECOND_IN_DAY;
    
//    private static final DayWindow[] dayWindowArray = {};
//    private static final HourWindow[] hourWindowArray = {};
//    private static final int periodInterval = Period.SECOND_IN_MINUTE;
    
    private ReentrantLock lock = new ReentrantLock();
    
    public CommissionIndustryCollectionTask(String taskName)
    {
        super(taskName);
    }

    @Override
    public Period generatePeriod()
    {
        Period period = new Period();
        period.setPeriodType(PeriodType.PERIOD);
        period.setInterval(periodInterval);
        List<DayWindow> dayWindowList = new ArrayList<DayWindow>();
        Collections.addAll(dayWindowList, dayWindowArray);
        period.setDayWindowList(dayWindowList);
        List<HourWindow> hourWindowList = new ArrayList<HourWindow>();
        Collections.addAll(hourWindowList, hourWindowArray);
        period.setHourWindowList(hourWindowList);
        return period;
    }

    @Override
    public void setTimeout()
    {
        this.timeout = MyConstants.TIMEOUT;
    }  
    
    @Override
    public void collectLogic() throws MalformedURLException, IOException
    {
        Map<String, Industry> res = new HashMap<String, Industry>();
        String target = URLMapper.COMPREHENSIVE_PAGE.toString();
        String content = request(new URL(target));
        Document doc = Jsoup.parse(content);
        Elements element = doc.getElementsByClass("second-nav")
                .get(1).children()
                .get(2).children()
                .get(3).children()
                .select("a");
        StringBuilder builder = new StringBuilder();
        for (Element ele : element) 
        {
            if (!ele.hasAttr("title") || !ele.hasAttr("href")) 
            {
                continue;
            }
            builder.append(ele.attr("href"));
            Industry industry = new Industry(ele.attr("title"), UnicodeUtils.unicodeDecode(builder.toString()));
            res.put(industry.getIndustryName(), industry);
            builder.delete(0, builder.length());
        }
        //这里需要将结果对照之前redis中的结果，如果有出入，需要将新增和修改或者删除的task加入到TaskManager中
        updateCommissionIndustry(res);
    }
    
    /**
     * 或者redis中存放的数据，如何为空，则生成一个对应的
     * @param res
     */
    private void updateCommissionIndustry(Map<String, Industry> res)
    {
        Map<String, Industry> oldIndustryMap = CollectionUtils.getCommissionIndustry();
        if (oldIndustryMap == null)
        {
            oldIndustryMap = new HashMap<String, Industry>();
        }
        updateCommissionIndustryTask(res, oldIndustryMap);
        CollectionUtils.setCommissionIndustry(JsonUtils.getJsonStr(res));
    }
    
    private void updateCommissionIndustryTask(Map<String, Industry> newIndustryMap, Map<String, Industry> oldIndustryMap)
    {
        lock.lock();
        try
        {
            List<Industry> addList = new ArrayList<Industry>();
            List<Industry> deleteList = new ArrayList<Industry>();
            List<Industry> updateList = new ArrayList<Industry>();
            
            for(Map.Entry<String, Industry> entry : newIndustryMap.entrySet())
            {
                String industryName = entry.getKey();
                Industry industry = entry.getValue();
                if (!oldIndustryMap.containsKey(industryName))
                {
                    addList.add(industry);
                }
                else if(!industry.equals(oldIndustryMap.get(industryName)))
                {
                    updateList.add(industry);
                }
            }
            
            for (Map.Entry<String, Industry> entry : oldIndustryMap.entrySet())
            {
                String industryName = entry.getKey();
                Industry industry = entry.getValue();
                if (!newIndustryMap.containsKey(industryName))
                {
                    deleteList.add(industry);
                }
            }
            
            if (!addList.isEmpty() || !deleteList.isEmpty() || !updateList.isEmpty())
            {
                logger.info("add list: " + addList + "; delete list: " + deleteList + "; update list: " + updateList);
                updateRegularManagerTask(addList, deleteList, updateList);
            }
        }
        finally
        {
            lock.unlock();
        }
    }
    
    /**
     * 这里会生成IndustryToStocksCollectionTask
     * 
     * @param addList
     * @param deleteList
     * @param updateList
     */
    private void updateRegularManagerTask(List<Industry> addList, List<Industry> deleteList, List<Industry> updateList)
    {
        if (addList.size() > 0)
        {
            for (Industry industry : addList)
            {
                AbstractRegularTask task = new IndustryToStocksCollectionTask(industry);
                RegularManagerSingleton.getInstance().addRegularTask(task);
            }
        }
        
        if (updateList.size() > 0)
        {
            for (Industry industry : updateList)
            {
                AbstractRegularTask task = new IndustryToStocksCollectionTask(industry);
                RegularManagerSingleton.getInstance().updateRegularTask(task);
            }
        }
        
        if (deleteList.size() > 0)
        {
            for (Industry industry : deleteList)
            {
                RegularManagerSingleton.getInstance().deleteRegularTask(industry.getIndustryName(), IndustryToStocksCollectionTask.class.getName());
            }
        }
    }
    
    class MyConstants
    {
        public static final int TIMEOUT = 1000;
    }
}
