package com.yanbinwa.stock.service.analysation.task;

import java.util.Date;
import java.util.List;

import com.emotibot.middleware.utils.TimeUtils;
import com.yanbinwa.stock.common.analysation.AbstractAnalysation;
import com.yanbinwa.stock.common.type.Period;
import com.yanbinwa.stock.common.type.PeriodType;
import com.yanbinwa.stock.entity.stockTrend.StockTrend;
import com.yanbinwa.stock.entity.stockTrend.StockTrendType;
import com.yanbinwa.stock.utils.DrawStockTrendUtils;
import com.yanbinwa.stock.utils.StockTrendUtils;

/**
 * 这里是更具股票的历史记录来刷选出盈利较大波段的位置，并且将其绘制出来
 * 
 * 如果出现，收集其前后20天的数据
 * 
 * 连续三天的收益大于15%
 * 
 * @author emotibot
 *
 */
public class FetchIdeaStockTrendByIdTask extends AbstractAnalysation
{
    private static final int DAY_NUM = 3;
    private static final double GAIN_RATE = 5d;
    private static final int WINDOW_GAP = 20;
    
    private String stockId;
    private long startTimestamp;
    private long endTimestamp;
    
    public FetchIdeaStockTrendByIdTask(String taskName, String stockId, long startTimestamp, long endTimestamp)
    {
        super(taskName);
        this.stockId = stockId;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
    }

    @Override
    public void analysationLogic()
    {
        List<StockTrend> stockTrendList = StockTrendUtils.getStockTrendByDate(StockTrendType.TYPE_1D, stockId, 
                new Date(startTimestamp), new Date(endTimestamp));
        if (stockTrendList.size() < DAY_NUM)
        {
            return;
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
                try
                {
                    DrawStockTrendUtils.stockKChart(chooseStockTrends);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    return;
                }
                i += DAY_NUM;
                continue;
            }
            else
            {
                i ++;
            }
        }
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
