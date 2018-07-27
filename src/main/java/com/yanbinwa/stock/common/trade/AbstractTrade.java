package com.yanbinwa.stock.common.trade;

import org.apache.log4j.Logger;

import com.yanbinwa.stock.common.regular.task.AbstractRegularTask;
import com.yanbinwa.stock.common.type.Period;

public abstract class AbstractTrade extends AbstractRegularTask implements Trade
{

    private static Logger logger = Logger.getLogger(AbstractTrade.class);
    
    public AbstractTrade(String taskName)
    {
        super(taskName);
    }
    
    public AbstractTrade(String taskName, String taskClass)
    {
        super(taskName, taskClass);
    }
    
    public AbstractTrade(String taskName, String taskClass, Period period)
    {
        super(taskName, taskClass, period);
    }
    
    @Override
    public void execute()
    {
        logger.debug(getClass().getSimpleName() + " trade...");
        tradeLogic();
    }

}
