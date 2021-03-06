package com.yanbinwa.stock.service.collection.utils;

import com.emotibot.middleware.utils.JsonUtils;
import com.emotibot.middleware.utils.StringUtils;
import com.google.gson.reflect.TypeToken;
import com.yanbinwa.stock.common.redis.RedisUtils;
import com.yanbinwa.stock.entity.stockTrend.StockTrend;
import com.yanbinwa.stock.service.collection.element.Industry;
import com.yanbinwa.stock.service.collection.entity.StockMetaData;
import com.yanbinwa.stock.service.collection.entity.StockTrendRaw;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 包含redis的操作
 * 
 * @author emotibot
 *
 */

public class CollectionUtils
{
    public static void setCommissionIndustry(String commissionIndustry)
    {
        RedisUtils.redisUtils.set(generateKey(MyConstants.COMMISSION_INDUSTRY_KEY), commissionIndustry);
    }
    
    @SuppressWarnings("unchecked")
    public static Map<String, Industry> getCommissionIndustry()
    {
        String json = getRedisCache(generateKey(MyConstants.COMMISSION_INDUSTRY_KEY));
        if (StringUtils.isEmpty(json))
        {
            return null;
        }
        Type resultType = new TypeToken<Map<String, Industry>>(){}.getType();
        Map<String, Industry> industryMap = (Map<String, Industry>) JsonUtils.getObject(json, resultType);
        return industryMap;
    }
    
    public static void deleteCommissionIndustry()
    {
        deleteRedisCache(generateKey(MyConstants.COMMISSION_INDUSTRY_KEY));
    }
    
    public static void setIndustryToStockId(String industryName, List<String> stockIds)
    {
        setRedisCache(generateKey(MyConstants.INDUSTRY_STOCKID_KEY + "-" + industryName), stockIds.toString());
    }

    public static List<String> getAllIndustryName() {
        Map<String, Industry> industryMap = getCommissionIndustry();
        if (industryMap == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(industryMap.keySet());
    }

    @SuppressWarnings("unchecked")
    public static List<String> getIndustryToStockId(String industryName)
    {
        String json = getRedisCache(generateKey(MyConstants.INDUSTRY_STOCKID_KEY + "-" + industryName));
        if (StringUtils.isEmpty(json))
        {
            return null;
        }
        Type stockIdType = new TypeToken<List<String>>(){}.getType();
        List<String> stockIdList = (List<String>) JsonUtils.getObject(json, stockIdType);
        return stockIdList;
    }

    public static Map<String, String> getStockIdToIndustryNameMap() {
        List<String> industryNames = getAllIndustryName();
        Map<String, String> stockInToIndustryNameMap = new HashMap<>();
        for (String industryName : industryNames) {
            List<String> stockIds = getIndustryToStockId(industryName);
            for (String stockId : stockIds) {
                stockInToIndustryNameMap.put(stockId, industryName);
            }
        }
        return stockInToIndustryNameMap;
    }
    
    public static void deleteIndustryToStockId(String industryName)
    {
        deleteRedisCache(generateKey(MyConstants.INDUSTRY_STOCKID_KEY + "-" + industryName));
    }
    
    public static void deleteAllIndustryToStockId()
    {
        deleteAllRedisCache(generateKey(MyConstants.INDUSTRY_STOCKID_KEY + "*"));
    }
    
    public static void setStockMetaData(String stockId, StockMetaData stockMetaData)
    {
        setRedisCache(generateKey(MyConstants.STOCK_META_DATA_KEY + "-" + stockId), stockMetaData.toString());
    }
    
    public static StockMetaData getStockMetaData(String stockId)
    {
        String json = getRedisCache(generateKey(MyConstants.STOCK_META_DATA_KEY + "-" + stockId));
        if (StringUtils.isEmpty(json))
        {
            return null;
        }
        StockMetaData stockMetaData = (StockMetaData) JsonUtils.getObject(json, StockMetaData.class);
        return stockMetaData;
    }
    
    public static void deleteStockMetaData(String stockId)
    {
        deleteRedisCache(generateKey(MyConstants.STOCK_META_DATA_KEY + "-" + stockId));
    }
    
    public static void deleteAllStockMetaData()
    {
        deleteAllRedisCache(generateKey(MyConstants.STOCK_META_DATA_KEY + "*"));
    }
    
    public static List<String> getAllStockId()
    {
        Set<String> keys = getRedisKeys(generateKey(MyConstants.STOCK_META_DATA_KEY + "-*"));
        if (keys == null)
        {
            return null;
        }
        List<String> ret = new ArrayList<>();
        keys.stream().map(key -> key.replace(MyConstants.COLLECTION_KEY + "-" + MyConstants.STOCK_META_DATA_KEY + "-", "")).forEach(stockId -> ret.add(stockId));

        return ret;
    }
    
    public static void setStockTrendData(String stockId, StockTrend stockTrend)
    {
        setRedisCache(generateKey(MyConstants.STOCK_TREND_KEY + "-" + stockId), stockTrend.toString());
    }
    
    public static StockTrend getStockTrendData(String stockId)
    {
        String json = getRedisCache(generateKey(MyConstants.STOCK_TREND_KEY + "-" + stockId));
        if (StringUtils.isEmpty(json))
        {
            return null;
        }
        StockTrendRaw stockTrend = (StockTrendRaw) JsonUtils.getObject(json, StockTrendRaw.class);
        return stockTrend;
    }
    
    private static Set<String> getRedisKeys(String keyPattern)
    {
        Set<String> ret = RedisUtils.redisUtils.keys(keyPattern);
        return ret;
    }
    
    private static String getRedisCache(String key)
    {
        String ret = RedisUtils.redisUtils.get(key);
        return ret;
    }
    
    private static void setRedisCache(String key, String value)
    {
        RedisUtils.redisUtils.set(key, value);
    }
    
    private static void deleteRedisCache(String key)
    {
        RedisUtils.redisUtils.remove(key);
    }
    
    private static void deleteAllRedisCache(String keyPattern)
    {
        Set<String> keys = RedisUtils.redisUtils.keys(keyPattern);
        if (keys == null)
        {
            return;
        }
        for (String key : keys)
        {
            RedisUtils.redisUtils.remove(key);
        }
    }
    
    private static String generateKey(String originalKey)
    {
        return RedisUtils.redisUtils.generalKey(MyConstants.COLLECTION_KEY, originalKey);
    }
}

class MyConstants 
{
    public static final String COLLECTION_KEY = "collection";
    public static final String COMMISSION_INDUSTRY_KEY = "commission_industry";
    public static final String INDUSTRY_STOCKID_KEY = "industry_stockId";
    public static final String STOCK_META_DATA_KEY = "stock_meta_data";
    public static final String STOCK_TREND_KEY = "stock_trend_data";
}
