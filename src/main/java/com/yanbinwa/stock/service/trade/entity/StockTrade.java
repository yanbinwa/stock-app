package com.yanbinwa.stock.service.trade.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 记录了某个用户的交易记录，包括买入和卖出
 * 
 * 每天结束时，会将用户买入股票的交易记录与用户持股的记录合并
 * 
 * @author emotibot
 *
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="stockTrade", indexes = {@Index(name = "stockIdAndAccountAndTimeIndex", columnList = "stockId,accountId,createdate")})
public class StockTrade
{
    public StockTrade(StockTradeApply stockTradeApply) {
        this.accountId = stockTradeApply.getAccountId();
        this.stockId = stockTradeApply.getStockId();
        this.createdate = new Date();
        this.num = stockTradeApply.getNum();
        this.price = stockTradeApply.getPrice();
        this.isBuyStock = stockTradeApply.isBuyStock();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column
    private Long accountId;
    
    @Column(length = 10, nullable = false)
    private String stockId;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdate;
    
    @Column
    private int num;
    
    @Column
    private double price;

    /**
     * 买入还是卖出
     *
     */
    @Column
    private boolean isBuyStock;
}
