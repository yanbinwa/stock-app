package com.yanbinwa.stock.service.collection.task;

import com.yanbinwa.stock.common.collector.AbstractCollector;
import com.yanbinwa.stock.common.singleton.RegularManagerSingleton;
import com.yanbinwa.stock.common.type.Period;
import com.yanbinwa.stock.entity.stockTrend.StockTrendType;
import com.yanbinwa.stock.service.collection.utils.CollectionUtils;
import lombok.Data;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

/**
 * 所有股票当天数据获取的root任务
 *
 */
@Data
public class StockToStockTrendTodayHistoryRootTask extends AbstractCollector
{
    private static StockTrendType[] stockTrendTypes = {StockTrendType.TYPE_1D};
    
    public StockToStockTrendTodayHistoryRootTask(String taskName)
    {
        super(taskName);
    }

    @Override
    public void collectLogic() throws MalformedURLException, IOException
    {
        List<String> stockIdList = CollectionUtils.getAllStockId();
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

    @Override
    public Period generatePeriod()
    {
        return buildEmptyPeriod();
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
