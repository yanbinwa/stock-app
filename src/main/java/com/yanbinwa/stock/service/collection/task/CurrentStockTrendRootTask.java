package com.yanbinwa.stock.service.collection.task;

import com.yanbinwa.stock.common.collector.AbstractCollector;
import com.yanbinwa.stock.common.singleton.RegularManagerSingleton;
import com.yanbinwa.stock.common.type.Period;
import com.yanbinwa.stock.common.type.PeriodType;
import lombok.Data;

import java.io.IOException;
import java.net.MalformedURLException;

@Data
public class CurrentStockTrendRootTask extends AbstractCollector
{
    private int range;
    private int step;

    public CurrentStockTrendRootTask(String taskName) {
        super(taskName);
    }

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
            int pageIndex = i / step + 1;
            CurrentStockTrendTask task = new CurrentStockTrendTask("CurrentStockTrendTask-" + pageIndex, pageIndex, step);
            RegularManagerSingleton.getInstance().addRegularTask(task);
            try
            {
                Thread.sleep(50);
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
        Period period = new Period();
        period.setPeriodType(PeriodType.NONE);
        return period;
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
