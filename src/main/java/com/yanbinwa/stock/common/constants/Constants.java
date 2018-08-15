package com.yanbinwa.stock.common.constants;

public class Constants
{
    public static final int REGULAR_MANAGER_THREAD_POOL_SIZE = 100;
    public static final String REGULAR_MANAGER_TASK_FILE_KEY = "REGULAR_MANAGER_TASK_FILE";
    public static final String REGULAR_MANAGER_INTRINSIC_TASK_FILE_KEY = "REGULAR_MANAGER_INTRINSIC_TASK_FILE";
    
    public static final long PERIOD_DEFAULT_NEXT_TIMESTAMP = -1L;
    public static final int REGULAR_POLL_INTERVAL = 100;
    public static final int REGULAR_CHECK_INTERVAL = 100;
    
    public static final String AREA_CODE_KEY = "AREA_CODE";
    public static final String USER_ID_KEY = "USER_ID";
    public static final String PASSWORD_KEY = "PASSWORD";
    public static final String REMEMBER_ME_KEY = "REMEMBER_ME";
    
    //MyEmbeddedServletContainerFactory
    public static final String TOMCAT_PORT_KEY = "TOMCAT_PORT";
    public static final String TOMCAT_MAX_CONNECTION_KEY = "TOMCAT_MAX_CONNECTION";
    public static final String TOMCAT_MAX_THREAD_KEY = "TOMCAT_MAX_THREAD";
    public static final String TOMCAT_CONNECTION_TIMEOUT_KEY = "TOMCAT_CONNECTION_TIMEOUT";
    
    public static final int INCREATE = 1;
    public static final int DECREATE = -1;
    public static final String TRAIN_DATA_DATAS = "datas";
    public static final String TRAIN_DATA_RESULTS = "results";
    public static final String PREDICT_DATA = "data";

    //return info
    public static final String CODE_KEY = "code";
    public static final String MESSAGE_KEY = "msg";
    public static final String DATA_KEY = "data";
}
