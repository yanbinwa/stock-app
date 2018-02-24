package com.yanbinwa.stock.service.aggragation.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.yanbinwa.stock.service.aggragation.entity.StockTrendAgg1m;

@Repository
public interface StockTrendAgg1mDao extends JpaRepository<StockTrendAgg1m, Long>, JpaSpecificationExecutor<StockTrendAgg1m>
{
    
}
