package com.tcl.base.net;

/**
 * Created by yangfeihu on 2017/1/9.
 * 以一种解耦的方式实现网络库的初始化，便于后期网络库的替换
 */
public class RequestManager<T> {

    private static RequestManager mRequestManager = null;
    //网络请求库
    private final int REQUEST_OKHTTP = 1;
    private final int REQUEST_ASYNC_HTTP = 2;
    private final int REQUEST_OTHER = 3;

    //当前使用的HTTP请求库
    private final int REQUEST_HTTP_LIB = REQUEST_OKHTTP;


    private RequestManager() {

    }
    public static <T> RequestManager getInstance() {
        if (mRequestManager == null) {
            synchronized (RequestManager.class) {
                if (mRequestManager == null) {
                    mRequestManager = new RequestManager<T>();
                }
            }
        }
        return mRequestManager;
    }

    public  IRequest<T> createRequest(){
       switch (REQUEST_HTTP_LIB) {
           case REQUEST_OKHTTP:
               return  RetrofitRequest.getInstance();
           case REQUEST_ASYNC_HTTP:

           case REQUEST_OTHER:
               break;
       }
       return  null;
    }

}
