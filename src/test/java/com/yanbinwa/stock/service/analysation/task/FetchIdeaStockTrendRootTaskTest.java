package com.yanbinwa.stock.service.analysation.task;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.emotibot.middleware.utils.TimeUtils;
import com.yanbinwa.stock.StockApplication;
import com.yanbinwa.stock.service.analysation.strategy.Strategy;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = StockApplication.class)
@WebAppConfiguration
public class FetchIdeaStockTrendRootTaskTest
{

    private Strategy strategy = FetchIdeaStockTrendByIdTaskTest.getStrategy1();
    
    @Test
    public void test() throws InterruptedException
    {
        Thread.sleep(1000);
        Date startDate = TimeUtils.getDateFromStr("20180626", "yyyyMMdd");
        Date endDate = TimeUtils.getDateFromStr("20180726", "yyyyMMdd");
        FetchIdeaStockTrendRootTask task = new FetchIdeaStockTrendRootTask("FetchIdeaStockTrendRootTask", startDate.getTime(), endDate.getTime(), strategy);
        task.execute();
        Thread.sleep(10000000);
    }

}
