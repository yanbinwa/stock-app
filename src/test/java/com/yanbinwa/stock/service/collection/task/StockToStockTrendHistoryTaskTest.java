package com.yanbinwa.stock.service.collection.task;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.yanbinwa.stock.StockApplication;
import com.yanbinwa.stock.entity.stockTrend.StockTrendType;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = StockApplication.class)
@WebAppConfiguration
public class StockToStockTrendHistoryTaskTest
{

    @Test
    public void test()
    {
        //SZ300085
        StockToStockTrendHistoryTask task = new StockToStockTrendHistoryTask("StockToStockTrendHistoryTask", "SZ300085", StockTrendType.TYPE_1D);
        task.execute();
    }

}
