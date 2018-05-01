package com.jordoncheng.smweibo.controlers;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.jordoncheng.smweibo.models.AccessTokenKeeper;
import com.jordoncheng.smweibo.models.Constants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;
import com.sina.weibo.sdk.utils.LogUtil;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.sina.weibo.sdk.statistic.WBAgent.TAG;

public abstract class WBStatusesCtrlActivity extends WBBaseCtrlAcitivity
        implements LoadStatusesCtrl, LoadImageCtrl {

    public static final int STATUS_GET_HOME_TIMELINE          = 0;
    public static final int STATUS_GET_PUBLIC_TIMELINE        = 1;
    public static final int STATUS_GET_USER_TIMELINE          = 2;
    public static final int STATUS_GET_BILATERAL_TIMELINE     = 3;

    public static final String HOME_CACHE_FILENAME            = "timeline_home.cache";
    public static final String PUBLIC_CACHE_FILENAME          = "timeline_public.cache";
    public static final String USER_CACHE_FILENAME            = "timeline_user.cache";
    public static final String BILATERAL_CACHE_FILENAME       = "timeline_bilateral.cache";

    private ArrayList<Status> mLoadDatas;
    private static int listenersTag = 0;
    private SparseArray<LoadStatusesListener> mLoadStatusesListeners = new SparseArray<>();
    private StatusesAPI WBAPI;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();
    }

    private void initialize() {
        WBAPI = new StatusesAPI(this, Constants.APP_KEY, 
                AccessTokenKeeper.readAccessToken(this));
    }

    private int putLoadStatusesListener(LoadStatusesListener listener) {
        int tag = ++listenersTag;
        mLoadStatusesListeners.put(tag, listener);
        return tag;
    }

    @Override
    public void loadMoreStatus(int timelineType, long uid, long max_id, LoadStatusesListener lSListener) {
        switch (timelineType) {
            case STATUS_GET_HOME_TIMELINE : WBAPI.homeTimeline(0,max_id,20,1,false,0,
                    false,new LoadMoreListener(this, putLoadStatusesListener(lSListener)));
                break;
            case STATUS_GET_PUBLIC_TIMELINE : WBAPI.publicTimeline(20,1,false,
                    new LoadMoreListener(this, putLoadStatusesListener(lSListener)));
                break;
            case STATUS_GET_USER_TIMELINE : WBAPI.userTimeline(uid,null,0,max_id,1,1,
                    false,0,false,new LoadMoreListener(this, putLoadStatusesListener(lSListener)));
                break;
            case STATUS_GET_BILATERAL_TIMELINE : WBAPI.bilateralTimeline(0,max_id,20,1,
                    false,0,false,new LoadMoreListener(this, putLoadStatusesListener(lSListener)));
                break;
        }
    }

    @Override
    public void refreshStatus(int timelineType, long uid, LoadStatusesListener lSListener) {
        String cacheFileName;
        switch (timelineType) {
            case STATUS_GET_HOME_TIMELINE : cacheFileName = HOME_CACHE_FILENAME;
            WBAPI.homeTimeline(0,0,20,1,false,0,
                    false,new StatusListener(this, putLoadStatusesListener(lSListener), cacheFileName));
                break;
            case STATUS_GET_PUBLIC_TIMELINE : cacheFileName = PUBLIC_CACHE_FILENAME;
            WBAPI.publicTimeline(20,1,false,
                    new StatusListener(this, putLoadStatusesListener(lSListener), cacheFileName));
                break;
            case STATUS_GET_USER_TIMELINE : cacheFileName = USER_CACHE_FILENAME;
            WBAPI.userTimeline(uid,null,0,0,1,1,
                    false,0,false,new StatusListener(this, putLoadStatusesListener(lSListener), cacheFileName));
                break;
            case STATUS_GET_BILATERAL_TIMELINE : cacheFileName = BILATERAL_CACHE_FILENAME;
            WBAPI.bilateralTimeline(0,0,20,1,
                    false,0,false,new StatusListener(this, putLoadStatusesListener(lSListener), cacheFileName));
                break;
        }
    }

    @Override
    public void loadLocalStatus(int timelineType, long uid, LoadStatusesListener lSListener) {
        String cacheFileName;
        switch (timelineType) {
            case STATUS_GET_HOME_TIMELINE :
                cacheFileName = HOME_CACHE_FILENAME;
                if ((mLoadDatas = fromFile(cacheFileName)) == null) WBAPI.homeTimeline(0,0,20,1,
                        false,0,false,new LocalStatusListener(this, putLoadStatusesListener(lSListener), cacheFileName));
                else lSListener.onSuccee(mLoadDatas);
                break;
            case STATUS_GET_PUBLIC_TIMELINE :
                cacheFileName = PUBLIC_CACHE_FILENAME;
                if ((mLoadDatas = fromFile(cacheFileName)) == null) WBAPI.publicTimeline(20,1,false,
                        new LocalStatusListener(this, putLoadStatusesListener(lSListener), cacheFileName));
                else lSListener.onSuccee(mLoadDatas);
                break;
            case STATUS_GET_USER_TIMELINE :
                cacheFileName = USER_CACHE_FILENAME;
                if ((mLoadDatas = fromFile(cacheFileName)) == null) WBAPI.userTimeline(uid,null,0,0,1,
                        1,false,0,false,new LocalStatusListener(this, putLoadStatusesListener(lSListener), cacheFileName));
                else lSListener.onSuccee(mLoadDatas);
                break;
            case STATUS_GET_BILATERAL_TIMELINE :
                cacheFileName = BILATERAL_CACHE_FILENAME;
                if ((mLoadDatas = fromFile(cacheFileName)) == null) WBAPI.bilateralTimeline(0,0,20,1,false,
                        0,false,new LocalStatusListener(this, putLoadStatusesListener(lSListener), cacheFileName));
                else lSListener.onSuccee(mLoadDatas);
                break;
        }
    }

    private ArrayList<Status> fromFile(String cacheFileName) {
        ArrayList<Status> list = null;
        String data = null;

        try (
                FileInputStream fis = openFileInput(cacheFileName);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br =  new BufferedReader(isr)) {
            data = br.readLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!TextUtils.isEmpty(data)) {
            LogUtil.i(TAG, data);
            list = StatusList.parse(data).statusList;
        }
        return list;
    }

    private void toFile(String data, String cacheFileName) {
        try (
                FileOutputStream fos = openFileOutput(cacheFileName, Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);) {
            fos.write(data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void setTransparentBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            /*window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);*/
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    static class StatusListener implements RequestListener {

        WeakReference<WBStatusesCtrlActivity> mCallback;
        int mListenerTag;
        String mCacheFileName;



        StatusListener(WBStatusesCtrlActivity context, int tag, String fileName) {
            mCallback = new WeakReference<>(context);
            mListenerTag = tag;
            mCacheFileName = fileName;
        }

        @Override
        public void onComplete(String s) {
            if (!TextUtils.isEmpty(s)) {
                LogUtil.i(TAG, s);
                (mCallback.get().mLoadStatusesListeners.get(mListenerTag))
                        .onSuccee(StatusList.parse(s).statusList);
                mCallback.get().mLoadStatusesListeners.remove(mListenerTag);
                mCallback.get().toFile(s, mCacheFileName);
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {

        }
    }

    static class LoadMoreListener implements RequestListener {

        WeakReference<WBStatusesCtrlActivity> mCallback;
        int mListenerTag;



        LoadMoreListener(WBStatusesCtrlActivity context, int tag) {
            mCallback = new WeakReference<>(context);
            mListenerTag = tag;
        }

        @Override
        public void onComplete(String s) {
            if (!TextUtils.isEmpty(s)) {
                LogUtil.i(TAG, s);
                (mCallback.get().mLoadStatusesListeners.get(mListenerTag))
                        .onSuccee(StatusList.parse(s).statusList);
                mCallback.get().mLoadStatusesListeners.remove(mListenerTag);
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {

        }
    }

    static class LocalStatusListener implements RequestListener {

        WeakReference<WBStatusesCtrlActivity> mCallback;
        int mListenerTag;
        String mCacheFileName;

        LocalStatusListener(WBStatusesCtrlActivity context, int tag, String fileName) {
            mCallback = new WeakReference<>(context);
            mListenerTag = tag;
            mCacheFileName = fileName;
        }

        @Override
        public void onComplete(String s) {
            if (!TextUtils.isEmpty(s)) {
                LogUtil.i(TAG, s);
                mCallback.get().toFile(s, mCacheFileName);
                mCallback.get().mLoadDatas = StatusList.parse(s).statusList;
                (mCallback.get().mLoadStatusesListeners.get(mListenerTag)).onSuccee(mCallback.get().mLoadDatas);
                mCallback.get().mLoadStatusesListeners.remove(mListenerTag);
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {

        }
    }
}
