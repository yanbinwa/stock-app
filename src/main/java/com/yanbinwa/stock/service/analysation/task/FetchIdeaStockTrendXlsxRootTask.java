package com.yanbinwa.stock.service.analysation.task;

import com.emotibot.middleware.utils.FileUtils;
import com.yanbinwa.stock.common.analysation.AbstractAnalysation;
import com.yanbinwa.stock.common.type.Period;
import com.yanbinwa.stock.common.type.PeriodType;
import com.yanbinwa.stock.entity.stockTrend.StockTrend;
import com.yanbinwa.stock.entity.stockTrend.StockTrendType;
import com.yanbinwa.stock.service.analysation.strategy.StrategyXlsx;
import com.yanbinwa.stock.service.collection.utils.CollectionUtils;
import com.yanbinwa.stock.utils.StockTrendUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FetchIdeaStockTrendXlsxRootTask extends AbstractAnalysation {

    public static final String FILE_PATH = "file/xlsx/stock.xlsx";

    private long startTimestamp;
    private long endTimestamp;
    private StrategyXlsx strategy;

    public FetchIdeaStockTrendXlsxRootTask(String taskName)
    {
        super(taskName);
    }

    public FetchIdeaStockTrendXlsxRootTask(String taskName, long startTimestamp, long endTimestamp, StrategyXlsx strategy)
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
        List<List<String>> contents = new ArrayList<>();
        for (String stockId : stockIdList) {
            List<StockTrend> stockTrendList = StockTrendUtils.getStockTrendByDate(StockTrendType.TYPE_1D, stockId,
                    new Date(startTimestamp), new Date(endTimestamp));
            List<String> content = strategy.getIdealStockTrendList(stockTrendList);
            if (content != null)
            {
                contents.add(content);
            }
        }
        FileUtils.writeLogForXls(FILE_PATH, contents);
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
        this.timeout = FetchIdeaStockTrendRootTask.MyConstants.TIMEOUT;
    }

    class MyConstants
    {
        public static final int TIMEOUT = 10000;
    }
}
