package com.hm.sweetreader;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;

/**
 * project：MyReader
 * author： FLY
 * date：   2016/5/30
 * time：   10:39
 * purpose：
 */
public class ScreenUtils {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param context
     * @param dpValue 要转换的dp值
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     *
     * @param context
     * @param pxValue 要转换的px值
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int screenWidth(Activity act){
        Point point = new Point();
        act.getWindowManager().getDefaultDisplay().getSize(point);
        return point.x;
    }
    public static int screenHeigh(Activity act){
        Point point = new Point();
        act.getWindowManager().getDefaultDisplay().getSize(point);
        return point.y;
    }
}
