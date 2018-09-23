package com.yanbinwa.stock.service.collection.task;

import com.emotibot.middleware.utils.JsonUtils;
import com.yanbinwa.stock.common.collector.AbstractCollector;
import com.yanbinwa.stock.common.http.RequestParaBuilder;
import com.yanbinwa.stock.common.http.URLMapper;
import com.yanbinwa.stock.common.type.Period;
import com.yanbinwa.stock.common.type.PeriodType;
import com.yanbinwa.stock.entity.stockTrend.StockTrend;
import com.yanbinwa.stock.service.collection.element.Industry;
import com.yanbinwa.stock.service.collection.element.IndustryToStockCollection.IndustryToStock;
import com.yanbinwa.stock.service.collection.element.IndustryToStockCollection.IndustryToStockCollectionElement;
import com.yanbinwa.stock.service.collection.entity.StockMetaData;
import com.yanbinwa.stock.service.collection.entity.StockTrendRaw;
import com.yanbinwa.stock.service.collection.utils.CollectionUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 这里每一个Industry单独作为一个task，应该在CommissionIndustry中创建
 * 在query时要考虑当前是否已经交
 * 
 * @author emotibot
 *
 */
@Data
@Slf4j
public class IndustryToStocksCollectionTask extends AbstractCollector
{

//    private static final DayWindow[] dayWindowArray = {DayWindow.MONDAY, DayWindow.TUESDAY, DayWindow.WEDNESDAY, DayWindow.THURSDAY, DayWindow.FRIDAY};
//    private static final HourWindow[] hourWindowArray = {HourWindow.HOUR9, HourWindow.HOUR10, HourWindow.HOUR13, HourWindow.HOUR14};
//    private static final int periodInterval = Period.SECOND_IN_MINUTE;
    
//    private static final DayWindow[] dayWindowArray = {};
//    private static final HourWindow[] hourWindowArray = {};
//    private static final int periodInterval = Period.SECOND_IN_MINUTE;
    
    private Industry industry;
    
    public IndustryToStocksCollectionTask(String taskName)
    {
        super(taskName);
    }
    
    public IndustryToStocksCollectionTask(Industry industry)
    {
        super(industry.getIndustryName());
        this.industry = industry;
    }

    @Override
    public void collectLogic() throws MalformedURLException, IOException
    {
        if(industry == null)
        {
            log.error("industry is empty");
        }

        String target = URLMapper.INDUSTRY_JSON.toString();
        RequestParaBuilder builder = new RequestParaBuilder(target);
        builder.addParameter("page", 1)
                .addParameter("size", 500)
                .addParameter("order", "desc")
                .addParameter("orderBy", "percent");
        String info = industry.getIndustryInfo();
        
        if (info.startsWith("#")) 
        {
            info = info.substring(1);
        }
        for (String s : info.split("&")) 
        {
            String[] keyAndVal = s.split("=");
            builder.addParameter(keyAndVal[0], keyAndVal[1]);
        }
        URL url = new URL(builder.build());
        String json = request(url);
        List<StockTrend> stockList = getStockTrendFromQuery(json);
        updateIndustryToStockId(stockList, industry.getIndustryName());
        List<StockMetaData> stockMateDataList = getStockMetaDataFromQuery(json);
        updateStockMetaData(stockMateDataList);
        //StockTrendUtils.storeStockTrend(stockList, StockTrendType.TYPE_RAW);
        log.debug("result is json " + json);
    }

    @Override
    public Period generatePeriod()
    {
//        Period period = new Period();
//        period.setPeriodType(PeriodType.PERIOD);
//        period.setInterval(periodInterval);
//        List<DayWindow> dayWindowList = new ArrayList<DayWindow>();
//        Collections.addAll(dayWindowList, dayWindowArray);
//        period.setDayWindowList(dayWindowList);
//        List<HourWindow> hourWindowList = new ArrayList<HourWindow>();
//        Collections.addAll(hourWindowList, hourWindowArray);
//        period.setHourWindowList(hourWindowList);
//        return period;
        Period period = new Period();
        period.setPeriodType(PeriodType.NONE);
        return period;
    }

    @Override
    public void setTimeout()
    {
        this.timeout = MyConstants.TIMEOUT;
    }
    
    @Override
    public String createUploadStr()
    {
        return JsonUtils.getJsonStr(industry);
    }

    private List<StockTrend> getStockTrendFromQuery(String result)
    {
        try
        {
            IndustryToStockCollectionElement element = 
                    (IndustryToStockCollectionElement)JsonUtils.getObject(result, IndustryToStockCollectionElement.class);
            if (element == null || element.getData() == null)
            {
                return null;
            }
            List<StockTrend> ret = new ArrayList<StockTrend>();
            for (IndustryToStock industryToStock : element.getData())
            {
                ret.add(new StockTrendRaw(industryToStock));
            }
            return ret;
        }
        catch(Exception e)
        {
            return null;
        }
    }
    
    private void updateIndustryToStockId(List<StockTrend> stockTrendList, String industryName)
    {
        List<String> stockIds = new ArrayList<String>();
        for (StockTrend stockTrend : stockTrendList)
        {
            stockIds.add(stockTrend.getStockId());
        }
        CollectionUtils.setIndustryToStockId(industryName, stockIds);
    }
    
    private List<StockMetaData> getStockMetaDataFromQuery(String result)
    {
        try
        {
            IndustryToStockCollectionElement element = 
                    (IndustryToStockCollectionElement)JsonUtils.getObject(result, IndustryToStockCollectionElement.class);
            if (element == null || element.getData() == null)
            {
                return null;
            }
            List<StockMetaData> ret = new ArrayList<StockMetaData>();
            for (IndustryToStock industryToStock : element.getData())
            {
                StockMetaData stockMetaData = new StockMetaData();
                stockMetaData.setStockId(industryToStock.getSymbol());
                stockMetaData.setName(industryToStock.getName());
                stockMetaData.setVolume(industryToStock.getVolume());
                ret.add(stockMetaData);
            }
            return ret;
        }
        catch(Exception e)
        {
            return null;
        }
    }
    
    private void updateStockMetaData(List<StockMetaData> stockMetaDataList)
    {
        for (StockMetaData stockMetaData : stockMetaDataList)
        {
            CollectionUtils.setStockMetaData(stockMetaData.getStockId(), stockMetaData);
        }
    }
    
    class MyConstants
    {
        public static final int TIMEOUT = 10000;
    }

}
