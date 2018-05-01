package com.jordoncheng.smweibo.controlers;

import com.sina.weibo.sdk.openapi.models.Status;

import java.util.ArrayList;

/**
 * Created by Administrator on 3/26/2018.
 */

public interface LoadStatusesCtrl {

    void loadMoreStatus(int timelineType, long uid, long max_id, LoadStatusesListener lSListener);
    void refreshStatus(int timelineType, long uid, LoadStatusesListener lSListener);
    void loadLocalStatus(int timelineType, long uid, LoadStatusesListener lSListener);

    interface LoadStatusesListener {
        void onSuccee(ArrayList<Status> arrayList);
        void onErrer(String errer);
    }
}
