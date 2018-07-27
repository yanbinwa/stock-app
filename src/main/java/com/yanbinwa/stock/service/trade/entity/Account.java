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

/**
 * 交易账户信息
 * 
 * @author emotibot
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="account", indexes = {@Index(name = "accountNameIndex", columnList = "name")})
public class Account
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(length = 10, nullable = false)
    private String name;
    
    @Column
    private double deposit;
}
