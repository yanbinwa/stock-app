package com.yanbinwa.stock.utils;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.SegmentedTimeline;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.ohlc.OHLCSeries;
import org.jfree.data.time.ohlc.OHLCSeriesCollection;

import com.emotibot.middleware.utils.TimeUtils;
import com.yanbinwa.stock.entity.stockTrend.StockTrend;
import com.yanbinwa.stock.service.collection.entity.StockMetaData;
import com.yanbinwa.stock.service.collection.utils.CollectionUtils;

/**
 * 输入一个stockTrend列表，输出这段时间的K线图
 * 
 * @author emotibot
 *
 */
public class DrawStockTrendUtils
{
    private static SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd"); 
    
    @SuppressWarnings("deprecation")
    public static void stockKChart(List<StockTrend> stockTrendList) throws ParseException, IOException 
    {
        if (stockTrendList == null || stockTrendList.isEmpty())
        {
            return;
        }
        Collections.sort(stockTrendList);
        OHLCSeriesCollection seriesCollection = new OHLCSeriesCollection();
        OHLCSeries series1 = new OHLCSeries("k_up");
        OHLCSeries series2 = new OHLCSeries("k_down");
        
        TimeSeriesCollection timeSeriesCollection = new TimeSeriesCollection();
        TimeSeries series3 = new TimeSeries("bar_up");
        TimeSeries series4 = new TimeSeries("bar_down");
        
        double mLow = Double.MAX_VALUE;
        double mHigh = Double.MIN_VALUE;
        int days = 0;
        String startDate = null;
        String endDate = null;
        List<Date> workDateList = new ArrayList<Date>();
        
        String stockId = stockTrendList.get(0).getStockId();
        StockMetaData stockMetaData = CollectionUtils.getStockMetaData(stockId);
        String title = stockMetaData.getName() + ":" + stockId;
        
        for (int i = 0; i < stockTrendList.size(); i ++)
        {
            StockTrend stockTrend = stockTrendList.get(i);
            Date date = new Date(TimeUtils.getDayFirstTimestamp(stockTrend.getCreatedate()));
            workDateList.add(date);
            Calendar quoteCalendar = Calendar.getInstance();
            quoteCalendar.setTimeInMillis(date.getTime());
            
            if (mHigh < stockTrend.getHigh())
            {
                mHigh = stockTrend.getHigh();
            }
            if (mLow > stockTrend.getLow())
            {
                mLow = stockTrend.getLow();
            }
            if (i == 0)
            {
                startDate = sdf.format(date);
            }
            else if (i == stockTrendList.size() - 1)
            {
                endDate = sdf.format(date);
            }
            
            if(stockTrend.getOpen() > stockTrend.getClose())
            {
                series2.add(new Day(quoteCalendar.get(Calendar.DAY_OF_MONTH), quoteCalendar.get(Calendar.MONTH) + 1, quoteCalendar.get(Calendar.YEAR)), 
                        stockTrend.getOpen(), stockTrend.getHigh(), stockTrend.getLow(), stockTrend.getClose());
                series4.add(new Day(quoteCalendar.get(Calendar.DAY_OF_MONTH), quoteCalendar.get(Calendar.MONTH) + 1, quoteCalendar.get(Calendar.YEAR)), 
                        stockTrend.getTurnrate());
            } 
            else 
            {
                series1.add(new Day(quoteCalendar.get(Calendar.DAY_OF_MONTH), quoteCalendar.get(Calendar.MONTH) + 1, quoteCalendar.get(Calendar.YEAR)), 
                        stockTrend.getOpen(), stockTrend.getHigh(), stockTrend.getLow(), stockTrend.getClose());
                series3.add(new Day(quoteCalendar.get(Calendar.DAY_OF_MONTH), quoteCalendar.get(Calendar.MONTH) + 1, quoteCalendar.get(Calendar.YEAR)), 
                        stockTrend.getTurnrate());
            }
            days ++;
        }
        
        seriesCollection.addSeries(series1);
        seriesCollection.addSeries(series2);
        timeSeriesCollection.addSeries(series3);
        timeSeriesCollection.addSeries(series4);
        List<Date> allHolidys = HolidayUtils.getHolidayDate(workDateList);
        
        
        StandardChartTheme standardChartTheme = new StandardChartTheme("CN");
        standardChartTheme.setExtraLargeFont(new Font("微软雅黑",Font.BOLD,20));
        standardChartTheme.setRegularFont(new Font("微软雅黑",Font.PLAIN,15));
        ChartFactory.setChartTheme(standardChartTheme);
        
        CandlestickRenderer candlestickRender = new CandlestickRenderer();
        candlestickRender.setUpPaint(Color.BLACK);
        candlestickRender.setDownPaint(Color.CYAN);
        candlestickRender.setSeriesPaint(1, Color.CYAN);
        candlestickRender.setSeriesPaint(0, Color.RED);
        candlestickRender.setAutoWidthMethod(CandlestickRenderer.WIDTHMETHOD_AVERAGE);
        candlestickRender.setAutoWidthGap(0.001);

        candlestickRender.setSeriesVisibleInLegend(false);
        
        //x轴
        DateAxis domainAxis = new DateAxis();
        domainAxis.setAutoRange(false);
        Date eda = sdf.parse(endDate);
        eda.setTime(eda.getTime() + 1);
        Date sda = sdf.parse(startDate);
        sda.setTime(sda.getTime() - 1);
        domainAxis.setRange(sda, eda);
        
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        domainAxis.setAutoTickUnitSelection(false);//设置不采用自动选择刻度值
        domainAxis.setTickMarkPosition(DateTickMarkPosition.START);//设置标记的位置
        domainAxis.setLabelFont(new Font("微软雅黑", Font.BOLD, 12));
        domainAxis.setStandardTickUnits(DateAxis.createStandardDateTickUnits());
        domainAxis.setTickUnit(new DateTickUnit(DateTickUnit.DAY, days));
        domainAxis.setDateFormatOverride(df);//设置时间格式
        SegmentedTimeline timeline = SegmentedTimeline.newMondayThroughFridayTimeline();
        for(Date holiday : allHolidys)
        {
            timeline.addException(holiday);
        }
        domainAxis.setTimeline(timeline);
        
        //y轴
        NumberAxis y1Axis = new NumberAxis();
        y1Axis.setAutoRange(false);
        y1Axis.setUpperMargin(0.5D);
        y1Axis.setLabelFont(new Font("微软雅黑", Font.BOLD, 12));
        y1Axis.setRange(mLow - (mLow * MyConstants.UP_OR_DOWN_RANGE), mHigh + (mHigh * MyConstants.UP_OR_DOWN_RANGE));
        
        XYPlot plot = new XYPlot(seriesCollection, domainAxis, y1Axis, candlestickRender);//生成画图细节
        plot.setBackgroundPaint(Color.BLACK);//设置曲线图背景色
        plot.setDomainGridlinesVisible(false);//不显示网格
        plot.setRangeGridlinePaint(Color.RED);//设置间距格线颜色为红色
        
        
        //柱状图x轴
        XYBarRenderer barRenderer = new XYBarRenderer();
        barRenderer.setDrawBarOutline(true);
        barRenderer.setBarPainter(new StandardXYBarPainter());
        barRenderer.setMargin(0.3);
               
        barRenderer.setSeriesPaint(0, Color.BLACK);
        barRenderer.setSeriesPaint(1, Color.CYAN);
        barRenderer.setSeriesOutlinePaint(0, Color.RED);
        barRenderer.setSeriesOutlinePaint(1, Color.CYAN);
        barRenderer.setSeriesVisibleInLegend(false);
        barRenderer.setShadowVisible(false);
        
        //柱状图y轴
        NumberAxis y2Axis=new NumberAxis();
        y2Axis.setLabelFont(new Font("微软雅黑", Font.BOLD, 12));
        y2Axis.setAutoRange(true);

        XYPlot plot2 = new XYPlot(timeSeriesCollection, null, y2Axis, barRenderer);
        plot2.setBackgroundPaint(Color.BLACK);
        plot2.setDomainGridlinesVisible(false);
        plot2.setRangeGridlinePaint(Color.RED);
        CombinedDomainXYPlot domainXYPlot = new CombinedDomainXYPlot(domainAxis);
        domainXYPlot.add(plot, 2);//添加图形区域对象，后面的数字是计算这个区域对象应该占据多大的区域2/3
        domainXYPlot.add(plot2, 1);//添加图形区域对象，后面的数字是计算这个区域对象应该占据多大的区域2/3
        domainXYPlot.setGap(10);//设置两个图形区域对象之间的间隔空间
        
        JFreeChart chart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT, domainXYPlot, true);
        String filePath = MyConstants.DIR + "/" + stockMetaData.getName() + "_" + stockId + "_" + df.format(sda) + "_" + df.format(eda) + ".png";
        File file = new File(filePath);
        if (file.exists())
        {
            file.delete();
        }
        ChartUtilities.saveChartAsPNG(file, chart, 520, 250);
    }
    
    class MyConstants
    {
        public static final int TIME_SCALE = 7;
        public static final double UP_OR_DOWN_RANGE = 0.1d;
        public static final String DIR = "./file/stock";
    }
}
