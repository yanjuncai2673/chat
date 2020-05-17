package com.talk.persenter.dynamic;

import com.talk.base.BasePersenter;
import com.talk.common.CommonSubscriber;
import com.talk.interfaces.dynamic.Dynamicstract;
import com.talk.models.HttpManager;
import com.talk.models.bean.dynamic.DynamicBean;
import com.talk.utils.RxUtils;

public class DynamicListPresenter extends BasePersenter<Dynamicstract.DynamicView>implements Dynamicstract.DynamicPresenter {
    @Override
    public void getDynamicList(int page, int size, int trendsid) {
        addSubscribe(HttpManager.getInstance().getChatApi().getDynamicList(page, size, trendsid)
        .compose(RxUtils.rxScheduler())
        .subscribeWith(new CommonSubscriber<DynamicBean>(mView) {
            @Override
            public void onNext(DynamicBean dynamicBean) {
                mView.getDynamicListReturn(dynamicBean);
            }
        }));
    }
}
