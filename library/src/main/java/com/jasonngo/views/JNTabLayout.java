package com.jasonngo.views;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by au.com.crimerates on 6/19/15.
 */
public class JNTabLayout extends HorizontalScrollView {

    /**
     * Allows complete control over the colors drawn in the tab layout. Set with
     * {@link #setCustomTabColorizer(TabColorizer)}.
     */
    public interface TabColorizer {

        /**
         * @return return the color of the indicator used when {@code position} is selected.
         */
        int getIndicatorColor(int position);

        /**
         * @return return the color of the divider drawn to the right of {@code position}.
         */
        int getDividerColor(int position);

    }

    private static final int TITLE_OFFSET_DIPS = 24;
    private static final int TAB_VIEW_PADDING_DIPS = 16;
    private static final int TAB_VIEW_TEXT_SIZE_SP = 16;

    private int mTitleOffset;

    private int mTabViewLayoutId;
    private int mTabViewTextViewId;

    private ViewPager mViewPager;
    private ViewPager.OnPageChangeListener mViewPagerPageChangeListener;
    private Button mTabBadgeNumberText;
    private String[] badgeNumbers;

    private Context mContext;

    private final JNTabStrip mTabStrip;
    private int colorItemVisible, colorItemInvisible;
    private boolean mMathParent = true;

    public JNTabLayout(Context context) {
        this(context, null);
    }

    public JNTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JNTabLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        this.mContext = context;

        // Disable the Scroll Bar
        setHorizontalScrollBarEnabled(false);
        // Make sure that the Tab Strips fills this View
        setFillViewport(true);

        mTitleOffset = (int) (TITLE_OFFSET_DIPS * getResources().getDisplayMetrics().density);

        mTabStrip = new JNTabStrip(context);
        addView(mTabStrip, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    /**
     * set the background color
     *
     * @param color
     */
    public void setBackgroundColor(int color) {
        mTabStrip.setBackgroundColor(color);
    }

    /**
     * @param colorItemVisible
     */
    public void setColorItemVisible(int colorItemVisible) {
        this.colorItemVisible = colorItemVisible;
    }

    /**
     * @param colorItemInvisible
     */
    public void setColorItemInvisible(@Nullable int colorItemInvisible) {
        this.colorItemInvisible = colorItemInvisible;
    }

    public void setBottomBorder(int borderHeight) {
        mTabStrip.setBottomBorderThickness(borderHeight);
    }

    public void setBottomWidth(int borderWidth) {
        mTabStrip.setBottomWidthThickness(borderWidth);
    }

    /**
     * Set the custom {@link TabColorizer} to be used.
     * <p/>
     * If you only require simple custmisation then you can use
     * {@link #setSelectedIndicatorColors(int...)} and {@link #setDividerColors(int...)} to achieve
     * similar effects.
     */
    public void setCustomTabColorizer(TabColorizer tabColorizer) {
        mTabStrip.setCustomTabColorizer(tabColorizer);
    }

    /**
     * Sets the colors to be used for indicating the selected tab. These colors are treated as a
     * circular array. Providing one color will mean that all tabs are indicated with the same color.
     */
    public void setSelectedIndicatorColors(int... colors) {
        mTabStrip.setSelectedIndicatorColors(colors);
    }

    /**
     * Sets the colors to be used for tab dividers. These colors are treated as a circular array.
     * Providing one color will mean that all tabs are indicated with the same color.
     */
    public void setDividerColors(int... colors) {
        mTabStrip.setDividerColors(colors);
    }

    /**
     * Set the {@link ViewPager.OnPageChangeListener}. When using {@link JNTabLayout} you are
     * required to set any {@link ViewPager.OnPageChangeListener} through this method. This is so
     * that the layout can update it's scroll position correctly.
     *
     * @see ViewPager#setOnPageChangeListener(ViewPager.OnPageChangeListener)
     */
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mViewPagerPageChangeListener = listener;
    }

    /**
     * Set the custom layout to be inflated for the tab views.
     *
     * @param layoutResId Layout id to be inflated
     * @param textViewId  id of the {@link TextView} in the inflated view
     */
    public void setCustomTabView(int layoutResId, int textViewId) {
        mTabViewLayoutId = layoutResId;
        mTabViewTextViewId = textViewId;
    }

