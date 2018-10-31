package com.yanbinwa.stock.service.analysation.strategy;

import com.emotibot.middleware.utils.StringUtils;
import com.emotibot.middleware.utils.TimeUtils;
import com.yanbinwa.stock.entity.stockTrend.StockTrend;
import com.yanbinwa.stock.service.analysation.service.AnalysationServiceImpl;
import com.yanbinwa.stock.utils.StockTrendUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 分析
 *
 * 当前处于连板的股票，N个板以上的
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DabanStrategy implements Strategy {

    private int dayNum;
    private double group_limit;
    private String dabanTime;
    private int window_gap;

    @Override
    public List<List<StockTrend>> getIdealStockTrendList(List<StockTrend> stockTrendList) {
        List<List<StockTrend>> ret = new ArrayList<>();
        int index = getTargetStockTrendIndex(stockTrendList);
        if ((index - dayNum) < 0) {
            return null;
        }
        for (int i = index - dayNum + 1; i <= index; i ++) {
            StockTrend stockTrend = stockTrendList.get(i);
            if (stockTrend.getPercent() < group_limit) {
                return null;
            }
        }
        List<StockTrend> chooseStockTrends = StockTrendUtils.getStockTrendFromWindowGap(stockTrendList,
                stockTrendList.get(index).getCreatedate().getTime() - (long)window_gap * TimeUtils.MILLISECOND_IN_DAY,
                stockTrendList.get(index).getCreatedate().getTime());
        ret.add(chooseStockTrends);
        return ret;
    }

    private int getTargetStockTrendIndex(List<StockTrend> stockTrendList) {
        if (StringUtils.isEmpty(dabanTime)) {
            return stockTrendList.size() - 1;
        }
        Date date = TimeUtils.getDateFromStr(dabanTime, AnalysationServiceImpl.DATE_FORMAT);
        for (int i = 0; i < stockTrendList.size(); i ++) {
            StockTrend stockTrend = stockTrendList.get(i);
            if (stockTrend.getCreatedate().equals(date)) {
                return i;
            }
        }
        return -1;
    }
}
