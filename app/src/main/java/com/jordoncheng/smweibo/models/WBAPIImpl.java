package com.jordoncheng.smweibo.models;

import android.app.Activity;
import android.app.Application;

import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Administrator on 3/23/2018.
 */

public class WBAPIImpl implements WBAPI {

    private static WBAPIImpl mWBAPI;

    private Application mAppContext;
    private StatusesAPI mStatusesAPI;
    private CommentsAPI mCommentsAPI;
    private Oauth2AccessToken mAccessToken;

    private WBAPIImpl(Application context, Activity activity) {
        mAppContext = context;
        initAccessToken(activity);
    }

    public static WBAPI getWBAPI(Application context, Activity activity) {
        if (mWBAPI == null) return mWBAPI = new WBAPIImpl(context, activity);
        else return mWBAPI;
    }

    public void initAccessToken(Activity activity) {
        mAccessToken = AccessTokenKeeper.readAccessToken(mAppContext);
        if(mAccessToken.getToken() == null) {
            WbSdk.install(mAppContext, new AuthInfo(mAppContext, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE));
            new SsoHandler(activity).authorize(new WbAuthListener() {
                @Override
                public void onSuccess(Oauth2AccessToken oauth2AccessToken) {
                    AccessTokenKeeper.writeAccessToken(mAppContext, oauth2AccessToken);
                }

                @Override
                public void cancel() {

                }

                @Override
                public void onFailure(WbConnectErrorMessage wbConnectErrorMessage) {

                }
            });
        }
    }

    @Override
    public void request(int requestType, long id, long max_id, RequestListener rL) {
        if (id == 0) {
            switch (requestType) {
                case WBAPI.STATUS_GET_USER_TIMELINE : getStatusesAPI().userTimeline(Long.parseLong(mAccessToken.getUid()), null, 0, max_id, 20, 1, false,0, false, rL);
                    break;

                case WBAPI.STATUS_GET_HOME_TIMELINE : getStatusesAPI().homeTimeline(0, max_id, 20, 1, false, 0, false, rL);
                    break;

                case WBAPI.STATUS_GET_PUBLIC_TIMELINE : getStatusesAPI().publicTimeline(50, 1, false, rL);
                    break;

                case WBAPI.STATUS_GET_BILATERAL_TIMELINE : getStatusesAPI().bilateralTimeline(0, max_id, 20, 1, false, 0, false, rL);
                    break;
            }
        } else {
            switch (requestType) {
                case WBAPI.STATUS_GET_USER_TIMELINE : getStatusesAPI().userTimeline(id, null, 0, max_id, 20, 1, false,0, false, rL);
                break;

                case WBAPI.COMMENTS_GET_SHOW : getCommentsAPI().show(id, 0, max_id, 20, 1, 0, rL);
                break;

                case WBAPI.STATUS_GET_REPOST_TIMELINE : getStatusesAPI().repostTimeline(id, 0, max_id, 20, 1, 0, rL);
                break;

                case WBAPI.STATUS_GET_PUBLIC_TIMELINE : getStatusesAPI().publicTimeline(50, 1, false, rL);
                break;
            }
        }

    }

    @Override
    public void request(int requestType, long id, String str, boolean and, RequestListener rL) {
        try {
            getCommentsAPI().create(URLEncoder.encode(str, "GBK"), id, false, rL);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            rL.onComplete("字符集错误");
        }
    }

    @Override
    public StatusesAPI getStatusesAPI() {
        if (mStatusesAPI == null) return mStatusesAPI = new StatusesAPI(mAppContext, Constants.APP_KEY, mAccessToken);
        else return mStatusesAPI;
    }

    @Override
    public CommentsAPI getCommentsAPI() {
        if (mCommentsAPI == null) return mCommentsAPI = new CommentsAPI(mAppContext, Constants.APP_KEY, mAccessToken);
        else return mCommentsAPI;
    }
}
