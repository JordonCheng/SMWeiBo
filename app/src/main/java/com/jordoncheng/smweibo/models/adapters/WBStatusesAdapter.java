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
import com.jordoncheng.smweibo.controlers.LoadStatusesCtrl;
import com.jordoncheng.smweibo.views.WBImageActivity;
import com.jordoncheng.smweibo.views.WBDetailActivity;
import com.jordoncheng.smweibo.views.WBUserPopupActivity;
import com.jordoncheng.smweibo.views.ImageTagView;
import com.jordoncheng.smweibo.views.StatusImageLayout;
import com.jordoncheng.smweibo.R;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.User;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class WBStatusesAdapter extends RecyclerView.Adapter<WBStatusesAdapter.VH> {

    private static final int HAS_NO_IMAGE_NO_RETWEET = 100001;
    private static final int HAS_IMAGE_NO_RETWEET = 100002;
    private static final int HAS_RETWEET_NO_IMAGE = 100003;
    private static final int HAS_RETWEET_AND_IMAGE = 100004;
    
    private Context mContext;
    private LoadStatusesCtrl mLoadStatusesCtrl;
    private LoadImageCtrl mLoadImageCtrl;
    private LoadMoreListener mLoadMoreCallback;
    private LoadImageListener mLoadImageListener;

    private ArrayList<Status> mStatuses;
    private int mCount;
    private long mLoadMoreID;
    private int mTimelineType;
    private SparseArray<ImageTagView> mTagImageViews;
    private int mImageTag;

    public WBStatusesAdapter(Context context, int timelineType) {
        this.mContext = context;
        this.mLoadStatusesCtrl = (LoadStatusesCtrl)context;
        this.mLoadImageCtrl = (LoadImageCtrl)context;
        this.mTimelineType = timelineType;
        mTagImageViews = new SparseArray<>();
        mLoadImageListener = new LoadImageListener();
        initialize();
    }

    public WBStatusesAdapter(Context context, int timelineType, ArrayList<Status> statuses) {
        this.mContext = context;
        this.mLoadStatusesCtrl = (LoadStatusesCtrl)context;
        this.mLoadImageCtrl = (LoadImageCtrl)context;
        this.mTimelineType = timelineType;
        mTagImageViews = new SparseArray<>();
        mStatuses = statuses;
        mLoadImageListener = new LoadImageListener();
        setupData();
    }

    public WBStatusesAdapter(Context context, Status status) {
        this.mContext = context;
        this.mLoadImageCtrl = (LoadImageCtrl)context;
        mTagImageViews = new SparseArray<>();
        mStatuses = new ArrayList<>();
        mStatuses.add(status);
        mLoadImageListener = new LoadImageListener();
        setupData();
    }

    private void setupData() {
        mCount = mStatuses.size();
        mLoadMoreID = mStatuses.get(mCount - 1).id;
    }

    private void initialize() {
        mLoadStatusesCtrl.loadLocalStatus(mTimelineType, 0, new InitializeListener());
    }

    private void loadMore() {
        if(mLoadMoreCallback == null) mLoadStatusesCtrl.loadMoreStatus(mTimelineType, 0
                , mLoadMoreID, mLoadMoreCallback = new LoadMoreListener());
        else mLoadStatusesCtrl.loadMoreStatus(mTimelineType, 0, mLoadMoreID, mLoadMoreCallback);
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
        View itemView = null;
        switch (viewType) {
            case HAS_IMAGE_NO_RETWEET : itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.status_list_item_contain_image_no_repost, parent, false);
            break;
            case HAS_NO_IMAGE_NO_RETWEET : itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.status_list_item_only_text, parent, false);
            break;
            case HAS_RETWEET_NO_IMAGE : itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.status_list_item_repost_no_image, parent, false);
            break;
            case HAS_RETWEET_AND_IMAGE : itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.status_list_item_repost_has_image, parent, false);
            break;
        }
        return new VH(itemView, viewType);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {

        if(holder == null) return;

        holder.imageViewHolder.clear();

        Status status = mStatuses.get(position);
        holder.statusTextContent.setOnClickListener(new TVOnClickListener(status));
        holder.statusUser.setOnClickListener(new UserPopupOnClickListner(status.user));

        loadImage((ImageTagView)holder.statusAvatar, status.user.avatar_large);
        holder.imageViewHolder.add(holder.statusAvatar);

        holder.statusScreenName.setText("@" + status.user.screen_name);
        holder.statusName.setText(status.user.name);
        holder.statusTextContent.setText(status.text);
        holder.statusRepostCount.setText(status.reposts_count + "");
        holder.statusAttitudesCount.setText(status.attitudes_count + "");

        if(holder.viewType == HAS_RETWEET_NO_IMAGE || holder.viewType == HAS_RETWEET_AND_IMAGE)
        {
            Status repost = status.retweeted_status;
            holder.repostTextContent.setOnClickListener(new TVOnClickListener(repost));
            holder.repostUser.setOnClickListener(new UserPopupOnClickListner(repost.user));

            loadImage((ImageTagView)holder.repostAvatar, repost.user.avatar_large);
            holder.imageViewHolder.add(holder.repostAvatar);

            holder.repostScreenName.setText("@" + repost.user.screen_name);
            holder.repostTextContent.setText(repost.text);
            holder.repostRepostCount.setText(String.valueOf(repost.reposts_count));
            holder.repostCommentsCount.setText(String.valueOf(repost.comments_count));
            holder.repostAttitudesCount.setText(String.valueOf(repost.attitudes_count));
        }
        else if(holder.viewType == HAS_IMAGE_NO_RETWEET)
        {
            ArrayList<String> picURLs = status.pic_urls;
            int picCount = picURLs.size();
            ((StatusImageLayout)holder.imageViews).setImageCount(picCount);

            for(int i = 0; i < picCount; i++) {
                String url = picURLs.get(i);
                Matcher m1 = Pattern.compile("thumbnail").matcher(url);
                String urlImage = m1.replaceFirst("bmiddle");
                ImageView child = (ImageView)holder.imageViews.getChildAt(i);
                child.setOnClickListener(new ImageOnClickListener(picURLs, i));
                loadImage((ImageTagView)child, urlImage);
                holder.imageViewHolder.add(child);
            }
        }

        if(holder.viewType == HAS_RETWEET_AND_IMAGE) {
            ArrayList<String> picURLs = status.retweeted_status.pic_urls;
            int picCount = picURLs.size();
            ((StatusImageLayout)holder.imageViews).setImageCount(picCount);

            for(int i = 0; i < picCount; i++) {
                String url = picURLs.get(i);
                Matcher m1 = Pattern.compile("thumbnail").matcher(url);
                String imageURL = m1.replaceFirst("bmiddle");
                ImageView child = (ImageView)holder.imageViews.getChildAt(i);
                child.setOnClickListener(new ImageOnClickListener(picURLs, i));
                loadImage((ImageTagView)child, imageURL);
                holder.imageViewHolder.add(child);
            }
        }
        
        if(position == mCount - 2) {
            loadMore();
        }
    }

    @Override
    public void onViewRecycled(VH holder) {
        super.onViewRecycled(holder);
        int count = holder.imageViewHolder.size();
        ImageTagView view;
        for (int i = 0; i < count; i++) {
            view = (ImageTagView)holder.imageViewHolder.get(i);
            view.setImageTag(0);
            view.setImageBitmap(null);
            view.setClickable(false);
        }
    }

    @Override
    public int getItemViewType(int position) {

        Status status = mStatuses.get(position);
        int viewType = 0;

        if(status.pic_urls == null && status.retweeted_status == null) viewType = HAS_NO_IMAGE_NO_RETWEET;
        if(status.pic_urls != null) viewType = HAS_IMAGE_NO_RETWEET;
        if(status.retweeted_status != null)
        {
            if(status.retweeted_status.pic_urls == null) viewType = HAS_RETWEET_NO_IMAGE;
            else viewType = HAS_RETWEET_AND_IMAGE;
        }
        return viewType;
    }

    @Override
    public int getItemCount() {
        return mCount;
    }

    public static class VH extends RecyclerView.ViewHolder {
        
        int viewType;
        ArrayList<ImageView> imageViewHolder;
        ViewGroup imageViews;

        View statusUser; ImageView statusAvatar; TextView statusScreenName; TextView statusName;
        TextView statusTextContent;
        TextView statusRepostCount; TextView statusAttitudesCount;

        View repostUser; ImageView repostAvatar; TextView repostScreenName;
        TextView repostTextContent;
        TextView repostRepostCount; TextView repostCommentsCount; TextView repostAttitudesCount;

        VH(View itemView, int viewType) {
            super(itemView);

            this.viewType = viewType;
            imageViewHolder = new ArrayList<>();
            statusUser = itemView.findViewById(R.id.line_list_item_user_on_large);
            statusAvatar = itemView.findViewById(R.id.line_list_item_avatar_on_large);
            statusScreenName = itemView.findViewById(R.id.line_list_item_screen_name_on_large);
            statusName = itemView.findViewById(R.id.line_list_item_name_on_large);
            statusTextContent = itemView.findViewById(R.id.line_list_item_text_content_on_status);
            statusRepostCount = itemView.findViewById(R.id.line_list_item_repost_count_on_status);
            statusAttitudesCount = itemView.findViewById(R.id.line_list_item_attitudes_count_on_status);

            switch (viewType) {
                case HAS_IMAGE_NO_RETWEET :
                    imageViews = itemView.findViewById(R.id.line_list_item_status_image);
                    break;
                case HAS_RETWEET_NO_IMAGE :
                    repostUser = itemView.findViewById(R.id.line_list_item_user_on_small);
                    repostAvatar = itemView.findViewById(R.id.line_list_item_avatar_on_small);
                    repostScreenName = itemView.findViewById(R.id.line_list_item_screen_name_on_small);
                    repostTextContent = itemView.findViewById(R.id.line_list_item_text_content_for_repost);
                    repostRepostCount = itemView.findViewById(R.id.line_list_item_repost_count_on_repost);
                    repostCommentsCount = itemView.findViewById(R.id.line_list_item_comments_count_on_repost);
                    repostAttitudesCount = itemView.findViewById(R.id.line_list_item_attitudes_count_on_repost);
                    break;
                case HAS_RETWEET_AND_IMAGE :
                    imageViews = itemView.findViewById(R.id.line_list_item_status_image);
                    repostUser = itemView.findViewById(R.id.line_list_item_user_on_small);
                    repostAvatar = itemView.findViewById(R.id.line_list_item_avatar_on_small);
                    repostScreenName = itemView.findViewById(R.id.line_list_item_screen_name_on_small);
                    repostTextContent = itemView.findViewById(R.id.line_list_item_text_content_for_repost);
                    repostRepostCount = itemView.findViewById(R.id.line_list_item_repost_count_on_repost);
                    repostCommentsCount = itemView.findViewById(R.id.line_list_item_comments_count_on_repost);
                    repostAttitudesCount = itemView.findViewById(R.id.line_list_item_attitudes_count_on_repost);
                    break;
            }
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

    class RepostOnClickListener implements View.OnClickListener {

        Status status;

        public RepostOnClickListener(Status status) {
            this.status = status;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, WBDetailActivity.class);
            intent.putExtra("get", "repost");
            intent.putExtra("status", status);
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

    class ImageOnClickListener implements View.OnClickListener {

        ArrayList<String> picURLs;
        int position;

        public ImageOnClickListener(ArrayList<String> picURLs, int position) {
            this.picURLs = picURLs;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, WBImageActivity.class);
            intent.putStringArrayListExtra("image urls", picURLs);
            intent.putExtra("position", position);
            mContext.startActivity(intent);
        }
    }

    class LoadImageListener implements LoadImageCtrl.LoadImageListener {

        @Override
        public void onLoadImageSuccee(Bitmap bm, int tag) {
            ImageTagView view = mTagImageViews.get(tag);
            if(view.getImageTag() == tag) {
                view.setImageBitmap(bm);
            }
            mTagImageViews.remove(tag);
        }

        @Override
        public void onLoadImageErrer(String errer, int tag) {
        }
    }

    class LoadMoreListener implements LoadStatusesCtrl.LoadStatusesListener {

        @Override
        public void onSuccee(ArrayList arrayList) {
            if (arrayList != null) {
                mStatuses.remove(mCount - 1);
                mStatuses.addAll(arrayList);
                setupData();
                notifyDataSetChanged();
            }
        }

        @Override
        public void onErrer(String errer) {

        }
    }

    class InitializeListener implements LoadStatusesCtrl.LoadStatusesListener {

        @Override
        public void onSuccee(ArrayList arrayList) {
            mStatuses = arrayList;
            setupData();
            notifyDataSetChanged();
        }

        @Override
        public void onErrer(String errer) {

        }
    }
}
