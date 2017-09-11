package com.jasonngo.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.jasonngo.R;
import com.jasonngo.imageUtils.IBImageLoader;

/**
 * Created by Jason Ngo on 9/8/17.
 */

public class JNImageView extends LinearLayout {

    ImageView mImageView;
    ProgressBar mProgressBar;

    public JNImageView(Context context) {
        super(context, null);
    }

    public JNImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initLayout(attrs);
    }

    private void initLayout(AttributeSet attrs){
        LayoutInflater.from(getContext()).inflate(R.layout.image_view_layout, this);
        mImageView  = findViewById(R.id.mImageView);
        mImageView  = findViewById(R.id.mIVProgressBar);

        // Load the attrs.xml file
        final TypedArray styledAttrs = getContext().obtainStyledAttributes(attrs, R.styleable.JNImageView);
        Drawable imageDrawable = styledAttrs.getDrawable(R.styleable.JNImageView_src);
        styledAttrs.recycle();

        mImageView.setImageDrawable(imageDrawable);
        mProgressBar.animate().alpha(0).start();
    }


    public void setImageUrl(String pUrl, final @DrawableRes int pResId){
        if (!TextUtils.isEmpty(pUrl)) {
            mProgressBar.animate().alpha(1).start();
            IBImageLoader.safeGlideImageLoadWithCallback(getContext(), pUrl, new SimpleTarget<GlideBitmapDrawable>() {
                @Override
                public void onResourceReady(GlideBitmapDrawable resource, GlideAnimation glideAnimation) {
                    mImageView.setImageDrawable(resource);
                    mProgressBar.animate().alpha(0).start();
                }

                @Override
                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                    super.onLoadFailed(e, errorDrawable);
                    mImageView.setImageResource(pResId);
                    mProgressBar.animate().alpha(0).start();
                }
            });
        } else {
            mImageView.setImageResource(pResId);
        }
    }
}
