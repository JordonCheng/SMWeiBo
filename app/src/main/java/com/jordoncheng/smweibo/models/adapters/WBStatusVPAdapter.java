package com.jordoncheng.smweibo.models.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.jordoncheng.smweibo.views.WBEmtyFragment;
import com.jordoncheng.smweibo.controlers.WBStatusesCtrlActivity;
import com.jordoncheng.smweibo.views.WBStatusesFragment;

/**
 * Created by JrDnCheng on 3/6/2018.
 */

public class WBStatusVPAdapter extends FragmentPagerAdapter {

    public WBStatusVPAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new WBStatusesFragment();
        Bundle args = new Bundle();
        int timelineType;
        switch (position) {
            case 0 : timelineType = WBStatusesCtrlActivity.STATUS_GET_HOME_TIMELINE;
            break;
            case 1 : timelineType = WBStatusesCtrlActivity.STATUS_GET_BILATERAL_TIMELINE;
            break;
            case 2 : timelineType = WBStatusesCtrlActivity.STATUS_GET_USER_TIMELINE;
            break;
            case 3 : timelineType = WBStatusesCtrlActivity.STATUS_GET_PUBLIC_TIMELINE;
            break;
            default: return new WBEmtyFragment();
        }
        args.putInt("timeline type", timelineType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }
}
