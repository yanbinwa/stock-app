package com.yanbinwa.stock.service.collection.task;

import java.io.IOException;
import java.net.MalformedURLException;

import com.yanbinwa.stock.common.collector.AbstractCollector;
import com.yanbinwa.stock.common.singleton.RegularManagerSingleton;
import com.yanbinwa.stock.common.type.Period;

public class CurrentStockTrendRootTask extends AbstractCollector
{
    private int range;
    private int step;
    
    public CurrentStockTrendRootTask(String taskName, int range, int step)
    {
        super(taskName);
        this.range = range;
        this.step = step;
    }

    @Override
    public void collectLogic() throws MalformedURLException, IOException
    {
        for(int i = 0; i < range; i += step)
        {
            int pageIndex = i % step + 1;
            CurrentStockTrendTask task = new CurrentStockTrendTask("CurrentStockTrendTask-" + pageIndex, pageIndex);
            RegularManagerSingleton.getInstance().addRegularTask(task);
            try
            {
                Thread.sleep(30);
            } 
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Period generatePeriod()
    {
        return null;
    }

    @Override
    public void setTimeout()
    {
        this.timeout = MyConstants.TIMEOUT;
    }

    class MyConstants
    {
        public static final int TIMEOUT = 1000;
    }
}
