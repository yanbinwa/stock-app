package com.yanbinwa.stock.entity.stockTrend;

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
    
}
