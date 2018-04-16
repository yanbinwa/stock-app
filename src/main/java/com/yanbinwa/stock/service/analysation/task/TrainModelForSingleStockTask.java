package com.yanbinwa.stock.service.analysation.task;

import com.yanbinwa.stock.common.analysation.AbstractAnalysation;
import com.yanbinwa.stock.common.type.Period;
import com.yanbinwa.stock.common.type.PeriodType;

import lombok.Getter;
import lombok.Setter;

/**
 * 这里是对特定股票的历史数据进行拟合，针对个股的算法
 * 
 * @author emotibot
 *
 */
@Getter
@Setter
public class TrainModelForSingleStockTask extends AbstractAnalysation
{
    private String stockId;
    
    public TrainModelForSingleStockTask(String taskName, String stockId)
    {
        super(taskName);
        this.stockId = stockId;
    }

    @Override
    public void analysationLogic()
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Period generatePeriod()
    {
        Period period = new Period();
        period.setPeriodType(PeriodType.NONE);
        return period;
    }

    @Override
    public void setTimeout()
    {
        this.timeout = MyConstants.TIMEOUT;
    }

    class MyConstants
    {
        public static final int TIMEOUT = 10 * 3600;
    }

}
