package com.jasonngo.views;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.jasonngo.R;

/**
 * Created on 12/17/14.
 */
public class JNColoredRatingBar extends View {

    private static final String TAG = JNColoredRatingBar.class.getSimpleName();

    private Bitmap mRatedStar;
    private Bitmap mProgressBackground;
    private Context mContext;
    private int mNumStars = 5;
    private float mRating = 0;
    private boolean mIndicator;
    private float slidePosition;

    /**
     * A callback that notifies clients when the rating has been changed. This
     * includes changes that were initiated by the user through a touch gesture
     * or arrow key/trackball as well as changes that were initiated
     * programmatically.
     */
    public interface OnRatingBarChangeListener {

        /**
         * Notification that the rating has changed. Clients can use the
         * fromUser parameter to distinguish user-initiated changes from those
         * that occurred programmatically. This will not be called continuously
         * while the user is dragging, only when the user finalizes a rating by
         * lifting the touch.
         *
         * @param ratingBar The RatingBar whose rating has changed.
         * @param rating    The current rating. This will be in the range
         *                  0..numStars.
         * @param fromUser  True if the rating change was initiated by a user's
         *                  touch gesture or arrow key/horizontal trackbell movement.
         */
        void onRatingChanged(JNColoredRatingBar ratingBar, float rating, boolean fromUser);

    }

    private OnRatingBarChangeListener mOnRatingBarChangeListener;

    public JNColoredRatingBar(Context context) {
        this(context, null);
    }

    public JNColoredRatingBar(Context context, AttributeSet attrs) {
        //this(context, attrs, R.attr.coloredRatingBarStyle);
        this(context, attrs, 0);
    }

    public JNColoredRatingBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.JNColoredRatingBar,
                defStyle, 0);
        final boolean indicator = a.getBoolean(R.styleable.JNColoredRatingBar_indicator, false);
        final float rating = a.getFloat(R.styleable.JNColoredRatingBar_rating, -1);
        a.recycle();

        setIndicator(indicator);
        setRating(rating);
        init(context);
    }


    private void init(Context context) {
        mContext = context;
        Resources res = getResources();
        mRatedStar = BitmapFactory.decodeResource(res, R.drawable.ic_star_selected);
        mProgressBackground = BitmapFactory.decodeResource(res, R.drawable.ic_star);
    }

    public void setIconRate(@DrawableRes int pNormalResId, @DrawableRes int pSelectedResId) {
        Resources res = getResources();
        mRatedStar = BitmapFactory.decodeResource(res, pSelectedResId);
        mProgressBackground = BitmapFactory.decodeResource(res, pNormalResId);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //draw empty stars bg
        for (int i = 0; i < mNumStars; i++) {
            drawStar(canvas, i);
        }

    }


    private void drawStar(Canvas canvas, int position) {
        float fraction = mRating - (position);
        Bitmap ratedStar = getRatedStar();
        if ((position + 1) < mRating) {
            canvas.drawBitmap(ratedStar, (position * ratedStar.getWidth()), 0, null);
        } else {
            if (fraction > 0 && fraction <= 1) {
                int sourceWidth = ratedStar.getWidth();
                int sourceHeight = ratedStar.getHeight();

                int targetWidth = (int) (ratedStar.getWidth() * fraction);
                int bgWidth = sourceWidth - targetWidth;

                if (targetWidth > 0) {
                    Bitmap croppedBmp = Bitmap.createBitmap(ratedStar, 0, 0, targetWidth, sourceHeight);
                    canvas.drawBitmap(croppedBmp, (position * sourceWidth), 0, null);
//                    croppedBmp.recycle();
                }
                if (bgWidth > 0) {
                    Bitmap croppedBg = Bitmap.createBitmap(mProgressBackground, targetWidth, 0, bgWidth, sourceHeight);
                    canvas.drawBitmap(croppedBg, (position * sourceWidth) + targetWidth, 0, null);
//                    croppedBg.recycle();
                }
            } else {
                canvas.drawBitmap(mProgressBackground, (position * mProgressBackground.getWidth()), 0, null);
            }
        }


    }

    private Bitmap getRatedStar() {
        return mRatedStar;

    }

    public int getNumStars() {
        return mNumStars;
    }

    public void setNumStars(int numStars) {
        this.mNumStars = numStars;
    }

    public float getRating() {
        return mRating;
    }

    public void setRating(float rating) {
        setRating(rating, false);
    }

    void setRating(float rating, boolean fromUser) {
        if (rating > mNumStars) {
            this.mRating = mNumStars;
        }
        this.mRating = rating;
        invalidate();
        dispatchRatingChange(fromUser);
    }

    public boolean isIndicator() {
        return mIndicator;
    }

    public void setIndicator(boolean indicator) {
        this.mIndicator = indicator;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mProgressBackground != null) {

            final int width = mProgressBackground.getWidth() * mNumStars;
            final int height = mProgressBackground.getHeight();
            setMeasuredDimension(resolveSizeAndState(width, widthMeasureSpec, 0),
                    resolveSizeAndState(height, heightMeasureSpec, 0));
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mIndicator) {
            return false;
        }

        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                slidePosition = getRelativePosition(event.getX());
                int newRating = (int) slidePosition + 1;
                if (newRating != mRating) {
                    setRating(newRating, true);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            default:
                break;
        }

        return false;
    }

    private float getRelativePosition(float x) {
        float position = x / mProgressBackground.getWidth();
        position = Math.max(position, 0);
        return Math.min(position, mNumStars - 1);
    }

    /**
     * Sets the listener to be called when the rating changes.
     *
     * @param listener The listener.
     */
    public void setOnRatingBarChangeListener(OnRatingBarChangeListener listener) {
        mOnRatingBarChangeListener = listener;
    }

    /**
     * @return The listener (may be null) that is listening for rating change
     * events.
     */
    public OnRatingBarChangeListener getOnRatingBarChangeListener() {
        return mOnRatingBarChangeListener;
    }

    void dispatchRatingChange(boolean fromUser) {
        if (mOnRatingBarChangeListener != null) {
            mOnRatingBarChangeListener.onRatingChanged(this, getRating(),
                    fromUser);
        }
    }
}