package com.yanbinwa.stock.service.trade.task;

import com.yanbinwa.stock.common.trade.AbstractTrade;
import com.yanbinwa.stock.common.type.DayWindow;
import com.yanbinwa.stock.common.type.HourWindow;
import com.yanbinwa.stock.common.type.Period;
import com.yanbinwa.stock.common.type.PeriodType;
import com.yanbinwa.stock.entity.stockTrend.StockTrend;
import com.yanbinwa.stock.service.collection.utils.CollectionUtils;
import com.yanbinwa.stock.service.trade.entity.StockTradeApply;
import com.yanbinwa.stock.service.trade.utils.StockTradeUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 处理当前正在申请的列表，判断是否到达了可以购买的股票价格
 *
 *
 *
 */
@Slf4j
@Data
public class StockTradeDealTask extends AbstractTrade {

    private static final DayWindow[] dayWindowArray = {DayWindow.MONDAY, DayWindow.TUESDAY, DayWindow.WEDNESDAY, DayWindow.THURSDAY, DayWindow.FRIDAY};
    private static final HourWindow[] hourWindowArray = {HourWindow.HOUR9_SH, HourWindow.HOUR10_FH, HourWindow.HOUR10_SH, HourWindow.HOUR11_FH, HourWindow.HOUR13_FH, HourWindow.HOUR13_SH, HourWindow.HOUR14_FH, HourWindow.HOUR14_SH};
    private static final int periodInterval = 10;

    public StockTradeDealTask(String taskName) {
        super(taskName);
    }

    /**
     * 从StockTradeApply表中读取申请，判断是否停牌，股价是否可以交易
     *
     * 1. 从数据库中读取全部申请
     * 2. 顺序判断是否完成，如果完成，将stockTradeApply写为stockTrade
     *
     */
    @Override
    public void tradeLogic() {
        List<StockTradeApply> stockTradeApplies = StockTradeUtils.getAllStockTradeApply();
        if (stockTradeApplies == null) {
            return;
        }
        for (StockTradeApply stockTradeApply : stockTradeApplies) {
            if (StockTradeUtils.isDealTime(System.currentTimeMillis()) &&
                    !StockTradeUtils.isStockSuspension(stockTradeApply.getStockId())) {
                //获取当前的股票价格，判断是否可以交易
                StockTrend stockTrend = CollectionUtils.getStockTrendData(stockTradeApply.getStockId());
                if (stockTrend == null) {
                    log.error("stock not in collection: " + stockTradeApply.getStockId());
                    return;
                }
                boolean isDeal = false;
                if (stockTradeApply.isBuyStock() && stockTradeApply.getPrice() >= stockTrend.getCurrentPrice()) {
                    isDeal = true;
                } else if (!stockTradeApply.isBuyStock() && stockTradeApply.getPrice() <= stockTrend.getCurrentPrice()) {
                    isDeal = true;
                }
                if (isDeal) {
                    if (!StockTradeUtils.execStockTradeApply(stockTradeApply)) {
                        log.error("execute stock fail: " + stockTradeApply);
                    }
                }
            }
        }
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

    class MyConstants
    {
        public static final int TIMEOUT = 10000;
    }
}
