package com.yanbinwa.stock.service.collection.task;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.yanbinwa.stock.StockApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = StockApplication.class)
@WebAppConfiguration
public class CommissionIndustryCollectionTaskTest
{

    public String taskString = "Industry";
    
    @Test
    public void test() throws MalformedURLException, IOException
    {
        CommissionIndustryCollectionTask commissionIndustryCollector = new CommissionIndustryCollectionTask(taskString);
        commissionIndustryCollector.collectLogic();
    }

}
