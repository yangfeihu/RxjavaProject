package com.tcl.base.model;

import com.google.gson.Gson;

/**
 * Created by yangfeihu on 2017/1/17.
 *
 *
 * 数据请求结果统一预处理实体类（要求服务器返回数据统一格式）
 * 如数据格式为：
 *
 *    {
 *    "resultcode": 0,
 *    "resultmsg": "成功",
 *    "data": {}
 *    }
 *
 * data字段可以是数组字符串等，根据需求而定。
 * 可以具体需求进行更改该类字段格式
 *
 */

public class TResult<T>{
    private T data;
    private int resultcode = -1;
    private String resultmsg = "";
    private int requestcode = 0;


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getRequestcode() {
        return requestcode;
    }

    public void setRequestcode(int requestcode) {
        this.requestcode = requestcode;
    }

    public int getResultCode() {
        return resultcode;
    }

    public void setResultCode(int resultCode) {
        this.resultcode = resultCode;
    }

    public String getResultMsg() {
        return resultmsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultmsg = resultMsg;
    }



    @Override
    public String toString() {
        return new Gson().toJson(data);
    }

}
