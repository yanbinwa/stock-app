package com.yanbinwa.stock.service.analysation.script;

import com.yanbinwa.stock.entity.stockTrend.StockTrend;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author emotibot
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScriptEle
{
    private ScriptTarget scriptTarget;
    private ScriptCompare scriptCompare;
    private double value;
    
    public boolean getValue(StockTrend stockTrend)
    {
        double targetValue = scriptTarget.getValue(stockTrend);
        return scriptCompare.getValue(targetValue, value);
    }
}
