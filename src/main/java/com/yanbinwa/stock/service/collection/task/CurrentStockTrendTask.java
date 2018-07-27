package com.yanbinwa.stock.service.collection.task;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.emotibot.middleware.utils.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yanbinwa.stock.common.collector.AbstractCollector;
import com.yanbinwa.stock.common.http.RequestParaBuilder;
import com.yanbinwa.stock.common.http.URLMapper;
import com.yanbinwa.stock.common.type.DayWindow;
import com.yanbinwa.stock.common.type.HourWindow;
import com.yanbinwa.stock.common.type.Period;
import com.yanbinwa.stock.common.type.PeriodType;
import com.yanbinwa.stock.entity.stockTrend.StockTrend;
import com.yanbinwa.stock.entity.stockTrend.StockTrendType;
import com.yanbinwa.stock.service.collection.entity.StockTrendRaw;
import com.yanbinwa.stock.service.collection.utils.CollectionUtils;
import com.yanbinwa.stock.utils.StockTrendUtils;

/**
 * 1. 更新当前股票信息，写入到redis中，20秒请求一次
 * 
 * 2. 维护分时信息
 * 
 * 3. 分页读取股票信息
 * 
 * 4. 停牌信息
 * 
 * @author emotibot
 *
 */
public class CurrentStockTrendTask extends AbstractCollector
{
    private static final DayWindow[] dayWindowArray = {DayWindow.MONDAY, DayWindow.TUESDAY, DayWindow.WEDNESDAY, DayWindow.THURSDAY, DayWindow.FRIDAY};
    private static final HourWindow[] hourWindowArray = {HourWindow.HOUR9_SH, HourWindow.HOUR10_FH, HourWindow.HOUR10_SH, HourWindow.HOUR11_FH, HourWindow.HOUR13_FH, HourWindow.HOUR13_SH, HourWindow.HOUR14_FH, HourWindow.HOUR14_SH};
    private static final int periodInterval = Period.MILLISECOND_IN_SECOND * 20;
    
    private static final String[] STOCK_PREFIX = {"SH60", "SZ30", "SZ00"};
    
    private int pageIndex;
    private Map<String, StockTrend> stockIdToStockTrend;
    
    public CurrentStockTrendTask(String taskName, int pageIndex)
    {
        super(taskName);
        this.pageIndex = pageIndex;
        stockIdToStockTrend = new ConcurrentHashMap<String, StockTrend>();
    }

    @Override
    public void collectLogic() throws MalformedURLException, IOException
    {
        String target = URLMapper.HU_SHEN_PAGE.toString();
        RequestParaBuilder builder = new RequestParaBuilder(target);
        builder.addParameter("page", pageIndex);
        builder.addParameter("size", MyConstants.PAGE_SIZE);
        builder.addParameter("order", "desc");
        builder.addParameter("orderby", "percent");
        builder.addParameter("type", "11%2C12");
        builder.addParameter("_", System.currentTimeMillis());
        URL url = new URL(builder.build());
        String json = request(url);
        
        List<StockTrend> stockTrendList = getStockTrendFromQuery(json);
        if (stockTrendList == null)
        {
            return;
        }
        //更新到map中
        stockTrendList.forEach(stockTrend -> stockIdToStockTrend.put(stockTrend.getStockId(), stockTrend));
        //更新到redis中
        stockTrendList.stream().forEach(stockTrend -> CollectionUtils.setStockTrendData(stockTrend.getStockId(), stockTrend));
        //更新到数据库中
        StockTrendUtils.storeStockTrend(stockTrendList, StockTrendType.TYPE_RAW);
    }
    
    private List<StockTrend> getStockTrendFromQuery(String json)
    {
        JsonObject stockJsonObject = (JsonObject) JsonUtils.getObject(json, JsonObject.class);
        List<StockTrend> stockTrendList = new ArrayList<StockTrend>();
        try
        {
            JsonArray stockListObj = stockJsonObject.get("stocks").getAsJsonArray();
            if (stockListObj.size() == 0)
            {
                return null;
            }
            for (int i = 0; i < stockListObj.size(); i ++)
            {
                JsonObject stockTrendObj = stockListObj.get(i).getAsJsonObject();
                StockTrend stockTrend = new StockTrendRaw();
                String symbol = stockTrendObj.get("symbol").getAsString().trim();
                boolean isStock = false;
                for (String stockPrefix : STOCK_PREFIX)
                {
                    if (symbol.startsWith(stockPrefix))
                    {
                        isStock = true;
                        break;
                    }
                }
                if (!isStock)
                {
                    continue;
                }
                stockTrend.setStockId(symbol);
                stockTrend.setCreatedate(new Date());
                stockTrend.setCurrentPrice(stockTrendObj.get("current").getAsDouble());
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

    @Override
    public Period generatePeriod()
    {
        Period period = new Period();
        period.setPeriodType(PeriodType.PERIOD);
        period.setInterval(periodInterval);
        List<DayWindow> dayWindowList = new ArrayList<DayWindow>();
        Collections.addAll(dayWindowList, dayWindowArray);
        period.setDayWindowList(dayWindowList);
        List<HourWindow> hourWindowList = new ArrayList<HourWindow>();
        Collections.addAll(hourWindowList, hourWindowArray);
        period.setHourWindowList(hourWindowList);
        return period;
    }

    @Override
    public void setTimeout()
    {
        this.timeout = MyConstants.TIMEOUT;
    }

    class MyConstants
    {
        public static final int TIMEOUT = 1000;
        public static final int PAGE_SIZE = 100; 
    }
}
