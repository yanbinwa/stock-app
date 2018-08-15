package com.yanbinwa.stock.service.collection.task;

import com.yanbinwa.stock.StockApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.net.MalformedURLException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = StockApplication.class)
@WebAppConfiguration
public class CurrentStockTrendTaskTest
{
    private static final int pageIndex = 1;
    private static final int pageSize = 100;
    
    @Test
    public void test() throws InterruptedException, MalformedURLException, IOException
    {
        CurrentStockTrendTask currentStockTrendTask = new CurrentStockTrendTask("CurrentStockTrendTask=" + pageIndex, pageIndex, pageSize);
        currentStockTrendTask.collectLogic();
    }
}
