package com.yanbinwa.stock.service.analysation.utils;

import java.util.ArrayList;
import java.util.List;

import com.yanbinwa.stock.common.constants.Constants;
import com.yanbinwa.stock.entity.stockTrend.StockTrend;
import com.yanbinwa.stock.service.analysation.element.TrainData;

public class TrainDataUtils
{
    public static List<TrainData> getTrainData(List<StockTrend> stockTrend, int trendNum, double increaseRate)
    {
        //如果数量不满足，则直接返回
        if (stockTrend.size() < trendNum + 1)
        {
            return null;
        }
        List<TrainData> ret = new ArrayList<TrainData>();
        for(int i = trendNum; i < stockTrend.size(); i ++)
        {
            StockTrend currentTrend = stockTrend.get(i);
            StockTrend lastTrend = stockTrend.get(i - 1);
            if ((currentTrend.getCurrentPrice() - lastTrend.getCurrentPrice()) / lastTrend.getCurrentPrice() >= increaseRate)
            {
                TrainData trainData = new TrainData(stockTrend.subList(i - trendNum, i), Constants.INCREATE);
                ret.add(trainData);
                break;
            }
        }
        
        for(int i = trendNum; i < stockTrend.size(); i ++)
        {
            StockTrend currentTrend = stockTrend.get(i);
            StockTrend lastTrend = stockTrend.get(i - 1);
            if ((currentTrend.getCurrentPrice() - lastTrend.getCurrentPrice()) / lastTrend.getCurrentPrice() < increaseRate)
            {
                TrainData trainData = new TrainData(stockTrend.subList(i - trendNum, i), Constants.DECREATE);
                ret.add(trainData);
                break;
            }
        }
        return ret;
    }
    
    public static void normalizationTrainData(List<TrainData> trainDatas)
    {
        if (trainDatas == null)
        {
            return;
        }
        for (TrainData trainData : trainDatas)
        {
            trainData.normalization();
        }
    }
}
