package com.tcl.base.view.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.tcl.base.R;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * Created by yangfeihu on 2017/5/2.
 */

public class ActionBar extends RelativeLayout {
    private Context context;
    private View rootView;


    public ActionBar(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public ActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ActionBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        this.context = context;
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AutoUtils.getPercentHeightSize(134));
        setLayoutParams(lp);
        rootView = inflate(context, R.layout.actionbar, this);
    }


}
