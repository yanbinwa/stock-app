package com.yanbinwa.stock.service.collection.task;

import com.yanbinwa.stock.common.collector.AbstractCollector;
import com.yanbinwa.stock.common.singleton.RegularManagerSingleton;
import com.yanbinwa.stock.common.type.Period;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;

public class DaPanTodayHistoryTrendRootTask  extends AbstractCollector {

    public static final List<String> stockIds = Arrays.asList("SH000001", "SZ399001", "SZ399006");

    public DaPanTodayHistoryTrendRootTask(String taskName) {
        super(taskName);
    }

    @Override
    public void collectLogic() throws MalformedURLException, IOException {
        for (String stockId : stockIds) {
            StockToStockTrendTodayHistoryTask task = new StockToStockTrendTodayHistoryTask("StockToStockTrendCurrentTask-" + stockId, stockId);
            RegularManagerSingleton.getInstance().addRegularTask(task);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
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
        this.timeout = DaPanCurrentTrendRootTask.MyConstants.TIMEOUT;
    }

    class MyConstants
    {
        public static final int TIMEOUT = 1000;
    }
}

