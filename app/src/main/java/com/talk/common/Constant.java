package com.talk.common;

import com.talk.apps.MyApp;
import com.talk.models.bean.FaceListItemVo;

import java.io.File;

public class Constant {
    /**
     * 常量类
     */

    public static final int FACE_SMALL_W = 40; //dp为单位
    public static final int FACE_SMALL_H = 40;

    public static final int FACE_BIG_W = 80;
    public static final int FACE_BIG_H = 80;


    public static final String self_avater = "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=276120544,3199945486&fm=15&gp=0.jpg";

    public static final String other_avater = "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3812470235,1283494916&fm=15&gp=0.jpg";

    //设一个全局变量  表情列表对象
    public static FaceListItemVo curItemVo;


    //上传接口的基础地址
    public static final String BASE_UPLOAD_IMAGE_URL = "http://yun918.cn/study/public/";

    //聊天服务的基础地址
    public static final String BASE_CHAT_URL = "http://cdwan.cn:9001/";

    //网络缓存地址                              //获取缓存文件夹  绝对目录  +  文件名
    public static final String PATH_DATA = MyApp.myApp.getCacheDir().getAbsolutePath()+ File.separator+"data";
    public static final String PATH_CACHE = PATH_DATA+"/chat";



}
