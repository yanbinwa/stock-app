package com.yanbinwa.stock.entity.stockTrend;

import com.emotibot.middleware.utils.JsonUtils;

public abstract class AbstractStockTrend implements StockTrend 
{
    
    
    @Override
    public int compareTo(StockTrend other)
    {
        if (this.getCreatedate().getTime() > other.getCreatedate().getTime())
        {
            return 1;
        }
        else if (this.getCreatedate().getTime() < other.getCreatedate().getTime())
        {
            return -1;
        }
        else
        {
            return 0;
        }
    }
    
    @Override
    public String toString()
    {
        return JsonUtils.getJsonStr(this);
    }
    
}
