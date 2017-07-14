package com.tcl.base.ui.login;


import com.tcl.base.net.TResult;
import com.tcl.base.net.ICallBack;

/**
 * Created by yangfeihu on 2017/1/12.
 */

public class LoginPresenter extends  LoginContract.Presenter{

    @Override
    void login(String name, String pwd) {
        String url = "http://172.19.0.11:8080/HttpSerer/Login";
        request.addParams("name",name)
               .addParams("password",pwd)
               .post(url, String.class, new ICallBack<String>() {
                   @Override
                   public void onFailure(String errorString, int errorCode, int requestCode) {
                       mView.onError(errorString,errorCode,requestCode);
                   }
                   @Override
                    public void onResponse(TResult<String> t) {
                        mView.onSuccess(t.getData(),t.getRequestcode());
                    }
                });
    }

    @Override
    void duobao(String pageNo, String pageSize) {
        request.addParams("pageNo",pageNo)
                .addParams("pageSize",pageSize)
                .getString("http://sblive.cn/api/homepage/category/detail.json?categoryId=9999", new ICallBack<String>() {
                    @Override
                    public void onFailure(String errorString, int errorCode, int requestCode) {
                        mView.onError(errorString,errorCode,requestCode);
                    }
                    @Override
                    public void onResponse(TResult<String> t) {
                        mView.onSuccess(t.getData(),t.getRequestcode());
                    }
                });

    }

    @Override
    public void onAttached() {

    }
}
