package com.jordoncheng.smweibo.models;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.LruCache;

import com.jordoncheng.smweibo.controlers.ImageThreadCtrl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageThread extends Thread implements ImageThreadCtrl {

    private static final int LOAD_IMAGE = 10101;
    private static final int DOWNLOAD_IMAGE = 10102;
    private static final int FINISH_LOAD = 10103;
    private static final  String RegExp = "([^<>/\\\\\\|:\"\"\\*\\?]+\\.\\w+$)";

    private static ImageThread mThread;
    private static LruCache<String, Bitmap> mBitmapCache;
    private static Application targetAppContext;
    private static LoadImageHandler mHandler;
    private static ReturnImageHandler targetHandler;

    private ImageThread(Application context) {
        mBitmapCache = new LruCache<>(40);
        targetHandler = new ReturnImageHandler();
        targetAppContext = context;
    }

    public static ImageThreadCtrl getImageThreadCtrl(Application context) {
        if (mThread == null || !mThread.isAlive() || mThread.isInterrupted()) {
            mThread = new ImageThread(context);
            mThread.start();
        }
        return mThread;
    }

    @Override
    public void run() {
        Looper.prepare();
        mHandler = new LoadImageHandler();
        Looper.loop();
    }

    private static void handleLoadImage(String imageURL, int requestCode, ImageThreadListener listener) {

        Message msg = mHandler.obtainMessage(LOAD_IMAGE, listener);
        Bundle data = new Bundle();
        data.putInt("request code", requestCode);
        data.putString("url", imageURL);
        msg.setData(data);
        mHandler.sendMessage(msg);
    }

    private static String parseFileName(String URL) {
        Matcher m = Pattern.compile(RegExp).matcher(URL);
        m.find();
        return m.group(1);
    }

    @Override
    public void loadImage(String imageURL, int requestCode, ImageThreadListener listener) {
        handleLoadImage(imageURL, requestCode, listener);
    }

    private static class LoadImageHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {

            if (msg.what == LOAD_IMAGE) {

                Bundle data = msg.getData();
                int requestCode  = data.getInt("request code");
                String imageURL = data.getString("url");
                String fileName;
                if ((fileName = data.getString("file name")) == null) {
                    fileName = parseFileName(imageURL);
                }

                Bitmap bitmap = loadLocal(fileName);
                if (bitmap != null) {
                    mBitmapCache.put(imageURL, bitmap);
                    handleUpdateImage(imageURL, requestCode, msg.obj);
                } else {
                    data.putString("file name", fileName);
                    Message Dmsg = mHandler.obtainMessage(DOWNLOAD_IMAGE, msg.obj);
                    Dmsg.setData(data);
                    mHandler.sendMessage(Dmsg);
                }
            }

            if (msg.what == DOWNLOAD_IMAGE) {

                Bundle data = msg.getData();
                int requestCode  = data.getInt("request code");
                String imageURL = data.getString("url");
                String fileName = data.getString("file name");

                if(downloadImage(imageURL, fileName)) {
                    handleLoadImage(imageURL, requestCode, (ImageThreadListener)msg.obj);
                }
            }
        }

        private Bitmap loadLocal(String fileName) {

            InputStream is = null;
            Bitmap bitmap = null;

            if (bitmap == null) {
                try {
                    is = targetAppContext.openFileInput(fileName);
                    bitmap = BitmapFactory.decodeStream(is);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return null;
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return bitmap;
        }

        private boolean downloadImage(String imageURL, String fileName) {
            try {
                URL url = new URL(imageURL);
                try (
                        InputStream is = url.openStream();
                        OutputStream os = targetAppContext.openFileOutput(fileName,
                                Context.MODE_PRIVATE)
                ) {
                    byte[] buff = new byte[1024];
                    int hasRead;
                    while((hasRead = is.read(buff)) > 0) {
                        os.write(buff, 0, hasRead);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        private void handleUpdateImage(String bitmapTag, int requestCode, Object obj) {
            Bundle bundle = new Bundle();
            bundle.putInt("request code", requestCode);
            bundle.putString("bitmap tag", bitmapTag);
            Message targetMsg = targetHandler.obtainMessage(FINISH_LOAD, obj);
            targetMsg.setData(bundle);
            targetHandler.sendMessage(targetMsg);
        }
    }

    private static class ReturnImageHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            if(msg.what == ImageThread.FINISH_LOAD) {
                Bundle data = msg.getData();
                ((ImageThreadListener)msg.obj).onImageThreadSuccee(mBitmapCache.get(data.getString("bitmap tag")), data.getInt("request code"));
            }
        }
    }
}
