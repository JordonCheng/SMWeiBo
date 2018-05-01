package com.jordoncheng.smweibo.controlers;

import com.sina.weibo.sdk.openapi.models.Comment;

import java.util.ArrayList;

/**
 * Created by Administrator on 3/27/2018.
 */

public interface LoadCommentsCtrl {

    void loadMoreComments(int commentsType, long mid, long max_id, LoadCommentsListener lCListener);
    void refreshComments(int commentsType, long mid, LoadCommentsListener lCListener);

    interface LoadCommentsListener {
        void onSuccee(ArrayList<Comment> arrayList);
        void onErrer(String errer);
    }

}
