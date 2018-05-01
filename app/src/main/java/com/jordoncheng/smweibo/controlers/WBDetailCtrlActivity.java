package com.jordoncheng.smweibo.controlers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.SparseArray;

import com.jordoncheng.smweibo.models.AccessTokenKeeper;
import com.jordoncheng.smweibo.models.Constants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.CommentsAPI;
import com.sina.weibo.sdk.openapi.models.Comment;
import com.sina.weibo.sdk.openapi.models.CommentList;
import com.sina.weibo.sdk.utils.LogUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.sina.weibo.sdk.statistic.WBAgent.TAG;

public abstract class WBDetailCtrlActivity extends WBBaseCtrlAcitivity implements LoadCommentsCtrl, LoadImageCtrl {

    public static final int COMMENT_GET_SHOW                 = 0;
    public static final int COMMENT_GET_BY_ME                = 1;
    public static final int COMMENT_GET_TO_ME                = 2;
    public static final int COMMENT_GET_TIMELINE             = 3;

    private ArrayList<Comment> mLoadDatas;
    private static int listenersTag = 0;
    private SparseArray<LoadCommentsListener> mListeners;
    private CommentsAPI WBAPI;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();
    }

    private void initialize() {
        mListeners = new SparseArray<>();
        WBAPI = new CommentsAPI(this, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(this));
    }

    private int putListener(LoadCommentsListener listener) {
        int tag = ++listenersTag;
        mListeners.put(tag, listener);
        return tag;
    }

    @Override
    public void loadMoreComments(int commentsType, long mid, long max_id, LoadCommentsListener lCListener) {
        switch (commentsType) {
            case COMMENT_GET_SHOW : WBAPI.show(mid,0,max_id,20,1,0,new CommentsListener(this, putListener(lCListener)));
            break;
            case COMMENT_GET_BY_ME : WBAPI.byME(0,max_id,20,1,0,new CommentsListener(this, putListener(lCListener)));
            break;
            case COMMENT_GET_TO_ME : WBAPI.byME(0,max_id,20,1,0,new CommentsListener(this, putListener(lCListener)));
            break;
            case COMMENT_GET_TIMELINE : WBAPI.timeline(0,max_id,20,1,false,new CommentsListener(this, putListener(lCListener)));
        }
    }

    @Override
    public void refreshComments(int commentsType, long mid, LoadCommentsListener lCListener) {
        switch (commentsType) {
            case COMMENT_GET_SHOW : WBAPI.show(mid,0,0,20,1,0,new CommentsListener(this, putListener(lCListener)));
                break;
            case COMMENT_GET_BY_ME : WBAPI.byME(0,0,20,1,0,new CommentsListener(this, putListener(lCListener)));
                break;
            case COMMENT_GET_TO_ME : WBAPI.byME(0,0,20,1,0,new CommentsListener(this, putListener(lCListener)));
                break;
            case COMMENT_GET_TIMELINE : WBAPI.timeline(0,0,20,1,false,new CommentsListener(this, putListener(lCListener)));
        }
    }

    static class CommentsListener implements RequestListener {

        WeakReference<WBDetailCtrlActivity> mCallback;
        int mListenerTag;

        CommentsListener(WBDetailCtrlActivity context, int tag) {
            mCallback = new WeakReference<>(context);
            mListenerTag = tag;
        }

        @Override
        public void onComplete(String s) {
            if (!TextUtils.isEmpty(s)) {
                LogUtil.i(TAG, s);
                mCallback.get().mLoadDatas = CommentList.parse(s).commentList;
                (mCallback.get().mListeners.get(mListenerTag)).onSuccee(mCallback.get().mLoadDatas);
                mCallback.get().mListeners.remove(mListenerTag);
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {

        }
    }
}
