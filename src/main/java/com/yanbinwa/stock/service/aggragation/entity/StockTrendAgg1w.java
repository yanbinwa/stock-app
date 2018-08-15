package com.yanbinwa.stock.service.aggragation.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import com.yanbinwa.stock.entity.stockTrend.AbstractStockTrend;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="stockTrendAgg1w", indexes = {@Index(name = "stockIdAndTimeIndex", columnList = "stockId,createdate")})
public class StockTrendAgg1w extends AbstractStockTrend implements Serializable
{       
    private static final long serialVersionUID = -377346289337614031L;

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
}
