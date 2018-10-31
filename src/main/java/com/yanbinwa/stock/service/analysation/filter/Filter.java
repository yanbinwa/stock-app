package com.yanbinwa.stock.service.analysation.filter;

import com.yanbinwa.stock.entity.stockTrend.StockTrend;

public interface Filter {
    boolean filter(StockTrend stockTrend);
}

