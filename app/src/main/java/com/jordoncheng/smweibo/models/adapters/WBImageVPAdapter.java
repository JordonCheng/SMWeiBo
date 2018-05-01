package com.jordoncheng.smweibo.models.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.jordoncheng.smweibo.controlers.LoadImageCtrl;
import com.jordoncheng.smweibo.views.ImageTagView;

import java.util.ArrayList;

/**
 * Created by Administrator on 3/20/2018.
 */

public class WBImageVPAdapter extends PagerAdapter implements LoadImageCtrl.LoadImageListener {

    private ArrayList<String> mImageURLSet;
    private SparseArray<ImageTagView> mImageViews;

    public WBImageVPAdapter(ArrayList<String> imageURLs) {
        super();
        mImageURLSet = imageURLs;
        mImageViews = new SparseArray<>();
    }

    @Override
    public int getCount() {
        return mImageURLSet.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageTagView image = new ImageTagView(container.getContext());
        mImageViews.put(position, image);
        ((LoadImageCtrl)(container.getContext())).loadImage(mImageURLSet.get(position), position, this);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)v.getContext()).finish();
            }
        });
        container.addView(image);
        return image;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }

    @Override
    public void onLoadImageSuccee(Bitmap bm, int tag) {
        mImageViews.get(tag).setImageBitmap(bm);
    }

    @Override
    public void onLoadImageErrer(String errer, int tag) {

    }
}
