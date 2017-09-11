package com.jasonngo.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import com.jasonngo.R;

public class JNButton extends AppCompatButton {

    private static final String TAG = "CarEditView";

    public JNButton(final Context context) {
        super(context);
    }

    public JNButton(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public JNButton(final Context context, final AttributeSet attrs, final int defStyle) {
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
            loadFont(getContext(), fontName);
        } else {
            loadFont(getContext(), getContext().getString(R.string.sf_ui_text_medium));
        }
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
}
