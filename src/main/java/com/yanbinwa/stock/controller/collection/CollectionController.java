package com.yanbinwa.stock.controller.collection;

import com.yanbinwa.stock.service.collection.request.FetchHistoryStockTrendRequest;
import com.yanbinwa.stock.service.collection.request.StockTrendRequest;
import com.yanbinwa.stock.service.collection.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/collection")
public class CollectionController
{
    @Autowired
    CollectionService collectionService;
    
    /**
     * 这里可以输入多个stockId，以及开始时间和截止时间
     */
    @PostMapping(value = "/getStockTrend")
    public String getStockTrend(@RequestBody StockTrendRequest request)
    {
        return collectionService.getStockTrend(request);
    }

    /**
     *
     */
    @PostMapping(value = "/fetchHistoryStockTrend")
    public void fetchHistoryStockTrend(@RequestBody FetchHistoryStockTrendRequest fetchHistoryStockTrendRequest) {
        collectionService.fetchHistoryStockTrend(fetchHistoryStockTrendRequest);
    }


    @PostMapping(value = "/fetchIndustryInfo")
    public void fetchIndustryInfo() {
        collectionService.fetchIndustryInfo();
    }
}
