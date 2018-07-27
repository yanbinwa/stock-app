package com.yanbinwa.stock.common.type;

import java.util.Calendar;

public enum HourWindow
{
    HOUR0_FH(0, true),
    HOUR0_SH(0, false),
    
    HOUR1_FH(1, true),
    HOUR1_SH(1, false),
    
    HOUR2_FH(2, true),
    HOUR2_SH(2, false),
    
    HOUR3_FH(3, true),
    HOUR3_SH(3, false),
    
    HOUR4_FH(4, true),
    HOUR4_SH(4, false),
    
    HOUR5_FH(5, true),
    HOUR5_SH(5, false),
    
    HOUR6_FH(6, true),
    HOUR6_SH(6, false),
    
    HOUR7_FH(7, true),
    HOUR7_SH(7, false),
    
    HOUR8_FH(8, true),
    HOUR8_SH(8, false),
    
    HOUR9_FH(9, true),
    HOUR9_SH(9, false),
    
    HOUR10_FH(10, true),
    HOUR10_SH(10, false),
    
    HOUR11_FH(11, true),
    HOUR11_SH(11, false),
    
    HOUR12_FH(12, true),
    HOUR12_SH(12, false),
    
    HOUR13_FH(13, true),
    HOUR13_SH(13, false),
    
    HOUR14_FH(14, true),
    HOUR14_SH(14, false),
    
    HOUR15_FH(15, true),
    HOUR15_SH(15, false),
    
    HOUR16_FH(16, true),
    HOUR16_SH(16, false),
    
    HOUR17_FH(17, true),
    HOUR17_SH(17, false),
    
    HOUR18_FH(18, true),
    HOUR18_SH(18, false),
    
    HOUR19_FH(19, true),
    HOUR19_SH(19, false),
    
    HOUR20_FH(20, true),
    HOUR20_SH(20, false),
    
    HOUR21_FH(21, true),
    HOUR21_SH(21, false),
    
    HOUR22_FH(22, true),
    HOUR22_SH(22, false),
    
    HOUR23_FH(23, true),
    HOUR23_SH(23, false);
    
    private int value;
    private boolean isFirstHalf;
    
    private HourWindow(int value, boolean isFirstHalf)
    {
        this.value = value;
        this.isFirstHalf = isFirstHalf;
    }
    
    public static boolean isDateInWindow(Calendar calendar, HourWindow window)
    {
        if (window.isFirstHalf)
        {
            if (calendar.get(Calendar.HOUR_OF_DAY) == window.value && calendar.get(Calendar.MINUTE) < 30)
            {
                return true;
            }
            return false;
        }
        else
        {
            if (calendar.get(Calendar.HOUR_OF_DAY) == window.value && calendar.get(Calendar.MINUTE) >= 30)
            {
                return true;
            }
            return false;
        }
    }
    
    public static boolean isDateBeforeWindow(Calendar calendar, HourWindow window)
    {
        if (window.isFirstHalf)
        {
            if (calendar.get(Calendar.HOUR_OF_DAY) < window.value)
            {
                return true;
            }
            return false;
        }
        else 
        {
            if (calendar.get(Calendar.HOUR_OF_DAY) < window.value ||
                    (calendar.get(Calendar.HOUR_OF_DAY) == window.value && calendar.get(Calendar.MINUTE) < 30))
            {
                return true;
            }
            return false;
        }
    }
    
    public static boolean isBeforeOrInWindow(Calendar calendar, HourWindow window)
    {
        return isDateInWindow(calendar, window) || isDateBeforeWindow(calendar, window);
    }
}
