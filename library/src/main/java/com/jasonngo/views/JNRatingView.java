package com.jasonngo.views;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatRatingBar;
import android.util.AttributeSet;

/**
 * Created by Jason Ngo on 9/22/17.
 */

public class JNRatingView extends AppCompatRatingBar {

    public JNRatingView(Context context) {
        super(context, null);
    }

    public JNRatingView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public JNRatingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * customize color
     * @param resColorNormal
     * @param resColorRate
     */
    public void setColor(@ColorInt int resColorNormal, @ColorInt int resColorRate) {
        try {
            LayerDrawable layerDrawable = (LayerDrawable) this.getProgressDrawable();
            layerDrawable.getDrawable(0).mutate().setColorFilter(resColorNormal, PorterDuff.Mode.SRC_ATOP);
            layerDrawable.getDrawable(1).mutate().setColorFilter(resColorNormal, PorterDuff.Mode.SRC_ATOP);
            layerDrawable.getDrawable(2).mutate().setColorFilter(resColorRate, PorterDuff.Mode.SRC_ATOP);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
