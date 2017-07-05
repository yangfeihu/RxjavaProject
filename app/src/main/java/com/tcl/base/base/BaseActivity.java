package com.tcl.base.base;

import android.databinding.ViewDataBinding;

import java.lang.reflect.ParameterizedType;

/**
 * Created by yangfeihu on 2017/1/12.
 */

public abstract class BaseActivity<P extends BasePresenter, B extends ViewDataBinding> extends DataBindingActivity<B> {

    //提供给子类使用
    public P mPresenter;

    @Override
    protected void initPresenter() {
        super.initPresenter();
            if (this instanceof BaseView &&
                    this.getClass().getGenericSuperclass() instanceof ParameterizedType &&
                    ((ParameterizedType) (this.getClass().getGenericSuperclass())).getActualTypeArguments().length > 0) {
                Class mPresenterClass = (Class) ((ParameterizedType) (this.getClass()
                        .getGenericSuperclass())).getActualTypeArguments()[0];
                try {
                    mPresenter = (P) mPresenterClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (mPresenter != null) {
                    mPresenter.setView(this,this);
                }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) mPresenter.onDetached();
    }
}
