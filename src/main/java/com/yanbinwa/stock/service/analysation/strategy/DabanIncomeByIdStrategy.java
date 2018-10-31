package com.yanbinwa.stock.service.analysation.strategy;

import com.yanbinwa.stock.entity.stockTrend.StockTrend;
import com.yanbinwa.stock.utils.StockTrendUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 分析所有股票通过打板获取收益的情况，输出成xlsx
 *
 * 输入可能是所有的股票，也可能是一支股票
 *
 * 三种情况的打版
 *
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DabanIncomeByIdStrategy implements StrategyXlsx {

    private double incomeRate;
    private int tag;

    @Override
    public List<List<String>> getIdealStockTrendList(List<StockTrend> stockTrendList) {
        Map<String, List<StockTrend>> stockIdToStockTrendMap = StockTrendUtils.classifyStockTrendById(stockTrendList);
        List<List<String>> ret = new ArrayList<>();
        for (List<StockTrend> stockTrends : stockIdToStockTrendMap.values()) {
            stockTrends = StockTrendUtils.sortStockTrendByTimestamp(stockTrends);
            Double profit = null;
            switch (tag) {
                case 1:
                    profit = getProfit1(stockTrends);
                    break;
                case 2:
                    profit = getProfit2(stockTrends);
                    break;
                case 3:
                    profit = getProfit3(stockTrends);
                    break;
                default:
                    break;
            }
            if (profit == null) {
                continue;
            }
            List<String> content = new ArrayList<>();
            content.add(stockTrends.get(0).getStockId());
            content.add(String.valueOf(profit));
            ret.add(content);
        }
        return ret;
    }

    private Double getProfit1(List<StockTrend> stockTrends) {
        int count = 0;
        Double sum = 0.0d;
        for (int i = 0; i < stockTrends.size() - 1; i ++) {
            StockTrend stockTrend = stockTrends.get(i);
            if (stockTrend.getPercent() > incomeRate) {
                sum += stockTrends.get(i + 1).getPercent();
                count ++;
            }
        }
        if (count == 0) {
            return null;
        }
        return sum / count;
    }

    private Double getProfit2(List<StockTrend> stockTrends) {
        int count = 0;
        Double sum = 0.0d;
        for (int i = 1; i < stockTrends.size() - 1; i ++) {
            StockTrend stockTrend = stockTrends.get(i);
            Double percent = (stockTrends.get(i).getHigh() - stockTrends.get(i - 1).getClose()) / stockTrends.get(i - 1).getClose() * 100;
            if (percent > incomeRate) {
                sum += stockTrends.get(i + 1).getPercent();
                count ++;
            }
        }
        if (count == 0) {
            return null;
        }
        return sum / count;
    }

    private Double getProfit3(List<StockTrend> stockTrends) {
        int count = 0;
        Double sum = 0.0d;
        for (int i = 1; i < stockTrends.size() - 1; i ++) {
            StockTrend stockTrend = stockTrends.get(i);
            Double percent = (stockTrends.get(i).getHigh() - stockTrends.get(i - 1).getClose()) / stockTrends.get(i - 1).getClose() * 100;
            if (percent > incomeRate && stockTrend.getPercent() < incomeRate) {
                sum += stockTrends.get(i + 1).getPercent();
                count ++;
            }
        }
        if (count == 0) {
            return null;
        }
        return sum / count;
    }
}
