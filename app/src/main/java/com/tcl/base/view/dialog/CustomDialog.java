package com.tcl.base.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.tcl.base.R;


/**
 * Created by yangfeihu on 2017/1/9.
 */

public class CustomDialog extends Dialog {

    private int title = -1;
    private int content = -1;
    private Context mContext;
    private OnClickListener listener;
    private boolean onlyShowPositiveButton = false;
    private Button sure;
    private Button cancle;
    private TextView tvTitle;
    private TextView tvContent;
    private Window window;


    public interface OnClickListener {
        //确定按钮回调
        public void onPositiveButtonClick();
        //取消按钮回调
        public void onNegativeButtonClick();
    }
    public CustomDialog setListener(OnClickListener listener) {
        this.listener = listener;
        return this;
    }

    //设置确定按钮的名称
    public CustomDialog setSureBtnTxt(String txt){
        if(null != sure && null != txt){
            sure.setText(txt);
        }
        return this;
    }
    public CustomDialog setSureBtnTxt(int txt){
        if(null != sure ){
            sure.setText(txt);
        }
        return this;
    }

    //设置标题
    public CustomDialog setDialogTitle(String txt){
        if(null != tvTitle && null != txt){
            tvTitle.setText(txt);
        }
        return this;
    }

    public CustomDialog setDialogTitle(int txt){
        if(null != tvTitle ){
            tvTitle.setText(txt);
        }
        return this;
    }

    //设置内容
    public CustomDialog setContent(int txt){
        if(null != tvContent ){
            tvContent.setText(txt);
        }
        return this;
    }
    public CustomDialog setContent(String txt){
        if(null != tvContent && txt != null ){
            tvContent.setText(txt);
        }
        return this;
    }

    public CustomDialog setOnlyShowPositiveButton(boolean isShow){

             this.onlyShowPositiveButton = isShow;
            if (onlyShowPositiveButton) {
                cancle.setVisibility(View.GONE);
                window.findViewById(R.id.space).setVisibility(View.GONE);
            }

        return this;
    }

    public CustomDialog(Context context) {
        super(context, R.style.ProgressDialogStyle);
        mContext = context;
        this.setContentView(R.layout.item_dialog);
        window = this.getWindow();
        initView();
    }
    public CustomDialog(Context context, final int title, final int content, final OnClickListener listener) {
        super(context, R.style.ProgressDialogStyle);
        mContext = context;
        this.setContentView(R.layout.item_dialog);
        window = this.getWindow();
        this.title = title;
        this.content = content;
        this.listener = listener;
        initView();
    }


    public void initView() {

        //标题
        tvTitle = (TextView) window.findViewById(R.id.title);
        if(title != -1) {
            tvTitle.setText(title);
        }

        //内容
        tvContent = (TextView) window.findViewById(R.id.content);
        if(content != -1) {
            tvContent.setText(content);
        }

        //按钮
        cancle = (Button) window.findViewById(R.id.cancle);
        sure = (Button) window.findViewById(R.id.sure);

        if (onlyShowPositiveButton) {
            cancle.setVisibility(View.GONE);
            window.findViewById(R.id.space).setVisibility(View.GONE);
        }

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null) {
                    listener.onNegativeButtonClick();
                }
                dismiss();
            }
        });

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null) {
                    listener.onPositiveButtonClick();
                }
                dismiss();
            }
        });
    }

}
