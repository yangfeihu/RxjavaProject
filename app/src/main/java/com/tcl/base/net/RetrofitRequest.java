package com.tcl.base.net;

import android.os.Environment;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tcl.base.App;
import com.tcl.base.api.Api;
import com.tcl.base.api.ApiService;
import com.tcl.base.model.TResult;
import com.tcl.base.util.DepthClone;
import com.tcl.base.util.JsonFormatUtil;
import com.tcl.base.util.LogHelper;
import com.tcl.base.util.MainHandler;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import static com.tcl.base.Config.CODE_ERROE;
import static com.tcl.base.Config.CODE_SUCCESS;
import static com.tcl.base.Config.RESULT_CODE;
import static com.tcl.base.Config.RESULT_DATA;
import static com.tcl.base.Config.RESULT_MSG;
import static com.tcl.base.util.DepthClone.stringCopy;
import static com.tcl.base.util.ShadowClone.mapCopy;

/**
 * Created by yangfeihu on 2017/3/24.
 */

public class RetrofitRequest<T> implements IRequest {

    private final String TAG = "RetrofitRequest";
    private HashMap<String,Object> mParaMap = new HashMap<>();
    private String mParaJsonString = null;
    private int requestCode = 0;

    private static  RetrofitRequest INSTANCE = null;
    private ApiService service;
    private Gson mGson;


