package com.talk.persenter.mine;

import com.talk.base.BasePersenter;
import com.talk.common.CommonSubscriber;
import com.talk.interfaces.mine.UserDetailInfoConstract;
import com.talk.models.HttpManager;
import com.talk.models.bean.login.RegisterInfoBean;
import com.talk.models.bean.mine.UserDetailInfoBean;
import com.talk.models.bean.mine.UserUpdateInfoBean;
import com.talk.utils.RxUtils;

import java.util.Map;

public class UserDetailInfoPresenter extends BasePersenter<UserDetailInfoConstract.DetailView>implements UserDetailInfoConstract.DetailPresenter {
    @Override
    public void userDetailInfo() {
        addSubscribe(HttpManager.getInstance().getChatApi().getUserDetailInfo()
                .compose(RxUtils.<UserDetailInfoBean>rxScheduler())
                .subscribeWith(new CommonSubscriber<UserDetailInfoBean>(mView) {
                    @Override
                    public void onNext(UserDetailInfoBean userDetailInfoBean) {
                        mView.getUserDetailInfoReturn(userDetailInfoBean);
                    }
                }));
    }

    @Override
    public void userUpdateInfo(Map<String, String> map) {
        addSubscribe(HttpManager.getInstance().getChatApi().getUpdateInfo(map)
                .compose(RxUtils.<UserUpdateInfoBean>rxScheduler())
                .subscribeWith(new CommonSubscriber<UserUpdateInfoBean>(mView) {
                    @Override
                    public void onNext(UserUpdateInfoBean updateInfoBean) {
                        mView.getUserUpdateInfoReturn(updateInfoBean);
                    }
                }));
    }
}
