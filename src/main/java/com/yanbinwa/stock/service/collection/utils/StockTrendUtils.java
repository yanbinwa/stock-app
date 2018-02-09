package com.yanbinwa.stock.service.collection.utils;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yanbinwa.stock.service.collection.dao.StockTrendDao;
import com.yanbinwa.stock.service.collection.entity.StockTrend;

@Component
public class StockTrendUtils
{
    public static StockTrendUtils stockTrendUtils;
    
    @Autowired
    StockTrendDao stockTrendDao;
    
    @PostConstruct
    public void init() 
    {    
        stockTrendUtils = this;
    } 
    
    public static void storeStockTrend(StockTrend stockTrend)
    {
        stockTrendUtils.stockTrendDao.save(stockTrend);
    }
    
    public static void storeStockTrend(List<StockTrend> stockTrendList)
    {
        stockTrendUtils.stockTrendDao.save(stockTrendList);
    }
}
