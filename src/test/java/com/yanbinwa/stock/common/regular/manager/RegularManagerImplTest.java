package com.yanbinwa.stock.common.regular.manager;

import org.junit.Test;

import com.yanbinwa.stock.service.collection.task.CommissionIndustryCollectionTask;

public class RegularManagerImplTest
{

    public String taskString = "Industry";
    
    @Test
    public void test() throws InterruptedException
    {
        RegularManager manager = new RegularManagerImpl();
        CommissionIndustryCollectionTask commissionIndustryCollector = new CommissionIndustryCollectionTask(taskString);
        manager.addRegularTask(commissionIndustryCollector);
        Thread.sleep(200000);
    }

}
