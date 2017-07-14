package com.tcl.base.net;

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

public class TResult<T> extends Result {
    private T data;
    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return new Gson().toJson(data);
    }

}
