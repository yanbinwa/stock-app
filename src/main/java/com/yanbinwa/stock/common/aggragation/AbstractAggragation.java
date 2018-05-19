package com.yanbinwa.stock.common.aggragation;

import org.apache.log4j.Logger;

import com.yanbinwa.stock.common.regular.task.AbstractRegularTask;
import com.yanbinwa.stock.common.type.Period;

public abstract class AbstractAggragation extends AbstractRegularTask implements Aggragation
{

    private static Logger logger = Logger.getLogger(AbstractAggragation.class);
    
    public AbstractAggragation(String taskName)
    {
        super(taskName);
    }
    
    public AbstractAggragation(String taskName, String taskClass)
    {
        super(taskName, taskClass);
    }
    
    public AbstractAggragation(String taskName, String taskClass, Period period)
    {
        super(taskName, taskClass, period);
    }
    
    @Override
    public void execute()
    {
        logger.debug(getClass().getSimpleName() + " aggragation...");
        aggragationLogic();
    }

}
