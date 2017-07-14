package com.tcl.base;

/**
 * Created by yangfeihu on 16/4/23.
 */
public class Config {

    public static String BASE_URL = "http://172.19.0.11:8080/";

    // 正确的Http请求返回状态码
    public final static int CODE_SUCCESS = 0;
    // 未知的错误 或者系统内部错误
    public final static int CODE_ERROE = 1;

    //token错误
    public final static int CODE_NO_TOKEN = 1001;
    public final static int CODE_TOKEN_ERROR = 1002;

    //JSON属性，可以根据需求修改
    public static final String RESULT_CODE = "returncode";//返回码
    public static final String RESULT_MSG = "returnmsg";//返回的信息
    public static final String RESULT_DATA = "data";//返回的数据

    //列表页面属性，可以根据需求修改
    public static final String PARA_PAGE_INDEX = "page_index";
    public static final String PARA_PAGE_SIZE = "page_size";
    public static final int PAGE_SIZE = 20;
    public static final int FLAG_MULTI_VH = 0x000001;

    public static final String PAGE = "page";
    public static final int PAGE_COUNT = 10;
    //log控制开关
    public static final boolean isDebug = true;
    //渠道配置
    public static final String CHANNEL_NAME = "CHANNEL";
    public static final String DEF_CHANNEL_NAME = "TCL";




}
