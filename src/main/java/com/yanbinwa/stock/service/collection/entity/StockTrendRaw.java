package com.yanbinwa.stock.service.collection.entity;

import com.yanbinwa.stock.entity.stockTrend.AbstractStockTrend;
import com.yanbinwa.stock.service.collection.element.IndustryToStockCollection.IndustryToStock;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="stockTrendRaw", indexes = {@Index(name = "stockIdAndTimeIndex", columnList = "stockId,createdate")})
public class StockTrendRaw extends AbstractStockTrend implements Serializable
{   
    private static final long serialVersionUID = -6550777752269466791L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(length = 10, nullable = false)
    private String stockId;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdate;
    
    @Column
    private double currentPrice;
    
    @Column
    private double open;
    
    @Column
    private double high;
    
    @Column
    private double close;
    
    @Column
    private double low;
    
    @Column
    private double chg;
    
    @Column
    private double percent;
    
    @Column
    private double turnrate;
    
    public StockTrendRaw(IndustryToStock stock)
    {
        this.stockId = stock.getSymbol();
        this.createdate = new Date();
        this.currentPrice = stock.getCurrent();
    }
}
