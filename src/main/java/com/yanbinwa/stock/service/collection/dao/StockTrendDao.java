package com.yanbinwa.stock.service.collection.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yanbinwa.stock.service.collection.entity.StockTrend;

@Repository
public interface StockTrendDao extends JpaRepository<StockTrend, Long>
{
    
}
