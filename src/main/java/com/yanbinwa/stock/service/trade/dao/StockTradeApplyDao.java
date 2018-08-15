package com.yanbinwa.stock.service.trade.dao;

import com.yanbinwa.stock.service.trade.entity.StockTradeApply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockTradeApplyDao extends JpaRepository<StockTradeApply, Long>, JpaSpecificationExecutor<StockTradeApply> {
    List<StockTradeApply> findAllByAccountId(Long accountId);
}
