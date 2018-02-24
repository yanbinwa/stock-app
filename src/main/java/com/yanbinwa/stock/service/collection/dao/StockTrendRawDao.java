package com.yanbinwa.stock.service.collection.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.yanbinwa.stock.service.collection.entity.StockTrendRaw;

@Repository
public interface StockTrendRawDao extends JpaRepository<StockTrendRaw, Long>, JpaSpecificationExecutor<StockTrendRaw>
{
    
}
