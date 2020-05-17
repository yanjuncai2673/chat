package com.talk.models.apis;

import com.talk.apps.MyApp;
import com.talk.models.bean.dynamic.DynamicBean;
import com.talk.models.bean.dynamic.DynamicSendBean;
import com.talk.models.bean.login.LoginInfoBean;
import com.talk.models.bean.login.RegisterInfoBean;
import com.talk.models.bean.mine.UserDetailInfoBean;
import com.talk.models.bean.mine.UserUpdateInfoBean;

import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 聊天界面Api接口
 */
public interface ChatApi {
    //登录api
    @POST("user/login")
    @FormUrlEncoded
    Flowable<LoginInfoBean>login(@Field("username")String username, @Field("password")String password);

    //注册
    @POST("user/register")
    @FormUrlEncoded
    Flowable<RegisterInfoBean>register(@Field("username")String username, @Field("password")String password);

    //用户详情信息接口
    @GET("user/details")
    Flowable<UserDetailInfoBean>getUserDetailInfo();


    //更新用户个人信息接口
    @POST("user/updateinfo")
    @FormUrlEncoded
    Flowable<UserUpdateInfoBean>getUpdateInfo(@FieldMap Map<String,String>map);

    //发布动态数据接口
    @POST("trends/sendTrends")
    @FormUrlEncoded
    Flowable<DynamicSendBean>getDynamicSend(@Field("content")String content,@Field("resources")String resources);

    //获取动态列表显示
    @GET("trends/queryTrends")
    Flowable<DynamicBean>getDynamicList(@Query("page")int page, @Query("size")int size, @Query("trendsid")int trendsid);
}
