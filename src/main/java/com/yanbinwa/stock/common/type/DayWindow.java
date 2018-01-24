package com.yanbinwa.stock.common.type;

import java.util.Calendar;

public enum DayWindow implements Comparable<DayWindow>
{
    MONDAY
    {
        @Override
        public boolean isDateInWindow(Calendar calendar)
        {
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY)
            {
                return true;
            }
            return false;
        }
    },
    
    TUESDAY
    {
        @Override
        public boolean isDateInWindow(Calendar calendar)
        {
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY)
            {
                return true;
            }
            return false;
        }
    },
    
    WEDNESDAY
    {
        @Override
        public boolean isDateInWindow(Calendar calendar)
        {
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY)
            {
                return true;
            }
            return false;
        }
    },
    
    THURSDAY
    {
        @Override
        public boolean isDateInWindow(Calendar calendar)
        {
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY)
            {
                return true;
            }
            return false;
        }
    },
    
    FRIDAY
    {
        @Override
        public boolean isDateInWindow(Calendar calendar)
        {
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY)
            {
                return true;
            }
            return false;
        }
    },
    
    SATURDAY
    {
        @Override
        public boolean isDateInWindow(Calendar calendar)
        {
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
            {
                return true;
            }
            return false;
        }
    },
    
    SUNDAY
    {
        @Override
        public boolean isDateInWindow(Calendar calendar)
        {
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
            {
                return true;
            }
            return false;
        }
    };
        
    public abstract boolean isDateInWindow(Calendar calendar);
    
    public static boolean isDateInWindow(Calendar calendar, DayWindow window)
    {
        return window.isDateInWindow(calendar);
    }
}
