package com.jordoncheng.smweibo.controlers;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.Window;
import android.view.WindowManager;

import com.jordoncheng.smweibo.models.ImageThread;

import java.io.RandomAccessFile;
import java.lang.ref.WeakReference;

public abstract class WBBaseCtrlAcitivity extends AppCompatActivity implements LoadImageCtrl {

    private SparseArray<LoadImageListener> mImageListeners = new SparseArray<>();
    private SparseIntArray mRequestCodes = new SparseIntArray();
    private ImageThreadCtrl.ImageThreadListener mImageThreadListener = new ImageThreadListener(this);

    //WBBaseCtrlAcitivity的全局递增记录码
    private static int mBaseRequestCode = 0;
    private static ImageThreadCtrl mImageThreadCtrl;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageThreadCtrl = ImageThread.getImageThreadCtrl(getApplication());
    }

    private int putRequestCode(int requestCode) {
        int code = mBaseRequestCode++;
        mRequestCodes.put(code, requestCode);
        return code;
    }

    @Override
    public void loadImage(String url, int requestCode, LoadImageListener lIListener) {
        int code = putRequestCode(requestCode);
        mImageListeners.put(code, lIListener);
        mImageThreadCtrl.loadImage(url, code, mImageThreadListener);
    }

    private void dimBackground(final float from, final float to) {
        final Window window = getWindow();
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(from, to);
        valueAnimator.setDuration(300);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                WindowManager.LayoutParams params = window.getAttributes();
                params.alpha = (Float) animation.getAnimatedValue();
                window.setAttributes(params);
            }
        });

        valueAnimator.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        dimBackground(0.3f,1.0f);
    }

    @Override
    protected void onPause() {
        super.onPause();
        dimBackground(1.0f,0.3f);
    }

    static class ImageThreadListener implements ImageThreadCtrl.ImageThreadListener {

        private WeakReference<WBBaseCtrlAcitivity> mCallback;

        ImageThreadListener(WBBaseCtrlAcitivity context) {
            mCallback = new WeakReference<>(context);
        }

        @Override
        public void onImageThreadSuccee(Bitmap bm, int requestCode) {
            mCallback.get().mImageListeners.get(requestCode)
                    .onLoadImageSuccee(bm, mCallback.get().mRequestCodes.get(requestCode));
        }

        @Override
        public void onImageThreadErrer(String errer, int requestCode) {

        }
    }
}
