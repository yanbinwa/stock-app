package com.yanbinwa.stock.service.analysation.strategy;

import com.emotibot.middleware.utils.TimeUtils;
import com.yanbinwa.stock.entity.stockTrend.StockTrend;
import com.yanbinwa.stock.utils.StockTrendUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 对于当前情况下连板达到特定数量时输出结果
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DabanNumStrategy implements Strategy {
    private int dayNum;
    private double group_limit;
    private String dabanTime;
    private int window_gap;

    @Override
    public List<List<StockTrend>> getIdealStockTrendList(List<StockTrend> stockTrendList) {
        Map<String, List<StockTrend>> stockIdToStockTrendMap = StockTrendUtils.classifyStockTrendById(stockTrendList);
        List<List<StockTrend>> ret = new ArrayList<>();
        for (List<StockTrend> stockTrends : stockIdToStockTrendMap.values()) {
            int index = StockTrendUtils.getTargetStockTrendIndex(stockTrends, dabanTime);
            if ((index - dayNum) < 0) {
                continue;
            }
            int count = 0;
            for (int i = index; i >= 0; i --) {
                StockTrend stockTrend = stockTrendList.get(i);
                if (stockTrend.getPercent() < group_limit) {
                    break;
                }
                count ++;
            }
            if (count == dayNum) {
                List<StockTrend> chooseStockTrends = StockTrendUtils.getStockTrendFromWindowGap(stockTrendList,
                        stockTrendList.get(index).getCreatedate().getTime() - (long)window_gap * TimeUtils.MILLISECOND_IN_DAY,
                        stockTrendList.get(index).getCreatedate().getTime());
                ret.add(chooseStockTrends);
            }
        }
        return ret;
    }
}
