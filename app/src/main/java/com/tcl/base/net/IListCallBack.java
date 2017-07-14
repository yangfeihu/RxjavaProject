package com.tcl.base.net;


/**
 * Created by yangfeihu on 2017/1/16.
 * 返回一个List<T>
 */

public interface IListCallBack<T> {
    void onFailure(String errorString, int errorCode, int requestCode);
    void onResponse(ListResult<T> t);
}
