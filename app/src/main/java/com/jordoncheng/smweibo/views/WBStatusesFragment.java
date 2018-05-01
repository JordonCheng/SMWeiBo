package com.jordoncheng.smweibo.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jordoncheng.smweibo.controlers.LoadStatusesCtrl;
import com.jordoncheng.smweibo.models.adapters.WBStatusesAdapter;
import com.jordoncheng.smweibo.R;

import java.util.ArrayList;

/**
 * Created by JrDnCh on 3/5/2018.
 */

public class WBStatusesFragment extends Fragment {

    private LoadStatusesCtrl mLoadStatusesCtrl;

    private SwipeRefreshLayout rootView;
    private RecyclerView mStatusRecyclerView;
    private int mTimelineType;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mLoadStatusesCtrl = (LoadStatusesCtrl)getContext();
        mTimelineType = getArguments().getInt("timeline type");

        rootView = (SwipeRefreshLayout)inflater.inflate(R.layout.list_home_fragment, container, false);
        rootView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mLoadStatusesCtrl.refreshStatus(mTimelineType, 0, new RefreshListener());
            }
        });

        mStatusRecyclerView = rootView.findViewById(R.id.status_recyclerview);
        mStatusRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        RecyclerView.Adapter mWBStatusesAdapter = new WBStatusesAdapter(getContext(), mTimelineType);
        mStatusRecyclerView.setAdapter(mWBStatusesAdapter);

        return rootView;
    }

    class RefreshListener implements LoadStatusesCtrl.LoadStatusesListener {

        @Override
        public void onSuccee(ArrayList arrayList) {
            mStatusRecyclerView.setAdapter(new WBStatusesAdapter(getContext(), mTimelineType, arrayList));
            rootView.setRefreshing(false);
        }

        @Override
        public void onErrer(String errer) {
            rootView.setRefreshing(false);
        }
    }
}

