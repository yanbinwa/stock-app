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

/**
 * 选出当前股票价格相比一段时间内最高点的股票价格较低的股票
 *
 * 并绘制出当前的股票趋势
 *
 * targetTime为特定的时间，默认是当前的时间
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LowPriceStockStratey implements Strategy {

    private double priceRate;
    private int window_gap;
    private String targetTime;

    @Override
    public List<List<StockTrend>> getIdealStockTrendList(List<StockTrend> stockTrendList) {

        List<List<StockTrend>> ret = new ArrayList<>();
        int index = StockTrendUtils.getTargetStockTrendIndex(stockTrendList, targetTime);
        double currentPrice = stockTrendList.get(index).getCurrentPrice();
        double maxPrice = stockTrendList.stream().max(Comparator.comparing(StockTrend::getCurrentPrice)).get().getCurrentPrice();
        if (currentPrice / maxPrice > priceRate) {
            return null;
        }

        List<StockTrend> chooseStockTrends = StockTrendUtils.getStockTrendFromWindowGap(stockTrendList,
                stockTrendList.get(index).getCreatedate().getTime() - (long)window_gap * TimeUtils.MILLISECOND_IN_DAY,
                stockTrendList.get(index).getCreatedate().getTime());
        ret.add(chooseStockTrends);
        return ret;
    }
}
