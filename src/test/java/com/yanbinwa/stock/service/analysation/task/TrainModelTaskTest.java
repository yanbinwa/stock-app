package com.yanbinwa.stock.service.analysation.task;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.yanbinwa.stock.StockApplication;
import com.yanbinwa.stock.entity.stockTrend.StockTrendType;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = StockApplication.class)
@WebAppConfiguration
public class TrainModelTaskTest
{

    public static final String startDateStr = "2017-01-01";
    public static final String endDateStr= "2017-04-01";
    public static final String dateFormatStr = "yyyy-MM-dd";
    
    @Test
    public void test() throws ParseException
    {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormatStr);
        TrainModelTask task = new TrainModelTask("TrainModelTask", StockTrendType.TYPE_1D, 30, 
                sdf.parse(startDateStr).getTime(), sdf.parse(endDateStr).getTime(), 0.03d);
        task.execute();
    }

}
