package com.yanbinwa.stock.service.collection.task;

import com.yanbinwa.stock.StockApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = StockApplication.class)
@WebAppConfiguration
public class CurrentStockTrendRootTaskTest {
    @Test
    public void test() throws IOException, InterruptedException {
        CurrentStockTrendRootTask currentStockTrendRootTask = new CurrentStockTrendRootTask("currentStockTrendRootTask");
        currentStockTrendRootTask.setRange(6500);
        currentStockTrendRootTask.setStep(100);
        currentStockTrendRootTask.collectLogic();
        Thread.sleep(100000);
    }
}
