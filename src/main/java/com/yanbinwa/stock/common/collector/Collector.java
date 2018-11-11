package com.yanbinwa.stock.common.collector;

import java.io.IOException;
import java.net.MalformedURLException;

import com.yanbinwa.stock.common.cookie.CookieProcessor;

/**
 * 爬数据使用的，需要网络连接
 * 
 * 1. cookie的获取
 * 2. http调用的封装
 * 3. retry机制
 * 
 * @author emotibot
 *
 * @param <T>
 */

public interface Collector extends CookieProcessor
{
    void collectLogic() throws MalformedURLException, IOException;
}
