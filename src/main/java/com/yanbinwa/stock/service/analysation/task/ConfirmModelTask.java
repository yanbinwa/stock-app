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
import com.yanbinwa.stock.entity.stockTrend.StockTrend;
import com.yanbinwa.stock.entity.stockTrend.StockTrendType;
import com.yanbinwa.stock.service.analysation.element.TrainData;
import com.yanbinwa.stock.service.analysation.utils.TrainDataUtils;
import com.yanbinwa.stock.utils.StockTrendUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.net.HttpURLConnection;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Slf4j
public class ConfirmModelTask extends AbstractAnalysation
{
    private StockTrendType type;
    private int trendNum;
    private long startTimestamp;
    private long endTimestamp;
    private double increaseRate;

    public ConfirmModelTask(String taskName)
    {
        super(taskName);
    }

    public ConfirmModelTask(String taskName, StockTrendType type, int trendNum, long startTimestamp, long endTimestamp, double increaseRate)
    {
        super(taskName);
        this.type = type;
        this.trendNum = trendNum;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.increaseRate = increaseRate;
    }
    
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
        Map<String, List<StockTrend>> stockIdToStockListMap = StockTrendUtils.classifyStockTrendById(allStockTrendList);
        for (Map.Entry<String, List<StockTrend>> entry : stockIdToStockListMap.entrySet())
        {
            List<StockTrend> stockTrendList = entry.getValue();
            StockTrendUtils.sortStockTrendByTimestamp(stockTrendList);
        }
        
        //3. 查找每只股票满足increaseRate的数据集, 以及不满足要求的数据集
        int count = 0;
        int errorCount = 0;
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
                JsonObject predictContent = new JsonObject();
                JsonArray stockPricesObj = trainData.getStockPricesForJson();
                if (stockPricesObj == null)
                {
                    continue;
                }
                JsonArray stockPredictObj = new JsonArray();
                stockPredictObj.add(stockPricesObj);
                predictContent.add(Constants.PREDICT_DATA, stockPredictObj);
                //4. 调用训练model的接口
                HttpRequest request = new HttpRequest(MyConstants.PREDICT_MODEL_URL, predictContent.toString(), HttpRequestType.POST);
                HttpResponse response = HttpUtils.call(request, 10000);
                if (response.getStateCode() != HttpURLConnection.HTTP_OK)
                {
                    log.error("analysationLogic is fail");
                    continue;
                }
                count ++;
                int predictTag = Integer.parseInt(response.getResponse());
                if (predictTag != trainData.getTag())
                {
                    errorCount ++;
                }
            }
        }
        log.info("count is: " + count + "; errorCount is: " + errorCount + "; errorRate: " + errorCount / (double) count);
    }

    @Override
    public Period generatePeriod()
    {
        return buildEmptyPeriod();
    }

    @Override
    public void setTimeout()
    {
        this.timeout = MyConstants.TIMEOUT;
    }

    class MyConstants
    {
        public static final int TIMEOUT = 10 * 3600;
        public static final String PREDICT_MODEL_URL = "http://localhost:8000/stock/predict";
    }
}