    public void setCustomBackGroundWithWrapContent(int resID) {
        mTabStrip.setBackgroundResource(resID);
        LayoutParams layoutParams = (LayoutParams) mTabStrip.getLayoutParams();
        layoutParams.width = LayoutParams.WRAP_CONTENT;
        layoutParams.height = LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        mTabStrip.setLayoutParams(layoutParams);
        mTabStrip.invalidate();

    }

    /**
     * Sets the associated view pager. Note that the assumption here is that the pager content
     * (number of tabs and tab titles) does not change after this call has been made.
     */
    public void setViewPager(ViewPager viewPager) {
        mTabStrip.removeAllViews();

        mViewPager = viewPager;
        if (viewPager != null) {
            viewPager.addOnPageChangeListener(new InternalViewPagerListener());
            populateTabStrip();
        }
    }


    /**
     * @param badgeNumbers
     */
    public void setBadgeNumbers(String[] badgeNumbers) {
        this.badgeNumbers = badgeNumbers;
    }

    public String[] getBadgeNumbers() {
        return badgeNumbers;
    }

    /**
     * Create a default view to be used for tabs. This is called if a custom tab view is not set via
     * {@link #setCustomTabView(int, int)}.
     */
    protected TextView createDefaultTabView(Context context) {
        JNAutoResizeTextView textView = new JNAutoResizeTextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, TAB_VIEW_TEXT_SIZE_SP);
        textView.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        // If we're running on Honeycomb or newer, then we can use the Theme's
        // selectableItemBackground to ensure that the View has a pressed state
        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground,
                outValue, true);
        textView.setBackgroundResource(outValue.resourceId);

        int padding = (int) (TAB_VIEW_PADDING_DIPS * getResources().getDisplayMetrics().density);
        textView.setPadding(padding, padding, padding, padding);

        return textView;
    }

    private void populateTabStrip() {
        final PagerAdapter adapter = mViewPager.getAdapter();
        final OnClickListener tabClickListener = new TabClickListener();

        for (int i = 0; i < adapter.getCount(); i++) {
            View tabView = null;
            TextView tabTitleView = null;

            if (mTabViewLayoutId != 0) {
                // If there is a custom tab view layout id set, try and inflate it
                tabView = LayoutInflater.from(getContext()).inflate(mTabViewLayoutId, mTabStrip,
                        false);
                tabTitleView = (TextView) tabView.findViewById(mTabViewTextViewId);
            }

            if (tabView == null) {
                tabView = createDefaultTabView(getContext());
            }

            if (tabTitleView == null && TextView.class.isInstance(tabView)) {
                tabTitleView = (TextView) tabView;
            }

            tabTitleView.setText(adapter.getPageTitle(i));
            tabView.setOnClickListener(tabClickListener);

            if (mTabBadgeNumberText != null) {
                if (getBadgeNumbers() != null && i < getBadgeNumbers().length && !TextUtils.isEmpty(getBadgeNumbers()[i]) && !getBadgeNumbers()[i].equalsIgnoreCase("0")) {
                    mTabBadgeNumberText.setVisibility(VISIBLE);
                    mTabBadgeNumberText.setText(getBadgeNumbers()[i]);
                } else
                    mTabBadgeNumberText.setVisibility(GONE);
            }


            if (isMathParent()) {
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        0,
                        LayoutParams.MATCH_PARENT, 1.0f);
                tabView.setLayoutParams(param);
            } else {
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.MATCH_PARENT);
                tabView.setLayoutParams(param);
            }

            mTabStrip.addView(tabView);
        }
    }

    public boolean isMathParent() {
        return mMathParent;
    }

    public void setMathParent(boolean mMathParent) {
        this.mMathParent = mMathParent;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (mViewPager != null) {
            scrollToTab(mViewPager.getCurrentItem(), 0, ViewPager.SCROLL_STATE_IDLE);
        }
    }

    private void scrollToTab(int tabIndex, int positionOffset, int mScrollState) {
        final int tabStripChildCount = mTabStrip.getChildCount();
        if (tabStripChildCount == 0 || tabIndex < 0 || tabIndex >= tabStripChildCount) {
            return;
        }

        View selectedChild = mTabStrip.getChildAt(tabIndex);
        if (selectedChild != null) {
            int targetScrollX = selectedChild.getLeft() + positionOffset;
            for (int index = 0; index < mTabStrip.getChildCount(); index++) {
                View childInVisible = mTabStrip.getChildAt(index);
                if (childInVisible != null) {
                    childInVisible.setSelected(index == tabIndex);
                    TextView textView = findTextView(childInVisible);
                    if (textView != null) {
                        if (index != tabIndex) {
                            textView.setTextColor(colorItemInvisible);//Color.parseColor("#C8C7CC"));
                            textView.setSelected(false);
                        } else {
                            textView.setTextColor(colorItemVisible);
                            textView.setSelected(true);
                        }
                    }
                }

            }

            if (tabIndex > 0 || positionOffset > 0) {
                // If we're not at the first child and are mid-scroll, make sure we obey the offset
                targetScrollX -= mTitleOffset;
            }

            scrollTo(targetScrollX, 0);

        }
    }

    private TextView findTextView(View childInVisible) {
        TextView textView = null;
        if (!(childInVisible instanceof TextView)) {
            for (int j = 0; j < ((ViewGroup) childInVisible).getChildCount(); j++) {
                View subView = ((ViewGroup) childInVisible).getChildAt(j);
                if (subView != null && subView instanceof TextView) {
                    textView = (TextView) subView;
                    break;
                } else if (ViewGroup.class.isInstance(subView))
                    textView = findTextView(subView);
            }
        } else
            textView = (TextView) childInVisible;

        return textView;
    }

    public void setUpdateBadgeNumber(int index, String numberText) {
        if (TextUtils.isEmpty(numberText)) return;

        View childInVisible = mTabStrip.getChildAt(index);
        if (childInVisible != null) {
            Button btnBadgeNumber = null;
            if (!(childInVisible instanceof Button)) {
                for (int j = 0; j < ((ViewGroup) childInVisible).getChildCount(); j++) {
                    View subView = ((ViewGroup) childInVisible).getChildAt(j);
                    if (subView instanceof ViewGroup) {
                        for (int k = 0; k < ((ViewGroup) subView).getChildCount(); k++) {
                            View kView = ((ViewGroup) subView).getChildAt(k);
                            if (kView instanceof Button) {
                                btnBadgeNumber = (Button) kView;
                                break;
                            }
                        }
                    } else {
                        if (subView != null && subView instanceof Button) {
                            btnBadgeNumber = (Button) subView;
                            break;
                        }
                    }
                }
            } else
                btnBadgeNumber = (Button) childInVisible;

            if (btnBadgeNumber != null) {
                if (numberText.equalsIgnoreCase("0"))
                    btnBadgeNumber.setVisibility(GONE);
                else {
                    btnBadgeNumber.setVisibility(VISIBLE);
                    btnBadgeNumber.setText(numberText);
                }
            }
        }
    }

    private class InternalViewPagerListener implements ViewPager.OnPageChangeListener {
        private int mScrollState;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int tabStripChildCount = mTabStrip.getChildCount();
            if ((tabStripChildCount == 0) || (position < 0) || (position >= tabStripChildCount)) {
                return;
            }

            mTabStrip.onViewPagerPageChanged(position, positionOffset);

            View selectedTitle = mTabStrip.getChildAt(position);
            int extraOffset = (selectedTitle != null)
                    ? (int) (positionOffset * selectedTitle.getWidth())
                    : 0;
            scrollToTab(position, extraOffset, mScrollState);

            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageScrolled(position, positionOffset,
                        positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            mScrollState = state;

            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                mTabStrip.onViewPagerPageChanged(position, 0f);
                scrollToTab(position, 0, ViewPager.SCROLL_STATE_IDLE);
            }

            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageSelected(position);
            }
        }

    }

    private class TabClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                if (v == mTabStrip.getChildAt(i)) {
                    mViewPager.setCurrentItem(i);
                    return;
                }
            }
        }
    }

}
