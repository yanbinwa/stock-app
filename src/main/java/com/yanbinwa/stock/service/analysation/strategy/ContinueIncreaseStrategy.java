package com.yanbinwa.stock.service.analysation.strategy;

import java.util.ArrayList;
import java.util.List;

import com.emotibot.middleware.utils.TimeUtils;
import com.yanbinwa.stock.entity.stockTrend.StockTrend;
import com.yanbinwa.stock.utils.StockTrendUtils;

/**
 * 最后三天连续增长，幅度超过20%
 * 
 * @author emotibot
 *
 */
public class ContinueIncreaseStrategy implements Strategy
{
    private static final int DAY_NUM = 2;
    private static final double INCREASE_RATE = 18d;
    private static final int WINDOW_GAP = 20;
    
    @Override
    public List<List<StockTrend>> getIdealStockTrendList(List<StockTrend> stockTrendList)
    {
        List<List<StockTrend>> ret = new ArrayList<List<StockTrend>>();
        if (stockTrendList.size() < DAY_NUM)
        {
            return null;
        }
        int size = stockTrendList.size();
        double totalIncreaseRate = stockTrendList.get(size - 1).getPercent() + stockTrendList.get(size - 2).getPercent() + stockTrendList.get(size - 3).getPercent();
        if (totalIncreaseRate < INCREASE_RATE)
        {
            return null;
        }
        List<StockTrend> chooseStockTrends = StockTrendUtils.getStockTrendFromWindowGap(stockTrendList, 
                stockTrendList.get(size - 1).getCreatedate().getTime() - (long)WINDOW_GAP * TimeUtils.MILLISECOND_IN_DAY, 
                stockTrendList.get(size - 1).getCreatedate().getTime() + (long)WINDOW_GAP * TimeUtils.MILLISECOND_IN_DAY);
        ret.add(chooseStockTrends);
        return ret;
    }

}
