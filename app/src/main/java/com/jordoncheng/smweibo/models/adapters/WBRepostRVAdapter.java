/*
package com.jordoncheng.smweibo.Model.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jordoncheng.smweibo.View.WBDetailActivity;
import com.jordoncheng.smweibo.View.WBUserPopupActivity;
import com.jordoncheng.smweibo.View.ImageTagView;
import com.jordoncheng.smweibo.R;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;
import com.sina.weibo.sdk.openapi.models.User;
import com.sina.weibo.sdk.utils.LogUtil;

import java.util.ArrayList;

import static com.sina.weibo.sdk.statistic.WBAgent.TAG;

*
 * Created by JrDnCheng on 3/6/2018.



public class WBRepostRVAdapter extends RecyclerView.Adapter<WBRepostRVAdapter.VH> {

    private Context mContext;
    private ArrayList<Status> mStatusArrayList;
    private Fragment mFragment;
    private long since_id;
    private long max_id;
    private int itemCount;

    public WBRepostRVAdapter(Fragment fragment, ArrayList<Status> Status) {
        mContext = fragment.getContext();
        mFragment = fragment;
        mStatusArrayList = Status;
        setupData();
    }

    public void setData(ArrayList<Status> data) {
        mStatusArrayList = data;
        setupData();
    }

    private void setupData() {
        if(mStatusArrayList != null) {
            setSinseID();
            setMaxID();
            setItemCount();
        }

    }

    private void setMaxID() {max_id = mStatusArrayList.get(mStatusArrayList.size()-1).id;}

    private void setSinseID() {since_id = mStatusArrayList.get(0).id;}

    private void setItemCount() {itemCount = mStatusArrayList.size();}

    public long getMaxId() {return max_id;}

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(mContext).inflate(R.layout.cmt_repost_list_item, parent, false);
        VH holder = new VH(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {

        holder.status = mStatusArrayList.get(position);
        User user = holder.status.user;
        holder.userView.setOnClickListener(new UserPopupOnClickListner(user));
        String userImageURL = user.avatar_large;
        ((WBBaseActivity)mContext).loadImage((ImageTagView)holder.avatarView, userImageURL);
        holder.screenName.setText("@" + holder.status.user.screen_name);
        holder.name.setText(holder.status.user.name);
        holder.contentTv.setText(holder.status.text);

        //往下滚动加载新微博
        if (position == mStatusArrayList.size() - 2) {
            ((WBBaseFragment)mFragment).performRequest(((WBBaseFragment)mFragment).getMid_or_Uid(), getMaxId(), new LoadMoreListener());
        }
    }

    @Override
    public void onViewRecycled(VH holder) {
        super.onViewRecycled(holder);
        holder.avatarView.setImageBitmap(null);
        ((ImageTagView)holder.avatarView).setImageTag(0);
    }

    @Override
    public int getItemCount() {
        return itemCount;
    }

    public static class VH extends RecyclerView.ViewHolder {

        public Status status;
        public ImageView avatarView;
        public TextView screenName;
        public TextView name;
        public TextView contentTv;
        public View userView;

        public VH(View itemView) {
            super(itemView);

            avatarView = itemView.findViewById(R.id.list_item_cmt_repost_avatar);
            screenName = itemView.findViewById(R.id.list_item_cmt_repost_screen_name);
            name = itemView.findViewById(R.id.item_cmt_repost_tv);
            userView = itemView.findViewById(R.id.cmt_repost_user);
            contentTv = itemView.findViewById(R.id.item_cmt_repost_tv);
        }
    }

private void setDetailListner(View view, Status status) {
        if(!(mFragment instanceof WBStatusDetailMainFragment)) {
            view.setOnClickListener(new TVOnClickListener(status));
        }
    }

    
    class UserPopupOnClickListner implements View.OnClickListener {

        private User mUser;

        public UserPopupOnClickListner(User user) {
            mUser = user;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, WBUserPopupActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("user", mUser);
            intent.putExtra("user", bundle);
            mContext.startActivity(intent);
        }
    }

    class TVOnClickListener implements View.OnClickListener {

        Status status;

        public TVOnClickListener(Status status) {
            this.status = status;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, WBDetailActivity.class);
            intent.putExtra("status", status);
            mContext.startActivity(intent);
        }
    }


    class LoadMoreListener implements RequestListener {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                LogUtil.i(TAG, response);
                ArrayList<Status> loadList = StatusList.parse(response).statusList;
                if (loadList != null) {
                    mStatusArrayList.remove(getItemCount()-1);
                    mStatusArrayList.addAll(loadList);
                }
                loadList = null; //recycle()
                response = null; //recycle()
                setupData();
                notifyDataSetChanged();
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {

        }
    }
}
*/
