package com.yanbinwa.stock.service.aggragation.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.yanbinwa.stock.service.aggragation.entity.StockTrendAgg5Min;

@Repository
public interface StockTrendAgg5MinDao extends JpaRepository<StockTrendAgg5Min, Long>, JpaSpecificationExecutor<StockTrendAgg5Min>
{
    
}
