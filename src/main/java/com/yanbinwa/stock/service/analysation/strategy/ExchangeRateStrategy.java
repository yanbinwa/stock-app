package com.yanbinwa.stock.service.analysation.strategy;

import java.util.ArrayList;
import java.util.List;

import com.emotibot.middleware.utils.TimeUtils;
import com.yanbinwa.stock.entity.stockTrend.StockTrend;
import com.yanbinwa.stock.utils.StockTrendUtils;

/**
 * 判读一段时间内连续换手率较高的情况
 * 
 * @author emotibot
 *
 */
public class ExchangeRateStrategy implements Strategy
{
    private static final int DAY_NUM = 3;
    private static final double EXCHANGE_RATE = 10d;
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
            boolean isChoose = true;
            for (int j = i - DAY_NUM + 1; j <= i; j++)
            {
                if (stockTrendList.get(j).getTurnrate() < EXCHANGE_RATE)
                {
                    isChoose = false;
                    break;
                }
            }
            if (isChoose)
            {
                List<StockTrend> chooseStockTrends = StockTrendUtils.getStockTrendFromWindowGap(stockTrendList, 
                        stockTrendList.get(i).getCreatedate().getTime() - (long)WINDOW_GAP * TimeUtils.MILLISECOND_IN_DAY, 
                        stockTrendList.get(i).getCreatedate().getTime() + (long)WINDOW_GAP * TimeUtils.MILLISECOND_IN_DAY);
                ret.add(chooseStockTrends);
                i += DAY_NUM;
            }
            else
            {
                i ++;
            }
        }
        return ret;
    }
}
