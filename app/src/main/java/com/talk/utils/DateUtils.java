package com.talk.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
//将获取的时间一串数字格式化成具体年月日
public class DateUtils {

    /**
     * 根据日期格式得到时间戳 2020-4-23 0:0
     * @param string
     * @return
     */
    public static long getTimeByDate(String string){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date();
        try {
            date = simpleDateFormat.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     * 用时间戳计算时间格式
     * @param time  time 单位 s  3800
     * h 60*60   m 60  s 1
     * @return 00:00:00
     */
    public static String getDateByTime(long time){
        int hour = (int) (time/3600);
        int minutes = (int) ((time%3600)/60);
        int second = (int) (time%3600%60);
        StringBuilder builder = new StringBuilder();
        if(hour < 10){
            builder.append("0"+hour);
        }else{
            builder.append(hour);
        }
        builder.append(":");
        if(minutes < 10){
            builder.append("0"+minutes);
        }else{
            builder.append(minutes);
        }
        builder.append(":");
        if(second < 10){
            builder.append("0"+second);
        }else{
            builder.append(second);
        }
        return builder.toString();
    }


}
