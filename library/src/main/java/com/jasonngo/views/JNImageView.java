package com.jasonngo.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.jasonngo.R;
import com.jasonngo.helper.NetworkUtil;
import com.jasonngo.imageUtils.IBImageLoader;

/**
 * Created by Jason Ngo on 9/8/17.
 */

public class JNImageView extends LinearLayout implements View.OnClickListener {

    ImageView mImageView;
    ProgressBar mProgressBar;
    ImageButton btnReload;

    private String mImageUrl = "";
    private
    @DrawableRes
    int mPlaceHolderResId;

    private boolean isShowReload;

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
        btnReload = findViewById(R.id.mIVReload);

        // Load the attrs.xml file
        final TypedArray styledAttrs = getContext().obtainStyledAttributes(attrs, R.styleable.JNImageView);
        final Drawable imageDrawable = styledAttrs.getDrawable(R.styleable.JNImageView_src);
        isShowReload = styledAttrs.getBoolean(R.styleable.JNImageView_showReload, false);
        styledAttrs.recycle();

        mImageView.setImageDrawable(imageDrawable);
        if (isShowReload) {
            btnReload.setVisibility(VISIBLE);
            btnReload.setOnClickListener(this);
        }
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
            IBImageLoader.safeGlideImageLoadWithCallback(getContext(), mImageUrl, new SimpleTarget<GlideBitmapDrawable>() {
                @Override
                public void onResourceReady(GlideBitmapDrawable resource, GlideAnimation glideAnimation) {
                    mImageView.setImageDrawable(resource);
                    mProgressBar.animate().alpha(0).start();
                    if (isShowReload)
                        btnReload.animate().alpha(0).start();
                }

                @Override
                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                    super.onLoadFailed(e, errorDrawable);
                    mImageView.setImageResource(mPlaceHolderResId);
                    mProgressBar.animate().alpha(0).start();
                    if (isShowReload && !NetworkUtil.isMobileNetworkAvailable(getContext()))
                        btnReload.animate().alpha(1).start();
                }
            });
        } else {
            mImageView.setImageResource(mPlaceHolderResId);
        }
    }

    @Override
    public void onClick(View view) {
        btnReload.animate().alpha(0).start();
        mProgressBar.animate().alpha(1).start();
        btnReload.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadImage();
            }
        }, 2000);
    }
}
