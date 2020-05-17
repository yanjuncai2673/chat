package com.talk.interfaces.login;

import com.talk.interfaces.IBasePersenter;
import com.talk.interfaces.IBaseView;
import com.talk.models.bean.login.LoginInfoBean;

public interface LoginConstract {
    interface View extends IBaseView{//登录信息的返回
        void loginReturn(LoginInfoBean loginInfoBean);
    }

    interface Presenter extends IBasePersenter<View>{
        void login(String username,String pwd);
    }
}
