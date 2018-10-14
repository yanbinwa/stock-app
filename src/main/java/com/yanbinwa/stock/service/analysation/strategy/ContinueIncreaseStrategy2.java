package com.yanbinwa.stock.service.analysation.strategy;

import com.emotibot.middleware.utils.TimeUtils;
import com.yanbinwa.stock.entity.stockTrend.StockTrend;
import com.yanbinwa.stock.utils.StockTrendUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 挑选连板的股票
 *
 */
public class ContinueIncreaseStrategy2 implements Strategy {

    private int dayNum;
    private int window_gap;

    public ContinueIncreaseStrategy2(int dayNum, int window_gap) {
        this.dayNum = dayNum;
        this.window_gap = window_gap;
    }

    @Override
    public List<List<StockTrend>> getIdealStockTrendList(List<StockTrend> stockTrendList) {
        List<List<StockTrend>> ret = new ArrayList<List<StockTrend>>();
        if (stockTrendList.size() < dayNum)
        {
            return null;
        }
        int size = stockTrendList.size();
        int maxIncreaseDay = 0;
        int increaseDay = 0;
        int maxStartIndex = 0;
        int startIndex = 0;
        for (int i = 0; i < size; i ++)
        {
            StockTrend stockTrend = stockTrendList.get(i);
            if (stockTrend.getPercent() > 1.0d) {
                increaseDay ++;
            } else {
                if (increaseDay > maxIncreaseDay) {
                    maxIncreaseDay = increaseDay;
                    maxStartIndex = startIndex;
                }
                increaseDay = 0;
                startIndex = i;
            }
        }
        if (increaseDay > maxIncreaseDay) {
            maxIncreaseDay = increaseDay;
            maxStartIndex = startIndex;
        }

        if (maxIncreaseDay < dayNum)
        {
            return null;
        }
        List<StockTrend> chooseStockTrends = StockTrendUtils.getStockTrendFromWindowGap(stockTrendList,
                stockTrendList.get(maxStartIndex).getCreatedate().getTime() - (long)window_gap * TimeUtils.MILLISECOND_IN_DAY,
                stockTrendList.get(maxStartIndex).getCreatedate().getTime() + (long)window_gap * TimeUtils.MILLISECOND_IN_DAY);
        ret.add(chooseStockTrends);
        return ret;
    }
}
