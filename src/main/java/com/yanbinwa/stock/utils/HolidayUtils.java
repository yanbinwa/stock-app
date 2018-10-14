package com.yanbinwa.stock.utils;

import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.emotibot.middleware.request.HttpRequest;
import com.emotibot.middleware.request.HttpRequestType;
import com.emotibot.middleware.response.HttpResponse;
import com.emotibot.middleware.utils.FileUtils;
import com.emotibot.middleware.utils.HttpUtils;
import com.emotibot.middleware.utils.JsonUtils;
import com.emotibot.middleware.utils.TimeUtils;
import com.google.gson.JsonObject;

public class HolidayUtils
{
    public static final String HOLIDAY_FILE = "./file/holiday/holiday.txt";
    public static final String FETCH_URL = "http://api.goseek.cn/Tools/holiday?date=%s";
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    private static Set<String> holidaySet;
    
    static
    {
        holidaySet = new HashSet<String>();
        List<String> lines = FileUtils.readFile(HOLIDAY_FILE);
        lines.stream().forEach(line -> holidaySet.add(line));
    }
    
    public static void buildHolidayData()
    {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(2017, 1, 1, 0, 0, 0);
        startCalendar.set(Calendar.MILLISECOND, 0);
        long startTimestamp = startCalendar.getTime().getTime();
        long endTimestamp = System.currentTimeMillis();
        List<String> holidayList = new ArrayList<String>();
        while (startTimestamp < endTimestamp)
        {
            HttpRequest request = new HttpRequest();
            request.setUrl(String.format(FETCH_URL, sdf.format(new Date(startTimestamp))));
            request.setType(HttpRequestType.GET);
            HttpResponse response = HttpUtils.call(request, 1000);
            if (response.getStateCode() != HttpURLConnection.HTTP_OK)
            {
                return;
            }
            JsonObject jsonObj = (JsonObject) JsonUtils.getObject(response.getResponse(), JsonObject.class);
            if (jsonObj.get("data").getAsInt() == 2)
            {
                holidayList.add(sdf.format(new Date(startTimestamp)));
            }
            startTimestamp += (long) 1000 * 60 * 60 * 24;
        }
        FileUtils.storeFile(holidayList, HOLIDAY_FILE);
    }
    
    public static List<Date> getHolidayDate(List<Date> dates)
    {
        List<Date> ret = new ArrayList<Date>();
        List<Date> realDates = TimeUtils.getDateListFromStartAndEndTimestamp(dates.get(0).getTime(), dates.get(dates.size() - 1).getTime());
        realDates.stream().filter(date -> isHoliday(date)).forEach(date -> ret.add(date));
        return ret;
    }
    
    public static boolean isHoliday(Date date)
    {
        if (holidaySet.contains(sdf.format(date)))
        {
            return true;
        }
        return false;
    }

    public static List<Date> removeHolidayAndWeekendDate(List<Date> dates) {
        List<Date> ret = new ArrayList<>();
        for (Date date : dates) {
            if (isHoliday(date)) {
                continue;
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
                continue;
            }
            ret.add(date);
        }
        return ret;
    }
}
