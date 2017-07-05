package com.tcl.base.ui.login;


import com.tcl.base.model.TResult;
import com.tcl.base.net.ICallBack;

/**
 * Created by yangfeihu on 2017/1/12.
 */

public class LoginPresenter extends  LoginContract.Presenter{

    private String url = "http://172.19.0.11:8080/HttpSerer/Login";
    @Override
    void login(String name, String pwd) {

        request.addParams("name",name)
               .addParams("password",pwd)
               .post(url, String.class, new ICallBack<String>() {
                   @Override
                   public void onFailure(String errorString, int errorCode, int requestCode) {
                       mView.loginFailed();
                   }
                   @Override
                    public void onResponse(TResult<String> t) {
                        mView.loginSuccess();
                    }
                });
    }
    @Override
    public void onAttached() {

    }
}
