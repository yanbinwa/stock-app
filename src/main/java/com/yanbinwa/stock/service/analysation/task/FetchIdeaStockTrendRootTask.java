package com.yanbinwa.stock.service.analysation.task;

import com.yanbinwa.stock.common.analysation.AbstractAnalysation;
import com.yanbinwa.stock.common.singleton.RegularManagerSingleton;
import com.yanbinwa.stock.common.type.Period;
import com.yanbinwa.stock.common.type.PeriodType;
import com.yanbinwa.stock.service.analysation.strategy.Strategy;
import com.yanbinwa.stock.service.collection.utils.CollectionUtils;
import lombok.Data;

import java.util.List;

@Data
public class FetchIdeaStockTrendRootTask extends AbstractAnalysation
{
    private long startTimestamp;
    private long endTimestamp;
    private Strategy strategy;
    
    public FetchIdeaStockTrendRootTask(String taskName)
    {
        super(taskName);
    }
    
    public FetchIdeaStockTrendRootTask(String taskName, long startTimestamp, long endTimestamp, Strategy strategy)
    {
        super(taskName);
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.strategy = strategy;
    }

    @Override
    public void analysationLogic()
    {
        List<String> stockIdList = CollectionUtils.getAllStockId();
        stockIdList.stream().forEach(stockId -> {
            RegularManagerSingleton.getInstance().addRegularTask(
                    new FetchIdeaStockTrendByIdTask("FetchIdeaStockTrendById-" + startTimestamp + "-" + endTimestamp + "-" + stockId, stockId, startTimestamp, endTimestamp, strategy));
            try
            {
                Thread.sleep(30);
            } 
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        });
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
        public static final int TIMEOUT = 100000;
    }
}
