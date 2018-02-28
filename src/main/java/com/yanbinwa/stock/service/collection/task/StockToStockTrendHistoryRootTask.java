package com.yanbinwa.stock.service.collection.task;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import com.emotibot.middleware.utils.JsonUtils;
import com.google.gson.reflect.TypeToken;
import com.yanbinwa.stock.common.collector.AbstractCollector;
import com.yanbinwa.stock.common.singleton.RegularManagerSingleton;
import com.yanbinwa.stock.common.type.Period;
import com.yanbinwa.stock.common.type.PeriodType;
import com.yanbinwa.stock.entity.stockTrend.StockTrendType;
import com.yanbinwa.stock.service.collection.element.Industry;
import com.yanbinwa.stock.service.collection.utils.CollectionUtils;

public class StockToStockTrendHistoryRootTask extends AbstractCollector
{

    private static StockTrendType[] stockTrendTypes = {StockTrendType.TYPE_1D, StockTrendType.TYPE_1W, StockTrendType.TYPE_1M};
    
    public StockToStockTrendHistoryRootTask(String taskName)
    {
        super(taskName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void collectLogic() throws MalformedURLException, IOException
    {
        String commissionIndustryStr = CollectionUtils.getCommissionIndustry();
        Type resultType = new TypeToken<Map<String, Industry>>(){}.getType();
        Map<String, Industry> industryMap = (Map<String, Industry>) JsonUtils.getObject(commissionIndustryStr, resultType);
        for (String industryName : industryMap.keySet())
        {
            String stockIdStr = CollectionUtils.getIndustryToStockId(industryName);
            Type stockIdType = new TypeToken<List<String>>(){}.getType();
            List<String> stockIdList = (List<String>) JsonUtils.getObject(stockIdStr, stockIdType);
            for (String stockId : stockIdList)
            {
                for (StockTrendType stockTrendType : stockTrendTypes)
                {
                    StockToStockTrendHistoryTask task = new StockToStockTrendHistoryTask("StockToStockTrendHistoryTask-" + stockId, stockId, stockTrendType);
                    RegularManagerSingleton.getInstance().addRegularTask(task);
                }
            }
        }
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
        public static final int TIMEOUT = 1000;
    }
}
