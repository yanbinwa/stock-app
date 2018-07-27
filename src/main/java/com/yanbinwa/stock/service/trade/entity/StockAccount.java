package com.yanbinwa.stock.service.trade.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="stockAccount", indexes = {@Index(name = "accountAndStockIdIndex", columnList = "accountId, stockId")})
public class StockAccount
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column
    private Long accountId;
    
    @Column(length = 10, nullable = false)
    private String stockId;
    
    @Column
    private int num;
}
