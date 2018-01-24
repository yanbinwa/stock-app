package com.yanbinwa.stock.controller.collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yanbinwa.stock.service.collection.request.StockTrendRequest;
import com.yanbinwa.stock.service.collection.service.CollectionService;


@RestController
@RequestMapping("/collection")
public class CollectionController
{
    @Autowired
    CollectionService collectionService;
    
    /**
     * 这里可以输入多个stockId，以及开始时间和截止时间
     */
    @RequestMapping(value="/getStockTrend", method = RequestMethod.GET)
    public String getStockTrend(@RequestBody StockTrendRequest request)
    {
        return collectionService.getStockTrend(request);
    }
}
