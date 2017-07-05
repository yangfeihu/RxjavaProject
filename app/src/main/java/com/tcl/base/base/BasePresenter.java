package com.tcl.base.base;
import android.content.Context;
import com.tcl.base.net.IRequest;
import com.tcl.base.net.RequestManager;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by yangfeihu on 16/4/22.
 */
public abstract class BasePresenter<V> {
    public V mView;
    public Context mContext;
    private CompositeDisposable mDisposables;
    public IRequest request = RequestManager.getInstance().createRequest();
    public void setView(V v,Context context) {
        this.mView = v;
        this.mContext = context;
        this.mDisposables = new CompositeDisposable();
        this.onAttached();
    }
    public abstract void onAttached();
    public void onDetached() {
        mDisposables.dispose();
    }
}
