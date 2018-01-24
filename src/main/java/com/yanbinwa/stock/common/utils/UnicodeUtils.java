package com.yanbinwa.stock.common.utils;

import java.io.UnsupportedEncodingException;

public class UnicodeUtils
{
    public static String unicodeDecode(String str) throws UnsupportedEncodingException
    {
        byte[] utf8 = str.getBytes("UTF-8");
        return new String(utf8, "UTF-8");
    }
}
