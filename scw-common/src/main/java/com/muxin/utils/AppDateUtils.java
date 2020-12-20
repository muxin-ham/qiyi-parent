package com.muxin.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AppDateUtils {
    /**
     * 生成默认形式的当前时间
     * @return
     */
    public static String getFormatTime(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = format.format(new Date());
        return date;
    }

    /**
     * 生成指定形式的当前时间
     * @return
     */
    public static String getFormatTime(String pattern){
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        String date = format.format(new Date());
        return date;
    }


    public static String getFormatTime(String pattern,Date time){
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        String date = format.format(time);
        return date;
    }
}
