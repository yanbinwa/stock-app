package com.yanbinwa.stock.utils;

import com.emotibot.middleware.utils.StringUtils;
import com.yanbinwa.stock.entity.stockTrend.StockTrend;
import com.yanbinwa.stock.entity.stockTrend.StockTrendType;
import com.yanbinwa.stock.service.aggragation.dao.*;
import com.yanbinwa.stock.service.aggragation.entity.*;
import com.yanbinwa.stock.service.collection.dao.StockTrendRawDao;
import com.yanbinwa.stock.service.collection.entity.StockTrendRaw;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

@Slf4j
@Component
public class StockTrendUtils
{
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
            stockTrendUtils.stockTrendRawDao.saveAll(stockTrendListToStockTrendRawList(stockTrendList));
            break;
        case TYPE_1MIN:
            stockTrendUtils.stockTrendAgg1MinDao.saveAll(stockTrendListToStockTrendAgg1MinList(stockTrendList));
            break;
        case TYPE_5MIN:
            stockTrendUtils.stockTrendAgg5MinDao.saveAll(stockTrendListToStockTrendAgg5MinList(stockTrendList));
            break;
        case TYPE_1H:
            stockTrendUtils.stockTrendAgg1hDao.saveAll(stockTrendListToStockTrendAgg1hList(stockTrendList));
            break;
        case TYPE_1D:
            stockTrendUtils.stockTrendAgg1dDao.saveAll(stockTrendListToStockTrendAgg1dList(stockTrendList));
            break;
        case TYPE_1W:
            stockTrendUtils.stockTrendAgg1wDao.saveAll(stockTrendListToStockTrendAgg1wList(stockTrendList));
            break;
        case TYPE_1M:
            stockTrendUtils.stockTrendAgg1mDao.saveAll(stockTrendListToStockTrendAgg1mList(stockTrendList));
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
    
    public static List<StockTrend> getStockTrendByDate(StockTrendType type, String stockId, Date startDate, Date endDate)
    {
        switch(type)
        {
        case TYPE_RAW:
            return getStockTrendRawByDate(stockId, startDate, endDate);
        case TYPE_1MIN:
            return getStockTrendAgg1MinByDate(stockId, startDate, endDate);
        case TYPE_5MIN:
            return getStockTrendAgg5MinByDate(stockId, startDate, endDate);
        case TYPE_1H:
            return getStockTrendAgg1hByDate(stockId, startDate, endDate);
        case TYPE_1D:
            return getStockTrendAgg1dByDate(stockId, startDate, endDate);
        case TYPE_1W:
            return getStockTrendAgg1wByDate(stockId, startDate, endDate);
        case TYPE_1M:
            return getStockTrendAgg1mByDate(stockId, startDate, endDate);
        }
        return null;
    }
    
