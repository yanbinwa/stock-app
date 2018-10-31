package com.yanbinwa.stock.service.analysation.filter;

import com.yanbinwa.stock.entity.stockTrend.StockTrend;

public class StockRiseRateFilter implements Filter {

    private double riseRate;

    @Override
    public boolean filter(StockTrend stockTrend) {
        if (stockTrend.getPercent() >= riseRate) {
            return true;
        } else {
            return false;
        }
    }
}
