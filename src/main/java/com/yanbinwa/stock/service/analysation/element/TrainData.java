package com.yanbinwa.stock.service.analysation.element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.emotibot.middleware.utils.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.yanbinwa.stock.entity.stockTrend.StockTrend;

import lombok.*;

@Data
@NoArgsConstructor
public class TrainData
{
    @SerializedName("stockPrices")
    @Expose
    private List<Double> stockPrices;
    
    @SerializedName("tag")
    @Expose
    private int tag;
    
    public TrainData(List<StockTrend> stockTrends, int tag)
    {
        stockPrices = new ArrayList<Double>();
        for (StockTrend stockTrend : stockTrends)
        {
            stockPrices.add(stockTrend.getCurrentPrice());
        }
        this.tag = tag;
    }
    
    public JsonArray getStockPricesForJson()
    {
        if (stockPrices == null)
        {
            return null;
        }
        JsonArray jsonArray = new JsonArray();
        for (Double stockPrice : stockPrices)
        {
            jsonArray.add(stockPrice);
        }
        return jsonArray;
    }
    
    public void normalization()
    {
        if (stockPrices == null)
        {
            return;
        }
        double max = Collections.max(this.stockPrices);
        double min = Collections.min(this.stockPrices);
        double diff = max - min;
        List<Double> newStockPrices = new ArrayList<Double>();
        for (double price : stockPrices)
        {
            double newPrice = (price - min) / diff;
            newStockPrices.add(newPrice);
        }
        this.stockPrices = newStockPrices;
    }
    
    @Override
    public String toString()
    {
        return JsonUtils.getJsonStr(this);
    }
}
