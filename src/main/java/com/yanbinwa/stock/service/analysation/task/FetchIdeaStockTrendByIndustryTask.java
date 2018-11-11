package com.yanbinwa.stock.service.analysation.task;

import com.yanbinwa.stock.common.analysation.AbstractAnalysation;
import com.yanbinwa.stock.common.type.Period;
import com.yanbinwa.stock.entity.stockTrend.StockTrend;
import com.yanbinwa.stock.entity.stockTrend.StockTrendType;
import com.yanbinwa.stock.service.analysation.filter.Filter;
import com.yanbinwa.stock.service.analysation.strategy.Strategy;
import com.yanbinwa.stock.service.collection.utils.CollectionUtils;
import com.yanbinwa.stock.utils.DrawStockTrendUtils;
import com.yanbinwa.stock.utils.StockTrendUtils;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class FetchIdeaStockTrendByIndustryTask extends AbstractAnalysation {

    public static final String OTHER_INDUSTRY_NAME = "其他";

    private long startTimestamp;
    private long endTimestamp;
    private Strategy strategy;
    private List<Filter> filterList;
    private String stockId;

    public FetchIdeaStockTrendByIndustryTask(String taskName, long startTimestamp, long endTimestamp, Strategy strategy, String stockId, List<Filter> filters) {
        super(taskName);
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.strategy = strategy;
        this.stockId = stockId;
        this.filterList = filters;
    }


    @Override
    public Period generatePeriod() {
        return buildEmptyPeriod();
    }

    @Override
    public void setTimeout() {
        this.timeout = FetchIdeaStockTrendRootTask.MyConstants.TIMEOUT;
    }

    @Override
    public void analysationLogic() {
        List<StockTrend> stockTrendList = StockTrendUtils.getStockTrendByDate(StockTrendType.TYPE_1D, stockId,
                new Date(startTimestamp), new Date(endTimestamp));
        stockTrendList = StockTrendUtils.removeDuplateStockTrend(stockTrendList);
        Map<String, String> stockIdToIndustryMap = CollectionUtils.getStockIdToIndustryNameMap();
        Map<String, List<StockTrend>> industryNameToStockTrendListMap = stockTrendList.stream().
                collect(Collectors.groupingBy(stockTrend -> stockIdToIndustryMap.containsKey(stockTrend) ? stockIdToIndustryMap.get(stockTrend) : OTHER_INDUSTRY_NAME));
        for (String industryName : industryNameToStockTrendListMap.keySet()) {
            List<List<StockTrend>> chooseStockTrendLists = strategy.getIdealStockTrendList(industryNameToStockTrendListMap.get(industryName));
            if (chooseStockTrendLists == null)
            {
                return;
            }
            //这里只返回一个List
            try
            {
                for (List<StockTrend> stockTrends : chooseStockTrendLists) {
                    DrawStockTrendUtils.stockKChart(stockTrends, strategy.getClass().getSimpleName() + "byIndustry" + "/" + industryName);
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    class MyConstants
    {
        public static final int TIMEOUT = 100000;
    }
}
