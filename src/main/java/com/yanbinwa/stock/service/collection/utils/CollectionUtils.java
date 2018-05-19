package com.yanbinwa.stock.service.collection.utils;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.emotibot.middleware.utils.JsonUtils;
import com.emotibot.middleware.utils.StringUtils;
import com.google.gson.reflect.TypeToken;
import com.yanbinwa.stock.common.utils.RedisUtils;
import com.yanbinwa.stock.service.collection.element.Industry;
import com.yanbinwa.stock.service.collection.entity.StockMetaData;
import com.yanbinwa.stock.service.collection.entity.StockTrendRaw;

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
        if (RedisUtils.isReady())
        {
            RedisUtils.set(generateKey(MyConstants.COMMISSION_INDUSTRY_KEY), commissionIndustry);
        }
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
    
    public static void deleteIndustryToStockId(String industryName)
    {
        deleteRedisCache(generateKey(MyConstants.INDUSTRY_STOCKID_KEY + "-" + industryName));
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
    
    public static void setStockTrendData(String stockId, StockTrendRaw stockTrend)
    {
        setRedisCache(generateKey(MyConstants.STOCK_TREND_KEY + "-" + stockId), stockTrend.toString());
    }
    
    public static StockTrendRaw getStockTrendData(String stockId)
    {
        String json = getRedisCache(generateKey(MyConstants.STOCK_TREND_KEY + "-" + stockId));
        if (StringUtils.isEmpty(json))
        {
            return null;
        }
        StockTrendRaw stockTrend = (StockTrendRaw) JsonUtils.getObject(json, StockTrendRaw.class);
        return stockTrend;
    }
    
    private static String getRedisCache(String key)
    {
        String ret = null;
        if (RedisUtils.isReady())
        {
            ret = RedisUtils.get(key);
        }
        return ret;
    }
    
    private static void setRedisCache(String key, String value)
    {
        if (RedisUtils.isReady())
        {
            RedisUtils.set(key, value);
        }
    }
    
    private static void deleteRedisCache(String key)
    {
        if (RedisUtils.isReady())
        {
            RedisUtils.delete(key);
        }
    }
    
    private static String generateKey(String originalKey)
    {
        return MyConstants.COLLECTION_KEY + "-" + originalKey;
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
