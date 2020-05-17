package com.talk.persenter.register;

import com.talk.base.BasePersenter;
import com.talk.common.CommonSubscriber;
import com.talk.interfaces.register.RegisterConstract;
import com.talk.models.HttpManager;
import com.talk.models.bean.login.RegisterInfoBean;
import com.talk.utils.RxUtils;

public class RegisterPersenter extends BasePersenter<RegisterConstract.View> implements RegisterConstract.Presenter {
    @Override
    public void goRegister(String username, String password) {
        addSubscribe(HttpManager.getInstance().getChatApi().register(username,password)
                .compose(RxUtils.<RegisterInfoBean>rxScheduler())
                .subscribeWith(new CommonSubscriber<RegisterInfoBean>(mView) {
                    @Override
                    public void onNext(RegisterInfoBean registerInfoBean) {
                        mView.registerReturn(registerInfoBean);
                    }
                }));
    }
}
