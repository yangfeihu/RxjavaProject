package com.tcl.base.ui.main;


import com.chenenyu.router.annotation.Route;
import com.tcl.base.R;
import com.tcl.base.base.DataBindingActivity;
import com.tcl.base.databinding.ActivityMainBinding;
import com.tcl.base.model.Student;
import com.tcl.base.view.list.TRecyclerView;

import java.util.List;

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

        mViewBinding.list.setUrl(url).setClazz(Student.class).map(new TRecyclerView.IDataChange() {
            @Override
            public  List onDataChange(Object data) {
                if (data != null) {
                    Student student = (Student) data;
                    return student.infos;
                } else {
                    return null;
                }
            }
        }) .fetch();

    }

}
