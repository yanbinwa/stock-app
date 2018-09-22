package com.yanbinwa.stock.common.trade;

import com.yanbinwa.stock.common.regular.task.AbstractRegularTask;
import com.yanbinwa.stock.common.type.Period;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractTrade extends AbstractRegularTask implements Trade
{

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
        log.debug(getClass().getSimpleName() + " trade...");
        tradeLogic();
    }

}
