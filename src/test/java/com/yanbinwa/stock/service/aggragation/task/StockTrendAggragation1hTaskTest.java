package com.yanbinwa.stock.service.aggragation.task;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.yanbinwa.stock.StockApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = StockApplication.class)
@WebAppConfiguration
public class StockTrendAggragation1hTaskTest
{

    @Test
    public void test()
    {
        StockTrendAggragation1hTask task = new StockTrendAggragation1hTask("StockTrendAggragation1hTask");
        task.execute();
    }

}
