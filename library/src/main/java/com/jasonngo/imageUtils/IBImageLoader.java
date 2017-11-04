package com.jasonngo.imageUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BaseTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Helper class for Image Loader.
 */
public final class IBImageLoader {

    public enum Thumbnail {
        XS("76"),
        S("152"),
        M("304"),
        L("608");
        private String mSize = "304"; // default

        Thumbnail(final String size) {
            mSize = size;
        }

        public String getSize() {
            return mSize;
        }
    }

    private IBImageLoader() {
        /* not used */
    }

    public static void doSafeLoad(final IBImageLoadExecutor IBImageLoadExecutor) {
        try {
            IBImageLoadExecutor.execute();
        } catch (IllegalArgumentException | IllegalStateException e) {
            IBImageLoadExecutor.handleException(e);
        }
    }

    /**
     * Attempt to download the thumbnail version of an image, with fallback to load full size on failure.
     *
     * @param pUri
     * @param pImageView
     * @param pSize
     */
    public static void displayThumbnailOrFullSizeImage(final String pUri, final ImageView pImageView, final Thumbnail pSize) {

        // Match URLs and extract a group after the first "/" character
        Pattern pattern = Pattern.compile("^https?://[^/]+/(.+)$");
        Matcher matcher = pattern.matcher(pUri);

        final String thumbnailUrl;

        // If the expression matches anything, modify the URL with the thumbnail size, otherwise use the original URL
        if (matcher.find()) {
            thumbnailUrl = new StringBuilder(pUri).insert(matcher.start(1), "t/" + pSize.getSize() + "/").toString();
        } else {
            thumbnailUrl = pUri;
        }

        // Try loading the thumbnail first.
        doSafeLoad(new IBImageLoadExecutor() {
            @Override
            public void execute() {
                Glide.with(pImageView.getContext())
                        .load(thumbnailUrl)
                        .into(new SimpleTarget<Drawable>() {

                            @Override
                            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                                Log.d("onResourceReady:", thumbnailUrl);
                                pImageView.setImageDrawable(resource);
                            }

                            @Override
                            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                super.onLoadFailed(errorDrawable);
                                Log.d("onLoadFailed: ", thumbnailUrl + " || Loading: " + pUri);
                                Glide.with(pImageView.getContext()).load(pUri).into(pImageView); // fallback if thumbnail doesn't exist
                            }
                        });
            }
        });
    }

    /**
     * This provides a catch for an illegal argument exception, thrown if trying to load an image into a destroyed activity
     *
     * @param pContext
     * @param pUrl
     * @param pImageView
     */
    public static void safeGlideImageLoad(final Context pContext, final String pUrl, final ImageView pImageView) {
        doSafeLoad(new IBImageLoadExecutor() {
            @Override
            public void execute() {
                Glide.with(pContext).load(pUrl.replace("https", "http")).into(pImageView);
            }
        });
    }

    /**
     * This provides a catch for an illegal argument exception, thrown if trying to load an image into a destroyed activity
     *
     * @param pContext
     * @param pUrl
     * @param pImageView
     */
    public static void safeGlideImageLoadWithNoCache(final Context pContext, final String pUrl, final ImageView pImageView) {
        doSafeLoad(new IBImageLoadExecutor() {
            @Override
            public void execute() {
                Glide.with(pContext).load(pUrl.replace("https", "http")).into(pImageView);
            }
        });
    }

    /**
     * This lets us load an image with a callback to complete when a resource has loaded
     *
     * @param pContext
     * @param pUrl
     * @param pSimpleTarget
     */
    public static void safeGlideImageLoadWithCallback(final Context pContext, final String pUrl, final SimpleTarget pSimpleTarget) {
        doSafeLoad(new IBImageLoadExecutor() {
            @Override
            public void execute() {
                Glide.with(pContext).load(pUrl.replace("https", "http")).into(pSimpleTarget);
            }
        });
    }

   /* *//**
     * Get Bitmap from Url.
     *
     * @param pContext
     * @param pUrl
     * @param pBitmapLoader callback to receive the bitmap
     *//*
    public static void getGlideBitmap(final Context pContext, final String pUrl, final IBitmapLoader pBitmapLoader) {
        doSafeLoad(new IBImageLoadExecutor() {
            @Override
            public void execute() {
                Glide.with(pContext).load(pUrl).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        pBitmapLoader.onBitmapLoaded(resource);
                    }
                });
            }
        });
    }*/


    /**
     * Interface to return a Bitmap from Glide
     */
    public interface IBitmapLoader {

        void onBitmapLoaded(final Bitmap pBitmap);
    }
}
