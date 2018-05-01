/*
package com.jordoncheng.smweibo.View;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.jordoncheng.smweibo.Model.Adapter.WBStatusVPAdapter;
import com.jordoncheng.smweibo.R;


*/
/**
 * Created by JrDnCheng on 3/5/2018.
 *//*


public class WBUserActivity extends WBBaseActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        mViewPager = (ViewPager)findViewById(R.id.viewpager);
        mTabLayout = (TabLayout)findViewById(R.id.tab_layout);
        mTabLayout.addOnTabSelectedListener(new PageChangeListener(mViewPager));

        mAdapter = new WBStatusVPAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        setIcon();
    }

    public class PageChangeListener extends TabLayout.ViewPagerOnTabSelectedListener{

        public PageChangeListener(ViewPager viewPager) {
            super(viewPager);
        }

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            Animation enter = AnimationUtils.loadAnimation(WBUserActivity.this, R.anim.tab_enter);
            View tabView = tab.getCustomView();
            if (tabView != null) {
                tabView.startAnimation(enter);
            }
            enter = null;
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            Animation exit = AnimationUtils.loadAnimation(WBUserActivity.this, R.anim.tab_exit);
            View tabView = tab.getCustomView();
            if (tabView != null) {
                tabView.startAnimation(exit);
            }
            exit = null;
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

        init = null;
        tabImageView = null;
    }
}
*/
