package com.yanbinwa.stock.service.aggragation.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.yanbinwa.stock.service.aggragation.entity.StockTrendAgg1d;

@Repository
public interface StockTrendAgg1dDao extends JpaRepository<StockTrendAgg1d, Long>, JpaSpecificationExecutor<StockTrendAgg1d>
{
    
}
