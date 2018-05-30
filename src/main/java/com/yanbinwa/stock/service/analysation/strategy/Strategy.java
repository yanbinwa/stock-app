package com.yanbinwa.stock.service.analysation.strategy;

import java.util.List;

import com.yanbinwa.stock.entity.stockTrend.StockTrend;

public interface Strategy
{
    public List<List<StockTrend>> getIdealStockTrendList(List<StockTrend> stockTrendList);
}
