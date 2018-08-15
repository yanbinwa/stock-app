package com.yanbinwa.stock.entity.stockTrend;

import java.util.Date;

public interface StockTrend extends Comparable<StockTrend>
{
    Long getId();
    
    String getStockId();
    void setStockId(String stockId);
    
    Date getCreatedate();
    void setCreatedate(Date createdate);
    
    double getCurrentPrice();
    void setCurrentPrice(double currentPrice);
    
    double getOpen();
    void setOpen(double open);
    
    double getHigh();
    void setHigh(double high);
    
    double getClose();
    void setClose(double close);
    
    double getLow();
    void setLow(double low);
    
    double getChg();
    void setChg(double chg);
    
    double getPercent();
    void setPercent(double percent);
    
    double getTurnrate();
    void setTurnrate(double turnrate);
}
