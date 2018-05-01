package com.jordoncheng.smweibo.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;

import com.jordoncheng.smweibo.controlers.WBStatusesCtrlActivity;
import com.jordoncheng.smweibo.models.adapters.WBImageVPAdapter;
import com.jordoncheng.smweibo.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 3/20/2018.
 */

public class WBImageActivity extends WBStatusesCtrlActivity {

    private ArrayList<String> mImageURLSet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mImageURLSet = intent.getStringArrayListExtra("image urls");

        setContentView(R.layout.image_page);
        ViewPager imagePager = findViewById(R.id.image_pager_view);
        imagePager.setAdapter(new WBImageVPAdapter(mImageURLSet));
        imagePager.setCurrentItem(intent.getIntExtra("position", 0));
    }
}
