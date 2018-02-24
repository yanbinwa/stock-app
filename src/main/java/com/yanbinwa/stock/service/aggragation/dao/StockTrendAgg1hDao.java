package com.yanbinwa.stock.service.aggragation.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.yanbinwa.stock.service.aggragation.entity.StockTrendAgg1h;

@Repository
public interface StockTrendAgg1hDao extends JpaRepository<StockTrendAgg1h, Long>, JpaSpecificationExecutor<StockTrendAgg1h>
{
    
}
