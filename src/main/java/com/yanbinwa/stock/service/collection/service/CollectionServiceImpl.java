package com.yanbinwa.stock.service.collection.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanbinwa.stock.service.collection.dao.StockTrendRawDao;
import com.yanbinwa.stock.service.collection.entity.StockTrendRaw;
import com.yanbinwa.stock.service.collection.request.StockTrendRequest;

/**
 * 这里接受用户调用，对于collection进行增删改查的操作，不做对于数据的查询工作
 * 
 * @author emotibot
 *
 */

@Service("collectionService")
public class CollectionServiceImpl implements CollectionService
{
    @Autowired
    StockTrendRawDao stockTrendRawDao;

    @Override
    public String getStockTrend(StockTrendRequest request)
    {
        StockTrendRaw stock = new StockTrendRaw();
        stock.setStockId("SZ300347");
        stock.setCreatedate(new Date());
        stock.setCurrentPrice(36.4);
        stockTrendRawDao.save(stock);
        return null;
    }

    
}
