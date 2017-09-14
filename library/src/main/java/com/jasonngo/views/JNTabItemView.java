package com.jasonngo.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.jasonngo.R;


/**
 * Created by Jason Ngo on 9/13/17.
 */

public class JNTabItemView extends FrameLayout {

    private ImageView mIcon;
    private ImageView mIconRight;
    private JNTextView mTextView;
    private JNTextView mTextViewRight;
    private View mTabItemDivider;

    public JNTabItemView(@NonNull Context context) {
        super(context, null);
    }

    public JNTabItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
        initLayout(attrs);
    }

    public JNTabItemView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout(attrs);
    }

    private void initLayout(AttributeSet attrs){
        LayoutInflater.from(getContext()).inflate(R.layout.tab_item_layout, this);
        // find view by id
        mIcon = findViewById(R.id.mTabItemIcon);
        mIconRight = findViewById(R.id.mTabItemIconRight);
        mTextView = findViewById(R.id.mTabItemText);
        mTextViewRight = findViewById(R.id.mTabItemTextRight);
        mTabItemDivider = findViewById(R.id.mTabItemDivider);

        final TypedArray styledAttrs = getContext().obtainStyledAttributes(attrs, R.styleable.JNTabItemView);
        final Drawable iconDrawable = styledAttrs.getDrawable(R.styleable.JNTabItemView_itemIcon);
        final Drawable iconRightDrawable = styledAttrs.getDrawable(R.styleable.JNTabItemView_iconRight);
        final String text = styledAttrs.getString(R.styleable.JNTabItemView_text);
        final String textRight = styledAttrs.getString(R.styleable.JNTabItemView_textRight);
        final boolean isHideDivider = styledAttrs.getBoolean(R.styleable.JNTabItemView_hideDivider, false);
        styledAttrs.recycle();

        if (iconDrawable != null) {
            mIcon.setImageDrawable(iconDrawable);
            mIcon.setVisibility(VISIBLE);
        }

        if (iconRightDrawable != null) {
            mIconRight.setImageDrawable(iconRightDrawable);
            mIconRight.setVisibility(VISIBLE);
        }

        if (text != null) {
            mTextView.setText(text);
        }

        if (!TextUtils.isEmpty(textRight)) {
            mTextViewRight.setText(textRight);
            mTextViewRight.setVisibility(VISIBLE);
        }

        if (isHideDivider) {
            mTabItemDivider.setVisibility(GONE);
        }
    }

    /**
     * set text item
     * @param pText
     */
    public void setText(String pText) {
        this.mTextView.setText(pText);
    }


    /**
     * set text right item
     * @param pText
     */
    public void setTextRight(String pText) {
        this.mTextViewRight.setText(pText);
    }
}
