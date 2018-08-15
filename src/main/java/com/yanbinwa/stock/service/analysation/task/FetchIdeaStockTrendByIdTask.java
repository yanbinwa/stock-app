package com.yanbinwa.stock.service.analysation.task;

import com.yanbinwa.stock.common.analysation.AbstractAnalysation;
import com.yanbinwa.stock.common.type.Period;
import com.yanbinwa.stock.common.type.PeriodType;
import com.yanbinwa.stock.entity.stockTrend.StockTrend;
import com.yanbinwa.stock.entity.stockTrend.StockTrendType;
import com.yanbinwa.stock.service.analysation.strategy.Strategy;
import com.yanbinwa.stock.utils.DrawStockTrendUtils;
import com.yanbinwa.stock.utils.StockTrendUtils;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 这里是通过不同策略来选择股票的
 * 
 * @author emotibot
 *
 */
@Data
public class FetchIdeaStockTrendByIdTask extends AbstractAnalysation
{
    private String stockId;
    private long startTimestamp;
    private long endTimestamp;
    private Strategy strategy;

    public FetchIdeaStockTrendByIdTask(String taskName)
    {
        super(taskName);
    }

    public FetchIdeaStockTrendByIdTask(String taskName, String stockId, long startTimestamp, long endTimestamp, Strategy strategy)
    {
        super(taskName);
        this.stockId = stockId;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.strategy = strategy;
    }

    @Override
    public void analysationLogic()
    {
        List<StockTrend> stockTrendList = StockTrendUtils.getStockTrendByDate(StockTrendType.TYPE_1D, stockId, 
                new Date(startTimestamp), new Date(endTimestamp));
        List<List<StockTrend>> chooseStockTrendLists = strategy.getIdealStockTrendList(stockTrendList);
        if (chooseStockTrendLists == null)
        {
            return;
        }
        chooseStockTrendLists.stream().forEach(chooseStockTrendList -> {
            try
            {
                DrawStockTrendUtils.stockKChart(chooseStockTrendList);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        });
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
