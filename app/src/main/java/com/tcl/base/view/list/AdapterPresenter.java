package com.tcl.base.view.list;
import com.tcl.base.Config;
import com.tcl.base.base.DataArr;
import com.tcl.base.model.TResult;
import com.tcl.base.net.ICallBack;
import com.tcl.base.net.IRequest;
import com.tcl.base.net.RequestManager;


/**
 * Created by yangfeihu on 2017/1/16.
 */

public class AdapterPresenter {
    private IRequest request;
    private int begin = 0;
    private final IAdapterView view;
    private DataArr dataArr = new DataArr();
    private String url;
    private Class clazz;
    private TRecyclerView.IDataChange dataChange;


    //对返回的数据进行处理
    public void setDataChange(TRecyclerView.IDataChange dataChange) {
        this.dataChange = dataChange;
    }


    public interface IAdapterView {
        void setEmpty();
        void setData(DataArr response, int begin);
        void reSetEmpty();
    }

    public AdapterPresenter(IAdapterView mIAdapterViewImpl) {
        this.view = mIAdapterViewImpl;
        request = RequestManager.getInstance().createRequest();
    }

    public AdapterPresenter setUrl(String url) {
        this.url = url;
        return this;
    }

    public <T> AdapterPresenter setClazz(Class<T> clazz) {
        this.clazz = clazz;
        return this;
    }

    public AdapterPresenter addParams(String key, String value) {
        request.addParams(key, value);
        return this;
    }

    public AdapterPresenter addParams(String jsonString) {
        request.addParams(jsonString);
        return this;
    }


    public void setBegin(int begin) {
        this.begin = begin;
    }

    public void fetch() {
        if (url == null) {
            return;
        }
        begin++;
        view.reSetEmpty();
        request.addParams(Config.PARA_PAGE_INDEX,begin);
        request.addParams(Config.PARA_PAGE_SIZE,Config.PAGE_SIZE);
        request.post(url, clazz, new ICallBack<Object>() {
            @Override
            public void onFailure(String errorString, int errorCode, int requestCode) {
                view.setEmpty();
            }
            @Override
            public void onResponse(TResult<Object> t) {
                if (dataChange != null) {
                    dataArr.results = dataChange.onDataChange(t.getData());
                    view.setData(dataArr, begin);
                }
            }
        });
    }

}
