package com.yanbinwa.stock.service.aggragation.task;

import com.yanbinwa.stock.common.aggragation.AbstractAggragation;
import com.yanbinwa.stock.common.type.DayWindow;
import com.yanbinwa.stock.common.type.HourWindow;
import com.yanbinwa.stock.common.type.Period;
import com.yanbinwa.stock.entity.stockTrend.StockTrendType;
import com.yanbinwa.stock.service.aggragation.entity.StockTrendAgg5Min;
import com.yanbinwa.stock.utils.StockTrendUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class StockTrendAggragation5MinTask extends AbstractAggragation
{
//    private static final DayWindow[] dayWindowArray = {DayWindow.MONDAY, DayWindow.TUESDAY, DayWindow.WEDNESDAY, DayWindow.THURSDAY, DayWindow.FRIDAY};
//    private static final HourWindow[] hourWindowArray = {HourWindow.HOUR9, HourWindow.HOUR10, HourWindow.HOUR13, HourWindow.HOUR14};
    private static final DayWindow[] dayWindowArray = {};
    private static final HourWindow[] hourWindowArray = {};
    private static final int periodInterval = Period.SECOND_IN_MINUTE * 5;
    
    public StockTrendAggragation5MinTask(String taskName)
    {
        super(taskName);
    }
    
    /**
     * 从数据库中读取一段时间的股票数据，按照股票ID进行聚合，之后写入到聚合的表中
     */
    @Override
    public void aggragationLogic()
    {
        log.info("aggragationLogic start: " + taskName);
        StockTrendUtils.stockTrendAggragationLogic(StockTrendType.TYPE_1MIN, StockTrendType.TYPE_5MIN, periodInterval, true, StockTrendAgg5Min.class);
    }

    @Override
    public Period generatePeriod()
    {
        return buildPeriod(periodInterval, dayWindowArray, hourWindowArray);
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
