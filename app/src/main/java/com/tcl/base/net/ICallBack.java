package com.tcl.base.net;


import com.tcl.base.model.TResult;

/**
 * Created by yangfeihu on 2017/1/4.
 * 普通请求的回调
 */

public interface ICallBack<T> {
    void onFailure(String errorString, int errorCode, int requestCode);
    void onResponse(TResult<T> t);
}
