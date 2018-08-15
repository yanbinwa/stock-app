package com.yanbinwa.stock.service.trade.service;

import com.yanbinwa.stock.service.trade.element.BuildAccountRequest;
import com.yanbinwa.stock.service.trade.element.StockTradeRequest;

/**
 *
 * 股票购买的操作包括
 *
 * 1. 股票申请(需要写入到数据库中，记录时间，每天会进行一次清空)
 * 2. 股票撤单
 * 3. 申请查询
 * 4. 历史交易查询
 * 5. 账户概况查询（包括当前盈利等）
 *
 */
public interface StockTradeService {

    String applyTradeStock(StockTradeRequest stockTradeRequest);

    String recallTradeStock(Long id);

    String getStockTradeApply(Long accountId);

    String getStockTradeHistory(Long accountId);

    String buildAccount(BuildAccountRequest buildAccountRequest);

}