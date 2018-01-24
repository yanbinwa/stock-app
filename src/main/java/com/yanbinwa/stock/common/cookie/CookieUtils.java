package com.yanbinwa.stock.common.cookie;

import com.yanbinwa.stock.common.utils.RedisUtils;

public class CookieUtils
{
    public static void storeCookie(String cookie, String website)
    {
        if (RedisUtils.isReady())
        {
            RedisUtils.set(generateKey(website), cookie);
        }
    }
    
    public static String getCookie(String website)
    {
        String cookie = null;
        if (RedisUtils.isReady())
        {
            cookie = RedisUtils.get(generateKey(website));
        }
        return cookie;
    }
    
    private static String generateKey(String originalKey)
    {
        return MyConstants.COOKIE_REDIS_KEY + "-" + originalKey;
    }
    
    class MyConstants
    {
        public static final String COOKIE_REDIS_KEY = "Cookie";
    }
}
