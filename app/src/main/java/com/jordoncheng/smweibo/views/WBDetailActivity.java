package com.jordoncheng.smweibo.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.jordoncheng.smweibo.controlers.WBDetailCtrlActivity;
import com.jordoncheng.smweibo.models.adapters.WBDetailVPAdapter;
import com.jordoncheng.smweibo.R;

/**
 * Created by Administrator on 3/21/2018.
 */

public class WBDetailActivity extends WBDetailCtrlActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.detail);
        //对状态栏有影响
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new WBDetailVPAdapter(getSupportFragmentManager()));
    }
}
