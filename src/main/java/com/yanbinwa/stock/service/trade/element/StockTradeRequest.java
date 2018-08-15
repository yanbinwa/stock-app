package com.yanbinwa.stock.service.trade.element;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockTradeRequest
{
    private String stockId;
    private Long accountId;
    private double price;
    private int num;
    private boolean isBuyStock;
}
