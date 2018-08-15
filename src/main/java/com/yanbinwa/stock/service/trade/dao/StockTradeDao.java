package com.yanbinwa.stock.service.trade.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.yanbinwa.stock.service.trade.entity.StockTrade;

import java.util.List;

@Repository
public interface StockTradeDao extends JpaRepository<StockTrade, Long>, JpaSpecificationExecutor<StockTrade> {
    List<StockTrade> findAllByAccountId(Long accountId);
}