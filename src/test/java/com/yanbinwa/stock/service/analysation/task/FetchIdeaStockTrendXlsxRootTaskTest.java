package com.yanbinwa.stock.service.analysation.task;

import com.emotibot.middleware.utils.TimeUtils;
import com.yanbinwa.stock.StockApplication;
import com.yanbinwa.stock.service.analysation.strategy.MaxProfitStrategy;
import com.yanbinwa.stock.service.analysation.strategy.StrategyXlsx;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = StockApplication.class)
@WebAppConfiguration
public class FetchIdeaStockTrendXlsxRootTaskTest {
    private StrategyXlsx strategy = getStrategy();

    @Test
    public void test() throws InterruptedException
    {
        Thread.sleep(1000);
        Date startDate = TimeUtils.getDateFromStr("20180601", "yyyyMMdd");
        Date endDate = TimeUtils.getDateFromStr("20180928", "yyyyMMdd");
        FetchIdeaStockTrendXlsxRootTask task = new FetchIdeaStockTrendXlsxRootTask("FetchIdeaStockTrendRootTask", startDate.getTime(), endDate.getTime(), strategy);
        task.execute();
        Thread.sleep(10000000);
    }

    private static StrategyXlsx getStrategy() {
        return new MaxProfitStrategy(0.3d);
    }
}
