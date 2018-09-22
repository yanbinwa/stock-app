package com.yanbinwa.stock.common.collector;

import com.yanbinwa.stock.common.http.HttpRequestHelper;
import com.yanbinwa.stock.common.http.URLMapper;
import com.yanbinwa.stock.common.regular.task.AbstractRegularTask;
import com.yanbinwa.stock.common.type.Period;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;

@Slf4j
public abstract class AbstractCollector extends AbstractRegularTask implements Collector
{
    public AbstractCollector(String taskName)
    {
        super(taskName);
    }
    
    public AbstractCollector(String taskName, String taskClass)
    {
        super(taskName, taskClass);
    }
    
    public AbstractCollector(String taskName, String taskClass, Period period)
    {
        super(taskName, taskClass, period);
    }
    
    @Override
    public void execute()
    {
        log.debug(getClass().getSimpleName() + " collecting...");

        int retryTime = MyConstants.RETRY_TIME;

        try 
        {
            int loopTime = 1;

            while (retryTime > loopTime) 
            {
                try 
                {
                    collectLogic();
                    break;
                } 
                catch (Exception e) 
                {
                    if(!(e instanceof IOException)) 
                    {
                        throw e;
                    }
                    log.debug("Collector:   -> " + loopTime + " times");
                    updateCookie(MyConstants.WEBSITE);
                    loopTime ++;
                }
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
    
    protected String request(URL url) throws IOException 
    {
        log.debug("url: " + url);
        return new HttpRequestHelper(MyConstants.WEBSITE).request(url);
    }
    
    static class MyConstants
    {
        public static final String WEBSITE = URLMapper.MAIN_PAGE.toString();
        public static final int RETRY_TIME = 3;
    }
}
