package com.tcl.base.net;

import java.io.File;
import java.util.Map;

import io.reactivex.disposables.Disposable;

/**
 * Created by yangfeihu on 2017/1/4.
 */

public interface IRequest<T> {
    //异步get()
    Disposable get(String url, Class<?> clazz, ICallBack<T> callBack);
    //异步post()
    Disposable post(String url, Class<?> clazz, ICallBack<T> callBack);
    //文件下载
    void download(String url,IFileCallBack callBack);
    //文件上传
    void uploadFile(String url,File file,IFileCallBack callBack);



    //添加参数
    IRequest addParams(String key, Object values);
    //添加参数
    IRequest addParams(Map<String, Object> map);
    //添加参数
    IRequest addParams(String jsonString);
    //添加请求码
    IRequest addRequestCode(int requestCode);
}
