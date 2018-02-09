package com.yanbinwa.stock.service.collection.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringContextUtils implements ApplicationContextAware
{

    private static ApplicationContext applicationContext;
    
    @SuppressWarnings("static-access")
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        this.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() 
    {
        return applicationContext;
    }
    
    public static Object getBean(String name) 
    {
        return getApplicationContext().getBean(name);
    }
    
    public static <T> T getBean(Class<T> clazz) 
    {
        return getApplicationContext().getBean(clazz);
    }
    
    public static <T> T getBean(String name, Class<T> clazz) 
    {
        return getApplicationContext().getBean(name, clazz);
    }
}
