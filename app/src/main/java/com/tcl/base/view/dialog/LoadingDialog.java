package com.tcl.base.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.tcl.base.R;


/**
 * Created by Administrator on 2016/11/12.
 */
public class LoadingDialog extends Dialog {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private View mView;

    private ProgressWheel mProgressWheel;
    private TextView mContent;
    public LoadingDialog(Context context) {
        super(context, R.style.DialogStyle_LoadingDlg);
        mContext=context;
        this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        mLayoutInflater= LayoutInflater.from(context);
        mView=  mLayoutInflater.inflate(R.layout.item_loading_dialog,null);
        mProgressWheel = (ProgressWheel) mView.findViewById(R.id.progress_wheel);
        mContent = (TextView) mView.findViewById(R.id.content);
        this.setContentView(mView);
        setCanceledOnTouchOutside(false);
        setCancelable(true);
    }

    public void setTouchOutsideClick(boolean isClick){
            setCancelable(isClick);
    }
    public void setTitle(String title){
        mContent.setText(title);
    }
    public void setTitle(int  id){
        mContent.setText(id);
    }
    @Override
    public void show() {
        if(this.isShowing()){
            this.dismiss();
        }
        mProgressWheel.spin();
        super.show();
    }

    @Override
    public void dismiss() {

        if(mProgressWheel.isSpinning()){
            mProgressWheel.stopSpinning();
        }
        super.dismiss();
    }
}
