package com.tcl.base.model;
import android.os.Bundle;
/**
 * Created by yangfeihu on 2017/3/27.
 */
public class Msg {
    public int type = 0;
    public Bundle bundle = new Bundle();

    public Msg(){
    }
    public Msg(int type){
        this.type = type;
    }
    public Msg(int type,Bundle bundle){
        this.type = type;
        this.bundle = bundle;
    }
}