    public static RetrofitRequest getInstance() {
        if (INSTANCE == null) {
            synchronized (RetrofitRequest.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RetrofitRequest();
                }
            }
        }
        return INSTANCE;
    }

    private RetrofitRequest() {
        service = Api.getInstance().service;
        mGson = new Gson();
    }

    public void clean() {
        requestCode = 0;
    }

    @Override
    public Disposable get(String url, Class clazz, ICallBack callBack) {
        String newString = stringCopy(mParaJsonString);
        if (TextUtils.isEmpty(newString)) {
           return doSubscribe(service.get(url, mapCopy(mParaMap)), clazz, callBack, requestCode);
        } else {
            return  doSubscribe(service.get(url, newString), clazz, callBack, requestCode);
        }
    }

    @Override
    public Disposable post(String url, Class clazz, ICallBack callBack) {
        String newString = stringCopy(mParaJsonString);
        if (TextUtils.isEmpty(newString)) {
            return doSubscribe(service.post(url, mapCopy(mParaMap)), clazz, callBack, requestCode);
        } else {
            return doSubscribe(service.post(url, newString), clazz, callBack, requestCode);
        }
    }

    @Override
    public void download(String url, IFileCallBack callBack) {
        service.download(url)
                .subscribeOn(Schedulers.io())//订阅在子线程
                .unsubscribeOn(Schedulers.io())//取消订阅在子线程
                .observeOn(Schedulers.io())//回调发生在子线程
                .subscribe(
                        responseBody -> {
                            if (TextUtils.isEmpty(url)) {
                                MainHandler.getInstance().post(() -> callBack.onFailure("url is null"));
                                return;
                            }
                            downloadFile(url, responseBody, callBack);
                        },
                        //正常情况
                        e -> {//异常的情况
                            MainHandler.getInstance().post(() -> callBack.onFailure("network abnormal."));
                            e.printStackTrace();
                            LogHelper.i(TAG, "network abnormal");
                        }
                );

    }


    //下载过程中取消下载
    private boolean isCancelDownload = false;
    private long current = 0;

    public void setCancelDownload(boolean cancelDownload) {
        isCancelDownload = cancelDownload;
    }

    private void downloadFile(String url, ResponseBody responseBody, final IFileCallBack callBack) {
        InputStream is = responseBody.byteStream();
        FileOutputStream fos = null;
        BufferedInputStream bis = null;

        final File destFileDir;
        String prefix = url.substring(url.lastIndexOf("."));
        final String fileName = "tcl" + prefix;
        long total = responseBody.contentLength();
        int len = 0;
        current = 0;

        String path = App.getAppContext().getDownLoadFile().getAbsolutePath();
        try {
            if (path == null) {
                StringBuilder sb = new StringBuilder();
                sb.append(Environment.getExternalStorageDirectory().getPath())
                        .append("/Android/data/").append(App.getAppContext().getPackageName())
                        .append("/cache/").append(File.separator).toString();
                destFileDir = new File(sb.toString(), fileName);
            } else {
                destFileDir = new File(path, fileName);
            }
            if (destFileDir.exists()) {
                try {
                    destFileDir.delete();
                    destFileDir.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            fos = new FileOutputStream(destFileDir);
            bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024 * 2];

            while ((len = bis.read(buffer)) != -1) {
                //取消下载
                if (isCancelDownload) {
                    MainHandler.getInstance().post(() -> callBack.onFailure("File downloads to cancel"));
                    if (fos != null) fos.close();
                    if (bis != null) bis.close();
                    if (is != null) is.close();
                    return;
                }
                fos.write(buffer, 0, len);
                current += len;
                MainHandler.getInstance().post(() -> callBack.onProgress(current, total));
            }
            fos.flush();

            MainHandler.getInstance().post(() -> callBack.onSuccess(destFileDir.getAbsolutePath()));
        } catch (IOException e) {
            e.printStackTrace();
            MainHandler.getInstance().post(() -> callBack.onFailure("IOException"));
        } finally {
            try {
                isCancelDownload = false;
                if (fos != null) fos.close();
                if (bis != null) bis.close();
                if (is != null) is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private Disposable doSubscribe(Flowable<ResponseBody> mObservable, Class claz, ICallBack callBack, int requestCode) {
       return mObservable.subscribeOn(Schedulers.io())//订阅在子线程
                .unsubscribeOn(Schedulers.io())//取消订阅在子线程
                .observeOn(AndroidSchedulers.mainThread())//回调发生在UI线程
                .subscribe(
                        //正常情况
                        responseBody -> {
                            String data = responseBody.string();
                            doResponse(data, claz, callBack, requestCode);
                            clean();//请求后还原数据
                        },
                        //异常的情况
                        e -> {
                            e.printStackTrace();
                            LogHelper.i(TAG, "network abnormal");
                            callBack.onFailure("network abnormal.", CODE_ERROE, requestCode);
                            clean();//请求后还原数据
                        }
                );
    }

    //处理返回的数据
    private void doResponse(String data, final Class<T> clazz, final ICallBack<T> iCallBack, final int requestCode) {
        LogHelper.i(TAG, "****Response Data = \n" + JsonFormatUtil.formatJson(data));
        TResult<T> result = new TResult<>();
        if (data == null) {//返回数量为空
            LogHelper.i(TAG, "The data returned is empty");
            iCallBack.onFailure("The data returned is empty.", CODE_ERROE, requestCode);
            return;
        }
        try {
            JSONTokener jsonParser = new JSONTokener(data);
            JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
            if (null == jsonObject) {//转换json错误
                LogHelper.i(TAG, "It is not a Json String");
                iCallBack.onFailure("It is not a Json String.", CODE_ERROE, requestCode);
                return;
            }
            if (!jsonObject.has(RESULT_CODE)) {//数据不完整
                LogHelper.i(TAG, "Ihe data is incomplete");
                iCallBack.onFailure("The data is incomplete.", CODE_ERROE, requestCode);
                return;
            }
            if (jsonObject.getInt(RESULT_CODE) != CODE_SUCCESS) {//服务器返回错误的结果
                iCallBack.onFailure(jsonObject.getString(RESULT_MSG), jsonObject.getInt(RESULT_CODE), requestCode);
                return;
            }
            result.setRequestcode(requestCode);
            result.setResultCode(jsonObject.getInt(RESULT_CODE));
            if (jsonObject.has(RESULT_MSG)) {
                result.setResultMsg(jsonObject.getString(RESULT_MSG));
            }
            if (jsonObject.has(RESULT_DATA)) {
                data = jsonObject.getString(RESULT_DATA);
                if (!clazz.equals(Void.class)) {
                    //需要转换的为基本数据类型
                    if (clazz.equals(String.class) || clazz.getSuperclass().equals(Number.class)) {
                        T t = castValue(data, clazz);
                        result.setData(t);
                    } else {
                        result.setData(mGson.fromJson(data, clazz));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            iCallBack.onFailure("Type mismatch.", CODE_ERROE, requestCode);
            return;
        }
        iCallBack.onResponse(result);
    }

    private T castValue(String data, Class<T> clazz) {
        try {
            Constructor<T> constructor = clazz.getConstructor(String.class);
            return constructor.newInstance(data);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void uploadFile(String url, File file, final IFileCallBack callBack) {
        RequestBody body1 = RequestBody.create(MediaType.parse("application/otcet-stream"), file);
        FileRequestBody body = new FileRequestBody(body1, callBack);
        MultipartBody.Part part = MultipartBody.Part.createFormData("avatarByte", file.getName(), body);
        Map<String, Object> newMap = DepthClone.mapCopy(mParaMap);
        service.uploadFile(url, part, newMap)
                .subscribeOn(Schedulers.io())//订阅在子线程
                .unsubscribeOn(Schedulers.io())//取消订阅在子线程
                .observeOn(AndroidSchedulers.mainThread())//回调发生在UI线程
                .subscribe(
                        //正常情况
                        responseBody -> {
                            String data = responseBody.string();
                            JSONTokener jsonParser = new JSONTokener(data);
                            JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
                            if (null == jsonObject) {//转换json错误
                                LogHelper.i(TAG, "It is not a Json String");
                                callBack.onFailure("It is not a Json String.");
                                return;
                            }
                            if (!jsonObject.has(RESULT_CODE)) {//数据不完整
                                LogHelper.i(TAG, "Ihe data is incomplete");
                                callBack.onFailure("The data is incomplete.");
                                return;
                            }
                            if (jsonObject.getInt(RESULT_CODE) != CODE_SUCCESS) {//服务器返回错误的结果
                                callBack.onFailure(jsonObject.getString(RESULT_MSG));
                                return;
                            }
                            callBack.onSuccess("File upload successfully.");
                            clean();//请求后还原数据
                        },
                        //异常的情况
                        e -> {
                            e.printStackTrace();
                            LogHelper.i(TAG, "network abnormal");
                            callBack.onFailure("network abnormal.");
                            clean();//请求后还原数据
                        }
                );
    }

    @Override
    public IRequest addParams(String key, Object values) {
        mParaMap.put(key, values);
        return this;
    }

    @Override
    public IRequest addParams(String jsonString) {
        mParaJsonString = jsonString;
        return this;
    }

    @Override
    public IRequest addParams(Map map) {
        mParaMap.putAll(map);
        return this;
    }

    @Override
    public IRequest addRequestCode(int requestCode) {
        this.requestCode = requestCode;
        return this;
    }
}
