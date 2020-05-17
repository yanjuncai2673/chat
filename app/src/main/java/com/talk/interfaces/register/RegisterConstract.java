package com.talk.interfaces.register;

import com.talk.interfaces.IBasePersenter;
import com.talk.interfaces.IBaseView;
import com.talk.models.bean.login.RegisterInfoBean;

public interface RegisterConstract {
    interface View extends IBaseView{
        void registerReturn(RegisterInfoBean registerInfoBean);
    }

    interface Presenter extends IBasePersenter<View>{
        void goRegister(String username,String password);
    }
}
