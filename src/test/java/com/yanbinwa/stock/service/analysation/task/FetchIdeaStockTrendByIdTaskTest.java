package com.yanbinwa.stock.service.analysation.task;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.emotibot.middleware.utils.TimeUtils;
import com.yanbinwa.stock.StockApplication;
import com.yanbinwa.stock.service.analysation.strategy.ContinueIncreaseStrategy;
import com.yanbinwa.stock.service.analysation.strategy.Strategy;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = StockApplication.class)
@WebAppConfiguration
public class FetchIdeaStockTrendByIdTaskTest
{
    private Strategy strategy = new ContinueIncreaseStrategy();
    
    @Test
    public void test()
    {
        Date startDate = TimeUtils.getDateFromStr("20180501", "yyyyMMdd");
        Date endDate = TimeUtils.getDateFromStr("20180528", "yyyyMMdd");
        FetchIdeaStockTrendByIdTask task = new FetchIdeaStockTrendByIdTask("FetchIdeaStockTrendByIdTask", "SH603320", startDate.getTime(), endDate.getTime(), strategy);
        task.execute();
    }

}
