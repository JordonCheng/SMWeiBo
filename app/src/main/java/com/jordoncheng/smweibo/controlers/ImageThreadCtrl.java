package com.jordoncheng.smweibo.controlers;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 3/27/2018.
 */

public interface ImageThreadCtrl {

    void loadImage(String url, int requestCode, ImageThreadListener listener);

    interface ImageThreadListener {
        void onImageThreadSuccee(Bitmap bm, int requestCode);
        void onImageThreadErrer(String errer, int requestCode);
    }
}
