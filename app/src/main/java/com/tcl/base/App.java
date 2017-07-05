package com.tcl.base;

import com.tcl.base.base.BaseApplication;

import java.io.File;

/**
 * Created by yangfeihu on 2017/3/23.
 */
public class App extends BaseApplication {
    private static App mApp;
    private File downLoadFilePath;
    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        downLoadFilePath = this.getApplicationContext().getExternalCacheDir();
    }
    public static App getAppContext() {
        return mApp;
    }
    public File getDownLoadFile(){
        return downLoadFilePath;
    }

}
