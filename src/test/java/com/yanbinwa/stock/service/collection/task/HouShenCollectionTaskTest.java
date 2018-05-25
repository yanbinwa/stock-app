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
public class HouShenCollectionTaskTest
{
    public String taskString = "HouShen";
    
    @Test
    public void test() throws InterruptedException, MalformedURLException, IOException
    {
        Thread.sleep(1000);
        HouShenCollectionTask houShenCollectionTask = new HouShenCollectionTask(taskString);
        houShenCollectionTask.collectLogic();
        Thread.sleep(100000);
    }
}
