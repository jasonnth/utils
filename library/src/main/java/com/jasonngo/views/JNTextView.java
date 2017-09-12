package com.jasonngo.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
import android.util.AttributeSet;

import com.jasonngo.R;

public class JNTextView extends AppCompatTextView {

    private static final String TAG = "CarEditView";

    public JNTextView(final Context context) {
        super(context);
    }

    public JNTextView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public JNTextView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
    }

    private void setCustomFont(final Context context, final AttributeSet attrs) {
        if (isInEditMode()) {
            return;
        }
        // Load the attrs.xml file
        final TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.TypefaceTextView);

        // Set the typeface based on what this view specifies
        String fontName = styledAttrs.getString(R.styleable.TypefaceTextView_customFont);
        styledAttrs.recycle();
        if (fontName != null) {
            loadFont(getContext(), fontName);
        } else {
            loadFont(getContext(), getContext().getString(R.string.sf_ui_text_light));
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

    /**
     * Displayable styled text from the provided HTML string with the legacy flags
     * @param pText
     */
    public void setTextHtml(String pText) {
        CharSequence text;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            text = Html.fromHtml(pText, 0);
        } else {
            text = Html.fromHtml(pText);
        }
        setText(text);
    }
}
