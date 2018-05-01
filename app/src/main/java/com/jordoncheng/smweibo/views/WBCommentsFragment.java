package com.jordoncheng.smweibo.views;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jordoncheng.smweibo.controlers.LoadCommentsCtrl;
import com.jordoncheng.smweibo.controlers.WBDetailCtrlActivity;
import com.jordoncheng.smweibo.models.adapters.WBCommentsAdapter;
import com.jordoncheng.smweibo.models.adapters.WBEmtyRVAdapter;
import com.jordoncheng.smweibo.R;
import com.sina.weibo.sdk.openapi.models.Status;

import java.util.ArrayList;

/**
 * Created by JrDnCh on 3/5/2018.
 */

public class WBCommentsFragment extends Fragment {

    private SwipeRefreshLayout rootView;
    private RecyclerView mRecyclerView;
    private WBCommentsAdapter mAdapter;
    private LoadCommentsCtrl mLoadCommentsCtrl;
    private int mCommentsType;
    private long mid;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mid = ((Status)getActivity().getIntent().getParcelableExtra("status")).id;
        mCommentsType = WBDetailCtrlActivity.COMMENT_GET_SHOW;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mLoadCommentsCtrl = (LoadCommentsCtrl)getContext();
        rootView = (SwipeRefreshLayout)inflater.inflate(R.layout.cmt_repost_list, container, false);
        mRecyclerView = rootView.findViewById(R.id.status_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(new WBEmtyRVAdapter());

        rootView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mLoadCommentsCtrl.refreshComments(mCommentsType, mid, new RefreshListener());
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
                if(position == 1 && mAdapter == null) {
                    mRecyclerView.setAdapter(new WBCommentsAdapter(getContext(), mCommentsType, mid));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        return rootView;
    }

    class RefreshListener implements LoadCommentsCtrl.LoadCommentsListener {

        @Override
        public void onSuccee(ArrayList arrayList) {
            mRecyclerView.setAdapter(new WBCommentsAdapter(getContext(), mCommentsType, mid, arrayList));
            rootView.setRefreshing(false);
        }

        @Override
        public void onErrer(String errer) {

        }
    }
}

