package com.yanbinwa.stock.service.analysation.strategy;

import com.emotibot.middleware.utils.TimeUtils;
import com.yanbinwa.stock.entity.stockTrend.StockTrend;
import com.yanbinwa.stock.utils.StockTrendUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 在一个时间窗口中，一只股票的连续收益情况大于预期收益，将图形画出
 *
 */
public class IncreaseRateCapStrategy implements Strategy
{
    private static final int DAY_NUM = 3;
    private static final double GAIN_RATE = 20d;
    private static final int WINDOW_GAP = 30;
    
    @Override
    public List<List<StockTrend>> getIdealStockTrendList(List<StockTrend> stockTrendList)
    {
        List<List<StockTrend>> ret = new ArrayList<List<StockTrend>>();
        if (stockTrendList.size() < DAY_NUM)
        {
            return null;
        }
        for (int i = DAY_NUM - 1; i < stockTrendList.size();)
        {
            double gainRate = 0.0d;
            for (int j = i - DAY_NUM + 1; j <= i; j++)
            {
                gainRate += stockTrendList.get(j).getPercent();
            }
            if (gainRate > GAIN_RATE)
            {
                List<StockTrend> chooseStockTrends = StockTrendUtils.getStockTrendFromWindowGap(stockTrendList, 
                        stockTrendList.get(i).getCreatedate().getTime() - (long)WINDOW_GAP * TimeUtils.MILLISECOND_IN_DAY, 
                        stockTrendList.get(i).getCreatedate().getTime() + (long)WINDOW_GAP * TimeUtils.MILLISECOND_IN_DAY);
                ret.add(chooseStockTrends);
                i += DAY_NUM;
                continue;
            }
            else
            {
                i ++;
            }
        }
        return ret;
    }
}
