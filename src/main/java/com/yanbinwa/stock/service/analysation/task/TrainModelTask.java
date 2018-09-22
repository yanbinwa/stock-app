package com.yanbinwa.stock.service.analysation.task;

import com.emotibot.middleware.request.HttpRequest;
import com.emotibot.middleware.request.HttpRequestType;
import com.emotibot.middleware.response.HttpResponse;
import com.emotibot.middleware.utils.HttpUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yanbinwa.stock.common.analysation.AbstractAnalysation;
import com.yanbinwa.stock.common.constants.Constants;
import com.yanbinwa.stock.common.type.Period;
import com.yanbinwa.stock.common.type.PeriodType;
import com.yanbinwa.stock.entity.stockTrend.StockTrend;
import com.yanbinwa.stock.entity.stockTrend.StockTrendType;
import com.yanbinwa.stock.service.analysation.element.TrainData;
import com.yanbinwa.stock.service.analysation.utils.TrainDataUtils;
import com.yanbinwa.stock.utils.StockTrendUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.net.HttpURLConnection;
import java.util.*;

/**
 * 这里是对所有股票在一定的范围内进行拟合操作
 * 
 * @author emotibot
 *
 */
@Data
@Slf4j
public class TrainModelTask extends AbstractAnalysation
{        
    private StockTrendType type;
    private int trendNum;
    private long startTimestamp;
    private long endTimestamp;
    private double increaseRate;

    public TrainModelTask(String taskName) {
        super(taskName);
    }

    public TrainModelTask(String taskName, StockTrendType type, int trendNum, long startTimestamp, long endTimestamp, double increaseRate)
    {
        super(taskName);
        this.type = type;
        this.trendNum = trendNum;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.increaseRate = increaseRate;
    }

    /**
     * 1. 获取所有限定时间段内股票的信息
     * 2. 对于每个股票的信息进行聚合，并按照时间排序
     * 3. 查找每只股票满足increaseRate的数据集, 以及不满足要求的数据集
     * 4. 将数据进行训练
     */
    @Override
    public void analysationLogic()
    {
        //1. 获取所有限定时间段内股票的信息
        List<StockTrend> allStockTrendList = StockTrendUtils.getStockTrendByDate(type, null, new Date(startTimestamp), new Date(endTimestamp));
        if (allStockTrendList == null || allStockTrendList.isEmpty())
        {
            return;
        }
        
        //2. 对于每个股票的信息进行聚合，并按照时间排序
        Map<String, List<StockTrend>> stockIdToStockListMap = new HashMap<String, List<StockTrend>>();
        for (StockTrend stockTrend : allStockTrendList)
        {
            String stockId = stockTrend.getStockId();
            List<StockTrend> stockTrendList = stockIdToStockListMap.get(stockId);
            if (stockTrendList == null)
            {
                stockTrendList = new ArrayList<StockTrend>();
                stockIdToStockListMap.put(stockId, stockTrendList);
            }
            stockTrendList.add(stockTrend);
        }
        for (Map.Entry<String, List<StockTrend>> entry : stockIdToStockListMap.entrySet())
        {
            List<StockTrend> stockTrendList = entry.getValue();
            Collections.sort(stockTrendList, new Comparator<StockTrend>() {

                @Override
                public int compare(StockTrend o1, StockTrend o2)
                {
                    if (o1.getCreatedate().before(o2.getCreatedate()))
                    {
                        return -1;
                    }
                    else if (o1.getCreatedate().after(o2.getCreatedate()))
                    {
                        return 1;
                    }
                    else
                    {
                        return 0;
                    }
                }
            });
        }
        
        //3. 查找每只股票满足increaseRate的数据集, 以及不满足要求的数据集
        JsonObject trainContent = new JsonObject();
        JsonArray trainDatas = new JsonArray();
        JsonArray trainResults = new JsonArray();
        for (Map.Entry<String, List<StockTrend>> entry : stockIdToStockListMap.entrySet())
        {
            List<TrainData> trainDataList = TrainDataUtils.getTrainData(entry.getValue(), this.trendNum, this.increaseRate);
            TrainDataUtils.normalizationTrainData(trainDataList);
            if (trainDataList == null)
            {
                continue;
            }
            for (TrainData trainData : trainDataList)
            {
                JsonArray stockPricesObj = trainData.getStockPricesForJson();
                if (stockPricesObj == null)
                {
                    continue;
                }
                trainDatas.add(stockPricesObj);
                trainResults.add(trainData.getTag());
            }
        }
        trainContent.add(Constants.TRAIN_DATA_DATAS, trainDatas);
        trainContent.add(Constants.TRAIN_DATA_RESULTS, trainResults);
        
        //4. 调用训练model的接口
        HttpRequest request = new HttpRequest(MyConstants.TRAIN_MODEL_URL, trainContent.toString(), HttpRequestType.POST);
        HttpResponse response = HttpUtils.call(request, 10000);
        if (response.getStateCode() == HttpURLConnection.HTTP_OK)
        {
            log.info("analysationLogic is success");
        }
        else
        {
            log.error("analysationLogic is fail");
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
        public static final int TIMEOUT = 10 * 3600;
        public static final String TRAIN_MODEL_URL = "http://localhost:8000/stock/trainModel";
    }
}
