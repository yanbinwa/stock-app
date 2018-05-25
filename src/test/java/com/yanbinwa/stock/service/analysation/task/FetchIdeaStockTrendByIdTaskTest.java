package com.yanbinwa.stock.service.analysation.task;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.emotibot.middleware.utils.TimeUtils;
import com.yanbinwa.stock.StockApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = StockApplication.class)
@WebAppConfiguration
public class FetchIdeaStockTrendByIdTaskTest
{

    @Test
    public void test()
    {
        Date date = TimeUtils.getDateFromStr("20170101", "yyyyMMdd");
        FetchIdeaStockTrendByIdTask task = new FetchIdeaStockTrendByIdTask("FetchIdeaStockTrendByIdTask", "SZ200045", date.getTime(), System.currentTimeMillis());
        task.execute();
    }

}
