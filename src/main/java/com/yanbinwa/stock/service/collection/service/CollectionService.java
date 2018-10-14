package com.yanbinwa.stock.service.collection.service;

import com.yanbinwa.stock.service.collection.request.FetchHistoryStockTrendRequest;
import com.yanbinwa.stock.service.collection.request.FetchStockTrendByDateRequest;
import com.yanbinwa.stock.service.collection.request.StockTrendRequest;

public interface CollectionService
{
    String getStockTrend(StockTrendRequest request);

    void fetchHistoryStockTrend(FetchHistoryStockTrendRequest fetchHistoryStockTrendRequest);

    void fetchStockTrendByDate(FetchStockTrendByDateRequest fetchStockTrendByDateRequest);

    void fetchIndustryInfo();

    void fetchStockMetaData();
}
