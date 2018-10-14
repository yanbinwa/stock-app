package com.yanbinwa.stock.service.trade.controller;

import com.yanbinwa.stock.service.trade.element.BuildAccountRequest;
import com.yanbinwa.stock.service.trade.element.StockTradeRequest;
import com.yanbinwa.stock.service.trade.service.StockTradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 实现账户开户，存入金额
 *
 *
 */
@RestController
public class TradeController {

    @Autowired
    StockTradeService stockTradeService;

    @PostMapping(value = "/trade/buildAccount", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String buildAccount(@RequestBody BuildAccountRequest buildAccountRequest) {
        return stockTradeService.buildAccount(buildAccountRequest);
    }

    @PostMapping(value = "/trade/applyStock", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String applyStock(@RequestBody StockTradeRequest stockTradeRequest) {
        return stockTradeService.applyTradeStock(stockTradeRequest);
    }

    @PostMapping(value = "/trade/recallTradeStock", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String recallTradeStock(@RequestParam("id") Long id) {
        return stockTradeService.recallTradeStock(id);
    }
}
