package com.tcl.base.view.list;
import android.util.Log;

import com.tcl.base.Config;
import com.tcl.base.base.DataArr;
import com.tcl.base.base.Repository;
import java.util.HashMap;


/**
 * Created by yangfeihu on 2017/1/16.
 */

public class AdapterPresenter {
    private Repository mRepository;//仓库
    private HashMap<String, Object> param = new HashMap<>();//设置仓库钥匙
    private int begin = 0;
    private final IAdapterView view;

    public interface IAdapterView {
        void setEmpty();

        void setData(DataArr response, int begin);

        void reSetEmpty();
    }

    public AdapterPresenter(IAdapterView mIAdapterViewImpl) {
        this.view = mIAdapterViewImpl;
    }

    public AdapterPresenter setRepository(Repository repository) {
        this.mRepository = repository;
        return this;
    }

    public AdapterPresenter setParam(String key, String value) {
        this.param.put(key, value);
        return this;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public void fetch() {
        begin++;
        view.reSetEmpty();
        if (mRepository == null) {
            Log.e("mRepository", "null");
            return;
        }
        param.put(Config.PAGE, begin);
        mRepository
                .getData(param)
                .subscribe(
                        res -> view.setData(res, begin),
                        e -> view.setEmpty());
    }
}
