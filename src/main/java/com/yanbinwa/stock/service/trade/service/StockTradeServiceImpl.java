package com.yanbinwa.stock.service.trade.service;

import com.yanbinwa.stock.common.response.ReturnInfo;
import com.yanbinwa.stock.service.trade.element.BuildAccountRequest;
import com.yanbinwa.stock.service.trade.element.StockTradeRequest;
import com.yanbinwa.stock.service.trade.entity.StockTrade;
import com.yanbinwa.stock.service.trade.entity.StockTradeApply;
import com.yanbinwa.stock.service.trade.utils.StockTradeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class StockTradeServiceImpl implements StockTradeService {

    @Autowired
    StockTradeUtils stockTradeUtils;

    /**
     * 1. 是否停牌
     * 2. 如果是买，就判断是否有钱，如果是卖，就判断是否账户里有这些股票
     *
     *
     * @param stockTradeRequest
     * @return
     */
    @Override
    public String applyTradeStock(StockTradeRequest stockTradeRequest) {
        if(stockTradeUtils.applyTradeStock(stockTradeRequest)) {
            return ReturnInfo.getReturnStr(null, ReturnInfo.OPERATION_SUCCESS);
        } else {
            return ReturnInfo.getReturnStr(null, ReturnInfo.APPLY_STOCK_FAIL);
        }
    }

    @Override
    public String recallTradeStock(Long id) {
        if(stockTradeUtils.recallTradeStock(id)) {
            return ReturnInfo.getReturnStr(null, ReturnInfo.OPERATION_SUCCESS);
        } else {
            return ReturnInfo.getReturnStr(null, ReturnInfo.APPLY_STOCK_FAIL);
        }
    }

    @Override
    public String getStockTradeApply(Long accountId) {
        List<StockTradeApply> stockTradeApplies = stockTradeUtils.getStockTradeApplyByAccountId(accountId);
        if (stockTradeApplies == null) {
            stockTradeApplies = new ArrayList<>();
        }
        return ReturnInfo.getReturnStr(stockTradeApplies, ReturnInfo.OPERATION_SUCCESS);
    }

    @Override
    public String getStockTradeHistory(Long accountId) {
        List<StockTrade> stockTrades = stockTradeUtils.getStockTradeByAccountId(accountId);
        if (stockTrades == null) {
            stockTrades = new ArrayList<>();
        }
        return ReturnInfo.getReturnStr(stockTrades, ReturnInfo.OPERATION_SUCCESS);
    }

    @Override
    public String buildAccount(BuildAccountRequest buildAccountRequest) {
        if(stockTradeUtils.buildAccount(buildAccountRequest)) {
            return ReturnInfo.getReturnStr(null, ReturnInfo.OPERATION_SUCCESS);
        } else {
            return ReturnInfo.getReturnStr(null, ReturnInfo.ACCOUNT_IS_EXIST);
        }
    }

}
