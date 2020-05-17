package com.talk.utils;

public class SexUtils {
    /**
     * 性别工具类
     */

    public static String parseSex(int sex){
        if (sex == 0) {
            return "无";
        }else if (sex == 1) {
            return "男";
        }
        return "女";
    }
}
