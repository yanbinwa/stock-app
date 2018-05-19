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
    
    public double getOpen();
    public void setOpen(double open);
    
    public double getHigh();
    public void setHigh(double high);
    
    public double getClose();
    public void setClose(double close);
    
    public double getLow();
    public void setLow(double low);
    
    public double getChg();
    public void setChg(double chg);
    
    public double getPercent();
    public void setPercent(double percent);
    
    public double getTurnrate();
    public void setTurnrate(double turnrate);
}
