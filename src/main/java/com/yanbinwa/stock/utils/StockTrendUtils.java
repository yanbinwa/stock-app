package com.yanbinwa.stock.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.yanbinwa.stock.entity.stockTrend.StockTrend;
import com.yanbinwa.stock.entity.stockTrend.StockTrendType;
import com.yanbinwa.stock.service.aggragation.dao.StockTrendAgg1MinDao;
import com.yanbinwa.stock.service.aggragation.dao.StockTrendAgg1dDao;
import com.yanbinwa.stock.service.aggragation.dao.StockTrendAgg1hDao;
import com.yanbinwa.stock.service.aggragation.dao.StockTrendAgg1mDao;
import com.yanbinwa.stock.service.aggragation.dao.StockTrendAgg1wDao;
import com.yanbinwa.stock.service.aggragation.dao.StockTrendAgg5MinDao;
import com.yanbinwa.stock.service.aggragation.entity.StockTrendAgg1Min;
import com.yanbinwa.stock.service.aggragation.entity.StockTrendAgg1d;
import com.yanbinwa.stock.service.aggragation.entity.StockTrendAgg1h;
import com.yanbinwa.stock.service.aggragation.entity.StockTrendAgg1m;
import com.yanbinwa.stock.service.aggragation.entity.StockTrendAgg1w;
import com.yanbinwa.stock.service.aggragation.entity.StockTrendAgg5Min;
import com.yanbinwa.stock.service.collection.dao.StockTrendRawDao;
import com.yanbinwa.stock.service.collection.entity.StockTrendRaw;

@Component
public class StockTrendUtils
{
    private static Logger logger = Logger.getLogger(StockTrendUtils.class);
    public static StockTrendUtils stockTrendUtils;
    
    @Autowired
    StockTrendRawDao stockTrendRawDao;
    
    @Autowired
    StockTrendAgg1MinDao stockTrendAgg1MinDao;
    
    @Autowired
    StockTrendAgg5MinDao stockTrendAgg5MinDao;
    
    @Autowired
    StockTrendAgg1hDao stockTrendAgg1hDao;
    
    @Autowired
    StockTrendAgg1dDao stockTrendAgg1dDao;
    
    @Autowired
    StockTrendAgg1wDao stockTrendAgg1wDao;
    
    @Autowired
    StockTrendAgg1mDao stockTrendAgg1mDao;
    
    @PostConstruct
    public void init() 
    {    
        stockTrendUtils = this;
    } 
    
    public static void storeStockTrend(StockTrend stockTrend, StockTrendType type)
    {
        switch(type)
        {
        case TYPE_RAW:
            stockTrendUtils.stockTrendRawDao.save((StockTrendRaw)stockTrend);
            break;
        case TYPE_1MIN:
            stockTrendUtils.stockTrendAgg1MinDao.save((StockTrendAgg1Min)stockTrend);
            break;
        case TYPE_5MIN:
            stockTrendUtils.stockTrendAgg5MinDao.save((StockTrendAgg5Min)stockTrend);
            break;
        case TYPE_1H:
            stockTrendUtils.stockTrendAgg1hDao.save((StockTrendAgg1h)stockTrend);
            break;
        case TYPE_1D:
            stockTrendUtils.stockTrendAgg1dDao.save((StockTrendAgg1d)stockTrend);
            break;
        case TYPE_1W:
            stockTrendUtils.stockTrendAgg1wDao.save((StockTrendAgg1w)stockTrend);
            break;
        case TYPE_1M:
            stockTrendUtils.stockTrendAgg1mDao.save((StockTrendAgg1m)stockTrend);
            break;
        }
    }
    
