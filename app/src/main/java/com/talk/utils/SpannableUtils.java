package com.talk.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.view.TextureView;

import com.talk.R;

/**
 * 富文本工具类
 */
public class SpannableUtils {

    /**
     * 将String 转化成SpannableString
     * 格式[XXX]二位文字
     */
    public static SpannableString stringtoSpannableString(Context context,String s){
        if (TextUtils.isEmpty(s)) return null;


        SpannableString spannableString = new SpannableString(s);
        Drawable drawable = context.getResources().getDrawable(R.drawable.music);
        //显示到图片大小
        drawable.setBounds(20,20,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
        //显示图标的Imagespan   图文对齐方式基线对齐
        ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
        int star = s.indexOf("[");
        int end = s.indexOf("]")+1;
        //设置图文显示位置 方式
        spannableString.setSpan(imageSpan,star,end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    /**
     * 超链接 富文本
     * @param context
     * @param s
     * @return
     */
    public static SpannableString getSpanUrl(Context context,String s ){
        if (TextUtils.isEmpty(s)) {
            return null;

        }
        URLSpan urlSpan = new URLSpan("https://baike.baidu.com/item/%E6%9D%A8%E5%B9%82/149851?fr=aladdin");
        int star = s.indexOf("<");
        int end = s.indexOf(">")+1;
        String substring = s.substring(star + 1, end - 1);
        String news = substring + s.substring(end + 1, s.length());

        SpannableString spannableString = new SpannableString(news);
        spannableString.setSpan(urlSpan,star,end-2,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;

    }

    /**
     * 富文本背景色
     *
     */
    public static SpannableString getSpanBackground(Context context,String s){
        if (TextUtils.isEmpty(s)) {
            return null;
        }
        SpannableString spannableString = new SpannableString(s);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#ff0000"));
        spannableString.setSpan(foregroundColorSpan,6,spannableString.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
}
