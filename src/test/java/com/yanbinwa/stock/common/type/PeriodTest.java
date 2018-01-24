package com.yanbinwa.stock.common.type;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

public class PeriodTest
{

    //public static final DayWindow[] dayWindowArray = {DayWindow.MONDAY, DayWindow.TUESDAY, DayWindow.WEDNESDAY, DayWindow.THURSDAY, DayWindow.FRIDAY};
    public static final DayWindow[] dayWindowArray = {};
    public static final HourWindow[] hourWindowArray = {HourWindow.HOUR9, HourWindow.HOUR10, HourWindow.HOUR13, HourWindow.HOUR14};
    public static final String dateStr = "2017-11-17 14:59:16";
    
    @Test
    public void test() throws ParseException
    {
        Period period = new Period();
        List<DayWindow> dayWindowList = new ArrayList<DayWindow>();
        for(int i = 0; i < dayWindowArray.length; i ++)
        {
            dayWindowList.add(dayWindowArray[i]);
        }
        List<HourWindow> hourWindowList = new ArrayList<HourWindow>();
        for(int i = 0; i < hourWindowArray.length; i ++)
        {
            hourWindowList.add(hourWindowArray[i]);
        }
        period.setDayWindowList(dayWindowList);
        period.setHourWindowList(hourWindowList);
        period.setInterval(60);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d H:m:s");  
        Date date = dateFormat.parse(dateStr);
        period.setNextTimestamp(date.getTime());
        period.setNextTime(true);
        date = new Date(period.getNextTimestamp());
        System.out.println(date);
    }

}
