package com.yanbinwa.stock.common.type;

import java.util.Calendar;

public enum HourWindow
{
    HOUR0(0)
    {
        @Override
        public boolean isDateInWindow(Calendar calendar)
        {
            if (calendar.get(Calendar.HOUR_OF_DAY) == 0)
            {
                return true;
            }
            return false;
        }
    },
    
    HOUR1(1)
    {
        @Override
        public boolean isDateInWindow(Calendar calendar)
        {
            if (calendar.get(Calendar.HOUR_OF_DAY) == 1)
            {
                return true;
            }
            return false;
        }
    },
    
    HOUR2(2)
    {
        @Override
        public boolean isDateInWindow(Calendar calendar)
        {
            if (calendar.get(Calendar.HOUR_OF_DAY) == 2)
            {
                return true;
            }
            return false;
        }
    },
    
    HOUR3(3)
    {
        @Override
        public boolean isDateInWindow(Calendar calendar)
        {
            if (calendar.get(Calendar.HOUR_OF_DAY) == 3)
            {
                return true;
            }
            return false;
        }
    },
    
    HOUR4(4)
    {
        @Override
        public boolean isDateInWindow(Calendar calendar)
        {
            if (calendar.get(Calendar.HOUR_OF_DAY) == 4)
            {
                return true;
            }
            return false;
        }
    },
    
    HOUR5(5)
    {
        @Override
        public boolean isDateInWindow(Calendar calendar)
        {
            if (calendar.get(Calendar.HOUR_OF_DAY) == 5)
            {
                return true;
            }
            return false;
        }
    },
    
    HOUR6(6)
    {
        @Override
        public boolean isDateInWindow(Calendar calendar)
        {
            if (calendar.get(Calendar.HOUR_OF_DAY) == 6)
            {
                return true;
            }
            return false;
        }
    },
    
    HOUR7(7)
    {
        @Override
        public boolean isDateInWindow(Calendar calendar)
        {
            if (calendar.get(Calendar.HOUR_OF_DAY) == 7)
            {
                return true;
            }
            return false;
        }
    },
    
    HOUR8(8)
    {
        @Override
        public boolean isDateInWindow(Calendar calendar)
        {
            if (calendar.get(Calendar.HOUR_OF_DAY) == 8)
            {
                return true;
            }
            return false;
        }
    },
    
    HOUR9(9)
    {
        @Override
        public boolean isDateInWindow(Calendar calendar)
        {
            if (calendar.get(Calendar.HOUR_OF_DAY) == 9)
            {
                return true;
            }
            return false;
        }
    },
    
    HOUR10(10)
    {
        @Override
        public boolean isDateInWindow(Calendar calendar)
        {
            if (calendar.get(Calendar.HOUR_OF_DAY) == 10)
            {
                return true;
            }
            return false;
        }
    },
    
    HOUR11(11)
    {
        @Override
        public boolean isDateInWindow(Calendar calendar)
        {
            if (calendar.get(Calendar.HOUR_OF_DAY) == 11)
            {
                return true;
            }
            return false;
        }
    },
    
    HOUR12(12)
    {
        @Override
        public boolean isDateInWindow(Calendar calendar)
        {
            if (calendar.get(Calendar.HOUR_OF_DAY) == 12)
            {
                return true;
            }
            return false;
        }
    },
    
    HOUR13(13)
    {
        @Override
        public boolean isDateInWindow(Calendar calendar)
        {
            if (calendar.get(Calendar.HOUR_OF_DAY) == 13)
            {
                return true;
            }
            return false;
        }
    },
    
    HOUR14(14)
    {
        @Override
        public boolean isDateInWindow(Calendar calendar)
        {
            if (calendar.get(Calendar.HOUR_OF_DAY) == 14)
            {
                return true;
            }
            return false;
        }
    },
    
    HOUR15(15)
    {
        @Override
        public boolean isDateInWindow(Calendar calendar)
        {
            if (calendar.get(Calendar.HOUR_OF_DAY) == 15)
            {
                return true;
            }
            return false;
        }
    },
    
    HOUR16(16)
    {
        @Override
        public boolean isDateInWindow(Calendar calendar)
        {
            if (calendar.get(Calendar.HOUR_OF_DAY) == 16)
            {
                return true;
            }
            return false;
        }
    },
    
    HOUR17(17)
    {
        @Override
        public boolean isDateInWindow(Calendar calendar)
        {
            if (calendar.get(Calendar.HOUR_OF_DAY) == 17)
            {
                return true;
            }
            return false;
        }
    },
    
    HOUR18(18)
    {
        @Override
        public boolean isDateInWindow(Calendar calendar)
        {
            if (calendar.get(Calendar.HOUR_OF_DAY) == 18)
            {
                return true;
            }
            return false;
        }
    },
    
    HOUR19(19)
    {
        @Override
        public boolean isDateInWindow(Calendar calendar)
        {
            if (calendar.get(Calendar.HOUR_OF_DAY) == 19)
            {
                return true;
            }
            return false;
        }
    },
    
    HOUR20(20)
    {
        @Override
        public boolean isDateInWindow(Calendar calendar)
        {
            if (calendar.get(Calendar.HOUR_OF_DAY) == 20)
            {
                return true;
            }
            return false;
        }
    },
    
    HOUR21(21)
    {
        @Override
        public boolean isDateInWindow(Calendar calendar)
        {
            if (calendar.get(Calendar.HOUR_OF_DAY) == 21)
            {
                return true;
            }
            return false;
        }
    },
    
    HOUR22(22)
    {
        @Override
        public boolean isDateInWindow(Calendar calendar)
        {
            if (calendar.get(Calendar.HOUR_OF_DAY) == 22)
            {
                return true;
            }
            return false;
        }
    },
    
    HOUR23(23)
    {
        @Override
        public boolean isDateInWindow(Calendar calendar)
        {
            if (calendar.get(Calendar.HOUR_OF_DAY) == 23)
            {
                return true;
            }
            return false;
        }
    };
    
    private int value;
    
    private HourWindow(int value)
    {
        this.value = value;
    }
    
    public abstract boolean isDateInWindow(Calendar calendar);
    
    public static boolean isDateInWindow(Calendar calendar, HourWindow window)
    {
        return window.isDateInWindow(calendar);
    }
    
    public static boolean isBeforeOrInWindow(Calendar calendar, HourWindow window)
    {
        return calendar.get(Calendar.HOUR_OF_DAY) <= window.value;
    }
}
