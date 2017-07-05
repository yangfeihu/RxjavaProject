package com.tcl.base.base;


import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by yangfeihu on 2017/1/4.
 */

public abstract class BaseFragment<B extends ViewDataBinding> extends Fragment {

    public DataBindingActivity mBaseActivityMyself;
    public B mViewBinding;  //提供给子类使用
    public View mRootView;
    public LayoutInflater mLayoutInflater;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mLayoutInflater = inflater;
        mBaseActivityMyself= (DataBindingActivity) this.getActivity();
        mRootView= mLayoutInflater.inflate(getRootView(),container,false);
        mViewBinding = DataBindingUtil.bind(mRootView);
        initView();
        initData();
        return mRootView;
    }

    @SuppressWarnings("unchecked")
    public <K extends View> K findViewById(int resId) {
        return (K) mRootView.findViewById(resId);
    }

    /**
     * initialization View ,such as findViewById() or new Object();
     */
    public  void initView(){}

    /**
     * initialization data
     */
    public  void initData(){}

    /**
     * ordering to getting layout to activity
     *
     * @return R.layout.xxx
     */
    public abstract int getRootView();

}
