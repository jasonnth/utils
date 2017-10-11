package com.jasonngo.imageUtils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

/**
 * Created by ideabox on 8/12/16.
 */
public class BitmapScaler {

    /**
     * scale and keep aspect ratio
     *
     * @param b
     * @param width
     * @return Bitmap
     */
    public static Bitmap scaleToFitWidth(Bitmap b, int width) {
        float factor = width / (float) b.getWidth();
        return Bitmap.createScaledBitmap(b, width, (int) (b.getHeight() * factor), true);
    }


    /**
     * scale and keep aspect ratio
     *
     * @param b
     * @param height
     * @return Bitmap
     */
    public static Bitmap scaleToFitHeight(Bitmap b, int height) {
        float factor = height / (float) b.getHeight();
        return Bitmap.createScaledBitmap(b, (int) (b.getWidth() * factor), height, true);
    }


    /**
     * scale and keep aspect ratio
     *
     * @param b
     * @param width
     * @param height
     * @return Bitmap
     */
    public static Bitmap scaleToFill(Bitmap b, int width, int height) {
        float factorH = height / (float) b.getWidth();
        float factorW = width / (float) b.getWidth();
        float factorToUse = (factorH > factorW) ? factorW : factorH;
        return Bitmap.createScaledBitmap(b, (int) (b.getWidth() * factorToUse),
                (int) (b.getHeight() * factorToUse), true);
    }


    /**
     * scale and don't keep aspect ratio
     *
     * @param b
     * @param width
     * @param height
     * @return Bitmap
     */
    public static Bitmap strechToFill(Bitmap b, int width, int height) {
        float factorH = height / (float) b.getHeight();
        float factorW = width / (float) b.getWidth();
        return Bitmap.createScaledBitmap(b, (int) (b.getWidth() * factorW),
                (int) (b.getHeight() * factorH), true);
    }

    /***
     *
     * @param bmp
     * @param borderSize
     * @return
     */
    public static Bitmap addBorder(Bitmap bmp, int borderSize) {
        Bitmap bmpWithBorder = Bitmap.createBitmap(bmp.getWidth() + borderSize * 2, bmp.getHeight() + borderSize * 2, bmp.getConfig());
        Canvas canvas = new Canvas(bmpWithBorder);
        canvas.drawColor(Color.RED);
        canvas.drawBitmap(bmp, borderSize, borderSize, null);
        return bmpWithBorder;
    }


}
