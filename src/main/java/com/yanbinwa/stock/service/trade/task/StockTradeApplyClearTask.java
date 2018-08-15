package com.yanbinwa.stock.service.trade.task;

import com.yanbinwa.stock.common.trade.AbstractTrade;
import com.yanbinwa.stock.common.type.DayWindow;
import com.yanbinwa.stock.common.type.HourWindow;
import com.yanbinwa.stock.common.type.Period;
import com.yanbinwa.stock.common.type.PeriodType;
import com.yanbinwa.stock.service.trade.entity.StockTradeApply;
import com.yanbinwa.stock.service.trade.utils.StockTradeUtils;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 每天股市结束后，将没有完成的apply清除，将股票或者钱退回到用户的账户中
 *
 */
@Data
public class StockTradeApplyClearTask extends AbstractTrade {

    private static final DayWindow[] dayWindowArray = {DayWindow.MONDAY, DayWindow.TUESDAY, DayWindow.WEDNESDAY, DayWindow.THURSDAY, DayWindow.FRIDAY};
    private static final HourWindow[] hourWindowArray = {HourWindow.HOUR16_FH};
    private static final int periodInterval = Period.SECOND_IN_DAY;

    public StockTradeApplyClearTask(String taskName) {
        super(taskName);
    }

    @Override
    public Period generatePeriod() {
        Period period = new Period();
        period.setPeriodType(PeriodType.PERIOD);
        period.setInterval(periodInterval);
        List<DayWindow> dayWindowList = new ArrayList<DayWindow>();
        Collections.addAll(dayWindowList, dayWindowArray);
        period.setDayWindowList(dayWindowList);
        List<HourWindow> hourWindowList = new ArrayList<HourWindow>();
        Collections.addAll(hourWindowList, hourWindowArray);
        period.setHourWindowList(hourWindowList);
        return period;
    }

    @Override
    public void setTimeout() {
        this.timeout = MyConstants.TIMEOUT;
    }

    @Override
    public void tradeLogic() {
        List<StockTradeApply> stockTradeApplies = StockTradeUtils.getAllStockTradeApply();
        for (StockTradeApply stockTradeApply : stockTradeApplies) {
            //申请的反向操作
            StockTradeUtils.recallTradeStock(stockTradeApply.getId());
        }
    }

    class MyConstants {
        public static final int TIMEOUT = 10000;
    }
}
