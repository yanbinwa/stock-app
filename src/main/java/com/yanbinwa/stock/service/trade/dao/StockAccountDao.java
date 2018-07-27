package com.yanbinwa.stock.service.trade.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.yanbinwa.stock.service.trade.entity.StockAccount;

public interface StockAccountDao extends JpaRepository<StockAccount, Long>, JpaSpecificationExecutor<StockAccount>
{

}
