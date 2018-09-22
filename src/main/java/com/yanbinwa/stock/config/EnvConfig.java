package com.yanbinwa.stock.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Data
@Configuration
public class EnvConfig {

    public static EnvConfig envConfig;

    @Value("${stock.taskFile}")
    private String taskFile;

    @Value("${stock.regularTaskFile}")
    private String regularTaskFile;

    @Value("${stock.areaCode}")
    private String areaCode;

    @Value("${stock.userId}")
    private String userId;

    @Value("${stock.passwd}")
    private String passwd;

    @Value("{stock.rememberMe}")
    private String rememberMe;

    @PostConstruct
    private void init() {
        envConfig = this;
    }
}
