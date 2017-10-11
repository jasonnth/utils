package com.jasonngo.imageUtils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageUtils {

    public static Bitmap scaleCenterCrop(Bitmap source, int newHeight,
                                         int newWidth) {
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();

        // Compute the scaling factors to fit the new height and width,
        // respectively.
        // To cover the final image, the final scaling will be the bigger
        // of these two.
        float xScale = (float) newWidth / sourceWidth;
        float yScale = (float) newHeight / sourceHeight;
        float scale = Math.max(xScale, yScale);

        // Now get the size of the source bitmap when scaled
        float scaledWidth = scale * sourceWidth;
        float scaledHeight = scale * sourceHeight;

        // Let's find out the upper left coordinates if the scaled bitmap
        // should be centered in the new size give by the parameters
        float left = (newWidth - scaledWidth) / 2;
        float top = (newHeight - scaledHeight) / 2;

        // The target rectangle for the new, scaled version of the source bitmap
        // will now
        // be
        RectF targetRect = new RectF(left, top, left + scaledWidth, top
                + scaledHeight);

        // Finally, we create a new bitmap of the specified size and draw our
        // new,
        // scaled bitmap onto it.
        Bitmap dest = Bitmap.createBitmap(newWidth, newHeight,
                source.getConfig());
        Canvas canvas = new Canvas(dest);
        canvas.drawBitmap(source, null, targetRect, null);
        return dest;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int color, Context context) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int borderSizePx = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, (float) 1, context.getResources()
                        .getDisplayMetrics());

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        float cornerSizePx = 270;
        if (bitmap.getWidth() > bitmap.getHeight()) {
            cornerSizePx = bitmap.getHeight() / 2;
        } else {
            cornerSizePx = bitmap.getWidth() / 2;
        }

        // prepare canvas for transfer
        paint.setAntiAlias(true);
        paint.setColor(0xFFFFFFFF);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, cornerSizePx, cornerSizePx, paint);

        // draw bitmap
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        // draw border
        if (color != 0) {
            paint.setColor(color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth((float) borderSizePx);
            canvas.drawRoundRect(rectF, cornerSizePx, cornerSizePx, paint);
        }
        return output;
    }

    public static Bitmap getRoundedCornerWith(Bitmap bitmap, int cornerRadius, Context context) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int borderSizePx = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, (float) 1, context.getResources()
                        .getDisplayMetrics());

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        final float densityMultiplier = context.getResources().getDisplayMetrics().density;
        final float roundPx = cornerRadius * densityMultiplier;

        // prepare canvas for transfer
        paint.setAntiAlias(true);
        paint.setColor(0xFFFFFFFF);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        // draw bitmap
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }


    public static void loadImageViewResource(Context context, String pngName, ImageView imageView) {
        if (!TextUtils.isEmpty(pngName))
            imageView.setImageResource(context.getResources().getIdentifier("drawable/" + pngName.toLowerCase(), null, context.getPackageName()));
        else {
            imageView.setImageResource(0);
        }
    }

    private static Bitmap rotateImageIfRequired(ContentResolver resolver, Bitmap img, Uri selectedImage) {

        // Detect rotation
        int rotation = getRotation(resolver, selectedImage);
        if (rotation != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);
            Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
            img.recycle();
            return rotatedImg;
        } else {
            return img;
        }
    }

    private static float exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    public static File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        return image;
    }


    public static File createImageFile(String fileName) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = fileName + "_JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        return image;
    }

    public static String getRealPathFromURI(ContentResolver resolver, Uri contentUri) {
        String result = "";
        try {
            Cursor cursor = resolver.query(contentUri, null, null, null, null);
            if (cursor == null) {
                result = contentUri.getPath();
            } else {
                if (cursor.moveToFirst()) {
                    int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    result = cursor.getString(idx);
                    cursor.close();
                } else {
                    result = contentUri.getPath();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    private static int getRotation(ContentResolver resolver, Uri selectedImage) {
        int rotation = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(getRealPathFromURI(resolver, selectedImage));
            rotation = (int) exifOrientationToDegrees(exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL));
            return rotation;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotation;
    }

    public static Bitmap readBitmapFromUri(ContentResolver resolver, Uri selectedImage) {
        Bitmap bm = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 5;
        AssetFileDescriptor fileDescriptor = null;
        try {
            fileDescriptor = resolver.openAssetFileDescriptor(selectedImage, "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                bm = BitmapFactory.decodeFileDescriptor(fileDescriptor.getFileDescriptor(), null, options);
                fileDescriptor.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return rotateImageIfRequired(resolver, bm, selectedImage);
    }

    public static Bitmap readBitmapFromUri(ContentResolver resolver, Uri selectedImage, int scale) {
        Bitmap bm = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = scale;
        AssetFileDescriptor fileDescriptor = null;
        try {
            fileDescriptor = resolver.openAssetFileDescriptor(selectedImage, "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                bm = BitmapFactory.decodeFileDescriptor(fileDescriptor.getFileDescriptor(), null, options);
                fileDescriptor.close();

                if (bm.getWidth() > 1920) { // too large
                    bm = BitmapScaler.scaleToFitWidth(bm, 1920);
                } else if (bm.getHeight() > 1920) {
                    bm = BitmapScaler.scaleToFitHeight(bm, 1920);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return rotateImageIfRequired(resolver, bm, selectedImage);

    }

    public static Bitmap readBitmapFromFilePath(String filePath) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeFile(filePath);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bitmap;
    }

    public static boolean checkLimitImageSize(Context mContext, Uri imageUri, long maxSize) {
        try {
            Cursor returnCursor = mContext.getContentResolver().query(imageUri, null, null, null, null);
            if (returnCursor != null) {
                int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                returnCursor.moveToFirst();
                Long imageSize = returnCursor.getLong(sizeIndex);
                if (imageSize >= maxSize)
                    return true;
            }
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static long getImageSize(Context mContext, Uri imageUri) {
        try {
            Cursor returnCursor = mContext.getContentResolver().query(imageUri, null, null, null, null);
            if (returnCursor != null) {
                int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                returnCursor.moveToFirst();
                return returnCursor.getLong(sizeIndex);
            }
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        return 1024;
    }


    // Custom method to add a border around bitmap
    public static Bitmap addBorderToBitmap(Bitmap srcBitmap, int borderWidth, int borderColor) {

        // Initialize a new Bitmap to make it bordered bitmap
        Bitmap dstBitmap = Bitmap.createBitmap(
                srcBitmap.getWidth() + borderWidth * 2, // Width
                srcBitmap.getHeight() + borderWidth * 2, // Height
                Bitmap.Config.ARGB_8888 // Config
        );

        /*
            Canvas
                The Canvas class holds the "draw" calls. To draw something, you need 4 basic
                components: A Bitmap to hold the pixels, a Canvas to host the draw calls (writing
                into the bitmap), a drawing primitive (e.g. Rect, Path, text, Bitmap), and a paint
                (to describe the colors and styles for the drawing).
        */
        // Initialize a new Canvas instance
        Canvas canvas = new Canvas(dstBitmap);

        // Initialize a new Paint instance to draw border
        Paint paint = new Paint();
        paint.setColor(borderColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(borderWidth);
        paint.setAntiAlias(true);

        /*
            Rect
                Rect holds four integer coordinates for a rectangle. The rectangle is represented by
                the coordinates of its 4 edges (left, top, right bottom). These fields can be accessed
                directly. Use width() and height() to retrieve the rectangle's width and height.
                Note: most methods do not check to see that the coordinates are sorted correctly
                (i.e. left <= right and top <= bottom).
        */
        /*
            Rect(int left, int top, int right, int bottom)
                Create a new rectangle with the specified coordinates.
        */

        // Initialize a new Rect instance
        /*
            We set left = border width /2, because android draw border in a shape
            by covering both inner and outer side.
            By padding half border size, we included full border inside the canvas.
        */
        Rect rect = new Rect(
                borderWidth / 2,
                borderWidth / 2,
                canvas.getWidth() - borderWidth / 2,
                canvas.getHeight() - borderWidth / 2
        );

        /*
            public void drawRect (Rect r, Paint paint)
                Draw the specified Rect using the specified Paint. The rectangle will be filled
                or framed based on the Style in the paint.

            Parameters
                r : The rectangle to be drawn.
                paint : The paint used to draw the rectangle

        */
        // Draw a rectangle as a border/shadow on canvas
        canvas.drawRect(rect, paint);

        /*
            public void drawBitmap (Bitmap bitmap, float left, float top, Paint paint)
                Draw the specified bitmap, with its top/left corner at (x,y), using the specified
                paint, transformed by the current matrix.

                Note: if the paint contains a maskfilter that generates a mask which extends beyond
                the bitmap's original width/height (e.g. BlurMaskFilter), then the bitmap will be
                drawn as if it were in a Shader with CLAMP mode. Thus the color outside of the
                original width/height will be the edge color replicated.

                If the bitmap and canvas have different densities, this function will take care of
                automatically scaling the bitmap to draw at the same density as the canvas.

            Parameters
                bitmap : The bitmap to be drawn
                left : The position of the left side of the bitmap being drawn
                top : The position of the top side of the bitmap being drawn
                paint : The paint used to draw the bitmap (may be null)
        */

        // Draw source bitmap to canvas
        canvas.drawBitmap(srcBitmap, borderWidth, borderWidth, null);

        /*
            public void recycle ()
                Free the native object associated with this bitmap, and clear the reference to the
                pixel data. This will not free the pixel data synchronously; it simply allows it to
                be garbage collected if there are no other references. The bitmap is marked as
                "dead", meaning it will throw an exception if getPixels() or setPixels() is called,
                and will draw nothing. This operation cannot be reversed, so it should only be
                called if you are sure there are no further uses for the bitmap. This is an advanced
                call, and normally need not be called, since the normal GC process will free up this
                memory when there are no more references to this bitmap.
        if (!srcBitmap.isRecycled())
            srcBitmap.recycle();
            */

        // Return the bordered circular bitmap
        return dstBitmap;
    }

    public static File persistImage(Context context, Bitmap bitmap, String name) {
        File filesDir = context.getFilesDir();
        File imageFile = new File(filesDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e("persistImage", "Error writing bitmap", e);
        }

        return imageFile;
    }
}
