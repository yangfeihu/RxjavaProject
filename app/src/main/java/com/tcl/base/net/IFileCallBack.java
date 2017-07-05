package com.tcl.base.net;


/**
 * Created by yangfeihu on 2017/1/4.
 *  文件上传下载带进度回调
 */

public interface IFileCallBack {
    void onProgress(long progress,long total);
    void onFailure(String error);
    //此处的path对于下载而言返回的是文件的路径
    //对于上传而言，返回的仅仅是一个成功的标识而已
    void onSuccess(String path);
}
