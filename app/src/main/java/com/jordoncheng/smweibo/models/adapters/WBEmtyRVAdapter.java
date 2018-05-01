package com.jordoncheng.smweibo.models.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 3/9/2018.
 */

public class WBEmtyRVAdapter extends RecyclerView.Adapter<WBEmtyRVAdapter.VH> {

    @Override
    public WBEmtyRVAdapter.VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(WBEmtyRVAdapter.VH holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class VH extends RecyclerView.ViewHolder {
        public VH(View itemView) {
            super(itemView);
        }
    }
}
