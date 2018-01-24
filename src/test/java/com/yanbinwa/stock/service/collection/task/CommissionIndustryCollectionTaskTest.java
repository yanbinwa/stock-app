package com.yanbinwa.stock.service.collection.task;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Test;

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
