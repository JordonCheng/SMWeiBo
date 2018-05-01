package com.jordoncheng.smweibo.views;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.jordoncheng.smweibo.*;
import com.jordoncheng.smweibo.controlers.WBStatusesCtrlActivity;
import com.jordoncheng.smweibo.models.adapters.*;



public class WBHomeActivity extends WBStatusesCtrlActivity {

    private TabLayout mTabLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTransparentBar();

        setContentView(R.layout.activity_home);

        ViewPager mViewPager = findViewById(R.id.viewpager);
        mTabLayout = findViewById(R.id.tab_layout);
        mTabLayout.addOnTabSelectedListener(new PageChangeListener(mViewPager));

        FragmentPagerAdapter mAdapter = new WBStatusVPAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        setIcon();
    }

    public class PageChangeListener extends TabLayout.ViewPagerOnTabSelectedListener{

        PageChangeListener(ViewPager viewPager) {
            super(viewPager);
        }

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            Animation enter = AnimationUtils.loadAnimation(WBHomeActivity.this, R.anim.tab_enter);
            View tabView = tab.getCustomView();
            if (tabView != null) {
                tabView.startAnimation(enter);
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            Animation exit = AnimationUtils.loadAnimation(WBHomeActivity.this, R.anim.tab_exit);
            View tabView = tab.getCustomView();
            if (tabView != null) {
                tabView.startAnimation(exit);
            }
        }
    }

    public void setIcon() {

        Animation init = AnimationUtils.loadAnimation(this, R.anim.tab_init);
        ImageView tabImageView = new ImageView(this);
        tabImageView.setImageResource(R.mipmap.ic_home_white_24dp);
        mTabLayout.getTabAt(0).setCustomView(tabImageView);
        tabImageView = new ImageView(this);
        tabImageView.setImageResource(R.mipmap.ic_favorite_white_24dp);
        mTabLayout.getTabAt(1).setCustomView(tabImageView);
        tabImageView.startAnimation(init);
        tabImageView = new ImageView(this);
        tabImageView.setImageResource(R.mipmap.ic_person_white_24dp);
        mTabLayout.getTabAt(2).setCustomView(tabImageView);
        tabImageView.startAnimation(init);
        tabImageView = new ImageView(this);
        tabImageView.setImageResource(R.mipmap.ic_earth_white_24dp);
        mTabLayout.getTabAt(3).setCustomView(tabImageView);
        tabImageView.startAnimation(init);
        tabImageView = new ImageView(this);
        tabImageView.setImageResource(R.mipmap.ic_settings_white_24dp);
        mTabLayout.getTabAt(4).setCustomView(tabImageView);
        tabImageView.startAnimation(init);
    }
}
