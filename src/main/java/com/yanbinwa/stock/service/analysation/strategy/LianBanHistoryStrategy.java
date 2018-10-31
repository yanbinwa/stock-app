package com.yanbinwa.stock.service.analysation.strategy;

import com.emotibot.middleware.utils.TimeUtils;
import com.yanbinwa.stock.entity.stockTrend.StockTrend;
import com.yanbinwa.stock.utils.StockTrendUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *  绘制出历史连板股票的趋势图，观察有什么规律
 *
 *  dayNum: 至少连板的次数
 *
 */
public class LianBanHistoryStrategy implements Strategy {

    private int dayNum;
    private double group_limit;
    private int window_gap;

    public LianBanHistoryStrategy(int dayNum, double group_limit, int window_gap) {
        this.dayNum = dayNum;
        this.group_limit = group_limit;
        this.window_gap = window_gap;
    }

    @Override
    public List<List<StockTrend>> getIdealStockTrendList(List<StockTrend> stockTrendList) {
        List<List<StockTrend>> ret = new ArrayList<>();
        //1. 先按照stockId分组
        Map<String, List<StockTrend>> stockIdToStockTrendMap = StockTrendUtils.classifyStockTrendById(stockTrendList);
        //2. 将每组stockId排序

        for (List<StockTrend> stockTrends : stockIdToStockTrendMap.values()) {
            if (stockTrends.size() < dayNum) {
                continue;
            }
            stockTrends = StockTrendUtils.sortStockTrendByTimestamp(stockTrends);
            int increateDayNum = 0;
            for (int i = 0; i < stockTrends.size(); i ++) {
                StockTrend stockTrend = stockTrends.get(i);
                if (stockTrend.getPercent() < group_limit) {
                    if (increateDayNum >= dayNum) {
                        //3. 遍历stock，获取满足连续涨停的条件，生成对应的stockList
                        List<StockTrend> chooseStockTrends = StockTrendUtils.getStockTrendFromWindowGap(stockTrends,
                                stockTrend.getCreatedate().getTime() - (long)window_gap * TimeUtils.MILLISECOND_IN_DAY,
                                stockTrend.getCreatedate().getTime() + (long)window_gap * TimeUtils.MILLISECOND_IN_DAY);
                        ret.add(chooseStockTrends);
                    }
                    increateDayNum = 0;
                } else {
                    increateDayNum ++;
                }
            }
        }
        return ret;
    }
}
