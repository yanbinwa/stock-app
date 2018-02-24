package com.yanbinwa.stock.service.aggragation.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.yanbinwa.stock.service.aggragation.entity.StockTrendAgg1Min;

@Repository
public interface StockTrendAgg1MinDao extends JpaRepository<StockTrendAgg1Min, Long>, JpaSpecificationExecutor<StockTrendAgg1Min>
{
    
}
