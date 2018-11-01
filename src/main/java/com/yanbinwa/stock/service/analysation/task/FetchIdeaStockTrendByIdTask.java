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

import java.util.*;

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
        stockTrendList = StockTrendUtils.removeDuplateStockTrend(stockTrendList);
        stockTrendList = StockTrendUtils.sortStockTrendByTimestamp(stockTrendList);
        List<List<StockTrend>> chooseStockTrendLists = strategy.getIdealStockTrendList(stockTrendList);
        if (chooseStockTrendLists == null)
        {
            return;
        }
        //对于重复的数据（stockId和timestamp都是一致的）进行去重，方便画图
        chooseStockTrendLists = adjustChooseStockTrendLists(chooseStockTrendLists);
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

    private List<List<StockTrend>> adjustChooseStockTrendLists(List<List<StockTrend>> chooseStockTrendLists) {
        List<List<StockTrend>> ret = new ArrayList<>();
        for (List<StockTrend> stockTrends : chooseStockTrendLists) {
            Set<String> stockIdAndTimeSet = new HashSet<>();
            List<StockTrend> tmp = new ArrayList<>();
            for (StockTrend stockTrend : stockTrends) {
                if (stockIdAndTimeSet.contains(stockTrend.getStockId() + ":" + stockTrend.getCreatedate().getTime())) {
                    continue;
                }
                tmp.add(stockTrend);
                stockIdAndTimeSet.add(stockTrend.getStockId() + ":" + stockTrend.getCreatedate().getTime());
            }
            ret.add(tmp);
        }
        return ret;
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
