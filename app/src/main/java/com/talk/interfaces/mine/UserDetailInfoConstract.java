package com.talk.interfaces.mine;

import com.talk.interfaces.IBasePersenter;
import com.talk.interfaces.IBaseView;
import com.talk.models.bean.mine.UserDetailInfoBean;
import com.talk.models.bean.mine.UserUpdateInfoBean;

import java.util.Map;

//用户详情信息接口
public interface UserDetailInfoConstract {
    interface DetailView extends IBaseView{
        //返回个人详情信息
        void getUserDetailInfoReturn(UserDetailInfoBean userDetailInfoBean);

        //返回更新个人信息
        void getUserUpdateInfoReturn(UserUpdateInfoBean updateInfoBean);
    }

    interface  DetailPresenter extends IBasePersenter<DetailView>{
        void userDetailInfo();

        //更新信息
        void userUpdateInfo(Map<String,String>map);//个人信息条目用map 存取key value值
    }
}
