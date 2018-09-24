package com.yanbinwa.stock.common.cookie;

import com.emotibot.middleware.utils.StringUtils;
import com.yanbinwa.stock.common.http.RequestParaBuilder;
import com.yanbinwa.stock.config.EnvConfig;

import java.net.HttpURLConnection;
import java.net.URL;

public interface CookieProcessor
{
    default String updateCookie(String website) throws Exception 
    {
        String areacode = EnvConfig.envConfig.getAreaCode();
        String userID = EnvConfig.envConfig.getUserId();
        String passwd = EnvConfig.envConfig.getPasswd();
        Boolean rememberMe = Boolean.valueOf(EnvConfig.envConfig.getRememberMe());

        HttpURLConnection connection = null;
        if (!StringUtils.isEmpty(userID) && !StringUtils.isEmpty(passwd))
        {
            connection = login(areacode, userID, passwd, rememberMe);
        }
        try 
        {
            connection = connection == null ? (HttpURLConnection) new URL(website).openConnection() : connection;
            connection.connect();

            String cookie = connection.getHeaderFields().get("Set-Cookie")
                    .stream()
                    .map(x -> x.split(";")[0].concat(";"))
                    .filter(x -> x.contains("token=") || x.contains("s="))
                    .reduce("", String::concat);
            
            //将cookie写入到redis中
            CookieUtils.storeCookie(cookie, website);
            return cookie;
        } 
        finally 
        {
            if (connection != null) 
            {
                connection.disconnect();
            }
        }

    }

    default HttpURLConnection login(String areacode, String userID, String passwd, boolean rememberMe) throws Exception 
    {
        areacode = areacode == null ? "86" : areacode;
        if (userID == null || passwd == null) 
        {
            throw new IllegalArgumentException("null parameter: userID or password");
        }

        RequestParaBuilder builder = new RequestParaBuilder("http://xueqiu.com/user/login")
                .addParameter("areacode", areacode)
                .addParameter("telephone", userID)
                .addParameter("password", passwd)
                .addParameter("remember_me", rememberMe ? "on" : "off");

        URL url = new URL(builder.build());
        return (HttpURLConnection) url.openConnection();
    }
}
