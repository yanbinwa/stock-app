package com.yanbinwa.stock.service.collection.task;

import com.emotibot.middleware.utils.TimeUtils;
import com.yanbinwa.stock.common.collector.AbstractCollector;
import com.yanbinwa.stock.common.singleton.RegularManagerSingleton;
import com.yanbinwa.stock.common.type.Period;
import com.yanbinwa.stock.common.type.PeriodType;
import com.yanbinwa.stock.service.collection.utils.CollectionUtils;
import com.yanbinwa.stock.utils.HolidayUtils;
import lombok.Data;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@Data
public class StockToStockTrendByDateRootTask extends AbstractCollector
{
    private long startTimestamp;
    private long endTimestamp;

    public StockToStockTrendByDateRootTask(String taskName) {
        super(taskName);
    }

    public StockToStockTrendByDateRootTask(String taskName, long startTimestamp, long endTimestamp)
    {
        super(taskName);
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
    }

    @Override
    public void collectLogic() throws MalformedURLException, IOException
    {
        //不处理时间跨度较大的case
        if ((endTimestamp - startTimestamp) % TimeUtils.MILLISECOND_IN_DAY > 30)
        {
            return;
        }
        List<String> stockIdList = CollectionUtils.getAllStockId();
        stockIdList.stream().forEach(stockId -> {
            StockToStockTrendByDateTask task = new StockToStockTrendByDateTask("StockToStockTrendByDateTask-" + startTimestamp + "-" + endTimestamp + "-" + stockId, 
                    stockId, HolidayUtils.removeHolidayAndWeekendDate(TimeUtils.getDateListFromStartAndEndTimestamp(startTimestamp, endTimestamp)));
            RegularManagerSingleton.getInstance().addRegularTask(task);
            try
            {
                Thread.sleep(50);
            } 
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        });
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
