package com.yanbinwa.stock.service.collection.utils;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.yanbinwa.stock.StockApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = StockApplication.class)
@WebAppConfiguration
public class CollectionUtilsTest
{

    @Test
    public void test()
    {
        getStockMetaIds();
    }
    
    public void clearStockInfo()
    {
        CollectionUtils.deleteCommissionIndustry();
        CollectionUtils.deleteAllIndustryToStockId();
        CollectionUtils.deleteAllStockMetaData();
    }

    public void getStockMetaIds()
    {
        List<String> stockIds = CollectionUtils.getAllStockId();
        System.out.println(stockIds.size());
    }
}
