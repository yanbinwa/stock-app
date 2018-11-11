package com.yanbinwa.stock.service.analysation.strategy;

import com.emotibot.middleware.utils.TimeUtils;
import com.yanbinwa.stock.entity.stockTrend.StockTrend;
import com.yanbinwa.stock.utils.StockTrendUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DabanTopNStrategy implements Strategy {

    private int topN;
    private int dayNum;
    private double group_limit;
    private int window_gap;

    @Override
    public List<List<StockTrend>> getIdealStockTrendList(List<StockTrend> stockTrendList) {
        Map<String, List<StockTrend>> stockIdToStockTrendMap = StockTrendUtils.classifyStockTrendById(stockTrendList);
        List<TopN> topNs = new ArrayList<>();
        for (List<StockTrend> stockTrends : stockIdToStockTrendMap.values()) {
            if (stockTrends.size() < dayNum) {
                continue;
            }
            stockTrends = StockTrendUtils.sortStockTrendByTimestamp(stockTrends);
            int lianBanNum = 0;
            for (int i = stockTrends.size() -1; i >= 0; i--) {
                StockTrend stockTrend = stockTrends.get(i);
                if (stockTrend.getPercent() > group_limit) {
                    lianBanNum ++;
                } else {
                    break;
                }
            }
            if (lianBanNum >= dayNum) {
                List<StockTrend> chooseStockTrends = StockTrendUtils.getStockTrendFromWindowGap(stockTrends,
                        stockTrends.get(stockTrends.size() - 1).getCreatedate().getTime() - (long)window_gap * TimeUtils.MILLISECOND_IN_DAY,
                        stockTrends.get(stockTrends.size() - 1).getCreatedate().getTime());
                TopN topN = new TopN(lianBanNum, chooseStockTrends);
                topNs.add(topN);
            }
        }
        return topNs.stream().sorted(Comparator.comparing(TopN::getLianBanNum).reversed()).limit(topN).map(TopN::getStockTrends).collect(Collectors.toList());
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class TopN {
        private Integer lianBanNum;
        private List<StockTrend> stockTrends;
    }
}
