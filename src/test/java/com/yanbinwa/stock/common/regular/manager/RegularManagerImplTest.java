package com.yanbinwa.stock.common.regular.manager;

import org.junit.Test;

import com.yanbinwa.stock.service.collection.task.CommissionIndustryCollectionTask;
import com.yanbinwa.stock.service.collection.task.IndustryToStocksCollectionTask;

public class RegularManagerImplTest
{
    
    @Test
    public void test() throws InterruptedException
    {
        RegularManager manager = new RegularManagerImpl();
        CommissionIndustryCollectionTask commissionIndustryCollector = new CommissionIndustryCollectionTask("Industry");
        manager.addRegularTask(commissionIndustryCollector);
        IndustryToStocksCollectionTask industryToStocksCollectionTask = new IndustryToStocksCollectionTask("Stock");
        manager.addRegularTask(industryToStocksCollectionTask);
        Thread.sleep(200000);
    }

}
