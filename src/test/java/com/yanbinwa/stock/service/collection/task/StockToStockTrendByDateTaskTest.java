package com.yanbinwa.stock.service.collection.task;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
public class StockToStockTrendByDateTaskTest
{

    @Test
    public void test() throws MalformedURLException, IOException
    {
        Date startDate = TimeUtils.getDateFromStr("20180928", "yyyyMMdd");
        Date endDate = TimeUtils.getDateFromStr("20181013", "yyyyMMdd");
        List<Date> dateList = TimeUtils.getDateListFromStartAndEndTimestamp(startDate.getTime(), endDate.getTime());
        StockToStockTrendByDateTask task = new StockToStockTrendByDateTask("StockToStockTrendByDateTask", "SZ000651", dateList);
        task.collectLogic();
    }

}
