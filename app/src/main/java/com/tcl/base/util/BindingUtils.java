package com.tcl.base.util;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by yangfeihu on 2017/4/5.
 */
public class BindingUtils {
    @BindingAdapter({"imageUrl"})
    public static void loadImg(ImageView v, String url) {
        Glide.with(v.getContext())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(v);
    }



}
