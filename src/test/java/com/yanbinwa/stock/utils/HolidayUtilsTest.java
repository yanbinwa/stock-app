package com.yanbinwa.stock.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

public class HolidayUtilsTest
{

    @Test
    public void test()
    {
        test1();
    }

    public void test1()
    {
        HolidayUtils.buildHolidayData();
    }
    
    public void test2()
    {
        List<Date> dates = new ArrayList<Date>();
        dates.add(new Date());
        HolidayUtils.getHolidayDate(dates);
    }
}
