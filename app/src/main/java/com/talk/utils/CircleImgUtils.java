package com.talk.utils;

import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.talk.apps.MyApp;

/**
 * 将方形图转化成圆型图
 */
public class CircleImgUtils {

    public static void userHeaderCircle(String url, ImageView img){
        if (!TextUtils.isEmpty(url) && img != null) {
            RequestOptions requestOptions = RequestOptions.circleCropTransform();
            Glide.with(MyApp.myApp).load(url).apply(requestOptions).into(img);
        }
    }

    /**
     * 带有图片加载是否完成的监听方法
     * @param url
     * @param img
     * @param listener
     */
    public static void userHeaderCircle(String url, ImageView img, RequestListener listener){
        if(!TextUtils.isEmpty(url) && img != null){
            RequestOptions options = RequestOptions.circleCropTransform();
            Glide.with(MyApp.myApp).load(url).addListener(listener).apply(options).into(img);
        }
    }
}
