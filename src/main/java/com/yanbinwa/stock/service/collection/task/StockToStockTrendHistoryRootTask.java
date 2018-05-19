package com.yanbinwa.stock.service.collection.task;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import com.yanbinwa.stock.common.collector.AbstractCollector;
import com.yanbinwa.stock.common.singleton.RegularManagerSingleton;
import com.yanbinwa.stock.common.type.Period;
import com.yanbinwa.stock.common.type.PeriodType;
import com.yanbinwa.stock.entity.stockTrend.StockTrendType;
import com.yanbinwa.stock.service.collection.element.Industry;
import com.yanbinwa.stock.service.collection.utils.CollectionUtils;

public class StockToStockTrendHistoryRootTask extends AbstractCollector
{

    //private static StockTrendType[] stockTrendTypes = {StockTrendType.TYPE_1D, StockTrendType.TYPE_1W, StockTrendType.TYPE_1M};
    private static StockTrendType[] stockTrendTypes = {StockTrendType.TYPE_1D};
    
    private long startTimestamp;
    private long endTimestamp;
    
    public StockToStockTrendHistoryRootTask(String taskName)
    {
        super(taskName);
        this.startTimestamp = 0L;
        this.endTimestamp = System.currentTimeMillis();
    }
    
    public StockToStockTrendHistoryRootTask(String taskName, long startTimestamp, long endTimestamp)
    {
        super(taskName);
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
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
                    StockToStockTrendHistoryTask task = new StockToStockTrendHistoryTask("StockToStockTrendHistoryTask-" + stockTrendType + stockId, 
                            stockId, stockTrendType, startTimestamp, endTimestamp);
                    RegularManagerSingleton.getInstance().addRegularTask(task);
                    try
                    {
                        Thread.sleep(300);
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
        period.setPeriodType(PeriodType.NONE);
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
