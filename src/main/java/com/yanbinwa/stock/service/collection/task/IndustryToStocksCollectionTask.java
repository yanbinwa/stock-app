package com.yanbinwa.stock.service.collection.task;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.emotibot.middleware.utils.JsonUtils;
import com.yanbinwa.stock.common.collector.AbstractCollector;
import com.yanbinwa.stock.common.http.RequestParaBuilder;
import com.yanbinwa.stock.common.http.URLMapper;
import com.yanbinwa.stock.common.type.DayWindow;
import com.yanbinwa.stock.common.type.HourWindow;
import com.yanbinwa.stock.common.type.Period;
import com.yanbinwa.stock.common.type.PeriodType;
import com.yanbinwa.stock.service.collection.element.Industry;
import com.yanbinwa.stock.service.collection.element.IndustryToStockCollection.IndustryToStock;
import com.yanbinwa.stock.service.collection.element.IndustryToStockCollection.IndustryToStockCollectionElement;
import com.yanbinwa.stock.service.collection.entity.StockTrend;
import com.yanbinwa.stock.service.collection.utils.StockTrendUtils;

/**
 * 这里每一个Industry单独作为一个task，应该在CommissionIndustry中创建
 * 在query时要考虑当前是否已经交
 * 
 * @author emotibot
 *
 */

public class IndustryToStocksCollectionTask extends AbstractCollector
{

    private static Logger logger = Logger.getLogger(IndustryToStocksCollectionTask.class);
    
    private static final DayWindow[] dayWindowArray = {DayWindow.MONDAY, DayWindow.TUESDAY, DayWindow.WEDNESDAY, DayWindow.THURSDAY, DayWindow.FRIDAY};
    private static final HourWindow[] hourWindowArray = {HourWindow.HOUR9, HourWindow.HOUR10, HourWindow.HOUR13, HourWindow.HOUR14};
    private static final int periodInterval = Period.SECOND_IN_MINUTE;
    
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
            logger.error("industry is empty");
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
        StockTrendUtils.storeStockTrend(stockList);
        logger.debug("result is json " + json);
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
    
    @Override
    public void upLoad(String uploadStr)
    {
        this.industry = (Industry) JsonUtils.getObject(uploadStr, Industry.class);
    }
    
    @Override
    public String createUploadStr()
    {
        return JsonUtils.getJsonStr(industry);
    }
    
    public void setIndustry(Industry industry)
    {
        this.industry = industry;
    }
    
    public Industry getIndutry()
    {
        return this.industry;
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
                ret.add(new StockTrend(industryToStock));
            }
            return ret;
        }
        catch(Exception e)
        {
            return null;
        }
    }
    
    class MyConstants
    {
        public static final int TIMEOUT = 1000;
    }

}
