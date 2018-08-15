package com.yanbinwa.stock.utils;

import com.emotibot.middleware.utils.JsonUtils;
import org.springframework.beans.BeanUtils;

public class DTOUtils {
    public static String toJsonStr(Object obj) {
        try {
            return JsonUtils.getJsonStr(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static <T> T copy(Object source, T target) {
        BeanUtils.copyProperties(source, target);
        return target;
    }
}
