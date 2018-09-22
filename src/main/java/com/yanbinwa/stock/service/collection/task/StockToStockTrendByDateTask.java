package com.yanbinwa.stock.service.collection.task;

import com.emotibot.middleware.utils.JsonUtils;
import com.emotibot.middleware.utils.StringUtils;
import com.emotibot.middleware.utils.TimeUtils;
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
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Data
public class StockToStockTrendByDateTask extends AbstractCollector
{
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    private String stockId;
    List<Date> dateList;

    public StockToStockTrendByDateTask(String taskName) {
        super(taskName);
    }

    public StockToStockTrendByDateTask(String taskName, String stockId, List<Date> dateList)
    {
        super(taskName);
        this.stockId = stockId;
        this.dateList = dateList;
    }

    @Override
    public void collectLogic() throws MalformedURLException, IOException
    {
        if(StringUtils.isEmpty(stockId) || dateList == null || dateList.isEmpty())
        {
            log.error("stockId or date is null");
        }
        List<StockTrend> ret = new ArrayList<StockTrend>();
        for (Date date : dateList)
        {
            List<StockTrend> stockTrendList = getStockTrendByDate(date);
            if (stockTrendList != null)
            {
                ret.addAll(stockTrendList);
            }
        }
        StockTrendUtils.storeStockTrend(ret, StockTrendType.TYPE_1D);
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
    
    private List<StockTrend> getStockTrendByDate(Date date)  throws MalformedURLException, IOException
    {
        String target = URLMapper.LONGHUBANG_JSON.toString();
        RequestParaBuilder builder = new RequestParaBuilder(target);
        builder.addParameter("date", sdf.format(date));
        builder.addParameter("symbol", stockId);
        
        URL url = new URL(builder.build());
        String json = request(url);
        List<StockTrend> stockTrendList = getStockTrendFromQuery(json, date);
        if (stockTrendList == null)
        {
            return null;
        }
        return stockTrendList;
    }
    
    /**
     * 在写入当天的股票信息的同时，需要将redis中缓存的当天分时的信息清楚掉
     * 
     * @param json
     * @return
     */
    private List<StockTrend> getStockTrendFromQuery(String json, Date date)
    {
        JsonObject stockJsonObject = (JsonObject) JsonUtils.getObject(json, JsonObject.class);
        List<StockTrend> stockTrendList = new ArrayList<>();
        try
        {
            JsonObject stockObj = stockJsonObject.get("detail").getAsJsonObject().get("tqQtSkdailyprice").getAsJsonObject();
            StockTrend stockTrend = new StockTrendAgg1d();
            stockTrend.setStockId(stockId);
            stockTrend.setCurrentPrice(stockObj.get("tclose").getAsDouble());
            stockTrend.setCreatedate(new Date(TimeUtils.getDayFirstTimestamp(date)));
            stockTrend.setOpen(stockObj.get("topen").getAsDouble());
            stockTrend.setHigh(stockObj.get("thigh").getAsDouble());
            stockTrend.setClose(stockObj.get("tclose").getAsDouble());
            stockTrend.setLow(stockObj.get("tlow").getAsDouble());
            stockTrend.setChg(stockObj.get("change").getAsDouble());
            stockTrend.setPercent(stockObj.get("pchg").getAsDouble());
            stockTrend.setTurnrate(Double.parseDouble(stockObj.get("turnrate").getAsString()));
            stockTrendList.add(stockTrend);
            return stockTrendList;
        }
        catch(Exception e)
        {
            log.info("Can not get stock data, stockId: " + stockId + "; date: " + date.toString());
            //e.printStackTrace();
            return null;
        }
    }
    
    class MyConstants
    {
        public static final int TIMEOUT = 1000;
    }
}