    public static void storeStockTrend(List<StockTrend> stockTrendList, StockTrendType type)
    {
        switch(type)
        {
        case TYPE_RAW:
            stockTrendUtils.stockTrendRawDao.save(stockTrendListToStockTrendRawList(stockTrendList));
            break;
        case TYPE_1MIN:
            stockTrendUtils.stockTrendAgg1MinDao.save(stockTrendListToStockTrendAgg1MinList(stockTrendList));
            break;
        case TYPE_5MIN:
            stockTrendUtils.stockTrendAgg5MinDao.save(stockTrendListToStockTrendAgg5MinList(stockTrendList));
            break;
        case TYPE_1H:
            stockTrendUtils.stockTrendAgg1hDao.save(stockTrendListToStockTrendAgg1hList(stockTrendList));
            break;
        case TYPE_1D:
            stockTrendUtils.stockTrendAgg1dDao.save(stockTrendListToStockTrendAgg1dList(stockTrendList));
            break;
        case TYPE_1W:
            stockTrendUtils.stockTrendAgg1wDao.save(stockTrendListToStockTrendAgg1wList(stockTrendList));
            break;
        case TYPE_1M:
            stockTrendUtils.stockTrendAgg1mDao.save(stockTrendListToStockTrendAgg1mList(stockTrendList));
            break;
        }
    }
    
    public static List<StockTrendRaw> stockTrendListToStockTrendRawList(List<StockTrend> stockList)
    {
        List<StockTrendRaw> newStockTrendList = new ArrayList<StockTrendRaw>();
        for (StockTrend stockTrend : stockList)
        {
            newStockTrendList.add((StockTrendRaw)stockTrend);
        }
        return newStockTrendList;
    }
    
    public static List<StockTrendAgg1Min> stockTrendListToStockTrendAgg1MinList(List<StockTrend> stockList)
    {
        List<StockTrendAgg1Min> newStockTrendList = new ArrayList<StockTrendAgg1Min>();
        for (StockTrend stockTrend : stockList)
        {
            newStockTrendList.add((StockTrendAgg1Min)stockTrend);
        }
        return newStockTrendList;
    }
    
    public static List<StockTrendAgg5Min> stockTrendListToStockTrendAgg5MinList(List<StockTrend> stockList)
    {
        List<StockTrendAgg5Min> newStockTrendList = new ArrayList<StockTrendAgg5Min>();
        for (StockTrend stockTrend : stockList)
        {
            newStockTrendList.add((StockTrendAgg5Min)stockTrend);
        }
        return newStockTrendList;
    }
    
    public static List<StockTrendAgg1h> stockTrendListToStockTrendAgg1hList(List<StockTrend> stockList)
    {
        List<StockTrendAgg1h> newStockTrendList = new ArrayList<StockTrendAgg1h>();
        for (StockTrend stockTrend : stockList)
        {
            newStockTrendList.add((StockTrendAgg1h)stockTrend);
        }
        return newStockTrendList;
    }
    
    public static List<StockTrendAgg1d> stockTrendListToStockTrendAgg1dList(List<StockTrend> stockList)
    {
        List<StockTrendAgg1d> newStockTrendList = new ArrayList<StockTrendAgg1d>();
        for (StockTrend stockTrend : stockList)
        {
            newStockTrendList.add((StockTrendAgg1d)stockTrend);
        }
        return newStockTrendList;
    }
    
    public static List<StockTrendAgg1w> stockTrendListToStockTrendAgg1wList(List<StockTrend> stockList)
    {
        List<StockTrendAgg1w> newStockTrendList = new ArrayList<StockTrendAgg1w>();
        for (StockTrend stockTrend : stockList)
        {
            newStockTrendList.add((StockTrendAgg1w)stockTrend);
        }
        return newStockTrendList;
    }
    
    public static List<StockTrendAgg1m> stockTrendListToStockTrendAgg1mList(List<StockTrend> stockList)
    {
        List<StockTrendAgg1m> newStockTrendList = new ArrayList<StockTrendAgg1m>();
        for (StockTrend stockTrend : stockList)
        {
            newStockTrendList.add((StockTrendAgg1m)stockTrend);
        }
        return newStockTrendList;
    }
    
