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
    private int dayNum;
    private double increase_rate;
    private int window_gap;
    
    public ContinueIncreaseStrategy(int dayNum, double increase_rate, int window_gap)
    {
        this.dayNum = dayNum;
        this.increase_rate = increase_rate;
        this.window_gap = window_gap;
    }
    
    @Override
    public List<List<StockTrend>> getIdealStockTrendList(List<StockTrend> stockTrendList)
    {
        List<List<StockTrend>> ret = new ArrayList<List<StockTrend>>();
        if (stockTrendList.size() < dayNum)
        {
            return null;
        }
        int size = stockTrendList.size();
        double totalIncreaseRate = 0;
        for (int i = 0; i < dayNum; i ++)
        {
            totalIncreaseRate += stockTrendList.get(size - 1 - i).getPercent();
        }
        if (totalIncreaseRate < increase_rate)
        {
            return null;
        }
        List<StockTrend> chooseStockTrends = StockTrendUtils.getStockTrendFromWindowGap(stockTrendList, 
                stockTrendList.get(size - 1).getCreatedate().getTime() - (long)window_gap * TimeUtils.MILLISECOND_IN_DAY, 
                stockTrendList.get(size - 1).getCreatedate().getTime() + (long)window_gap * TimeUtils.MILLISECOND_IN_DAY);
        ret.add(chooseStockTrends);
        return ret;
    }

}
