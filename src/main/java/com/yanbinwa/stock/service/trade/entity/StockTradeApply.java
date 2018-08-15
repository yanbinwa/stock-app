package com.yanbinwa.stock.service.trade.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * 记录用户股票交易的申请，会有专门的task进行定期的处理，如果交易成功，会将StockTradeApply转为StockTrade
 *
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="stockTradeApply", indexes = {@Index(name = "stockIdAndAccountAndTimeIndex", columnList = "stockId,accountId,createdate")})
public class StockTradeApply {
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
