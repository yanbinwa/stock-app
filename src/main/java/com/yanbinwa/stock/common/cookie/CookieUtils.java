package com.yanbinwa.stock.common.cookie;

import com.yanbinwa.stock.common.redis.RedisUtils;

public class CookieUtils
{
    public static void storeCookie(String cookie, String website)
    {
        RedisUtils.redisUtils.set(generateKey(website), cookie);
    }
    
    public static String getCookie(String website)
    {
        String cookie = RedisUtils.redisUtils.get(generateKey(website));
        return cookie;
    }
    
    private static String generateKey(String originalKey)
    {
        return RedisUtils.redisUtils.generalKey(MyConstants.COOKIE_REDIS_KEY, originalKey);
    }
    
    class MyConstants
    {
        public static final String COOKIE_REDIS_KEY = "Cookie";
    }
}
