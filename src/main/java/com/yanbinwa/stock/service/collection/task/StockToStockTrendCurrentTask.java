package com.yanbinwa.stock.service.collection.task;

import com.emotibot.middleware.utils.JsonUtils;
import com.emotibot.middleware.utils.StringUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.yanbinwa.stock.common.collector.AbstractCollector;
import com.yanbinwa.stock.common.http.RequestParaBuilder;
import com.yanbinwa.stock.common.http.URLMapper;
import com.yanbinwa.stock.common.type.DayWindow;
import com.yanbinwa.stock.common.type.HourWindow;
import com.yanbinwa.stock.common.type.Period;
import com.yanbinwa.stock.entity.stockTrend.StockTrend;
import com.yanbinwa.stock.entity.stockTrend.StockTrendType;
import com.yanbinwa.stock.service.aggragation.entity.StockTrendAgg1d;
import com.yanbinwa.stock.utils.StockTrendUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 获取任意一只股票和大盘指数的实时数据
 *
 */
@Slf4j
@Data
public class StockToStockTrendCurrentTask extends AbstractCollector
{
    private static final DayWindow[] dayWindowArray = {DayWindow.MONDAY, DayWindow.TUESDAY, DayWindow.WEDNESDAY, DayWindow.THURSDAY, DayWindow.FRIDAY};
    private static final HourWindow[] hourWindowArray = {HourWindow.HOUR9_SH, HourWindow.HOUR10_FH, HourWindow.HOUR10_SH, HourWindow.HOUR11_FH, HourWindow.HOUR13_FH, HourWindow.HOUR13_SH, HourWindow.HOUR14_FH, HourWindow.HOUR14_SH};
    private static final int periodInterval = 120;

    private String stockId;

    public StockToStockTrendCurrentTask(String taskName)
    {
        super(taskName);
    }

    public StockToStockTrendCurrentTask(String taskName, String stockId)
    {
        super(taskName);
        this.stockId = stockId;
    }

    @Override
    public void collectLogic() throws MalformedURLException, IOException
    {
        if(StringUtils.isEmpty(stockId))
        {
            log.error("stockId is null");
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
        StockTrendUtils.storeStockTrend(stockTrendList, StockTrendType.TYPE_RAW);
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
                stockTrend.setCreatedate(new Date());
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
