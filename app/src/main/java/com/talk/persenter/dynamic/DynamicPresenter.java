package com.talk.persenter.dynamic;

import com.talk.base.BasePersenter;
import com.talk.common.CommonSubscriber;
import com.talk.interfaces.dynamic.Dynamicstract;
import com.talk.models.HttpManager;
import com.talk.models.bean.dynamic.DynamicSendBean;
import com.talk.utils.RxUtils;

public class DynamicPresenter extends BasePersenter<Dynamicstract.View> implements Dynamicstract.Presenter {
    @Override
    public void sendDynamicData(String txt, String resouces) {
        addSubscribe(HttpManager.getInstance().getChatApi().getDynamicSend(txt,resouces)
        .compose(RxUtils.rxScheduler())
        .subscribeWith(new CommonSubscriber<DynamicSendBean>(mView) {
            @Override
            public void onNext(DynamicSendBean dynamicSendBean) {
                mView.sendDynamicReturn(dynamicSendBean);
            }
        }));
    }
}
