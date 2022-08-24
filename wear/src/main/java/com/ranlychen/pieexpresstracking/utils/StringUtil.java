package com.ranlychen.pieexpresstracking.utils;


public class StringUtil {
    /**
     * 方 法 名：convertString(String str)
     * 功    能：将传入的字符串中的小写字母转为大写字母
     * 参    数：String str
     * 返 回 值：String
     */
    public static String toUpperCase(String str)
    {
        String upStr = str.toUpperCase();
        StringBuffer buf = new StringBuffer(str.length());
        for(int i=0;i<str.length();i++) {
            buf.append(upStr.charAt(i));
        }
        return   buf.toString();
    }
}
