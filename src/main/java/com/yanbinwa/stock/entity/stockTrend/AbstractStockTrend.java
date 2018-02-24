package com.yanbinwa.stock.entity.stockTrend;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractStockTrend
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    
    @Column(length = 10, nullable = false)
    protected String stockId;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date createdate;
    
    @Column
    protected double currentPrice;
}
