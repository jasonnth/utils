package com.jasonngo.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import com.jasonngo.R;

public class JNEditView extends AppCompatEditText {

    private static final String TAG = "CarEditView";
    private String dotsString;
    private String storeString;
    private String mFontName;

    public JNEditView(final Context context) {
        super(context);
    }

    public JNEditView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public JNEditView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
    }

    private void setCustomFont(final Context context, final AttributeSet attrs) {
        // Load the attrs.xml file
        final TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.TypefaceTextView);

        // Set the typeface based on what this view specifies
        String fontName = styledAttrs.getString(R.styleable.TypefaceTextView_customFont);
        styledAttrs.recycle();
        if (fontName != null) {
            mFontName = fontName;
        } else {
            mFontName = getContext().getString(R.string.sf_ui_text_light);
        }
        loadFont(getContext(), mFontName);
    }

    /**
     * Loads and sets a font for this typeface.
     *
     * @param pContext  android context.
     * @param pFontName font name to load.
     */
    public void loadFont(final Context pContext, final String pFontName) {
        Typeface font = Typeface.createFromAsset(pContext.getAssets(), "fonts/" + pFontName);
        setTypeface(font);
    }

    @Override
    public void setInputType(int type) {
        super.setInputType(type);
        loadFont(getContext(), mFontName);
    }
}
