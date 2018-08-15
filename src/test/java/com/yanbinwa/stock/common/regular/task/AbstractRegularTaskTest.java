package com.yanbinwa.stock.common.regular.task;

import com.yanbinwa.stock.service.collection.task.CurrentStockTrendTask;
import org.junit.Test;


public class AbstractRegularTaskTest {

    private static final int pageIndex = 1;
    private static final int pageSize = 100;

    @Test
    public void test() throws ClassNotFoundException {
        AbstractRegularTask currentStockTrendTask = new CurrentStockTrendTask("CurrentStockTrendTask=" + pageIndex, pageIndex, pageSize);
        RegularTaskWarp warp = currentStockTrendTask.getTaskWarp();
        System.out.println(warp);
        currentStockTrendTask = AbstractRegularTask.buildRegularTask(warp, (Class<? extends AbstractRegularTask>) Class.forName(warp.getTaskClass()));
        System.out.println(currentStockTrendTask);
    }
}
