package com.tcl.base.util;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import com.tcl.base.BuildConfig;


public class LogHelper {
	public static final String TAG="LogHelper";
	public static boolean isPrintHttpRequest = BuildConfig.DEBUG;

		public static void toast(Handler mHandler, final Context context, final String msg){
			if (isPrintHttpRequest){
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(context,msg, Toast.LENGTH_SHORT).show();
					}
				});
			}
		}
		// 下面四个是默认tag的函数
		public static void i(String msg) {
			if (isPrintHttpRequest)
				Log.i(TAG, msg);
		}
		public static void i(String TAG, String msg) {
			if (isPrintHttpRequest)
				Log.i(TAG, msg);
		}
		public static void e(String TAG, String msg) {
			if (isPrintHttpRequest)
				Log.e(TAG, msg);
		}

		public static void d(String TAG, String msg) {
			if (isPrintHttpRequest)
				Log.d(TAG, msg);
		}

		public static void d(String msg) {
			if (isPrintHttpRequest)
				Log.d(TAG, msg);
		}

		public static void e(String msg) {
			if (isPrintHttpRequest)
				Log.e(TAG, msg);
		}

		public static void v(String msg) {
			if (isPrintHttpRequest)
				Log.v(TAG, msg);
		}
		/*// 下面是传入自定义tag的函数
		public static void i(String tag, String msg) {
			if (golive.http.HttpHelper.isPrintHttpRequest)
				Log.i(tag, msg);
		}

		public static void d(String tag, String msg) {
			if (golive.http.HttpHelper.isPrintHttpRequest)
				Log.d(tag, msg);
		}

		public static void e(String tag, String msg) {
			if (golive.http.HttpHelper.isPrintHttpRequest)
				Log.e(tag, msg);
		}

		public static void v(String tag, String msg) {
			if (golive.http.HttpHelper.isPrintHttpRequest)
				Log.v(tag, msg);
		}*/
}
