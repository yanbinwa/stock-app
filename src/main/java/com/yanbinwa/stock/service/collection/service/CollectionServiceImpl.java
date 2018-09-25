package com.yanbinwa.stock.service.collection.service;

import com.emotibot.middleware.utils.JsonUtils;
import com.emotibot.middleware.utils.TimeUtils;
import com.yanbinwa.stock.common.singleton.RegularManagerSingleton;
import com.yanbinwa.stock.entity.stockTrend.StockTrend;
import com.yanbinwa.stock.service.collection.request.FetchHistoryStockTrendRequest;
import com.yanbinwa.stock.service.collection.request.StockTrendRequest;
import com.yanbinwa.stock.service.collection.task.CommissionIndustryCollectionTask;
import com.yanbinwa.stock.service.collection.task.StockToStockTrendHistoryTask;
import com.yanbinwa.stock.utils.StockTrendUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 这里接受用户调用，对于collection进行增删改查的操作，不做对于数据的查询工作
 * 
 * @author emotibot
 *
 */

@Service("collectionService")
public class CollectionServiceImpl implements CollectionService
{
    @Override
    public String getStockTrend(StockTrendRequest request)
    {
        if (request == null)
        {
            return "";
        }
        List<String> stockIdList = request.getStockIdList();
        if (stockIdList == null || stockIdList.isEmpty())
        {
            return "";
        }
        long startTimestamp = request.getStartTimestamp();
        long endTimestamp = request.getEndTimestamp();
        Map<String, List<StockTrend>> stockIdToStockTrendListMap = 
                new HashMap<>();
        for (String stockId : stockIdList)
        {
            List<StockTrend> stockTrendList = 
                    StockTrendUtils.getStockTrendByDate(request.getStockTrendType(), stockId, new Date(startTimestamp), new Date(endTimestamp));
            Collections.sort(stockTrendList);
            stockIdToStockTrendListMap.put(stockId, stockTrendList);
        }
        return JsonUtils.getJsonStr(stockIdToStockTrendListMap);
    }

    @Override
    public void fetchHistoryStockTrend(FetchHistoryStockTrendRequest fetchHistoryStockTrendRequest) {
        long startTimestamp = TimeUtils.getDateFromStr(fetchHistoryStockTrendRequest.getStartTime(), "yyyyMMdd").getTime();
        long endTimestamp = TimeUtils.getDateFromStr(fetchHistoryStockTrendRequest.getEndTime(), "yyyyMMdd").getTime();
        StockToStockTrendHistoryTask task = new StockToStockTrendHistoryTask("StockToStockTrendHistoryTask", startTimestamp, endTimestamp);
        RegularManagerSingleton.getInstance().addRegularTask(task);
    }

    @Override
    public void fetchIndustryInfo() {
        CommissionIndustryCollectionTask task = new CommissionIndustryCollectionTask("CommissionIndustryCollectionTask");
        RegularManagerSingleton.getInstance().addRegularTask(task);
    }
}
