package com.yanbinwa.stock.service.collection.task;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.Data;
import org.apache.log4j.Logger;

import com.emotibot.middleware.utils.JsonUtils;
import com.emotibot.middleware.utils.StringUtils;
import com.emotibot.middleware.utils.TimeUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.yanbinwa.stock.common.collector.AbstractCollector;
import com.yanbinwa.stock.common.http.RequestParaBuilder;
import com.yanbinwa.stock.common.http.URLMapper;
import com.yanbinwa.stock.common.type.Period;
import com.yanbinwa.stock.common.type.PeriodType;
import com.yanbinwa.stock.entity.stockTrend.StockTrend;
import com.yanbinwa.stock.entity.stockTrend.StockTrendType;
import com.yanbinwa.stock.service.aggragation.entity.StockTrendAgg1d;
import com.yanbinwa.stock.utils.StockTrendUtils;

@Data
public class StockToStockTrendTodayHistoryTask extends AbstractCollector
{
    private static Logger logger = Logger.getLogger(StockToStockTrendTodayHistoryTask.class);
    
    private String stockId;
    
    public StockToStockTrendTodayHistoryTask(String taskName)
    {
        super(taskName);
    }
    
    public StockToStockTrendTodayHistoryTask(String taskName, String stockId)
    {
        super(taskName);
        this.stockId = stockId;
    }

    @Override
    public void collectLogic() throws MalformedURLException, IOException
    {
        if(StringUtils.isEmpty(stockId))
        {
            logger.error("stockId is null");
        }
        
        String target = URLMapper.STOCK_JSON.toString();
        RequestParaBuilder builder = new RequestParaBuilder(target);
        builder.addParameter("code", stockId);
        
        URL url = new URL(builder.build());
        String json = request(url);
        List<StockTrend> stockTrendList = getStockTrendFromQuery(json);
        if (stockTrendList == null)
        {
            return;
        }
        StockTrendUtils.storeStockTrend(stockTrendList, StockTrendType.TYPE_1D);
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
    
    public void setStockId(String stockId)
    {
        this.stockId = stockId;
    }
    
    /**
     * 在写入当天的股票信息的同时，需要将redis中缓存的当天分时的信息清楚掉
     * 
     * @param json
     * @return
     */
    private List<StockTrend> getStockTrendFromQuery(String json)
    {
        JsonObject stockJsonObject = (JsonObject) JsonUtils.getObject(json, JsonObject.class);
        try
        {
            List<StockTrend> stockTrendList = new ArrayList<StockTrend>();
            for (Map.Entry<String, JsonElement> entry : stockJsonObject.entrySet())
            {
                JsonObject stockObj = entry.getValue().getAsJsonObject();
                StockTrend stockTrend = new StockTrendAgg1d();
                stockTrend.setStockId(stockId);
                stockTrend.setCurrentPrice(stockObj.get("current").getAsDouble());
                stockTrend.setCreatedate(new Date(TimeUtils.getTodayTimestamp()));
                stockTrend.setOpen(stockObj.get("open").getAsDouble());
                stockTrend.setHigh(stockObj.get("high").getAsDouble());
                stockTrend.setClose(stockObj.get("close").getAsDouble());
                stockTrend.setLow(stockObj.get("low").getAsDouble());
                stockTrend.setChg(stockObj.get("change").getAsDouble());
                stockTrend.setPercent(stockObj.get("percentage").getAsDouble());
                stockTrend.setTurnrate(Double.parseDouble(stockObj.get("turnover_rate").getAsString().replace("%","")));
                stockTrendList.add(stockTrend);
            }
            return stockTrendList;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    class MyConstants
    {
        public static final int TIMEOUT = 10000;
    }
}
