package com.yanbinwa.stock.service.analysation.strategy;

import com.emotibot.middleware.utils.TimeUtils;
import com.yanbinwa.stock.entity.stockTrend.StockTrend;
import com.yanbinwa.stock.utils.StockTrendUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 通过换手率的趋势来选取股票
 *
 * 长期换手比较低，之后突然有换手率的增长，抓取这样的趋势
 *
 */
public class ChangeRateTrendStrategy implements Strategy {

    private int lowChangeRateDay;
    private double lowChangeRate;

    private int targetChangeRateDay;
    private double targetChangeRate;

    private int window_gap;

    public ChangeRateTrendStrategy(int lowChangeRateDay, double lowChangeRate, int targetChangeRateDay, double targetChangeRate, int window_gap) {
        this.lowChangeRate = lowChangeRate;
        this.lowChangeRateDay = lowChangeRateDay;
        this.targetChangeRate = targetChangeRate;
        this.targetChangeRateDay = targetChangeRateDay;
        this.window_gap = window_gap;
    }

    @Override
    public List<List<StockTrend>> getIdealStockTrendList(List<StockTrend> stockTrendList) {
        if (stockTrendList.size() < lowChangeRateDay + targetChangeRateDay) {
            return null;
        }
        List<List<StockTrend>> ret = new ArrayList<>();
        for (int i = 0; i <= stockTrendList.size() - (lowChangeRateDay + targetChangeRateDay);) {
            if (isMatchLowChangeRate(i, stockTrendList) && isMatchHighChangeRate(i + lowChangeRateDay, stockTrendList)) {
                //符合上述需要的条件，输出结果
                List<StockTrend> chooseStockTrends = StockTrendUtils.getStockTrendFromWindowGap(stockTrendList,
                        stockTrendList.get(i).getCreatedate().getTime() - (long)window_gap * TimeUtils.MILLISECOND_IN_DAY,
                        stockTrendList.get(i + lowChangeRateDay + targetChangeRateDay - 1).getCreatedate().getTime() + (long)window_gap * TimeUtils.MILLISECOND_IN_DAY);
                ret.add(chooseStockTrends);
                i += lowChangeRateDay + targetChangeRateDay;
            } else {
                i ++;
            }
        }
        return ret;
    }

    private boolean isMatchLowChangeRate(int startIndex, List<StockTrend> stockTrendList) {
        for (int i = startIndex; i < lowChangeRateDay; i ++) {
            if (stockTrendList.get(i).getTurnrate() > lowChangeRate) {
                return false;
            }
        }
        return true;
    }

    private boolean isMatchHighChangeRate(int startIndex, List<StockTrend> stockTrendList) {
        double sum = 0d;
        for (int i = startIndex; i < targetChangeRateDay; i ++) {
            sum += stockTrendList.get(i).getTurnrate();
        }
        if (sum / targetChangeRateDay < targetChangeRate) {
            return false;
        }
        return true;
    }
}