    private static Specification<StockTrendRaw> getSpecificationForStockTrendRawByDate(String stockId, Date startDate, Date endDate)
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
                if (stockId != null)
                {
                    predicates.add(cb.equal(root.get("stockId").as(String.class), stockId));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            } 
        };
        return querySpecifi;
    }
    
    private static Specification<StockTrendAgg1Min> getSpecificationForStockTrendAgg1MinByDate(String stockId, Date startDate, Date endDate)
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
                if (stockId != null)
                {
                    predicates.add(cb.equal(root.get("stockId").as(String.class), stockId));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            } 
        };
        return querySpecifi;
    }
    
    private static Specification<StockTrendAgg5Min> getSpecificationForStockTrendAgg5MinByDate(String stockId, Date startDate, Date endDate)
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
                if (stockId != null)
                {
                    predicates.add(cb.equal(root.get("stockId").as(String.class), stockId));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            } 
        };
        return querySpecifi;
    }
    
    private static Specification<StockTrendAgg1h> getSpecificationForStockTrendAgg1hByDate(String stockId, Date startDate, Date endDate)
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
                if (stockId != null)
                {
                    predicates.add(cb.equal(root.get("stockId").as(String.class), stockId));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            } 
        };
        return querySpecifi;
    }
    
    private static Specification<StockTrendAgg1d> getSpecificationForStockTrendAgg1dByDate(String stockId, Date startDate, Date endDate)
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
                if (stockId != null)
                {
                    predicates.add(cb.equal(root.get("stockId").as(String.class), stockId));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            } 
        };
        return querySpecifi;
    }
    
    private static Specification<StockTrendAgg1w> getSpecificationForStockTrendAgg1wByDate(String stockId, Date startDate, Date endDate)
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
                if (stockId != null)
                {
                    predicates.add(cb.equal(root.get("stockId").as(String.class), stockId));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            } 
        };
        return querySpecifi;
    }
    
    private static Specification<StockTrendAgg1m> getSpecificationForStockTrendAgg1mByDate(String stockId, Date startDate, Date endDate)
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
                if (stockId != null)
                {
                    predicates.add(cb.equal(root.get("stockId").as(String.class), stockId));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            } 
        };
        return querySpecifi;
    }
    
    private static List<StockTrend> getStockTrendRawByDate(String stockId, Date startDate, Date endDate)
    {
        List<StockTrendRaw> queryResult = stockTrendUtils.stockTrendRawDao.findAll(getSpecificationForStockTrendRawByDate(stockId, startDate, endDate));
        List<StockTrend> ret = new ArrayList<StockTrend>();
        ret.addAll(queryResult);
        return ret;
    }
    
    private static List<StockTrend> getStockTrendAgg1MinByDate(String stockId, Date startDate, Date endDate)
    {
        List<StockTrendAgg1Min> queryResult = stockTrendUtils.stockTrendAgg1MinDao.findAll(getSpecificationForStockTrendAgg1MinByDate(stockId, startDate, endDate));
        List<StockTrend> ret = new ArrayList<StockTrend>();
        ret.addAll(queryResult);
        return ret;
    }
    
    private static List<StockTrend> getStockTrendAgg5MinByDate(String stockId, Date startDate, Date endDate)
    {
        List<StockTrendAgg5Min> queryResult = stockTrendUtils.stockTrendAgg5MinDao.findAll(getSpecificationForStockTrendAgg5MinByDate(stockId, startDate, endDate));
        List<StockTrend> ret = new ArrayList<StockTrend>();
        ret.addAll(queryResult);
        return ret;
    }
    
    private static List<StockTrend> getStockTrendAgg1hByDate(String stockId, Date startDate, Date endDate)
    {
        List<StockTrendAgg1h> queryResult = stockTrendUtils.stockTrendAgg1hDao.findAll(getSpecificationForStockTrendAgg1hByDate(stockId, startDate, endDate));
        List<StockTrend> ret = new ArrayList<StockTrend>();
        ret.addAll(queryResult);
        return ret;
    }
    
    private static List<StockTrend> getStockTrendAgg1dByDate(String stockId, Date startDate, Date endDate)
    {
        List<StockTrendAgg1d> queryResult = stockTrendUtils.stockTrendAgg1dDao.findAll(getSpecificationForStockTrendAgg1dByDate(stockId, startDate, endDate));
        List<StockTrend> ret = new ArrayList<StockTrend>();
        ret.addAll(queryResult);
        return ret;
    }
    
    private static List<StockTrend> getStockTrendAgg1wByDate(String stockId, Date startDate, Date endDate)
    {
        List<StockTrendAgg1w> queryResult = stockTrendUtils.stockTrendAgg1wDao.findAll(getSpecificationForStockTrendAgg1wByDate(stockId, startDate, endDate));
        List<StockTrend> ret = new ArrayList<StockTrend>();
        ret.addAll(queryResult);
        return ret;
    }
    
    private static List<StockTrend> getStockTrendAgg1mByDate(String stockId, Date startDate, Date endDate)
    {
        List<StockTrendAgg1m> queryResult = stockTrendUtils.stockTrendAgg1mDao.findAll(getSpecificationForStockTrendAgg1mByDate(stockId, startDate, endDate));
        List<StockTrend> ret = new ArrayList<StockTrend>();
        ret.addAll(queryResult);
        return ret;
    }
    
    public static void deleteStockTrendByDate(StockTrendType type, String stockId, Date startDate, Date endDate)
    {
        List<StockTrend> deleteStockTrendList = getStockTrendByDate(type, stockId, startDate, endDate);
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
        stockTrendUtils.stockTrendRawDao.deleteAll(deleteStockTrends);
    }
    
    private static void deleteStockTrendAgg1MinByDate(List<StockTrend> deleteStockTrendList)
    {
        List<StockTrendAgg1Min> deleteStockTrends = stockTrendListToStockTrendAgg1MinList(deleteStockTrendList);
        stockTrendUtils.stockTrendAgg1MinDao.deleteAll(deleteStockTrends);
    }
    
    private static void deleteStockTrendAgg5MinByDate(List<StockTrend> deleteStockTrendList)
    {
        List<StockTrendAgg5Min> deleteStockTrends = stockTrendListToStockTrendAgg5MinList(deleteStockTrendList);
        stockTrendUtils.stockTrendAgg5MinDao.deleteAll(deleteStockTrends);
    }
    
    private static void deleteStockTrendAgg1hByDate(List<StockTrend> deleteStockTrendList)
    {
        List<StockTrendAgg1h> deleteStockTrends = stockTrendListToStockTrendAgg1hList(deleteStockTrendList);
        stockTrendUtils.stockTrendAgg1hDao.deleteAll(deleteStockTrends);
    }
    
    private static void deleteStockTrendAgg1dByDate(List<StockTrend> deleteStockTrendList)
    {
        List<StockTrendAgg1d> deleteStockTrends = stockTrendListToStockTrendAgg1dList(deleteStockTrendList);
        stockTrendUtils.stockTrendAgg1dDao.deleteAll(deleteStockTrends);
    }
    
    private static void deleteStockTrendAgg1wByDate(List<StockTrend> deleteStockTrendList)
    {
        List<StockTrendAgg1w> deleteStockTrends = stockTrendListToStockTrendAgg1wList(deleteStockTrendList);
        stockTrendUtils.stockTrendAgg1wDao.deleteAll(deleteStockTrends);
    }
    
    private static void deleteStockTrendAgg1mByDate(List<StockTrend> deleteStockTrendList)
    {
        List<StockTrendAgg1m> deleteStockTrends = stockTrendListToStockTrendAgg1mList(deleteStockTrendList);
        stockTrendUtils.stockTrendAgg1mDao.deleteAll(deleteStockTrends);
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
        long startTimestamp = endTimestamp - interval * 1000;
        List<StockTrend> fetchStockTrendList = StockTrendUtils.getStockTrendByDate(fromType, null, 
                new Date(startTimestamp), new Date(endTimestamp));
        if (fetchStockTrendList == null || fetchStockTrendList.isEmpty())
        {
            log.info("fetchStockTrendList is empty");
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
                log.error("can not build StockTrend");
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
            StockTrendUtils.deleteStockTrendByDate(fromType, null, new Date(startTimestamp), new Date(endTimestamp));
        }
    }
    
    public static List<StockTrend> getStockTrendFromWindowGap(List<StockTrend> stockTrendList, long startTimestamp, long endTimestamp)
    {
        List<StockTrend> ret = new ArrayList<StockTrend>();
        for (StockTrend stockTrend : stockTrendList)
        {
            if (stockTrend.getCreatedate().getTime() >= startTimestamp && stockTrend.getCreatedate().getTime() <= endTimestamp)
            {
                ret.add(stockTrend);
            }
        }
        return ret;
    }

    public static List<StockTrend> sortStockTrendByTimestamp(List<StockTrend> stockTrends) {
        Collections.sort(stockTrends, (o1, o2) -> {
            if (o1.getCreatedate().before(o2.getCreatedate())) {
                return -1;
            } else if (o1.getCreatedate().after(o2.getCreatedate())) {
                return 1;
            } else {
                return 0;
            }
        });
        return stockTrends;
    }

    public static Map<String, List<StockTrend>> classifyStockTrendById(List<StockTrend> stockTrends) {
        Map<String, List<StockTrend>> stockIdToStockTrendMap = new HashMap<>();
        for (StockTrend stockTrend : stockTrends) {
            String stockId = stockTrend.getStockId();
            List<StockTrend> stockTrendList = stockIdToStockTrendMap.get(stockId);
            if (stockTrendList == null) {
                stockTrendList = new ArrayList<>();
                stockIdToStockTrendMap.put(stockId, stockTrendList);
            }
            stockTrendList.add(stockTrend);
        }
        return stockIdToStockTrendMap;
    }

    /**
     * stockId + createTime, stockId 可以为空
     *
     * @param stockTrends
     * @return
     */
    public static List<StockTrend> removeDuplateStockTrend(List<StockTrend> stockTrends) {
        Map<String, StockTrend> ret = new HashMap<>();
        for (StockTrend stockTrend : stockTrends) {
            String stockId = StringUtils.isEmpty(stockTrend.getStockId()) ? "" : stockTrend.getStockId();
            String key = stockId + "_" + stockTrend.getCreatedate().getTime();
            ret.put(key, stockTrend);
        }
        return new ArrayList<>(ret.values());
    }
}
