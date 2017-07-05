package com.tcl.base.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.antfortune.freeline.FreelineCore;
import com.chenenyu.router.Router;
import com.squareup.leakcanary.LeakCanary;
import com.zhy.autolayout.config.AutoLayoutConifg;

/**
 * Created by yangfeihu on 2017/1/4.
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //检测内存泄露
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        //分辨率适配
        AutoLayoutConifg.getInstance().useDeviceSize().init(this);
        //快速打包方案
        FreelineCore.init(this);
        //路由
        Router.initialize(this);
    }

    //dex突破65535的限制
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
