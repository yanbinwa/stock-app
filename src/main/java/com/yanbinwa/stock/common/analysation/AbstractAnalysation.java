package com.yanbinwa.stock.common.analysation;

import org.apache.log4j.Logger;

import com.yanbinwa.stock.common.regular.task.AbstractRegularTask;
import com.yanbinwa.stock.common.type.Period;

public abstract class AbstractAnalysation extends AbstractRegularTask implements Analysation
{
    private static Logger logger = Logger.getLogger(AbstractAnalysation.class);
    
    public AbstractAnalysation(String taskName)
    {
        super(taskName);
    }

    public AbstractAnalysation(String taskName, String taskClass)
    {
        super(taskName, taskClass);
    }
    
    public AbstractAnalysation(String taskName, String taskClass, Period period)
    {
        super(taskName, taskClass, period);
    }
    
    @Override
    public void execute()
    {
        logger.debug(getClass().getSimpleName() + " aggragation...");
        analysationLogic();
    }
}
