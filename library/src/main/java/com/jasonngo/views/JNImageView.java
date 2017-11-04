package com.jasonngo.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.jasonngo.R;
import com.jasonngo.imageUtils.IBImageLoader;

/**
 * Created by Jason Ngo on 9/8/17.
 */

public class JNImageView extends LinearLayout{

    ImageView mImageView;
    ProgressBar mProgressBar;
    private String mImageUrl = "";
    private
    @DrawableRes
    int mPlaceHolderResId;

    public JNImageView(Context context) {
        super(context, null);
    }

    public JNImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initLayout(attrs);
    }

    private void initLayout(AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.image_view_layout, this);
        if (isInEditMode()) {
            return;
        }

        mImageView = findViewById(R.id.mImageView);
        mProgressBar = findViewById(R.id.mIVProgressBar);
        // Load the attrs.xml file
        final TypedArray styledAttrs = getContext().obtainStyledAttributes(attrs, R.styleable.JNImageView);
        final Drawable imageDrawable = styledAttrs.getDrawable(R.styleable.JNImageView_src);
        styledAttrs.recycle();
        mImageView.setImageDrawable(imageDrawable);
    }

    public void setImageUrl(String pUrl, final @DrawableRes int pResId) {
        this.mImageUrl = pUrl;
        this.mPlaceHolderResId = pResId;
        loadImage();
    }

    public void setImageUrl(String pUrl) {
        this.mImageUrl = pUrl;
        loadImage();
    }

    private void loadImage() {
        if (!TextUtils.isEmpty(mImageUrl)) {
            mProgressBar.animate().alpha(1).start();
            IBImageLoader.safeGlideImageLoadWithCallback(getContext(), mImageUrl, new SimpleTarget<BitmapDrawable>() {

                @Override
                public void onResourceReady(BitmapDrawable resource, Transition<? super BitmapDrawable> transition) {
                    mImageView.setImageDrawable(resource);
                    mProgressBar.animate().alpha(0).start();
                }

                @Override
                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                    super.onLoadFailed(errorDrawable);
                    mImageView.setImageResource(mPlaceHolderResId);
                    mProgressBar.animate().alpha(0).start();
                }
            });
        } else {
            mImageView.setImageResource(mPlaceHolderResId);
        }
    }

    public ImageView getImageView() {
        return mImageView;
    }

    public ProgressBar getProgressBar() {
        return mProgressBar;
    }

    public String getImageUrl() {
        return mImageUrl;
    }
}
