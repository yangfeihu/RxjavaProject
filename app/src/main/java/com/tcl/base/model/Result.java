package com.tcl.base.model;

/**
 * Created by yangfeihu on 2017/1/5.
 *
 *
 */

public class Result{

    private int resultcode = -1;
    private String resultmsg = "";
    private int requestcode = 0;


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

}



