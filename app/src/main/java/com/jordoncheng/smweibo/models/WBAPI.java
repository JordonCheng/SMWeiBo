package com.jordoncheng.smweibo.models;

import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.*;

/**
 * Created by Administrator on 3/23/2018.
 */

public interface WBAPI {

    int STATUS      = 1000;
    int COMMENTS    = 1001;


    int STATUS_GET                         = 0;
    int STATUS_GET_HOME_TIMELINE           = 1;
    int STATUS_GET_PUBLIC_TIMELINE         = 2;
    int STATUS_GET_REPOST_TIMELINE         = 3;
    int STATUS_GET_REPOST_TIMELINE_IDS     = 4;
    int STATUS_GET_REPOST_BY_ME            = 5;
    int STATUS_GET_MENTIONS                = 6;
    int STATUS_GET_MENTIONS_IDS            = 7;
    int STATUS_GET_SHOW                    = 8;
    int STATUS_GET_QUERYMID                = 9;
    int STATUS_GET_QUERYID                 = 10;
    int STATUS_GET_FRIENDS_TIMELINE_IDS    = 11;
    int STATUS_GET_USER_TIMELINE           = 12;
    int STATUS_GET_USER_TIMELINE_IDS       = 13;
    int STATUS_GET_COUNT                   = 14;
    int STATUS_GET_GO                      = 15;

    int COMMENTS_GET                       = 16;
    int COMMENTS_GET_SHOW                  = 17;
    int COMMENTS_GET_BY_ME                 = 18;
    int COMMENTS_GET_TO_ME                 = 19;
    int COMMENTS_GET_TIMELINE              = 20;
    int COMMENTS_GET_MENTIONS              = 21;
    int COMMENTS_GET_SHOW_BATCH            = 22;

    int STATUS_GET_BILATERAL_TIMELINE      = 23;

    void request(int requestType, long id, long max_id, RequestListener rL);

    void request(int requestType, long id, String str, boolean and, RequestListener rL);

    StatusesAPI getStatusesAPI();
    CommentsAPI getCommentsAPI();

}
