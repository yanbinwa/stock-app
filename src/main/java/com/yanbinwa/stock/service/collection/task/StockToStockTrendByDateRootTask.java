package com.yanbinwa.stock.service.collection.task;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import com.emotibot.middleware.utils.TimeUtils;
import com.yanbinwa.stock.common.collector.AbstractCollector;
import com.yanbinwa.stock.common.singleton.RegularManagerSingleton;
import com.yanbinwa.stock.common.type.Period;
import com.yanbinwa.stock.common.type.PeriodType;
import com.yanbinwa.stock.service.collection.element.Industry;
import com.yanbinwa.stock.service.collection.utils.CollectionUtils;

public class StockToStockTrendByDateRootTask extends AbstractCollector
{
    private long startTimestamp;
    private long endTimestamp;
    
    public StockToStockTrendByDateRootTask(String taskName, long startTimestamp, long endTimestamp)
    {
        super(taskName);
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
    }

    @Override
    public void collectLogic() throws MalformedURLException, IOException
    {
        //不处理时间跨度较大的case
        if ((endTimestamp - startTimestamp) % TimeUtils.MILLISECOND_IN_DAY > 30)
        {
            return;
        }
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
                StockToStockTrendByDateTask task = new StockToStockTrendByDateTask("StockToStockTrendByDateTask-" + startTimestamp + "-" + endTimestamp + "-" + stockId, 
                        stockId, TimeUtils.getDateListFromStartAndEndTimestamp(startTimestamp, endTimestamp));
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
