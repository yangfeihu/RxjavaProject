package com.tcl.base.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Hqy on 2017/5/23.
 * 时间工具类
 */

public class DateFormatUtil {

    // 将字符串转为时间戳
    public static String getTime(String user_time) {
        if(user_time == null)return null;
        String re_time = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d;
        try {
            d = sdf.parse(user_time);
            long l = d.getTime();
            String str = String.valueOf(l);
            re_time = str.substring(0, 10);
        }catch (ParseException e) {
            // TODO Auto-generated catch block e.printStackTrace();
        }
        return re_time;
    }


    // 将时间戳转为字符串
    public static String getStrTime(String cc_time) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));
        return re_StrTime;
    }

    /*
    *计算time2减去time1的差值 差值只设置 几天 几个小时 或 几分钟
    * 根据差值返回多长之间前或多长时间后
    * */
    public static String getDistanceTime(String time1,String time2 ) {
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        long diff ;
        String flag;
        long lcc_time1 = Long.valueOf(time1);
        long lcc_time2 = Long.valueOf(time2);
        if(lcc_time1<lcc_time2) {
            diff = lcc_time2 - lcc_time1;
            flag="前";
        } else {
            diff = lcc_time1 - lcc_time2;
            flag="后";
        }
        day = diff / (24 * 60 * 60);
        hour = (diff / (60 * 60) - day * 24);
        min = ((diff / 60 ) - day * 24 * 60 - hour * 60);
        sec = (diff-day*24*60*60-hour*60*60-min*60);
        if(day!=0)return day+"天"+flag;
        if(hour!=0)return hour+"小时"+flag;
        if(min!=0)return min+"分钟"+flag;
        return "刚刚";
    }
}
