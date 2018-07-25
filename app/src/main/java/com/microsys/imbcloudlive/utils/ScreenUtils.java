package com.microsys.imbcloudlive.utils;

import android.util.DisplayMetrics;

import com.microsys.imbcloudlive.app.MyApplication;


public class ScreenUtils {
    private static volatile ScreenUtils mScreenUtils;
    private int screenWidth;
    private int screenHeight;
    private float xdpi, ydpi;

    public ScreenUtils() {
    }

    public static ScreenUtils getInstance() {
        if (null == mScreenUtils) {
            synchronized (ScreenUtils.class) {
                if (null == mScreenUtils) {
                    mScreenUtils = new ScreenUtils();
                }
            }
        }
        return mScreenUtils;
    }

    public void init() {
        DisplayMetrics dm = MyApplication.getInstance().getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        xdpi = dm.xdpi;
        ydpi = dm.ydpi;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getSceenHeight() {
        return screenHeight;
    }

    public float getXdpi() {
        return xdpi;
    }

    public float getYdpi() {
        return ydpi;
    }
}
