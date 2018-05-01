package com.jordoncheng.smweibo.models.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.jordoncheng.smweibo.views.WBCommentsFragment;
import com.jordoncheng.smweibo.views.WBEmtyFragment;
import com.jordoncheng.smweibo.views.WBDetailStatusFragment;

/**
 * Created by JrDnCheng on 3/6/2018.
 */

public class WBDetailVPAdapter extends FragmentPagerAdapter {

    public WBDetailVPAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0 : return new WBDetailStatusFragment();
            case 1 : return new WBCommentsFragment();
        }
        return new WBEmtyFragment();
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }
}
