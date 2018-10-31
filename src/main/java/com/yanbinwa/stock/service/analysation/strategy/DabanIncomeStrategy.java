package com.yanbinwa.stock.service.analysation.strategy;

import com.yanbinwa.stock.entity.stockTrend.StockTrend;
import com.yanbinwa.stock.service.aggragation.entity.StockTrendAgg1d;
import com.yanbinwa.stock.utils.StockTrendUtils;

import java.util.*;

/**
 * Map<String, List<StockTrend> stockId -> StockTrend
 * Map<String, List<Double> data -> increaseRate
 *
 * 1. 如果尾盘是涨停的收益率 （这个比较难去掌握）
 * 2. 如果出现过涨停，看盘尾收益情况
 * 3. 如果只算盘中涨停的收益率 （）
 *
 */
public class DabanIncomeStrategy implements Strategy {

    private double incomeRate;
    private static int tag;

    private double standard = 10.0d;

    public DabanIncomeStrategy(double incomeRate, int tag) {
        this.incomeRate = incomeRate;
        this.tag = tag;
    }

    @Override
    public List<List<StockTrend>> getIdealStockTrendList(List<StockTrend> stockTrendList) {
        Map<String, List<StockTrend>> stockIdToStockTrendMap = StockTrendUtils.classifyStockTrendById(stockTrendList);

        Map<Date, List<Double>> dateToIncreateRateMap;
        switch (tag) {
            case 1:
                dateToIncreateRateMap = dateToIncreateRateMap1(stockIdToStockTrendMap);
                break;
            case 2:
                dateToIncreateRateMap = dateToIncreateRateMap2(stockIdToStockTrendMap);
                break;
            case 3:
                dateToIncreateRateMap = dateToIncreateRateMap3(stockIdToStockTrendMap);
                break;
            default:
                dateToIncreateRateMap = new HashMap<>();
                break;
        }

        List<StockTrend> stockLists = new ArrayList<>();
        for (Map.Entry<Date, List<Double>> entry : dateToIncreateRateMap.entrySet()) {
            StockTrend stockTrend = new StockTrendAgg1d();
            List<Double> increateRates = entry.getValue();
            Double sum = 0.0d;
            for (Double increateRate : increateRates) {
                sum += increateRate;
            }
            Double arangeIncreateRate = sum / increateRates.size();
            stockTrend.setOpen(standard);
            stockTrend.setClose(standard + arangeIncreateRate);
            if (arangeIncreateRate > 0) {
                stockTrend.setLow(standard);
                stockTrend.setHigh(standard + arangeIncreateRate);
            } else {
                stockTrend.setLow(standard + arangeIncreateRate);
                stockTrend.setHigh(standard);
            }
            stockTrend.setCreatedate(entry.getKey());
            stockTrend.setTurnrate(0.0d);
            stockLists.add(stockTrend);
        }

        List<List<StockTrend>> ret = new ArrayList<List<StockTrend>>();
        ret.add(stockLists);
        return ret;
    }

    private Map<Date, List<Double>> dateToIncreateRateMap1(Map<String, List<StockTrend>> stockIdToStockTrendMap) {
        Map<Date, List<Double>> dateToIncreateRateMap = new HashMap<>();
        for (Map.Entry<String, List<StockTrend>> entry : stockIdToStockTrendMap.entrySet()) {
            List<StockTrend> stockTrends = entry.getValue();
            for (int i = 0; i < stockTrends.size() - 1; i ++) {
                StockTrend stockTrend = stockTrends.get(i);
                if (stockTrend.getPercent() > incomeRate) {
                    List<Double> increateRates = dateToIncreateRateMap.get(stockTrend.getCreatedate());
                    if (increateRates == null) {
                        increateRates = new ArrayList<>();
                        dateToIncreateRateMap.put(stockTrend.getCreatedate(), increateRates);
                    }
                    Double increateRate = (stockTrends.get(i + 1).getClose() - stockTrends.get(i).getClose()) / stockTrends.get(i).getClose() * 100;
                    increateRates.add(increateRate);
                }
            }
        }
        return dateToIncreateRateMap;
    }

    private Map<Date, List<Double>> dateToIncreateRateMap2(Map<String, List<StockTrend>> stockIdToStockTrendMap) {
        Map<Date, List<Double>> dateToIncreateRateMap = new HashMap<>();
        for (Map.Entry<String, List<StockTrend>> entry : stockIdToStockTrendMap.entrySet()) {
            List<StockTrend> stockTrends = entry.getValue();
            for (int i = 1; i < stockTrends.size() - 1; i ++) {
                StockTrend stockTrend = stockTrends.get(i);
                if ((stockTrends.get(i).getHigh() - stockTrends.get(i - 1).getClose()) / stockTrends.get(i - 1).getClose() * 100 > incomeRate) {
                    List<Double> increateRates = dateToIncreateRateMap.get(stockTrend.getCreatedate());
                    if (increateRates == null) {
                        increateRates = new ArrayList<>();
                        dateToIncreateRateMap.put(stockTrend.getCreatedate(), increateRates);
                    }
                    Double increateRate = (stockTrends.get(i + 1).getClose() - stockTrends.get(i).getClose()) / stockTrends.get(i).getClose() * 100;
                    increateRates.add(increateRate);
                }
            }
        }
        return dateToIncreateRateMap;
    }

    private Map<Date, List<Double>> dateToIncreateRateMap3(Map<String, List<StockTrend>> stockIdToStockTrendMap) {
        Map<Date, List<Double>> dateToIncreateRateMap = new HashMap<>();
        for (Map.Entry<String, List<StockTrend>> entry : stockIdToStockTrendMap.entrySet()) {
            List<StockTrend> stockTrends = entry.getValue();
            for (int i = 1; i < stockTrends.size() - 1; i ++) {
                StockTrend stockTrend = stockTrends.get(i);
                if ((stockTrends.get(i).getHigh() - stockTrends.get(i - 1).getClose()) / stockTrends.get(i - 1).getClose() * 100 > incomeRate
                        && stockTrends.get(i).getPercent() < incomeRate) {
                    List<Double> increateRates = dateToIncreateRateMap.get(stockTrend.getCreatedate());
                    if (increateRates == null) {
                        increateRates = new ArrayList<>();
                        dateToIncreateRateMap.put(stockTrend.getCreatedate(), increateRates);
                    }
                    Double increateRate = (stockTrends.get(i + 1).getClose() - stockTrends.get(i).getClose()) / stockTrends.get(i).getClose() * 100;
                    increateRates.add(increateRate);
                }
            }
        }
        return dateToIncreateRateMap;
    }
}
