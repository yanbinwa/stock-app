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

import org.springframework.format.annotation.DateTimeFormat;

import com.yanbinwa.stock.entity.stockTrend.StockTrend;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="stockTrendAgg1h", indexes = {@Index(name = "stockIdAndTimeIndex", columnList = "stockId,createdate")})
public class StockTrendAgg1h implements Serializable, StockTrend
{       
    private static final long serialVersionUID = 2922567306558922405L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(length = 10, nullable = false)
    private String stockId;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdate;
    
    @Column
    private double currentPrice;

    public StockTrendAgg1h()
    {
        
    }
}