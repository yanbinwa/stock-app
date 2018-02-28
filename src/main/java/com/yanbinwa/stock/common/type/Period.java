package com.yanbinwa.stock.common.type;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.emotibot.middleware.utils.JsonUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.yanbinwa.stock.common.constants.Constants;
import com.yanbinwa.stock.common.utils.ListUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * 设定执行周期，包括是否是周期，下一次运行的时间（不管是否为周期，都要有运行的时间，这个时间会不断更新），运行的时间窗口（分为每周和每天的窗口），运行的周期
 * 
 * @author emotibot
 *
 */
@Getter
@Setter
public class Period
{
    public static final int MILLISECOND_IN_SECOND = 1000;
    public static final int SECOND_IN_MINUTE = 60;
    public static final int SECOND_IN_HOUR = 60 * SECOND_IN_MINUTE;
    public static final int SECOND_IN_DAY = 24 * SECOND_IN_HOUR;
    public static final int BAD_SET_NEXT_TIME_LOOP = 100;
    
    @SerializedName("dayWindowList")
    @Expose
    private List<DayWindow> dayWindowList;
    
    @SerializedName("hourWindowList")
    @Expose
    private List<HourWindow> hourWindowList;
    
    @SerializedName("interval")
    @Expose
    private int interval;
    
    @SerializedName("nextTimestamp")
    @Expose
    private long nextTimestamp = Constants.PERIOD_DEFAULT_NEXT_TIMESTAMP;
    
    @SerializedName("periodType")
    @Expose
    private PeriodType periodType = PeriodType.NONE;
    
    public Period()
    {
        
    }
    
    public boolean shouldExecute()
    {
        if (nextTimestamp < System.currentTimeMillis())
        {
            return true;
        }
        return false;
    }
    
    /**
     * 获取下一次的触发的时间，首先要保证interval，即按照现在的date + interval作为查找的起点，如果dayWindow不满足，则直接跳到一下天的00:00，再比较dayWindow，
     * 一旦dayWindow成立，比较hourWindow，如果不成立，直接跳到下一个hour的起始时间，在做比较。
     * 
     * 这里的tag为是否第一次设置时间，如果是第一次设置时间，就不需要加interval了
     * 
     * @return
     */
    public boolean setNextTime(boolean tag)
    {
        if (!tag && periodType == PeriodType.NONE)
        {
            return false;
        }
        
        if (ListUtils.isEmpty(dayWindowList) && ListUtils.isEmpty(hourWindowList))
        {
            if (tag)
            {
                nextTimestamp = System.currentTimeMillis();
            }
            else
            {
                nextTimestamp = System.currentTimeMillis() + ((long)interval) * MILLISECOND_IN_SECOND;
            }
            return true;
        }
        
        Date date = null;
        if (tag)
        {
            date = new Date(System.currentTimeMillis());
        }
        else
        {
            date = new Date(System.currentTimeMillis() + ((long)interval) * MILLISECOND_IN_SECOND);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);        
        calendar = chooseDay(calendar);
        calendar = chooseTime(calendar);
        nextTimestamp = calendar.getTime().getTime();
        return true;
    }
    
    public boolean setNextTime()
    {
        return setNextTime(false);
    }
    
    private Calendar chooseDay(Calendar calendar)
    {
        if (ListUtils.isEmpty(dayWindowList))
        {
            for(HourWindow window : hourWindowList)
            {
                if (HourWindow.isDateInWindow(calendar, window))
                {
                    return calendar;
                }
            }
            //如果当天没有找到，就跳到下一天的起始
            calendar.add(Calendar.DATE, 1);
            clearDayTime(calendar);
            return calendar;
        }
        else
        {
            for(DayWindow window : dayWindowList)
            {
                if(DayWindow.isDateInWindow(calendar, window))
                {
                    //判断是否符合小时的窗口
                    if (ListUtils.isEmpty(hourWindowList))
                    {
                        return calendar;
                    }
                    for (HourWindow hourWindow : hourWindowList)
                    {
                        if (HourWindow.isBeforeOrInWindow(calendar, hourWindow))
                        {
                            return calendar;
                        }
                    }
                    calendar.add(Calendar.DATE, 1);
                    clearDayTime(calendar);
                    break;
                }
            }
            
            clearDayTime(calendar);
            
            while(true)
            {
                for(DayWindow window : dayWindowList)
                {
                    if(DayWindow.isDateInWindow(calendar, window))
                    {
                        return calendar;
                    }
                }
                calendar.add(Calendar.DATE, 1);
            }
        }
    }
    
    private Calendar chooseTime(Calendar calendar)
    {
        if (ListUtils.isEmpty(hourWindowList))
        {
            return calendar;
        }
        while(true)
        {
            for(HourWindow window : hourWindowList)
            {
                if(HourWindow.isDateInWindow(calendar, window))
                {
                    return calendar;
                }
            }
            calendar.add(Calendar.HOUR_OF_DAY, 1);
            clearHourTime(calendar);
        }
    }
    
    private Calendar clearDayTime(Calendar calandar)
    {
        calandar.set(Calendar.HOUR_OF_DAY, 0);
        calandar.set(Calendar.MINUTE, 0);
        calandar.set(Calendar.SECOND, 0);
        return calandar;
    }
    
    private Calendar clearHourTime(Calendar calandar)
    {
        calandar.set(Calendar.MINUTE, 0);
        calandar.set(Calendar.SECOND, 0);
        return calandar;
    }
    
    @Override
    public String toString()
    {
        return JsonUtils.getJsonStr(this);
    }
}
