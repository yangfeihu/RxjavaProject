package com.tcl.base.ui.main;


import com.chenenyu.router.annotation.Route;
import com.tcl.base.R;
import com.tcl.base.base.DataBindingActivity;
import com.tcl.base.databinding.ActivityMainBinding;
import com.tcl.base.model.Student;
import com.tcl.base.net.IListCallBack;
import com.tcl.base.net.IRequest;
import com.tcl.base.net.ListResult;
import com.tcl.base.net.RequestManager;

/**
 * Created by yangfeihu on 2017/1/16.
 */
@Route("MainActivity")
public class MainActivity extends DataBindingActivity<ActivityMainBinding> {
    private String url = "http://172.19.0.11:8080/HttpSerer/MainList";
    @Override
    public int getRootView() {
         return R.layout.activity_main;
    }
    @Override
    public void initView() {
        super.initView();

         IRequest request = RequestManager.getInstance().createRequest();
        request.post(url, Student.class, new IListCallBack<Student>() {
            @Override
            public void onFailure(String errorString, int errorCode, int requestCode) {

            }

            @Override
            public void onResponse(ListResult<Student> t) {

            }
        });
    }

}
