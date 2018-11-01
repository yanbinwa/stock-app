package com.yanbinwa.stock.utils;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.emotibot.middleware.utils.TimeUtils;
import com.yanbinwa.stock.StockApplication;
import com.yanbinwa.stock.entity.stockTrend.StockTrend;
import com.yanbinwa.stock.entity.stockTrend.StockTrendType;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = StockApplication.class)
@WebAppConfiguration
public class DrawStockTrendUtilsTest
{

    @Test
    public void test() throws ParseException, IOException
    {
        long endTimestamp = System.currentTimeMillis();
        long startTimestamp = endTimestamp - (long)TimeUtils.MILLISECOND_IN_DAY * 50;
        List<StockTrend> stockTrendList = StockTrendUtils.getStockTrendByDate(StockTrendType.TYPE_1D, "SZ002389", new Date(startTimestamp), new Date(endTimestamp));
        DrawStockTrendUtils.stockKChart(stockTrendList, "test");
    }

}
