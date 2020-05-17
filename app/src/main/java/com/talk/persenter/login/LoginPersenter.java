package com.talk.persenter.login;

import com.talk.base.BasePersenter;
import com.talk.common.CommonSubscriber;
import com.talk.interfaces.login.LoginConstract;
import com.talk.models.HttpManager;
import com.talk.models.bean.login.LoginInfoBean;
import com.talk.utils.RxUtils;

public class LoginPersenter extends BasePersenter<LoginConstract.View> implements LoginConstract.Presenter {
    @Override
    public void login(String username, String pwd) {
        addSubscribe(HttpManager.getInstance().getChatApi().login(username,pwd)
        .compose(RxUtils.<LoginInfoBean>rxScheduler())
        .subscribeWith(new CommonSubscriber<LoginInfoBean>(mView) {
            @Override
            public void onNext(LoginInfoBean loginInfoBean) {
                mView.loginReturn(loginInfoBean);
            }
        }));
    }
}
