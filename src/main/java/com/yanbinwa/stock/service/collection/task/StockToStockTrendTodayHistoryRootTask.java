package com.yanbinwa.stock.service.collection.task;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.yanbinwa.stock.common.collector.AbstractCollector;
import com.yanbinwa.stock.common.singleton.RegularManagerSingleton;
import com.yanbinwa.stock.common.type.DayWindow;
import com.yanbinwa.stock.common.type.HourWindow;
import com.yanbinwa.stock.common.type.Period;
import com.yanbinwa.stock.common.type.PeriodType;
import com.yanbinwa.stock.entity.stockTrend.StockTrendType;
import com.yanbinwa.stock.service.collection.element.Industry;
import com.yanbinwa.stock.service.collection.utils.CollectionUtils;
import lombok.Data;

@Data
public class StockToStockTrendTodayHistoryRootTask extends AbstractCollector
{
    private static final DayWindow[] dayWindowArray = {DayWindow.MONDAY, DayWindow.TUESDAY, DayWindow.WEDNESDAY, DayWindow.THURSDAY, DayWindow.FRIDAY};
    private static final HourWindow[] hourWindowArray = {HourWindow.HOUR16_FH};
    private static final int periodInterval = Period.SECOND_IN_DAY;
    
    private static StockTrendType[] stockTrendTypes = {StockTrendType.TYPE_1D};
    
    public StockToStockTrendTodayHistoryRootTask(String taskName)
    {
        super(taskName);
    }
    
    public StockToStockTrendTodayHistoryRootTask(String taskName, long startTimestamp, long endTimestamp)
    {
        super(taskName);
    }

    @Override
    public void collectLogic() throws MalformedURLException, IOException
    {
        Map<String, Industry> industryMap = CollectionUtils.getCommissionIndustry();
        if (industryMap == null)
        {
            return;
        }
        for (String industryName : industryMap.keySet())
        {
            List<String> stockIdList = CollectionUtils.getIndustryToStockId(industryName);
            if (stockIdList == null)
            {
                continue;
            }
            for (String stockId : stockIdList)
            {
                for (StockTrendType stockTrendType : stockTrendTypes)
                {
                    StockToStockTrendTodayHistoryTask task = new StockToStockTrendTodayHistoryTask("StockToStockTrendTodayHistoryTask-" + stockTrendType + stockId, 
                            stockId);
                    RegularManagerSingleton.getInstance().addRegularTask(task);
                    try
                    {
                        Thread.sleep(50);
                    } 
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                        return;
                    }
                }
            }
        }
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

    class MyConstants
    {
        public static final int TIMEOUT = 1000;
    }
}
