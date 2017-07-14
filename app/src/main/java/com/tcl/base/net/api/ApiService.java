package com.tcl.base.net.api;

import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by yangfeihu on 2017/3/23.
 */
public interface ApiService {

//通用的接口
    @GET()
    Flowable<ResponseBody> get(
            @Url String url,
            @QueryMap Map<String,Object> maps);


    @POST()
    Flowable<ResponseBody> post(
            @Url String url,
            @Body Map<String,Object> maps);

    @POST()
    Flowable<ResponseBody> post(
            @Url String url,
            @Body String jsonString);

    @GET()
    Flowable<ResponseBody> download(@Url String url);


    @Multipart
    @POST()
    Flowable<ResponseBody> uploadFile(
            @Url String url,
            @Part MultipartBody.Part file,
            @PartMap Map<String,Object> map);

    //私有的接口
    /* @GET("login")
    Observable<Boolean> login(@Query("username") String username, @Query("password") String password);*/

}
