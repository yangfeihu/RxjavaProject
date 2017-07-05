package com.tcl.base.ui.login;


import com.tcl.base.base.BasePresenter;
import com.tcl.base.base.BaseView;

/**
 * Created by yangfeihu on 2017/1/12.
 */

public interface LoginContract {

    //由activity去实现
    interface View extends BaseView {
        void loginSuccess();
        void loginFailed();
    }
    //用于处理业务逻辑
    abstract class Presenter extends BasePresenter<View> {
        abstract void login(String name,String pwd);
    }

}
