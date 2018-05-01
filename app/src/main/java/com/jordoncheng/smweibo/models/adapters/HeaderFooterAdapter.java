package com.jordoncheng.smweibo.models.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public class HeaderFooterAdapter<SVH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int HEAD_VIEW_TYPE = 1000000000;
    private int FOOT_VIEW_TYPE = 1000000001;
    private final int HEAD_VIEW_ORI_TYPE = HEAD_VIEW_TYPE;
    private final int FOOT_VIEW_ORI_TYPE = FOOT_VIEW_TYPE;

    private SparseArray<View> headerViews = new SparseArray<>();
    private SparseArray<View> footerViews = new SparseArray<>();

    private RecyclerView.Adapter adapter;

    public HeaderFooterAdapter(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }

    public HeaderFooterAdapter(RecyclerView.Adapter adapter, View headerView, View footerView) {
        this.adapter = adapter;
        if(headerView != null) addHeaderView(headerView);
        if(footerView != null) addFooterView(footerView);
    }

    protected void addHeaderView(View view) {
        headerViews.put(--HEAD_VIEW_TYPE, view);
    }

    protected void addFooterView(View view) {
        footerViews.put(++FOOT_VIEW_TYPE, view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        if(!(holder instanceof VH)) adapter.onViewAttachedToWindow((SVH)holder);
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        if(!(holder instanceof VH)) adapter.onViewDetachedFromWindow((SVH)holder);
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        if(!(holder instanceof VH)) adapter.onViewRecycled((SVH)holder);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType > FOOT_VIEW_ORI_TYPE) return new VH(footerViews.get(viewType));
        else if(viewType < HEAD_VIEW_ORI_TYPE - headerViews.size()) {
            return adapter.onCreateViewHolder(parent, viewType);
        } else return new VH(headerViews.get(viewType));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(!(holder instanceof VH))
            adapter.onBindViewHolder((SVH)holder, position - headerViews.size());
    }

    @Override
    public int getItemCount() {
        return adapter.getItemCount() + headerViews.size() + footerViews.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(position < headerViews.size()) return position + HEAD_VIEW_TYPE;
        if(position > headerViews.size() + adapter.getItemCount() - 1) return FOOT_VIEW_TYPE - (position - headerViews.size() - adapter.getItemCount());
        return adapter.getItemViewType(position - headerViews.size());
    }

    static class VH extends RecyclerView.ViewHolder {

        VH(View itemView) {
            super(itemView);
        }
    }
}
