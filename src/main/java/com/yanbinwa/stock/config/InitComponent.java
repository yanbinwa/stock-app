package com.yanbinwa.stock.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.yanbinwa.stock.common.singleton.RegularManagerSingleton;

@Component
public class InitComponent implements ApplicationListener<ApplicationReadyEvent>
{

    @Override
    public void onApplicationEvent(ApplicationReadyEvent arg0)
    {
        RegularManagerSingleton.getInstance();
    }

}
