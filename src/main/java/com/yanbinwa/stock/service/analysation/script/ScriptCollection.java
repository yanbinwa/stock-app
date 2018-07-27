package com.yanbinwa.stock.service.analysation.script;

import java.util.List;

import com.yanbinwa.stock.entity.stockTrend.StockTrend;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 指定一系列的比较
 * 
 * @author emotibot
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScriptCollection
{
    private List<ScriptEle> scriptEleList;
    
    public boolean getValue(StockTrend stockTrend)
    {
        boolean isMatch = true;
        for (ScriptEle scriptEle : scriptEleList)
        {
            if (!scriptEle.getValue(stockTrend))
            {
                isMatch = false;
                break;
            }
        }
        return isMatch;
    }
}
