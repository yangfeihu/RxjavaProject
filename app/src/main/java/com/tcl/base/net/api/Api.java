package com.tcl.base.net.api;

/**
 * Created by yangfeihu on 2017/3/22.
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tcl.base.App;
import com.tcl.base.Config;
import com.tcl.base.util.LogHelper;
import com.tcl.base.util.NetWorkUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Api {
    public Retrofit retrofit;
    public ApiService service;
    private static  Api INSTANCE = null;
    private App application = App.getAppContext();


    //此处可以添加公共的Header 和 参数
    BasicParamsInterceptor basicParamsInterceptor =
            new BasicParamsInterceptor.Builder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("device", "1111")
                    .build();

    public static Api getInstance() {
            if (INSTANCE == null) {
                synchronized (Api.class) {
                    if (INSTANCE == null) {
                        INSTANCE = new Api();
                    }
                }
            }
            return INSTANCE;
    }



    //构造方法私有
    private Api() {
        //日志
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //缓存地址
         File cacheFile = new File(application.getAppContext().getCacheDir(), "cache");
        //缓存的大小
         Cache cache = new Cache(cacheFile, 1024 * 1024 * 100); //100Mb*/

        //实例化okhttp
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //超时时间
                .writeTimeout(8000, TimeUnit.MILLISECONDS)
                .readTimeout(8000, TimeUnit.MILLISECONDS)
                .connectTimeout(8000, TimeUnit.MILLISECONDS)
                //拦截器
                .addInterceptor(basicParamsInterceptor)//公共的参数拦截
                .addInterceptor(loggingInterceptor)//日志拦截器
                .addNetworkInterceptor(new HttpCacheInterceptor())//缓存拦截器
                .cache(cache)//缓存地址
                .build();



        //retrofit实例化
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").serializeNulls().create();
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                //支持gson返回
                .addConverterFactory(GsonConverterFactory.create(gson))
                //支持String返回
                .addConverterFactory(ScalarsConverterFactory.create())
                //支持RxJava2
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                //baseUrl
                .baseUrl(Config.BASE_URL)
                .build();
        service = retrofit.create(ApiService.class);
    }

   // 有时候需要在无网络时增加缓存功能，因此给Retrofit加入基础拦截器,来处理缓存问题
    class HttpCacheInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {


            //方案一：有网和没有网都是先读缓存
//                         request = chain.request();
//                Log.i(TAG, "request=" + request);
//                Response response = chain.proceed(request);
//                Log.i(TAG, "response=" + response);
//
//                String cacheControl = request.cacheControl().toString();
//                if (TextUtils.isEmpty(cacheControl)) {
//                    cacheControl = "public, max-age=60";
//                }
//                return response.newBuilder()
//                        .header("Cache-Control", cacheControl)
//                        .removeHeader("Pragma")
//                        .build();



  //方案二：无网读缓存，有网根据过期时间重新请求


            Request request = chain.request();
            if (!NetWorkUtil.isNetConnected(App.getAppContext())) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
                LogHelper.d("Okhttp", "no network");
            }

            Response originalResponse = chain.proceed(request);
            if (NetWorkUtil.isNetConnected(App.getAppContext())) {
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                return originalResponse.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma") //// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                        .build();
            } else {
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=2419200")
                        .removeHeader("Pragma")
                        .build();
            }
        }
    }
}