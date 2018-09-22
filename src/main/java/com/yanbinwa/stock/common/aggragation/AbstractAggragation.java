package com.yanbinwa.stock.common.aggragation;

import com.yanbinwa.stock.common.regular.task.AbstractRegularTask;
import com.yanbinwa.stock.common.type.Period;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractAggragation extends AbstractRegularTask implements Aggragation
{
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
        log.debug(getClass().getSimpleName() + " aggragation...");
        aggragationLogic();
    }

}
