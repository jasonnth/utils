package com.jasonngo.helper;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by ideabox on 10/4/16.
 */

public class DeviceDimensionsHelper {
    // DeviceDimensionsHelper.getDisplayWidth(context) => (display width in pixels)
    public static int getDisplayWidth(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    // DeviceDimensionsHelper.getDisplayHeight(context) => (display height in pixels)
    public static int getDisplayHeight(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    // DeviceDimensionsHelper.convertDpToPixel(25f, context) => (25dp converted to pixels)
    public static int convertDpToPixel(float dp, Context context){
        Resources r = context.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    // DeviceDimensionsHelper.convertPixelsToDp(25f, context) => (25px converted to dp)
    public static int convertPixelsToDp(float px, Context context){
        Resources r = context.getResources();
        DisplayMetrics metrics = r.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return (int) dp;
    }
}
