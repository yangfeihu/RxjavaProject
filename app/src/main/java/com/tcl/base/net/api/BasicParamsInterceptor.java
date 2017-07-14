package com.tcl.base.net.api;

/**
 * Created by yangfeihu on 2017/3/24.
 */

import android.text.TextUtils;
import android.util.Log;

import com.tcl.base.util.JsonFormatUtil;
import com.tcl.base.util.LogHelper;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;


/**
 * Created by yangfeihu on 2017/3/22.
 * 主要是添加公共的参数
 */
public class BasicParamsInterceptor implements Interceptor {


    private final String TAG = "BasicParamsInterceptor";

    Map<String, String> queryParamsMap = new HashMap<>();
    Map<String, String> paramsMap = new HashMap<>();
    Map<String, String> headerMap = new HashMap<>();
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");


    private BasicParamsInterceptor() {

    }


    private  Request request;
    private  Request.Builder requestBuilder;
    @Override
    public Response intercept(Chain chain) throws IOException {

         request = chain.request();
         requestBuilder = request.newBuilder();
         addHeader();
         addParaTobody();
        return chain.proceed(requestBuilder.build());
    }

    private Request.Builder addHeader(){
        if(headerMap.size() > 0){
            Iterator iterator = headerMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
                requestBuilder.addHeader(key,value);
            }
        }
        return requestBuilder;
    }


    private Request.Builder addParaTobody(){
        reAddPublicPara();
        //向dody中添加公共的参数
        if (paramsMap.size() > 0) {
            if (canInjectIntoBody(request)) {

                RequestBody body = request.body();
                String postBodyString = bodyToString(body);
                try {
                    JSONTokener jsonParser = new JSONTokener(postBodyString);
                    JSONObject rootJsonObj = (JSONObject) jsonParser.nextValue();
                    if (null == rootJsonObj) {//转换json错误
                        LogHelper.i(TAG, "It is not a Json String");
                        return requestBuilder;
                    }
                    Iterator iterator = paramsMap.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry entry = (Map.Entry) iterator.next();
                        String key = (String) entry.getKey();
                        Object value =  entry.getValue();
                        if(value != null) {
                            if(value instanceof String){
                                rootJsonObj.put(key, value);
                            }else if(value instanceof JSONObject){
                                rootJsonObj.put(key,value);
                            }else if(value instanceof Object){
                                JSONObject obj = new JSONObject(value.toString());
                                rootJsonObj.put(key,obj);
                            }
                        }
                    }
                    Log.i("yangfeihu","request url = "+request.url().toString());
                    Log.i(TAG, "****Request Data =\n" + JsonFormatUtil.formatJson(rootJsonObj.toString()));
                    requestBuilder.post(RequestBody.create(MEDIA_TYPE, rootJsonObj.toString()));
                } catch (JSONException e) {
                    LogHelper.i(TAG, "It is not a Json String");
                    e.printStackTrace();
                }
                return requestBuilder;
            }
        }
        return requestBuilder;
    }

    //根据实际情况添加公共参数
    private void reAddPublicPara() {
        paramsMap.put("accountid", "");
        paramsMap.put("token","");
    }


    private boolean canInjectIntoBody(Request request) {
        if (request == null) {
            return false;
        }
        if (!TextUtils.equals(request.method(), "POST")) {
            return false;
        }
        RequestBody body = request.body();
        if (body == null) {
            return false;
        }
        MediaType mediaType = body.contentType();
        if (mediaType == null) {
            return false;
        }
        if (!MEDIA_TYPE.toString().equalsIgnoreCase(mediaType.toString())) {
            return false;
        }
        return true;
    }

    // func to inject params into url
    private Request injectParamsIntoUrl(HttpUrl.Builder httpUrlBuilder, Request.Builder requestBuilder, Map<String, String> paramsMap) {
        if (paramsMap.size() > 0) {
            Iterator iterator = paramsMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                httpUrlBuilder.addQueryParameter((String) entry.getKey(), (String) entry.getValue());
            }
            requestBuilder.url(httpUrlBuilder.build());
            return requestBuilder.build();
        }

        return null;
    }

    private static String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    public static class Builder {

        BasicParamsInterceptor interceptor;

        public Builder() {
            interceptor = new BasicParamsInterceptor();
        }




        public Builder addHeader(String key, String value) {
            interceptor.headerMap.put(key, value);
            return this;
        }


        public Builder addHeaderMap(Map<String, String> headMap) {
            interceptor.headerMap.putAll(headMap);
            return this;
        }


        public Builder addParam(String key, String value) {
            interceptor.paramsMap.put(key, value);
            return this;
        }

        public Builder addParamsMap(Map<String, String> paramsMap) {
            interceptor.paramsMap.putAll(paramsMap);
            return this;
        }


        public Builder addQueryParam(String key, String value) {
            interceptor.queryParamsMap.put(key, value);
            return this;
        }

        public Builder addQueryParamsMap(Map<String, String> queryParamsMap) {
            interceptor.queryParamsMap.putAll(queryParamsMap);
            return this;
        }

        public BasicParamsInterceptor build() {
            return interceptor;
        }

    }
}