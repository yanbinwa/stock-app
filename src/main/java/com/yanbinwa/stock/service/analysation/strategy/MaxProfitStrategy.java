package com.yanbinwa.stock.service.analysation.strategy;

import com.yanbinwa.stock.entity.stockTrend.StockTrend;
import com.yanbinwa.stock.service.collection.entity.StockMetaData;
import com.yanbinwa.stock.service.collection.utils.CollectionUtils;
import com.yanbinwa.stock.utils.StockTrendUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MaxProfitStrategy implements StrategyXlsx {

    private double targetProfitRate;

    public MaxProfitStrategy(double targetProfitRate) {
        this.targetProfitRate = targetProfitRate;
    }

    @Override
    public List<String> getIdealStockTrendList(List<StockTrend> stockTrendList) {
        if (stockTrendList == null || stockTrendList.isEmpty()) {
            return null;
        }
        double maxProfitRate = 0d;
        double lowPrice = Double.MAX_VALUE;
        stockTrendList = StockTrendUtils.sortStockTrendByTimestamp(stockTrendList);
        Date startDate = stockTrendList.get(0).getCreatedate();
        Date endDate = null;
        for (StockTrend stockTrend : stockTrendList) {
            if (stockTrend.getCurrentPrice() < lowPrice) {
                lowPrice = stockTrend.getCurrentPrice();
                startDate = stockTrend.getCreatedate();
            } else {
                double profitRate = (stockTrend.getCurrentPrice() - lowPrice) / lowPrice;

                if (profitRate > maxProfitRate) {
                    maxProfitRate = profitRate;
                    endDate = stockTrend.getCreatedate();
                }
            }
        }
        if (maxProfitRate < targetProfitRate) {
            return null;
        }

        if (endDate == null) {
            return null;
        }
        StockMetaData stockMetaData = CollectionUtils.getStockMetaData(stockTrendList.get(0).getStockId());
        List<String> content = new ArrayList<>();
        content.add(stockMetaData.getStockId());
        content.add(stockMetaData.getName());
        content.add(String.valueOf(stockMetaData.getVolume()));
        content.add(String.valueOf(maxProfitRate));
        content.add(String.valueOf(startDate));
        content.add(String.valueOf(endDate));
        return content;
    }

}
