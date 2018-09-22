package com.yanbinwa.stock.service.collection.task;

import com.emotibot.middleware.utils.TimeUtils;
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
public class StockToStockTrendByDateRootTaskTest
{

    @Test
    public void test() throws MalformedURLException, IOException, InterruptedException
    {
        Thread.sleep(1000);
        long startTimestamp = TimeUtils.getDateFromStr("20180727", "yyyyMMdd").getTime();
        long endTimestamp = TimeUtils.getDateFromStr("20180920", "yyyyMMdd").getTime();
        StockToStockTrendByDateRootTask task = new StockToStockTrendByDateRootTask("StockToStockTrendByDateRootTask", startTimestamp, endTimestamp);
        task.collectLogic();
        Thread.sleep(200000);
    }

}
