package com.yanbinwa.stock.service.collection.utils;

import com.yanbinwa.stock.common.utils.RedisUtils;

/**
 * 包含redis的操作
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
    
    public static String getCommissionIndustry()
    {
        String ret = null;
        if (RedisUtils.isReady())
        {
            ret = RedisUtils.get(generateKey(MyConstants.COMMISSION_INDUSTRY_KEY));
        }
        return ret;
    }
    
    public static void deleteCommissionIndustry()
    {
        if (RedisUtils.isReady())
        {
            RedisUtils.delete(generateKey(MyConstants.COMMISSION_INDUSTRY_KEY));
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
}
