package com.yanbinwa.stock.service.collection.task;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.yanbinwa.stock.StockApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = StockApplication.class)
@WebAppConfiguration
public class StockToStockTrendTodayHistoryRootTaskTest
{

    @Test
    public void test() throws InterruptedException
    {
        Thread.sleep(1000);
        StockToStockTrendTodayHistoryRootTask task = new StockToStockTrendTodayHistoryRootTask("StockToStockTrendTodayHistoryRootTask");
        task.execute();
        Thread.sleep(10000000);
    }

}
