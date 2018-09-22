package com.yanbinwa.stock.common.analysation;

import com.yanbinwa.stock.common.regular.task.AbstractRegularTask;
import com.yanbinwa.stock.common.type.Period;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractAnalysation extends AbstractRegularTask implements Analysation
{
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
        log.debug(getClass().getSimpleName() + " aggragation...");
        analysationLogic();
    }
}
