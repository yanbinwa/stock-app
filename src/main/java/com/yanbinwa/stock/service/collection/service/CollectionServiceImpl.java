package com.yanbinwa.stock.service.collection.service;

import com.emotibot.middleware.utils.JsonUtils;
import com.emotibot.middleware.utils.TimeUtils;
import com.yanbinwa.stock.common.singleton.RegularManagerSingleton;
import com.yanbinwa.stock.entity.stockTrend.StockTrend;
import com.yanbinwa.stock.service.collection.request.FetchHistoryStockTrendRequest;
import com.yanbinwa.stock.service.collection.request.FetchStockTrendByDateRequest;
import com.yanbinwa.stock.service.collection.request.StockTrendRequest;
import com.yanbinwa.stock.service.collection.task.*;
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
        StockToStockTrendHistoryRootTask task = new StockToStockTrendHistoryRootTask(StockToStockTrendHistoryRootTask.class.getSimpleName(), startTimestamp, endTimestamp);
        RegularManagerSingleton.getInstance().addRegularTask(task);
    }

    @Override
    public void fetchStockTrendByDate(FetchStockTrendByDateRequest fetchStockTrendByDateRequest) {
        long startTimestamp = TimeUtils.getDateFromStr(fetchStockTrendByDateRequest.getStartTime(), "yyyyMMdd").getTime();
        long endTimestamp = TimeUtils.getDateFromStr(fetchStockTrendByDateRequest.getEndTime(), "yyyyMMdd").getTime();
        StockToStockTrendByDateRootTask task = new StockToStockTrendByDateRootTask(StockToStockTrendByDateRootTask.class.getSimpleName(), startTimestamp, endTimestamp);
        RegularManagerSingleton.getInstance().addRegularTask(task);
    }

    @Override
    public void fetchIndustryInfo() {
        CommissionIndustryCollectionTask task = new CommissionIndustryCollectionTask(CommissionIndustryCollectionTask.class.getSimpleName());
        RegularManagerSingleton.getInstance().addRegularTask(task);
    }

    @Override
    public void fetchStockMetaData() {
        HouShenCollectionTask task = new HouShenCollectionTask(HouShenCollectionTask.class.getSimpleName());
        RegularManagerSingleton.getInstance().addRegularTask(task);
    }

    @Override
    public void fetchCurrentStockTrend() {
        CurrentStockTrendRootTask task = new CurrentStockTrendRootTask(CurrentStockTrendRootTask.class.getSimpleName(), 6000, 100);
        RegularManagerSingleton.getInstance().addRegularTask(task);
    }
}
