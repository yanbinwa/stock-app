package com.yanbinwa.stock.common.analysation;

import com.yanbinwa.stock.common.regular.task.AbstractRegularTask;
import com.yanbinwa.stock.common.type.Period;

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
        System.out.println(getClass().getSimpleName() + " aggragation...");
        analysationLogic();
    }
}
