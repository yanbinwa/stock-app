package com.yanbinwa.stock.service.aggragation.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.yanbinwa.stock.service.aggragation.entity.StockTrendAgg1w;

@Repository
public interface StockTrendAgg1wDao extends JpaRepository<StockTrendAgg1w, Long>, JpaSpecificationExecutor<StockTrendAgg1w>
{
    
}