    public static List<StockTrend> getStockTrendByDate(StockTrendType type, Date startDate, Date endDate)
    {
        switch(type)
        {
        case TYPE_RAW:
            return getStockTrendRawByDate(startDate, endDate);
        case TYPE_1MIN:
            return getStockTrendAgg1MinByDate(startDate, endDate);
        case TYPE_5MIN:
            return getStockTrendAgg5MinByDate(startDate, endDate);
        case TYPE_1H:
            return getStockTrendAgg1hByDate(startDate, endDate);
        case TYPE_1D:
            return getStockTrendAgg1dByDate(startDate, endDate);
        case TYPE_1W:
            return getStockTrendAgg1wByDate(startDate, endDate);
        case TYPE_1M:
            return getStockTrendAgg1mByDate(startDate, endDate);
        }
        return null;
    }
    
    private static Specification<StockTrendRaw> getSpecificationForStockTrendRawByDate(Date startDate, Date endDate)
    {
        Specification<StockTrendRaw> querySpecifi = new Specification<StockTrendRaw>() 
        {
            @Override
            public Predicate toPredicate(Root<StockTrendRaw> root, CriteriaQuery<?> paramCriteriaQuery,
                    CriteriaBuilder cb)
            {
                List<Predicate> predicates = new ArrayList<>();
                if (startDate != null)
                {
                    predicates.add(cb.greaterThanOrEqualTo(root.get("createdate").as(Date.class), startDate));
                }
                if (endDate != null)
                {
                    predicates.add(cb.lessThanOrEqualTo(root.get("createdate").as(Date.class), endDate));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            } 
        };
        return querySpecifi;
    }
    
    private static Specification<StockTrendAgg1Min> getSpecificationForStockTrendAgg1MinByDate(Date startDate, Date endDate)
    {
        Specification<StockTrendAgg1Min> querySpecifi = new Specification<StockTrendAgg1Min>() 
        {
            @Override
            public Predicate toPredicate(Root<StockTrendAgg1Min> root, CriteriaQuery<?> paramCriteriaQuery,
                    CriteriaBuilder cb)
            {
                List<Predicate> predicates = new ArrayList<>();
                if (startDate != null)
                {
                    predicates.add(cb.greaterThanOrEqualTo(root.get("createdate").as(Date.class), startDate));
                }
                if (endDate != null)
                {
                    predicates.add(cb.lessThanOrEqualTo(root.get("createdate").as(Date.class), endDate));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            } 
        };
        return querySpecifi;
    }
    
    private static Specification<StockTrendAgg5Min> getSpecificationForStockTrendAgg5MinByDate(Date startDate, Date endDate)
    {
        Specification<StockTrendAgg5Min> querySpecifi = new Specification<StockTrendAgg5Min>() 
        {
            @Override
            public Predicate toPredicate(Root<StockTrendAgg5Min> root, CriteriaQuery<?> paramCriteriaQuery,
                    CriteriaBuilder cb)
            {
                List<Predicate> predicates = new ArrayList<>();
                if (startDate != null)
                {
                    predicates.add(cb.greaterThanOrEqualTo(root.get("createdate").as(Date.class), startDate));
                }
                if (endDate != null)
                {
                    predicates.add(cb.lessThanOrEqualTo(root.get("createdate").as(Date.class), endDate));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            } 
        };
        return querySpecifi;
    }
    
    private static Specification<StockTrendAgg1h> getSpecificationForStockTrendAgg1hByDate(Date startDate, Date endDate)
    {
        Specification<StockTrendAgg1h> querySpecifi = new Specification<StockTrendAgg1h>() 
        {
            @Override
            public Predicate toPredicate(Root<StockTrendAgg1h> root, CriteriaQuery<?> paramCriteriaQuery,
                    CriteriaBuilder cb)
            {
                List<Predicate> predicates = new ArrayList<>();
                if (startDate != null)
                {
                    predicates.add(cb.greaterThanOrEqualTo(root.get("createdate").as(Date.class), startDate));
                }
                if (endDate != null)
                {
                    predicates.add(cb.lessThanOrEqualTo(root.get("createdate").as(Date.class), endDate));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            } 
        };
        return querySpecifi;
    }
    
    private static Specification<StockTrendAgg1d> getSpecificationForStockTrendAgg1dByDate(Date startDate, Date endDate)
    {
        Specification<StockTrendAgg1d> querySpecifi = new Specification<StockTrendAgg1d>() 
        {
            @Override
            public Predicate toPredicate(Root<StockTrendAgg1d> root, CriteriaQuery<?> paramCriteriaQuery,
                    CriteriaBuilder cb)
            {
                List<Predicate> predicates = new ArrayList<>();
                if (startDate != null)
                {
                    predicates.add(cb.greaterThanOrEqualTo(root.get("createdate").as(Date.class), startDate));
                }
                if (endDate != null)
                {
                    predicates.add(cb.lessThanOrEqualTo(root.get("createdate").as(Date.class), endDate));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            } 
        };
        return querySpecifi;
    }
    
    private static Specification<StockTrendAgg1w> getSpecificationForStockTrendAgg1wByDate(Date startDate, Date endDate)
    {
        Specification<StockTrendAgg1w> querySpecifi = new Specification<StockTrendAgg1w>() 
        {
            @Override
            public Predicate toPredicate(Root<StockTrendAgg1w> root, CriteriaQuery<?> paramCriteriaQuery,
                    CriteriaBuilder cb)
            {
                List<Predicate> predicates = new ArrayList<>();
                if (startDate != null)
                {
                    predicates.add(cb.greaterThanOrEqualTo(root.get("createdate").as(Date.class), startDate));
                }
                if (endDate != null)
                {
                    predicates.add(cb.lessThanOrEqualTo(root.get("createdate").as(Date.class), endDate));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            } 
        };
        return querySpecifi;
    }
    
    private static Specification<StockTrendAgg1m> getSpecificationForStockTrendAgg1mByDate(Date startDate, Date endDate)
    {
        Specification<StockTrendAgg1m> querySpecifi = new Specification<StockTrendAgg1m>() 
        {
            @Override
            public Predicate toPredicate(Root<StockTrendAgg1m> root, CriteriaQuery<?> paramCriteriaQuery,
                    CriteriaBuilder cb)
            {
                List<Predicate> predicates = new ArrayList<>();
                if (startDate != null)
                {
                    predicates.add(cb.greaterThanOrEqualTo(root.get("createdate").as(Date.class), startDate));
                }
                if (endDate != null)
                {
                    predicates.add(cb.lessThanOrEqualTo(root.get("createdate").as(Date.class), endDate));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            } 
        };
        return querySpecifi;
    }
    
    private static List<StockTrend> getStockTrendRawByDate(Date startDate, Date endDate)
    {
        List<StockTrendRaw> queryResult = stockTrendUtils.stockTrendRawDao.findAll(getSpecificationForStockTrendRawByDate(startDate, endDate));
        List<StockTrend> ret = new ArrayList<StockTrend>();
        ret.addAll(queryResult);
        return ret;
    }
    
    private static List<StockTrend> getStockTrendAgg1MinByDate(Date startDate, Date endDate)
    {
        List<StockTrendAgg1Min> queryResult = stockTrendUtils.stockTrendAgg1MinDao.findAll(getSpecificationForStockTrendAgg1MinByDate(startDate, endDate));
        List<StockTrend> ret = new ArrayList<StockTrend>();
        ret.addAll(queryResult);
        return ret;
    }
    
    private static List<StockTrend> getStockTrendAgg5MinByDate(Date startDate, Date endDate)
    {
        List<StockTrendAgg5Min> queryResult = stockTrendUtils.stockTrendAgg5MinDao.findAll(getSpecificationForStockTrendAgg5MinByDate(startDate, endDate));
        List<StockTrend> ret = new ArrayList<StockTrend>();
        ret.addAll(queryResult);
        return ret;
    }
    
    private static List<StockTrend> getStockTrendAgg1hByDate(Date startDate, Date endDate)
    {
        List<StockTrendAgg1h> queryResult = stockTrendUtils.stockTrendAgg1hDao.findAll(getSpecificationForStockTrendAgg1hByDate(startDate, endDate));
        List<StockTrend> ret = new ArrayList<StockTrend>();
        ret.addAll(queryResult);
        return ret;
    }
    
    private static List<StockTrend> getStockTrendAgg1dByDate(Date startDate, Date endDate)
    {
        List<StockTrendAgg1d> queryResult = stockTrendUtils.stockTrendAgg1dDao.findAll(getSpecificationForStockTrendAgg1dByDate(startDate, endDate));
        List<StockTrend> ret = new ArrayList<StockTrend>();
        ret.addAll(queryResult);
        return ret;
    }
    
    private static List<StockTrend> getStockTrendAgg1wByDate(Date startDate, Date endDate)
    {
        List<StockTrendAgg1w> queryResult = stockTrendUtils.stockTrendAgg1wDao.findAll(getSpecificationForStockTrendAgg1wByDate(startDate, endDate));
        List<StockTrend> ret = new ArrayList<StockTrend>();
        ret.addAll(queryResult);
        return ret;
    }
    
    private static List<StockTrend> getStockTrendAgg1mByDate(Date startDate, Date endDate)
    {
        List<StockTrendAgg1m> queryResult = stockTrendUtils.stockTrendAgg1mDao.findAll(getSpecificationForStockTrendAgg1mByDate(startDate, endDate));
        List<StockTrend> ret = new ArrayList<StockTrend>();
        ret.addAll(queryResult);
        return ret;
    }
    
    public static void deleteStockTrendByDate(StockTrendType type, Date startDate, Date endDate)
    {
        List<StockTrend> deleteStockTrendList = getStockTrendByDate(type, startDate, endDate);
        if (deleteStockTrendList == null || deleteStockTrendList.isEmpty())
        {
            return;
        }
        switch(type)
        {
        case TYPE_RAW:
            deleteStockTrendRawByDate(deleteStockTrendList);
            break;
        case TYPE_1MIN:
            deleteStockTrendAgg1MinByDate(deleteStockTrendList);
            break;
        case TYPE_5MIN:
            deleteStockTrendAgg5MinByDate(deleteStockTrendList);
            break;
        case TYPE_1H:
            deleteStockTrendAgg1hByDate(deleteStockTrendList);
            break;
        case TYPE_1D:
            deleteStockTrendAgg1dByDate(deleteStockTrendList);
            break;
        case TYPE_1W:
            deleteStockTrendAgg1wByDate(deleteStockTrendList);
            break;
        case TYPE_1M:
            deleteStockTrendAgg1mByDate(deleteStockTrendList);
            break;
        }
    }
    
    private static void deleteStockTrendRawByDate(List<StockTrend> deleteStockTrendList)
    {
        List<StockTrendRaw> deleteStockTrends = stockTrendListToStockTrendRawList(deleteStockTrendList);
        stockTrendUtils.stockTrendRawDao.delete(deleteStockTrends);
    }
    
    private static void deleteStockTrendAgg1MinByDate(List<StockTrend> deleteStockTrendList)
    {
        List<StockTrendAgg1Min> deleteStockTrends = stockTrendListToStockTrendAgg1MinList(deleteStockTrendList);
        stockTrendUtils.stockTrendAgg1MinDao.delete(deleteStockTrends);
    }
    
    private static void deleteStockTrendAgg5MinByDate(List<StockTrend> deleteStockTrendList)
    {
        List<StockTrendAgg5Min> deleteStockTrends = stockTrendListToStockTrendAgg5MinList(deleteStockTrendList);
        stockTrendUtils.stockTrendAgg5MinDao.delete(deleteStockTrends);
    }
    
    private static void deleteStockTrendAgg1hByDate(List<StockTrend> deleteStockTrendList)
    {
        List<StockTrendAgg1h> deleteStockTrends = stockTrendListToStockTrendAgg1hList(deleteStockTrendList);
        stockTrendUtils.stockTrendAgg1hDao.delete(deleteStockTrends);
    }
    
    private static void deleteStockTrendAgg1dByDate(List<StockTrend> deleteStockTrendList)
    {
        List<StockTrendAgg1d> deleteStockTrends = stockTrendListToStockTrendAgg1dList(deleteStockTrendList);
        stockTrendUtils.stockTrendAgg1dDao.delete(deleteStockTrends);
    }
    
    private static void deleteStockTrendAgg1wByDate(List<StockTrend> deleteStockTrendList)
    {
        List<StockTrendAgg1w> deleteStockTrends = stockTrendListToStockTrendAgg1wList(deleteStockTrendList);
        stockTrendUtils.stockTrendAgg1wDao.delete(deleteStockTrends);
    }
    
    private static void deleteStockTrendAgg1mByDate(List<StockTrend> deleteStockTrendList)
    {
        List<StockTrendAgg1m> deleteStockTrends = stockTrendListToStockTrendAgg1mList(deleteStockTrendList);
        stockTrendUtils.stockTrendAgg1mDao.delete(deleteStockTrends);
    }
    
    /**
     * @param fromType  聚合数据的type
     * @param toType    聚合结果数据的type
     * @param interval  聚合的时间间隔
     * @param tag       是否从数据库中删除聚合数据
     */
    public static void stockTrendAggragationLogic(StockTrendType fromType, StockTrendType toType, int interval, boolean tag, Class<? extends StockTrend> stockTrendClass)
    {
        long endTimestamp = System.currentTimeMillis();
        //long startTimestamp = endTimestamp - interval * 1000;
        long startTimestamp = 0;
        List<StockTrend> fetchStockTrendList = StockTrendUtils.getStockTrendByDate(fromType, 
                new Date(startTimestamp), new Date(endTimestamp));
        if (fetchStockTrendList == null || fetchStockTrendList.isEmpty())
        {
            logger.info("fetchStockTrendList is empty");
            return;
        }
        Map<String, List<StockTrend>> stockIdToStockTrendListMap = new HashMap<String, List<StockTrend>>();
        for (StockTrend stockTrend : fetchStockTrendList)
        {
            String stockId = stockTrend.getStockId();
            List<StockTrend> stockTrendList = stockIdToStockTrendListMap.get(stockId);
            if (stockTrendList == null)
            {
                stockTrendList = new ArrayList<StockTrend>();
                stockIdToStockTrendListMap.put(stockId, stockTrendList);
            }
            stockTrendList.add(stockTrend);
        }
        List<StockTrend> resultStockTrendList = new ArrayList<StockTrend>();
        for (Map.Entry<String, List<StockTrend>> entry : stockIdToStockTrendListMap.entrySet())
        {
            String stockId = entry.getKey();
            List<StockTrend> stockTrendList = entry.getValue();
            StockTrend resultStockTrend;
            try
            {
                resultStockTrend = stockTrendClass.newInstance();
            } 
            catch (Exception e)
            {
                e.printStackTrace();
                logger.error("can not build StockTrend");
                return;
            } 
            double aggCurrentPrice = 0;
            for (StockTrend stockTrend : stockTrendList)
            {
                aggCurrentPrice += stockTrend.getCurrentPrice();
            }
            aggCurrentPrice = aggCurrentPrice / stockTrendList.size();
            resultStockTrend.setStockId(stockId);
            resultStockTrend.setCurrentPrice(aggCurrentPrice);
            resultStockTrend.setCreatedate(new Date(endTimestamp));
            resultStockTrendList.add(resultStockTrend);
        }
        StockTrendUtils.storeStockTrend(resultStockTrendList, toType);
        if (tag)
        {
            StockTrendUtils.deleteStockTrendByDate(fromType, new Date(startTimestamp), new Date(endTimestamp));
        }
    }
}
