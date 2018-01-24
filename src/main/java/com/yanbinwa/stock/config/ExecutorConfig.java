package com.yanbinwa.stock.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableAsync
public class ExecutorConfig 
{
    private int corePoolSize = 100;
    private int maxPoolSize = 120;
    private int queueCapacity = 100;
    
    @Bean
    public Executor async() 
    {
        ThreadPoolTaskExecutor asyncExecutor = new ThreadPoolTaskExecutor();
        asyncExecutor.setCorePoolSize(corePoolSize);
        asyncExecutor.setMaxPoolSize(maxPoolSize);
        asyncExecutor.setQueueCapacity(queueCapacity);
        asyncExecutor.setThreadNamePrefix("sync");
        asyncExecutor.initialize();
        return asyncExecutor;
    }
}