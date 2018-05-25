package com.yanbinwa.stock.common.utils;

import java.util.Set;

import com.yanbinwa.stock.common.redis.RedisSingleton;

public class RedisUtils
{
    private static final RedisSingleton.RedisClient redisClient = RedisSingleton.INSTANCE.getRedisClient();
    
    public static String get(String key)
    {
        return redisClient.get(key);
    }
    
    public static boolean set(String key, String value)
    {
        return redisClient.put(key, value);
    }
    
    public static boolean delete(String key)
    {
        return redisClient.delete(key);
    }
    
    public static Set<String> keys(String key)
    {
        return redisClient.keys(key);
    }
    
    //用于初始化服务的
    public static void test()
    {
        
    }
    
    public static boolean isReady()
    {
        return redisClient.isReady();
    }
}
