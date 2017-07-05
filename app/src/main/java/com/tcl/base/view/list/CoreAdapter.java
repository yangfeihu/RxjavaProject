package com.tcl.base.view.list;

import android.databinding.DataBindingUtil;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tcl.base.Config;
import com.tcl.base.R;
import com.tcl.base.model.BaseBean;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import static com.tcl.base.Config.FLAG_MULTI_VH;


/**
 * Created by yangfeihu on 2017/1/13.
 */
public class CoreAdapter<M extends BaseBean> extends RecyclerView.Adapter<BaseViewHolder> {
    private TypeSelector<M> mTypeSelector;
    private List<M> mItemList = new ArrayList<>();
    //是否有更多的数据
    public boolean isHasMore = true;
    //是否有尾部的元素和头部的元素
    private int isHasFooter = 1, isHasHeader = 0;
    //头部对应的layout
    private int mHeadViewType;
    //listView对应的layout
    private int viewType;
    //尾部对应的lyout
    private int mFooterViewType = R.layout.list_footer_view;
    //头部数据和尾部对应的数据,
    private Object mHeadData;
    //尾部的数据
    private Object mFootData;
    private TRecyclerView.OnItemClickListener mOnItemClickListener;
    private TRecyclerView.OnFocusChangeListener mOnFocusChangeListener;


    public void setOnItemClickListener(TRecyclerView.OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnFocusChangeListener(TRecyclerView.OnFocusChangeListener onFocusChangeListener) {
        this.mOnFocusChangeListener = onFocusChangeListener;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder baseViewHolder = new BaseViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), viewType, parent, false));
        //分辨率适配
        AutoUtils.autoSize(baseViewHolder.mViewDataBinding.getRoot());
        return baseViewHolder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {

        Object data = getItem(position);
        holder.mViewDataBinding.setVariable(com.tcl.base.BR.item, data);
        holder.itemView.setFocusable(true);
        holder.itemView.setClickable(true);
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(view -> mOnItemClickListener.onItemClick(holder, position, data));
        }
        if (mOnFocusChangeListener != null) {
            holder.itemView.setOnFocusChangeListener((v, b) -> mOnFocusChangeListener.onFocusChange(b, holder, position, data));
        }
        holder.mViewDataBinding.executePendingBindings();
    }


    public void setViewType(@LayoutRes int type) {
        this.viewType = type;
    }

    public void setTypeSelector(TypeSelector mTypeSelector) {
        this.mTypeSelector = mTypeSelector;
        this.viewType = FLAG_MULTI_VH;
    }

    public void setHeadViewType(@LayoutRes int i, Object data) {
        this.isHasHeader = (i == 0 ? 0 : 1);
        if (isHasHeader == 1) {
            this.isHasHeader = 1;
            this.mHeadViewType = i;
            this.mHeadData = data;
        }
    }

    public void setFooterViewType(@LayoutRes int i, Object data) {
        this.isHasFooter = i == 0 ? 0 : 1;
        if (isHasFooter == 1) {
            this.mFootData = data;
            this.isHasFooter = 1;
            this.mFooterViewType = i;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public Object getItem(int position) {

        return isHasFooter == 1 && position + 1 == getItemCount()
                ? (mFootData == null ? isHasMore : mFootData)
                : isHasHeader == 1 && position == 0 ? mHeadData : mItemList.get(position - isHasHeader);
    }

    @Override
    public int getItemViewType(int position) {
        return isHasHeader == 1 && position == 0 ? mHeadViewType : (isHasFooter == 1 && position + 1 == getItemCount() ? mFooterViewType :
                viewType == FLAG_MULTI_VH ? mTypeSelector.getType((M) getItem(position)) : viewType);
    }

    @Override
    public int getItemCount() {
        return mItemList.size() + isHasFooter + isHasHeader;
    }


    public void cleanData() {
        this.mItemList.clear();
    }

    /**
     * @param data 数据
     * @param begin 页面索引
     */
    int position = 0;

    public void setBeans(List<M> data, int begin) {
        if (data == null) data = new ArrayList<>();
        this.isHasMore = data.size() >= Config.PAGE_SIZE;
        position = this.mItemList.size();
        if (position <= 0) {
            position = 0;
        }
        if (begin > 1) {
            this.mItemList.addAll(data);
        } else {//如果是首页的数据
            this.mItemList = data;
        }
        notifyDataSetChanged();
    }
}
