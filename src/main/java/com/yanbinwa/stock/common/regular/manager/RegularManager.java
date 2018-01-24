package com.yanbinwa.stock.common.regular.manager;

import com.yanbinwa.stock.common.regular.task.AbstractRegularTask;

/**
 * 
 * 这里提供Regular task的管理工作，每个服务在启动时会读取当前的regluar task，注册到RegularManager中，RegluarManager会定期遍历task列表，并行执行task，并对外提供可以执行某个task的接口，以供测试
 * 
 * 每个task对应一个对象，该对象有一个接口，是判断是否需要运行task的，并且如果task正在运行，则不需要打断
 * 
 * 每个task对象都可以进行持久化操作，对应于一个json对象
 * 
 * Manager会在启动的时候读取配置文件，把所有的task配置出来
 * 
 * @author emotibot
 *
 */
public interface RegularManager
{
    public boolean addRegularTask(AbstractRegularTask task);
    
    public boolean updateRegularTask(AbstractRegularTask task);
    
    public boolean deleteRegularTask(String taskName, String taskClass);
}
