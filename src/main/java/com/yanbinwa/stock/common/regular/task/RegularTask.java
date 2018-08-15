package com.yanbinwa.stock.common.regular.task;

import com.yanbinwa.stock.common.type.Period;

/**
 * 主要是设定执行的时间点，比如interval是每天，具体的执行时间是多少，哪些时间不执行
 * 
 * 又例如：实时信息是interval是1分钟，只有早上9-11，下午1-3点执行，周末不执行
 * 
 * 
 * @author emotibot
 *
 */

public interface RegularTask extends Runnable
{
    public void execute();
    
    public boolean shouldExecute();
    
    public boolean isPeriodTask();
    
    public Period generatePeriod();
    
    public void setTimeout();
    
    public RegularTaskWarp getTaskWarp();
    
//    public void upLoad(String uploadStr);
    
    public String createUploadStr();
}
