package com.tcl.base.view.list;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.LayoutRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.tcl.base.R;
import com.tcl.base.base.DataArr;
import com.tcl.base.model.BaseBean;

import java.util.List;

/**
 * Created by yangfeihu on 2017/1/14.
 */

public class TRecyclerView<M extends BaseBean> extends FrameLayout implements AdapterPresenter.IAdapterView {
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView recyclerview;
    private LinearLayout ll_emptyView;
    private LinearLayoutManager mLayoutManager;
    private CoreAdapter<M> mCommAdapter;
    private AdapterPresenter mCoreAdapterPresenter;
    private boolean isHasHeadView = false, isHasFootView = false, isEmpty = false, isReverse = false;
    private int headType, footType;

    public interface OnItemClickListener {
        void onItemClick(BaseViewHolder viewHolder, int position, Object data);
        void onItemLOngClick(BaseViewHolder viewHolder, int position, Object data);
    }

    public interface OnFocusChangeListener {
        void onFocusChange(View view, boolean hasFocus, BaseViewHolder viewHolder, int position, Object data);
    }

    public interface OnKeyListener {
        boolean onKey(View view, int i, KeyEvent keyEvent);
    }

    public TRecyclerView(Context context) {
        super(context);
        init(context, null);
    }

    public TRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public AdapterPresenter getPresenter() {
        return mCoreAdapterPresenter;
    }

    public void init(Context context, AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.TRecyclerView);
        headType = ta.getResourceId(R.styleable.TRecyclerView_headType, 0);
        int itemType = ta.getResourceId(R.styleable.TRecyclerView_itemType, 0);
        footType = ta.getResourceId(R.styleable.TRecyclerView_footType, 0);
        isReverse = ta.getBoolean(R.styleable.TRecyclerView_isReverse, false);
        boolean isRefreshable = ta.getBoolean(R.styleable.TRecyclerView_isRefreshable, true);
        ta.recycle();

        View layout = inflate(context, R.layout.layout_list_recyclerview, this);
        swipeRefresh = (SwipeRefreshLayout) layout.findViewById(R.id.swiperefresh);
        recyclerview = (RecyclerView) layout.findViewById(R.id.recyclerview);
        ll_emptyView = (LinearLayout) layout.findViewById(R.id.ll_emptyview);
        mCoreAdapterPresenter = new AdapterPresenter(this);

        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright);
        swipeRefresh.setOnRefreshListener(this::reFetch);
        recyclerview.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        mCommAdapter = new CoreAdapter<M>();
        recyclerview.setAdapter(mCommAdapter);
        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (recyclerview.getAdapter() != null
                        && newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == recyclerview.getAdapter()
                        .getItemCount() && mCommAdapter.isHasMore)
                    mCoreAdapterPresenter.fetch();
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int arg0, int arg1) {
                super.onScrolled(recyclerView, arg0, arg1);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });
        ll_emptyView.setOnClickListener((view -> {
            isEmpty = false;
            ll_emptyView.setVisibility(View.GONE);
            swipeRefresh.setVisibility(View.VISIBLE);
            reFetch();
        }));

        if (itemType != 0) setViewType(itemType);
        swipeRefresh.setEnabled(isRefreshable);
        if (isReverse) {
            mLayoutManager.setStackFromEnd(true);//列表再底部开始展示，反转后由上面开始展示
            mLayoutManager.setReverseLayout(true);//列表翻转
            recyclerview.setLayoutManager(mLayoutManager);
        }
    }

    public TRecyclerView<M> setTypeSelector(TypeSelector mTypeSelector) {
        this.mCommAdapter.setTypeSelector(mTypeSelector);
        return this;
    }

    public TRecyclerView<M> setFootData(Object data) {
        isHasFootView = footType != 0;
        this.mCommAdapter.setFooterViewType(footType, data);
        return this;
    }

    public TRecyclerView<M> setHeadData(Object data) {
        isHasHeadView = headType != 0;
        this.mCommAdapter.setHeadViewType(headType, data);
        return this;
    }

    public void setViewType(@LayoutRes int type) {
        this.mCommAdapter.setViewType(type);
    }

    public TRecyclerView<M> setData(List<M> data) {
        reSetEmpty();
        mCommAdapter.setBeans(data, 1);
        return this;
    }

    public void reFetch() {
        mCoreAdapterPresenter.setBegin(0);
        swipeRefresh.setRefreshing(true);
        mCoreAdapterPresenter.fetch();
    }

    @Override
    public void setEmpty() {
        if ((!isHasHeadView || isReverse && !isHasFootView) && !isEmpty) {
            isEmpty = true;
            ll_emptyView.setVisibility(View.VISIBLE);
            swipeRefresh.setVisibility(View.GONE);
        }
    }

    @Override
    public void setData(DataArr response, int begin) {
        swipeRefresh.setRefreshing(false);
        mCommAdapter.setBeans(response.results, begin);
        if (begin == 1 && (response.results == null || response.results.size() == 0))
            setEmpty();
        else if (isReverse)
            recyclerview.scrollToPosition(mCommAdapter.getItemCount() - response.results.size() - 2);
    }

    @Override
    public void reSetEmpty() {
        if (isEmpty) {
            ll_emptyView.setVisibility(View.GONE);
            swipeRefresh.setVisibility(View.VISIBLE);
        }
    }
}