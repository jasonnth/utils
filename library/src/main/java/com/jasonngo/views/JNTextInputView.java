package com.jasonngo.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.Editable;
import android.text.NoCopySpan;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.jasonngo.R;

/**
 * Created by Jason Ngo on 8/30/17.
 */

public class JNTextInputView extends FrameLayout implements TextWatcher {

    LinearLayout mInputRootView;
    JNTextView mInputTextView;
    JNEditView mInputEditView;
    ImageButton btnShowPassword;
    View mInputContentView;
    InputType mInputType;

    private enum InputType {
        PHONE, EMAIL, NUMBER, PASSWORD, NORMAL
    }

    /**
     * listener edit text on focus
     */
    private OnFocusChangeListener onFocusChangeListener;

    /**
     * listener edit text on text change
     */
    private TextChange onTextChange;

    public JNTextInputView(Context context) {
        super(context, null);
    }

    public JNTextInputView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initLayout(context, attrs);
    }

    private void initLayout(Context context, @Nullable AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.text_input_layout, this);
        if (isInEditMode()) {
            return;
        }

        mInputRootView = findViewById(R.id.mInputRootView);
        mInputTextView = findViewById(R.id.mInputTextView);
        mInputEditView = findViewById(R.id.mInputEditView);
        btnShowPassword = findViewById(R.id.mInputShowPassword);
        mInputContentView = findViewById(R.id.mInputContentView);

        // Load the attrs.xml file
        final TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.JNTextInputView);
        float weightOptional = styledAttrs.getFloat(R.styleable.JNTextInputView_weight, 0f);
        String title = styledAttrs.getString(R.styleable.JNTextInputView_title);
        String hintText = styledAttrs.getString(R.styleable.JNTextInputView_hint);
        int inputType = styledAttrs.getInt(R.styleable.JNTextInputView_inputType, 4);
        styledAttrs.recycle();

        if (title != null) {
            mInputTextView.setText(title);
        }

        if (hintText != null) {
            mInputEditView.setHint(hintText);
        }

        mInputType = InputType.values()[inputType];
        mInputEditView.setInputType(android.text.InputType.TYPE_CLASS_TEXT | getInputType(mInputType));
        if (mInputType == InputType.PASSWORD) {
            btnShowPassword.setVisibility(VISIBLE);
            btnShowPassword.animate().alpha(0).start();
        }


        if (weightOptional > 0) {
            LinearLayout.LayoutParams paramsTitle = (LinearLayout.LayoutParams) mInputTextView.getLayoutParams();
            paramsTitle.weight = weightOptional;
            paramsTitle.width = 0;
            mInputTextView.setLayoutParams(paramsTitle);
            mInputTextView.invalidate();

            LinearLayout.LayoutParams paramsInput = (LinearLayout.LayoutParams) mInputContentView.getLayoutParams();
            paramsInput.weight = 10 - weightOptional;
            paramsInput.width = 0;
            mInputContentView.setLayoutParams(paramsInput);
            mInputContentView.invalidate();
        }

        mInputEditView.addTextChangedListener(this);
        mInputEditView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (getFocusListener() != null) {
                    getFocusListener().onFocusChange(view, b);
                }
            }
        });

        btnShowPassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showPassword();
            }
        });
    }

    private int getInputType(InputType type) {
        switch (type) {
            case PHONE:
                return android.text.InputType.TYPE_CLASS_PHONE;
            case EMAIL:
                return android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS;
            case NUMBER:
                return android.text.InputType.TYPE_CLASS_NUMBER;
            case PASSWORD:
                return android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD;
            case NORMAL:
                return android.text.InputType.TYPE_CLASS_TEXT;
        }

        return android.text.InputType.TYPE_CLASS_TEXT;
    }

    public void onShowPassword() {
        showPassword();
    }

    private void showPassword() {
        btnShowPassword.setActivated(!btnShowPassword.isActivated());
        if (btnShowPassword.isActivated()) {
            btnShowPassword.setImageResource(R.drawable.ic_eye_show);
            mInputEditView.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            btnShowPassword.setImageResource(R.drawable.ic_eye_hide);
            mInputEditView.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }

        try {
            mInputEditView.setSelection(mInputEditView.getText().length());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * get text input
     */
    public String getText() {
        return mInputEditView.getText().toString();
    }

    /**
     * set text input
     *
     * @param pText
     */
    public void setText(String pText) {
        mInputEditView.setText(pText);
        try {
            mInputEditView.setSelection(mInputEditView.getText().length());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setEnabled(boolean pEnabled) {
        super.setEnabled(pEnabled);
        mInputEditView.setEnabled(pEnabled);
    }

    private OnFocusChangeListener getFocusListener() {
        return onFocusChangeListener;
    }

    private TextChange getOnTextChange() {
        return onTextChange;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (getOnTextChange() != null) {
            getOnTextChange().beforeTextChanged(charSequence, i, i1, i2);
        }
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (getOnTextChange() != null) {
            getOnTextChange().onTextChanged(charSequence, i, i1, i2);
        }

        if (!getText().isEmpty()) {
            setError(null);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (getOnTextChange() != null) {
            getOnTextChange().afterTextChanged(editable);
        }

        if (mInputType == InputType.PASSWORD) {
            if (getText().isEmpty()) {
                btnShowPassword.animate().alpha(0).start();
            } else {
                btnShowPassword.animate().alpha(1).start();
            }
        }
    }

    //======================== public methods =====================
    public void setError(String pText) {
        if (!TextUtils.isEmpty(pText)) {
            mInputEditView.requestFocus();
        }
        mInputEditView.setError(pText);
    }

    /**
     * set error's input
     *
     * @param resId
     */
    public void setError(@StringRes int resId) {
        mInputEditView.requestFocus();
        mInputEditView.setText(resId);
    }

    /**
     * set focus change listener
     *
     * @param onFocusChangeListener
     */
    public void setOnFocusChangeListener(JNTextInputView.OnFocusChangeListener onFocusChangeListener) {
        this.onFocusChangeListener = onFocusChangeListener;
    }


    /**
     * set text changed listener
     *
     * @param onTextChange
     */
    public void addTextChangedListener(TextChange onTextChange) {
        this.onTextChange = onTextChange;
    }

    //======================== end public methods =====================


    //======================== interface =====================
    public interface OnFocusChangeListener {
        void onFocusChange(View pView, boolean pFocus);
    }

    public interface TextChange extends NoCopySpan {
        void beforeTextChanged(CharSequence var1, int var2, int var3, int var4);

        void onTextChanged(CharSequence var1, int var2, int var3, int var4);

        void afterTextChanged(Editable var1);
    }
}
