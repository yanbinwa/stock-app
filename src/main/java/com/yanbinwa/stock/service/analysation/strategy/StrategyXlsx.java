package com.yanbinwa.stock.service.analysation.strategy;

import com.yanbinwa.stock.entity.stockTrend.StockTrend;

import java.util.List;

/**
 * 输出xlsx
 *
 */
public interface StrategyXlsx {
    List<List<String>> getIdealStockTrendList(List<StockTrend> stockTrendList);
}
