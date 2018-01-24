package com.yanbinwa.stock.common.utils;

import java.util.List;

public class ListUtils
{
    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(List list)
    {
        if (list == null)
        {
            return true;
        }
        return list.isEmpty();
    }
}
