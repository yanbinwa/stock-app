package com.yanbinwa.stock.entity.stockTrend;

import java.util.Date;

public interface StockTrend extends Comparable<StockTrend>
{
    public Long getId();
    
    public String getStockId();
    public void setStockId(String stockId);
    
    public Date getCreatedate();
    public void setCreatedate(Date createdate);
    
    public double getCurrentPrice();
    public void setCurrentPrice(double currentPrice);
}
