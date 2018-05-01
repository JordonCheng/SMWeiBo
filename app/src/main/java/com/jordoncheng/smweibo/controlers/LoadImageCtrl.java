package com.jordoncheng.smweibo.controlers;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 3/27/2018.
 */

public interface LoadImageCtrl {

    void loadImage(String url, int requestCode, LoadImageListener lIListener);

    interface LoadImageListener {
        void onLoadImageSuccee(Bitmap bm, int requestCode);
        void onLoadImageErrer(String errer, int requestCode);
    }
}
