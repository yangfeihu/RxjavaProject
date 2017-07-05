package com.tcl.base.base;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yangfeihu on 2017/1/4.
 */
public class ActivityCollector {


    public static List<DataBindingActivity> activities = new ArrayList<>();

    public static void add(DataBindingActivity activity) {
        activities.add(activity);
    }

    public static void pop(DataBindingActivity activity) {
        activities.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        System.exit(0);
    }


}
