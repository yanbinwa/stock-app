package com.yanbinwa.stock.service.collection.task;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.emotibot.middleware.utils.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yanbinwa.stock.common.collector.AbstractCollector;
import com.yanbinwa.stock.common.http.RequestParaBuilder;
import com.yanbinwa.stock.common.http.URLMapper;
import com.yanbinwa.stock.common.type.Period;
import com.yanbinwa.stock.common.type.PeriodType;
import com.yanbinwa.stock.service.collection.entity.StockMetaData;
import com.yanbinwa.stock.service.collection.utils.CollectionUtils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * https://xueqiu.com/stock/cata/stocklist.json?page=1&size=1000&order=desc&orderby=percent&type=11%2C12&_=1527172657298
 * 
 * 获取所有股票的实时信息，还有所有股票的stockid
 * 
 * @author emotibot
 *
 */
@Slf4j
@Data
public class HouShenCollectionTask extends AbstractCollector
{

    private static final String[] STOCK_PREFIX = {"SH60", "SZ30", "SZ00"};
    
    public HouShenCollectionTask(String taskName)
    {
        super(taskName);
    }

    @Override
    public void collectLogic() throws MalformedURLException, IOException
    {
        String target = URLMapper.HU_SHEN_PAGE.toString();
        int pageIndex = 1;
        Map<String, StockMetaData> stockIdToMetaDatMap = new HashMap<String, StockMetaData>();
        while(true)
        {
            RequestParaBuilder builder = new RequestParaBuilder(target);
            builder.addParameter("page", pageIndex);
            builder.addParameter("size", MyConstants.PAGE_SIZE);
            builder.addParameter("order", "desc");
            builder.addParameter("orderby", "percent");
            builder.addParameter("type", "11%2C12");
            builder.addParameter("_", System.currentTimeMillis());
            URL url = new URL(builder.build());
            String json = request(url);
            
            List<StockMetaData> stockMetaDataList = getStockMetaDataFromQuery(json);
            if (stockMetaDataList == null)
            {
                break;
            }
            stockMetaDataList.stream().forEach(stockMetaData -> stockIdToMetaDatMap.put(stockMetaData.getStockId(), stockMetaData));
            pageIndex ++;
        }
        log.info("HouShenCollectionTask collect: " + stockIdToMetaDatMap.size());
        stockIdToMetaDatMap.values().stream().forEach(stockMetaData -> CollectionUtils.setStockMetaData(stockMetaData.getStockId(), stockMetaData));
    }
    
    private List<StockMetaData> getStockMetaDataFromQuery(String json)
    {
        JsonObject stockJsonObject = (JsonObject) JsonUtils.getObject(json, JsonObject.class);
        List<StockMetaData> stockMetaDataList = new ArrayList<StockMetaData>();
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
                StockMetaData stockMetaData = new StockMetaData();
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
                stockMetaData.setStockId(symbol);
                stockMetaData.setName(stockTrendObj.get("name").getAsString());
                stockMetaData.setVolume(stockTrendObj.get("volume").getAsDouble());
                stockMetaDataList.add(stockMetaData);
            }
            return stockMetaDataList;
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
        public static final int TIMEOUT = 10000;
        public static final String URL_TEMPLATE = "?page=%d&size=%d&order=desc&orderby=percent&type=11%2C12&_=%d";
        public static final int PAGE_SIZE = 100;
    }
}
