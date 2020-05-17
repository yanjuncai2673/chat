package com.talk.utils;

import android.content.Context;

/**
 * 控件 宽高单位转换工具类
 */
public class DpTools {


    //dp转px
    public static int dptopx(Context context,int dpValue){
        float density = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue*density+0.5f);
    }
}
