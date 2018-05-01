package com.jordoncheng.smweibo.models.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jordoncheng.smweibo.controlers.LoadImageCtrl;
import com.jordoncheng.smweibo.controlers.LoadCommentsCtrl;
import com.jordoncheng.smweibo.views.WBUserPopupActivity;
import com.jordoncheng.smweibo.views.ImageTagView;
import com.jordoncheng.smweibo.R;
import com.sina.weibo.sdk.openapi.models.Comment;
import com.sina.weibo.sdk.openapi.models.User;

import java.util.ArrayList;

/**
 * Created by Administrator on 3/26/2018.
 */

public class WBCommentsAdapter extends RecyclerView.Adapter<WBCommentsAdapter.VH> {

    public static final int HAS_NO_IMAGE_NO_RETWEET = 100001;
    public static final int HAS_IMAGE_NO_RETWEET = 100002;
    public static final int HAS_RETWEET_NO_IMAGE = 100003;
    public static final int HAS_RETWEET_AND_IMAGE = 100004;
    
    private Context mContext;
    private LoadCommentsCtrl mLoadCommentsCtrl;
    private LoadImageCtrl mLoadImageCtrl;
    private LoadMoreListener mLoadMoreCallback;
    private LoadImageListener mLoadImageListener;

    private ArrayList<Comment> mComments;
    private int mCount;
    private long mLoadMoreID;
    private int mCommentsType;
    private long mid;
    private SparseArray<ImageTagView> mTagImageViews;
    private int mImageTag;

    public WBCommentsAdapter(Context context, int commentsType, long mid) {
        this.mContext = context;
        this.mLoadCommentsCtrl = (LoadCommentsCtrl)context;
        this.mLoadImageCtrl = (LoadImageCtrl)context;
        this.mCommentsType = commentsType;
        this.mid = mid;
        mTagImageViews = new SparseArray<>();
        mLoadImageListener = new LoadImageListener();
        initialize();
    }

    public WBCommentsAdapter(Context context, int commentsType, long mid, ArrayList<Comment> comments) {
        this.mContext = context;
        this.mLoadCommentsCtrl = (LoadCommentsCtrl)context;
        this.mLoadImageCtrl = (LoadImageCtrl)context;
        this.mCommentsType = commentsType;
        this.mid = mid;
        this.mComments = comments;
        mTagImageViews = new SparseArray<>();
        mLoadImageListener = new LoadImageListener();
    }

    private void setupData() {
        mCount = mComments.size();
        mLoadMoreID = mComments.get(mCount - 1).id;
    }

    private void initialize() {
        mComments = new ArrayList<>();
        loadMore();
    }

    private void loadMore() {
        if(mLoadMoreCallback == null) mLoadCommentsCtrl.loadMoreComments(mCommentsType, mid, mLoadMoreID, mLoadMoreCallback = new LoadMoreListener());
        else mLoadCommentsCtrl.loadMoreComments(mCommentsType, mid, mLoadMoreID, mLoadMoreCallback);
    }

    private void loadImage(ImageTagView view, String url) {
        int tag = ++mImageTag;
        view.setImageTag(tag);
        mTagImageViews.put(tag, view);
        mLoadImageCtrl.loadImage(url, tag, mLoadImageListener);
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mCount == 0) return null;
        else {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.cmt_repost_list_item, parent, false);
            VH holder = new VH(itemView);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {

        holder.comment = mComments.get(position);
        User user = holder.comment.user;
        holder.userView.setOnClickListener(new UserPopupOnClickListner(user));
        String userImageURL = user.avatar_large;
        loadImage((ImageTagView)holder.avatarView, userImageURL);
        holder.screenName.setText("@" + holder.comment.user.screen_name);
        holder.name.setText(holder.comment.user.name);
        holder.contentTv.setText(holder.comment.text);
        
        if(position == mCount - 2) {
            loadMore();
        }
    }


    @Override
    public int getItemCount() {
        return mCount;
    }

    @Override
    public void onViewRecycled(VH holder) {
        super.onViewRecycled(holder);
        holder.avatarView.setImageBitmap(null);
        ((ImageTagView)holder.avatarView).setImageTag(0);
    }

    public static class VH extends RecyclerView.ViewHolder {

        public Comment comment;
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
            contentTv = itemView.findViewById(R.id.item_cmt_repost_tv);
            userView = itemView.findViewById(R.id.cmt_repost_user);
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

    class LoadImageListener implements LoadImageCtrl.LoadImageListener {

        @Override
        public void onLoadImageSuccee(Bitmap bm, int tag) {
            ImageTagView view = mTagImageViews.get(tag);
            if(view.getImageTag() == tag && view.getVisibility() == View.VISIBLE) {
                view.setImageBitmap(bm);
            }
            mTagImageViews.remove(tag);
        }

        @Override
        public void onLoadImageErrer(String errer, int tag) {
        }
    }

    class LoadMoreListener implements LoadCommentsCtrl.LoadCommentsListener {

        @Override
        public void onSuccee(ArrayList arrayList) {
            if(mCount > 0) mComments.remove(mCount - 1);
            mComments.addAll(arrayList);
            setupData();
            notifyDataSetChanged();
        }

        @Override
        public void onErrer(String errer) {

        }
    }
}
