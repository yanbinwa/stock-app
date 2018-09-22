package com.yanbinwa.stock.service.collection.task;

import com.emotibot.middleware.utils.JsonUtils;
import com.emotibot.middleware.utils.StringUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yanbinwa.stock.common.collector.AbstractCollector;
import com.yanbinwa.stock.common.http.RequestParaBuilder;
import com.yanbinwa.stock.common.http.URLMapper;
import com.yanbinwa.stock.common.type.Period;
import com.yanbinwa.stock.common.type.PeriodType;
import com.yanbinwa.stock.entity.stockTrend.StockTrend;
import com.yanbinwa.stock.entity.stockTrend.StockTrendType;
import com.yanbinwa.stock.service.aggragation.entity.StockTrendAgg1d;
import com.yanbinwa.stock.service.aggragation.entity.StockTrendAgg1m;
import com.yanbinwa.stock.service.aggragation.entity.StockTrendAgg1w;
import com.yanbinwa.stock.utils.StockTrendUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Slf4j
public class StockToStockTrendHistoryTask extends AbstractCollector
{
    private String stockId;
    private StockTrendType stockTrendType;
    private long startTimestamp;
    private long endTimestamp;
    
    public StockToStockTrendHistoryTask(String taskName)
    {
        super(taskName);
        this.startTimestamp = 0L;
        this.endTimestamp = System.currentTimeMillis();
    }
    
    public StockToStockTrendHistoryTask(String taskName, long startTimestamp, long endTimestamp)
    {
        super(taskName);
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
    }
    
    public StockToStockTrendHistoryTask(String taskName, String stockId, StockTrendType stockTrendType, long startTimestamp, long endTimestamp)
    {
        super(taskName);
        this.stockId = stockId;
        this.stockTrendType = stockTrendType;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
    }

    @Override
    public void collectLogic() throws MalformedURLException, IOException
    {
        if(StringUtils.isEmpty(stockId) || stockTrendType == null)
        {
            log.error("stockId or stockTrendType is null");
        }

        String stockPeriodStr = getStockPeriodStr();
        if (StringUtils.isEmpty(stockPeriodStr))
        {
            log.error("stockPeriodStr is empty");
            return;
        }
        
        String target = URLMapper.STOCK_TREND_JSON.toString();
        RequestParaBuilder builder = new RequestParaBuilder(target);
        builder.addParameter("symbol", stockId)
                .addParameter("period", stockPeriodStr)
                .addParameter("begin", startTimestamp)
                .addParameter("end", endTimestamp);
        
        URL url = new URL(builder.build());
        String json = request(url);
        List<StockTrend> stockTrendList = getStockTrendFromQuery(json);
        if (stockTrendList == null)
        {
            return;
        }
        StockTrendUtils.storeStockTrend(stockTrendList, stockTrendType);
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
    
    public void setStockTrendType(StockTrendType stockTrendType)
    {
        this.stockTrendType = stockTrendType;
    }
    
    private String getStockPeriodStr()
    {
        if (stockTrendType == null)
        {
            return null;
        }
        switch(stockTrendType)
        {
        case TYPE_1D:
            return "1day";
        case TYPE_1W:
            return "1week";
        case TYPE_1M:
            return "1month";
        default:
            return null;    
        }
    }
    
    /**
     * 
     * chartlist: [
        {
            volume: 44102642,
            open: 46,
            high: 47.2,
            close: 43.04,
            low: 43,
            chg: -1.1,
            percent: -2.49,
            turnrate: 9.15,
            ma5: 41.096,
            ma10: 36.89,
            ma20: 33.811,
            ma30: 32.919,
            dif: 1,
            dea: -0.94,
            macd: 3.88,
            lot_volume: 441026,
            timestamp: 1446480000000,
            time: "Tue Nov 03 00:00:00 +0800 2015"
        },
     * 
     * @param json
     * @return
     */
    private List<StockTrend> getStockTrendFromQuery(String json)
    {
        Class<? extends StockTrend> stockTrendClass = null;
        switch(stockTrendType)
        {
        case TYPE_1D:
            stockTrendClass = StockTrendAgg1d.class;
            break;
        case TYPE_1W:
            stockTrendClass = StockTrendAgg1w.class;
            break;
        case TYPE_1M:
            stockTrendClass = StockTrendAgg1m.class;
            break;
        default:
            log.info("invalid stockTrendType");
            return null;    
        }
        JsonObject stockJsonObject = (JsonObject) JsonUtils.getObject(json, JsonObject.class);
        try
        {
            if (!stockJsonObject.has("success"))
            {
                return null;
            }
            List<StockTrend> stockTrendList = new ArrayList<StockTrend>();
            JsonArray stockJsonList = stockJsonObject.get("chartlist").getAsJsonArray();
            for(int i = 0; i < stockJsonList.size(); i ++)
            {
                JsonObject stockObj = stockJsonList.get(i).getAsJsonObject();
                StockTrend stockTrend = stockTrendClass.newInstance();
                stockTrend.setStockId(stockId);
                stockTrend.setCurrentPrice(stockObj.get("close").getAsDouble());
                stockTrend.setCreatedate(new Date(stockObj.get("timestamp").getAsLong()));
                stockTrend.setOpen(stockObj.get("open").getAsDouble());
                stockTrend.setHigh(stockObj.get("high").getAsDouble());
                stockTrend.setClose(stockObj.get("close").getAsDouble());
                stockTrend.setLow(stockObj.get("low").getAsDouble());
                stockTrend.setChg(stockObj.get("chg").getAsDouble());
                stockTrend.setPercent(stockObj.get("percent").getAsDouble());
                stockTrend.setTurnrate(stockObj.get("turnrate").getAsDouble());
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
        public static final String PARAM_STR = "?symbol=%s&period=%s&type=normal&begin=%ld&end=%ld";
        public static final int TIMEOUT = 10000;
    }
}
