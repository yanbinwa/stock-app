package com.yanbinwa.stock.service.analysation.comparer;

import com.yanbinwa.stock.entity.stockTrend.StockTrend;

import java.util.Comparator;

public class StockComparerByDate implements Comparator<StockTrend> {

    @Override
    public int compare(StockTrend o1, StockTrend o2) {
        if (o1.getCreatedate().getTime() > o2.getCreatedate().getTime()) {
            return 1;
        } else if (o1.getCreatedate().getTime() < o2.getCreatedate().getTime()) {
            return -1;
        }
        return 0;
    }
}
