package com.tcl.base.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tcl.base.model.Msg;
import com.tcl.base.util.NetWorkUtil;
import com.tcl.base.util.RxBus;
import com.tcl.base.view.dialog.LoadingDialog;
import com.zhy.autolayout.AutoLayoutActivity;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yangfeihu on 2017/1/4.
 */

public abstract class DataBindingActivity<B extends ViewDataBinding> extends AutoLayoutActivity {


    public B mViewBinding;  //提供给子类使用
    public Context mContext;
    public Resources mRs;
    protected com.tcl.base.view.widget.ActionBar mActionBar;
    private LoadingDialog mLoadingDialog;
    Disposable disposable;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();
        mContext = this;
        mRs = this.getResources();
        mLoadingDialog = new LoadingDialog(this);
        initRxBus();
        registerNetworkReceiver();
        ActivityCollector.add(this);
        initPresenter();
        initView();
        initData();
    }
    private void setContentView(){
        View rootView = getLayoutInflater().inflate(this.getRootView(), null, false);
        ViewGroup.LayoutParams layoutParams = new  ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        rootView.setLayoutParams(layoutParams);
        mViewBinding = DataBindingUtil.bind(rootView);
        super.setContentView(getContainer(rootView));
    }
    private View getContainer(View rootView) {
        initActionBar();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setLayoutParams(lp);
        container.addView(mActionBar);
        container.addView(rootView);
        return container;
    }

    private void initActionBar() {
        mActionBar = new com.tcl.base.view.widget.ActionBar(this);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AutoUtils.getPercentHeightSize(134));
        mActionBar.setLayoutParams(lp);

/*        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            // 是否启用自定义的布局
            mActionBar.setDisplayShowCustomEnabled(true);
            // 决定左上角的图标是否可以点击。没有向左的小图标。 true 图标可以点击 false 不可以点击
            mActionBar.setDisplayShowHomeEnabled(false);
            // 是否显示标题
            mActionBar.setDisplayShowTitleEnabled(false);
            // 决定左上角图标的右侧是否有向左的小箭头
            mActionBar.setDisplayHomeAsUpEnabled(false);
            // 仅仅显示自定义布局
            mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            mActionBar.setCustomView(new com.tcl.vodshop.view.widget.ActionBar(this));

        }*/
    }




    //网络状态发送变化
    private BroadcastReceiver mConnectionChangeReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            //网络已经连接
            if (NetWorkUtil.isNetConnected(DataBindingActivity.this)) {
                onNetworkConnected();
            }else{//网络已经断开
                onNetworkDisConnected();
            }
        }
    };
    private void initRxBus() {
        disposable = RxBus.getDefault().toObservable(Msg.class)
                .subscribeOn(Schedulers.io())//订阅在子线程
                .unsubscribeOn(Schedulers.io())//取消订阅在子线程
                .observeOn(AndroidSchedulers.mainThread())//回调发生在UI线程
                .subscribe(msg -> handler(msg));
    }

    private void registerNetworkReceiver() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(mConnectionChangeReceiver, filter);
    }

    private void unRegisterNetworkReceiver() {
        this.unregisterReceiver(mConnectionChangeReceiver);
    }


    //处理RxBus信息（事件总线）
    protected void handler(Msg msg) {

    }

    protected void initPresenter() {

    }


    //由子类去实现
    protected void onNetworkConnected(){

    }
    //由子类去实现
    protected void onNetworkDisConnected(){

    }

    /**
     * initialization View ,such as findViewById() or new Object();
     */
    public  void initView(){};

    /**
     * initialization data
     */
    public  void initData(){};

    /**
     * ordering to getting layout to activity
     *
     * @return R.layout.xxx
     */
    public abstract int getRootView();


    public void showLoadingDialog(String title) {
        runOnUiThread(() -> {
            mLoadingDialog.setTitle(title);
            mLoadingDialog.show();
        });
    }
    public void showLoadingDialog(int title) {
        runOnUiThread(() -> {
            mLoadingDialog.setTitle(title);
            mLoadingDialog.show();
        });
    }

    public void dismissLoadingDialog() {
        runOnUiThread(() -> {
            mLoadingDialog.dismiss();
        });
    }



    //以下方法最好使用路由代替
    protected void openActivity(Class<?> cls) {
        openActivity(cls, null);
    }

    protected void openActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    protected void openActivity(Class<?> cls, String name, Parcelable parcelable) {
        Intent intent = new Intent(this, cls);
        if (parcelable != null) {
            intent.putExtra(name, parcelable);
        }
        startActivity(intent);
    }

    protected void openActivity(Class<?> cls, String name, ArrayList<String> value) {

        Intent intent = new Intent(this, cls);
        intent.putStringArrayListExtra(name, value);
        startActivity(intent);
    }


    protected void openActivity(Class<?> cls, String name, String value) {
        Intent intent = new Intent(this, cls);
        intent.putExtra(name, value);
        startActivity(intent);
    }

    protected void openActivity(Class<?> cls, String name, int value) {
        Intent intent = new Intent(this, cls);
        intent.putExtra(name, value);
        startActivity(intent);
    }

    protected void openActivity(Class<?> cls, String name, String[] values) {
        Intent intent = new Intent(this, cls);
        intent.putExtra(name, values);
        startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.pop(this);
        dismissLoadingDialog();
        if(disposable !=null )disposable.dispose();//取消订阅
        unRegisterNetworkReceiver();
    }
}
