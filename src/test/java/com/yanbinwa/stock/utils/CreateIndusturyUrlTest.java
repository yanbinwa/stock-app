package com.yanbinwa.stock.utils;

import java.util.Map;

import org.junit.Test;

import com.emotibot.middleware.utils.FileUtils;
import com.emotibot.middleware.utils.JsonUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class CreateIndusturyUrlTest
{
    public static final String Template = "https://xueqiu.com/industry/quote_order.json?page=1&size=1000&order=desc&orderBy=percent&%s";
    public static final String InputFile = "file/test.txt";
    
    @Test
    public void test()
    {
        String jsonStr = FileUtils.readFileToString(InputFile);
        JsonObject jsonObj = (JsonObject)JsonUtils.getObject(jsonStr, JsonObject.class);
        for(Map.Entry<String, JsonElement> entry : jsonObj.entrySet())
        {
            String industryInfo = jsonObj.get(entry.getKey()).getAsJsonObject().get("industryInfo").getAsString();
            industryInfo.substring(1);
            System.out.println(String.format(Template, industryInfo));
        }
    }

}
