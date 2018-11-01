package com.yanbinwa.stock.service.analysation.task;

import com.yanbinwa.stock.common.analysation.AbstractAnalysation;
import com.yanbinwa.stock.common.type.Period;
import com.yanbinwa.stock.common.type.PeriodType;
import com.yanbinwa.stock.entity.stockTrend.StockTrend;
import com.yanbinwa.stock.entity.stockTrend.StockTrendType;
import com.yanbinwa.stock.service.analysation.filter.Filter;
import com.yanbinwa.stock.service.analysation.strategy.Strategy;
import com.yanbinwa.stock.utils.DrawStockTrendUtils;
import com.yanbinwa.stock.utils.StockTrendUtils;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 需要获取所有股票的一定时间内的股票信息，做统一处理
 *
 * 通过FilterList来过滤获取的股票数据
 *
 */
@Data
public class FetchAllIdeaStockTrendTask extends AbstractAnalysation {

    private long startTimestamp;
    private long endTimestamp;
    private Strategy strategy;
    private List<Filter> filterList;
    private String stockId;

    public FetchAllIdeaStockTrendTask(String taskName, long startTimestamp, long endTimestamp, Strategy strategy, String stockId, List<Filter> filters) {
        super(taskName);
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.strategy = strategy;
        this.stockId = stockId;
        this.filterList = filters;
    }

    @Override
    public void analysationLogic() {
        //按照时间来获取股票数据
        List<StockTrend> stockTrendList = StockTrendUtils.getStockTrendByDate(StockTrendType.TYPE_1D, stockId,
                new Date(startTimestamp), new Date(endTimestamp));
        stockTrendList = StockTrendUtils.removeDuplateStockTrend(stockTrendList);
        List<StockTrend> adjustStockTrendList = new ArrayList<>();
        if (filterList != null && !filterList.isEmpty()) {
            for (StockTrend stockTrend : stockTrendList) {
                boolean isFiltered = true;
                for (Filter filter : filterList) {
                    if (!filter.filter(stockTrend)) {
                        isFiltered = false;
                        break;
                    }
                }
                if (isFiltered) {
                    adjustStockTrendList.add(stockTrend);
                }
            }
        }
        List<List<StockTrend>> chooseStockTrendLists = strategy.getIdealStockTrendList(stockTrendList);
        if (chooseStockTrendLists == null)
        {
            return;
        }
        //这里只返回一个List
        try
        {
            for (List<StockTrend> stockTrends : chooseStockTrendLists) {
                DrawStockTrendUtils.stockKChart(stockTrends, strategy.getClass().getSimpleName());
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public Period generatePeriod() {
        Period period = new Period();
        period.setPeriodType(PeriodType.NONE);
        return period;
    }

    @Override
    public void setTimeout() {
        this.timeout = FetchIdeaStockTrendRootTask.MyConstants.TIMEOUT;
    }

    class MyConstants
    {
        public static final int TIMEOUT = 100000;
    }
}
