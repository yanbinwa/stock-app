package com.yanbinwa.stock.service.analysation.script;

import com.yanbinwa.stock.entity.stockTrend.StockTrend;

/**
 * 具体对于股票的哪个参数做比较
 * 
 * @author emotibot
 *
 */
public enum ScriptTarget
{
    PRICE() 
    {
        @Override
        public double getValue(StockTrend stockTrend)
        {
            return stockTrend.getCurrentPrice();
        }
    },
    
    TURNRATE()
    {
        @Override
        public double getValue(StockTrend stockTrend)
        {
            return stockTrend.getTurnrate();
        }
    }, 
    
    PERCENT()
    {
        @Override
        public double getValue(StockTrend stockTrend)
        {
            return stockTrend.getPercent();
        }
    };
    
    public abstract double getValue(StockTrend stockTrend);
}
