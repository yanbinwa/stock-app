package com.yanbinwa.stock.service.analysation.strategy;

import java.util.ArrayList;
import java.util.List;

import com.yanbinwa.stock.entity.stockTrend.StockTrend;
import com.yanbinwa.stock.service.analysation.script.ScriptCollection;

/**
 * 匹配历史当中有哪只股票的趋势与当前股票类似
 *
 */
public class ScriptStrategy implements Strategy
{
    private List<ScriptCollection> scriptList;
    
    public ScriptStrategy(List<ScriptCollection> scriptList)
    {
        this.scriptList = scriptList;
    }
    
    @Override
    public List<List<StockTrend>> getIdealStockTrendList(List<StockTrend> stockTrendList)
    {
        if (scriptList == null || scriptList.isEmpty())
        {
            return null;
        }
        if (stockTrendList == null || stockTrendList.size() < scriptList.size())
        {
            return null;
        }
        List<List<StockTrend>> ret = new ArrayList<List<StockTrend>>();
        int size = scriptList.size();
        int stockSize = stockTrendList.size();
        boolean isMatch = true;
        for (int i = 0; i < size; i ++)
        {
            StockTrend stockTrend = stockTrendList.get(stockSize - i - 1);
            ScriptCollection script = scriptList.get(size - i - 1);
            if (!script.getValue(stockTrend))
            {
                isMatch = false;
                break;
            }
        }
        if (isMatch)
        {
            ret.add(stockTrendList);
        }
        return ret;
    }

}
