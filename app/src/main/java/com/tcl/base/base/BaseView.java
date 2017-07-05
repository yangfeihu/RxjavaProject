package com.tcl.base.base;

/**
 * Created by yangfeihu on 2017/1/12.
 */

public interface BaseView {
    void onError(String errorString, int errorcode, int requestcode);
    <T> void onSuccess(T t, int requestcode);
}
