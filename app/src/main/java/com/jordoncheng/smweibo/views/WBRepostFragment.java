/*
package com.jordoncheng.smweibo.View;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jordoncheng.smweibo.Model.Adapter.WBRepostRVAdapter;
import com.jordoncheng.smweibo.Model.Adapter.WBEmtyRVAdapter;
import com.jordoncheng.smweibo.R;
import com.jordoncheng.smweibo.Model.WBAPI;
import com.jordoncheng.smweibo.WeiBoAPI.WBBaseFragment;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;
import com.sina.weibo.sdk.utils.LogUtil;

import java.util.ArrayList;

import static com.sina.weibo.sdk.statistic.WBAgent.TAG;

*/
/**
 * Created by JrDnCh on 3/5/2018.
 *//*


public class WBRepostFragment extends WBBaseFragment {

    public Context mContext;

    private SwipeRefreshLayout rootView;
    private RecyclerView mRecyclerView;
    private WBRepostRVAdapter mRVAdapter;
    public ArrayList<Status> mArrayList;
    private long id;

    @Override
    protected void onSetupType() {
        mRequestType = WBAPI.STATUS_GET_REPOST_TIMELINE;
        mid_or_uid = ((Status)getActivity().getIntent().getParcelableExtra("status")).id;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = (SwipeRefreshLayout)inflater.inflate(R.layout.cmt_repost_list, container, false);
        mRecyclerView = rootView.findViewById(R.id.status_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(new WBEmtyRVAdapter());

        rootView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshList();
            }
        });

        rootView.findViewById(R.id.click_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        ((ViewPager)getActivity().getWindow().getDecorView().findViewById(R.id.viewpager)).addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if(position == 2 && mRVAdapter == null) {
                    refreshList();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        return rootView;
    }

    public void setupListData(ArrayList<Status> data) {
        mRVAdapter.setData(data);
        mRVAdapter.notifyDataSetChanged();
    }


    public void refreshList() {
        performRequest(mid_or_uid, 0, new RepostRequestListener());
    }


    class RepostRequestListener implements RequestListener {

        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                LogUtil.i(TAG, response);
                if(mArrayList != null) {
                    mArrayList = StatusList.parse(response).statusList;
                    setupListData(mArrayList);
                    rootView.setRefreshing(false);
                } else {
                    mArrayList = StatusList.parse(response).statusList;
                    mRVAdapter = new WBRepostRVAdapter(WBRepostFragment.this, mArrayList);
                    mRecyclerView.setAdapter(mRVAdapter);
                }
                response="";
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            LogUtil.e(TAG, e.getMessage());
            ErrorInfo info = ErrorInfo.parse(e.getMessage());
            Snackbar.make(rootView, info.toString(), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            rootView.setRefreshing(false);
        }
    }
}

*/
