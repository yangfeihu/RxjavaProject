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
    //异步get(),返回List<T>
    Disposable get(String url, Class<T> claz, IListCallBack<T> callBack);
    //请求的数据不做任何的处理，直接以字符串的形式返回
    Disposable getString(String url,ICallBack<String> callBack);
    //异步post()
    Disposable post(String url, Class<?> clazz, ICallBack<T> callBack);
    //异步post(),返回List<T>
    Disposable post(String url, Class<T> claz, IListCallBack<T> callBack);
    //请求的数据不做任何的处理，直接以字符串的形式返回
    Disposable postString(String url,ICallBack<String> callBack);
    //文件下载
    void download(String url,IFileCallBack callBack);
    //文件上传
    void uploadFile(String url,File file,IFileCallBack callBack);
    //添加参数
    IRequest addParams(String key, Object values);
    //添加参数
    IRequest addParams(Map<String, Object> map);
    //添加参数（直接添加Json格式的字符串,这个是针对post请求的）
    IRequest addParams(String jsonString);
    //添加请求码(据此可以判断哪个请求返回的结果)
    IRequest addRequestCode(int requestCode);
}
