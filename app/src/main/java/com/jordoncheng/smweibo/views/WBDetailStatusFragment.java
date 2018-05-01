package com.jordoncheng.smweibo.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jordoncheng.smweibo.models.adapters.WBStatusesAdapter;
import com.jordoncheng.smweibo.R;
import com.sina.weibo.sdk.openapi.models.Status;

/**
 * Created by JrDnCh on 3/5/2018.
 */

public class WBDetailStatusFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.detail_status, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.status_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        WBStatusesAdapter adapter = new WBStatusesAdapter(getContext(), (Status) getActivity().getIntent().getParcelableExtra("status"));
        recyclerView.setAdapter(adapter);

        rootView.findViewById(R.id.click_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
/*
        final View ed = rootView.findViewById(R.id.input_box);
        final ScrollView sV = rootView.findViewById(R.id.scroll_view);

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rVrect = new Rect();
                Rect eDrect = new Rect();
                ed.getLocalVisibleRect(eDrect);
                rootView.getWindowVisibleDisplayFrame(rVrect);
                Log.d("底部高度：" , String.valueOf(rVrect.bottom));
                if(rVrect.bottom != 2560) {
                    sV.getRootView().setPadding(0,0,0,1000);
                    sV.fullScroll(View.FOCUS_DOWN);
                } else {
                    sV.getRootView().setPadding(0,0,0,0);
                    sV.fullScroll(View.FOCUS_UP);
                }
            }
        });*/

        return rootView;
    }
}

