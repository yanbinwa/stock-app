package com.yanbinwa.stock.service.collection.task;

import com.yanbinwa.stock.common.collector.AbstractCollector;
import com.yanbinwa.stock.common.singleton.RegularManagerSingleton;
import com.yanbinwa.stock.common.type.Period;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;

/**
 * 获取大盘（上证，深证，创业板）的实时数据
 *
 */
public class DaPanCurrentTrendRootTask extends AbstractCollector {

    public static final List<String> stockIds = Arrays.asList("SH000001", "SZ399001", "SZ399006");

    public DaPanCurrentTrendRootTask(String taskName) {
        super(taskName);
    }

    @Override
    public void collectLogic() throws MalformedURLException, IOException {
        for (String stockId : stockIds)
        {
            StockToStockTrendCurrentTask task = new StockToStockTrendCurrentTask("StockToStockTrendCurrentTask-" + stockId, stockId);
            RegularManagerSingleton.getInstance().addRegularTask(task);
            try
            {
                Thread.sleep(50);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
                return;
            }
        }
    }

    @Override
    public Period generatePeriod() {
        return buildEmptyPeriod();
    }

    @Override
    public void setTimeout() {
        this.timeout = MyConstants.TIMEOUT;
    }

    class MyConstants
    {
        public static final int TIMEOUT = 1000;
    }
}
