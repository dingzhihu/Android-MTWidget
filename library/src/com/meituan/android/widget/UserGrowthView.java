package com.meituan.android.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * meituan user growth view
 * Created with IntelliJ IDEA.
 * User: dingzhihu
 * Date: 13-3-5
 * Time: 下午6:55
 */
public class UserGrowthView extends View {
    private static final int MAX_GROWTH = 6;

    private UserGrowthAdapter mAdapter;

    private int[] mGrowthValues;
    private Drawable[] mGrowthDrawables;
    private Drawable mPreGrowthDrawable;
    private Drawable mNextGrowthDrawable;
    private int mDrawablePadding;
    private NinePatchDrawable mProgressDrawable;
    private NinePatchDrawable mSecondaryProgressDrawable;
    private int mProgressDrawablePadding;
    private Drawable mIndicatorDrawable;
    private float mIndicatorPadding;
    private int mIndicatorCount;

    private Paint mPaintText;

    public UserGrowthView(Context context) {
        this(context, null);
    }

    public UserGrowthView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.mtUserGrowthViewStyle);
    }

    public UserGrowthView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        final Resources res = getResources();
        final float defaultDrawablePadding = res.getDimension(R.dimen.default_user_growth_view_drawable_padding);
        final float defaultProgressDrawablePadding = res.getDimension(R.dimen.default_user_growth_view_progress_drawable_padding);
        final float defaultIndicatorPadding = res.getDimension(R.dimen.default_user_growth_view_indicator_padding);
        final int defaultIndicatorCount = res.getInteger(R.integer.default_user_growth_view_indicator_count);
        final int defaultTextSize = res.getDimensionPixelSize(R.dimen.default_user_growth_view_text_size);
        final int defaultTextColor = res.getColor(R.color.default_user_growth_view_text_color);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.UserGrowthView, defStyle, 0);
        mGrowthDrawables = new Drawable[MAX_GROWTH + 1];
        mGrowthDrawables[0] = a.getDrawable(R.styleable.UserGrowthView_drawable0);
        mGrowthDrawables[1] = a.getDrawable(R.styleable.UserGrowthView_drawable1);
        mGrowthDrawables[2] = a.getDrawable(R.styleable.UserGrowthView_drawable2);
        mGrowthDrawables[3] = a.getDrawable(R.styleable.UserGrowthView_drawable3);
        mGrowthDrawables[4] = a.getDrawable(R.styleable.UserGrowthView_drawable4);
        mGrowthDrawables[5] = a.getDrawable(R.styleable.UserGrowthView_drawable5);
        mGrowthDrawables[6] = a.getDrawable(R.styleable.UserGrowthView_drawable6);

        Drawable drawable = a.getDrawable(R.styleable.UserGrowthView_progressDrawable);
        mProgressDrawable = (NinePatchDrawable) (drawable == null ? res.getDrawable(R.drawable.user_growth_progress) :
                drawable);
        mDrawablePadding = (int) a.getDimension(R.styleable.UserGrowthView_android_drawablePadding, defaultDrawablePadding);

        drawable = a.getDrawable(R.styleable.UserGrowthView_secondaryProgressDrawable);
        mSecondaryProgressDrawable = (NinePatchDrawable) (drawable == null ? res.getDrawable(R.drawable.user_growth_secondary_progress) :
                drawable);
        mProgressDrawablePadding = (int) a.getDimension(R.styleable.UserGrowthView_progressDrawablePadding,
                defaultProgressDrawablePadding);

        drawable = a.getDrawable(R.styleable.UserGrowthView_indicatorDrawable);
        mIndicatorDrawable = (drawable == null ? res.getDrawable(R.drawable.user_growth_indicator) : drawable);
        mIndicatorPadding = a.getDimension(R.styleable.UserGrowthView_indicatorPadding,
                defaultIndicatorPadding);
        mIndicatorCount = a.getInt(R.styleable.UserGrowthView_indicatorCount, defaultIndicatorCount);

        mPaintText = new Paint();
        mPaintText.setAntiAlias(true);
        mPaintText.setTextSize(a.getDimension(R.styleable.UserGrowthView_android_textSize, defaultTextSize));
        mPaintText.setColor(a.getColor(R.styleable.UserGrowthView_android_textColor, defaultTextColor));

        a.recycle();

    }

    public void setAdapter(UserGrowthAdapter adapter) {
        if (adapter == null) {
            return;
        }
        int curGrowth = adapter.getCurrentGrowth();
        if (curGrowth < 0 || curGrowth > MAX_GROWTH) {
            throw new IllegalStateException(String.format("current growth %d not accepted", curGrowth));
        }

        int curGrowthValue = adapter.getCurrentGrowthValue();
        int preGrowthValue = adapter.getPreGrowthValue();
        int nextGrowthValue = adapter.getNextGrowthValue();
        if (curGrowthValue < preGrowthValue || curGrowthValue > nextGrowthValue || preGrowthValue == nextGrowthValue) {
            throw new IllegalStateException(String.format("curGrowthValue:%d,preGrowthValue:%s,nextGrowthValue:%d,not accepted",
                    curGrowthValue, preGrowthValue, nextGrowthValue));
        }

        mAdapter = adapter;

        calculateGrowthValues();
        calculateGrowthDrawables();

        requestLayout();
        invalidate();
    }

    private void calculateGrowthValues() {
        mGrowthValues = new int[2 + mIndicatorCount];
        final int pre = mAdapter.getPreGrowthValue();
        final int next = mAdapter.getNextGrowthValue();

        mGrowthValues[0] = pre;
        mGrowthValues[mIndicatorCount + 1] = next;

        int step = (next - pre) / (mIndicatorCount + 1);
        for (int i = 1; i <= mIndicatorCount; i++) {
            mGrowthValues[i] = pre + i * step;
        }
    }

    private void calculateGrowthDrawables() {
        final int curGrowth = mAdapter.getCurrentGrowth();
        int preGrowth, nextGrowth;
        if (curGrowth < MAX_GROWTH) {
            preGrowth = curGrowth;
            nextGrowth = curGrowth + 1;
        } else {
            preGrowth = curGrowth - 1;
            nextGrowth = curGrowth;
        }
        mPreGrowthDrawable = getGrowthDrawable(preGrowth);
        mNextGrowthDrawable = getGrowthDrawable(nextGrowth);
    }

    private Drawable getGrowthDrawable(int growth) {
        Drawable drawable = mGrowthDrawables[growth];
        if (drawable == null) {
            final Resources res = getResources();
            switch (growth) {
                case 0:
                    drawable = res.getDrawable(R.drawable.ic_user_growth_0);
                    break;
                case 1:
                    drawable = res.getDrawable(R.drawable.ic_user_growth_1);
                    break;
                case 2:
                    drawable = res.getDrawable(R.drawable.ic_user_growth_2);
                    break;
                case 3:
                    drawable = res.getDrawable(R.drawable.ic_user_growth_3);
                    break;
                case 4:
                    drawable = res.getDrawable(R.drawable.ic_user_growth_4);
                    break;
                case 5:
                    drawable = res.getDrawable(R.drawable.ic_user_growth_5);
                    break;
                case 6:
                    drawable = res.getDrawable(R.drawable.ic_user_growth_6);
                    break;
                default:
                    break;
            }
        }

        return drawable;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mAdapter == null) {
            setMeasuredDimension(0, 0);
            return;
        }
        final int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);

        float height = 0;
        final int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightSpecMode == MeasureSpec.EXACTLY) {
            height = MeasureSpec.getSize(heightMeasureSpec);
        } else {
            height += getPaddingTop();

            height += getGrowthDrawableHeight();

            final int outIndicatorHeight = mIndicatorDrawable.getIntrinsicHeight() - mProgressDrawablePadding;
            if (outIndicatorHeight > 0) {
                height += outIndicatorHeight;
            }
            height += mIndicatorPadding;

            Paint.FontMetrics metrics = mPaintText.getFontMetrics();
            height += (metrics.descent - metrics.ascent);

            height += getPaddingBottom();
        }

        final int measuredHeight = (int) height;
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mAdapter == null) {
            return;
        }

        final int paddingLeft = getPaddingLeft();
        final int paddingTop = getPaddingTop();
        final int paddingRight = getPaddingRight();
        final int measuredWidth = getMeasuredWidth();

        // draw growth drawables
        Bitmap preGrowthBitmap = ((BitmapDrawable) mPreGrowthDrawable).getBitmap();
        Bitmap nextGrowthBitmap = ((BitmapDrawable) mNextGrowthDrawable).getBitmap();

        canvas.drawBitmap(preGrowthBitmap, paddingLeft, paddingTop, null);
        canvas.drawBitmap(nextGrowthBitmap, measuredWidth - paddingRight - mNextGrowthDrawable.getIntrinsicWidth(), paddingTop, null);

        // draw progress drawables
        int progressLeft = paddingLeft + mPreGrowthDrawable.getIntrinsicWidth() + mDrawablePadding;
        int progressRight = measuredWidth - paddingRight - mNextGrowthDrawable.getIntrinsicWidth() - mDrawablePadding;
        int progressBottom = paddingTop + getGrowthDrawableHeight() - mProgressDrawablePadding;
        int progressTop = progressBottom - getProgressDrawableHeight();
        mProgressDrawable.setBounds(progressLeft, progressTop, progressRight, progressBottom);
        mProgressDrawable.draw(canvas);

        final int secondaryProgressRight = progressLeft + (int) (secondProgressRatio() * (progressRight - progressLeft));
        mSecondaryProgressDrawable.setBounds(progressLeft, progressTop, secondaryProgressRight, progressBottom);
        mSecondaryProgressDrawable.draw(canvas);

        // draw indicators
        float indicatorStep = (progressRight - progressLeft) / ((mIndicatorCount + 1) * 1.0f);
        final int indicatorWidth = mIndicatorDrawable.getIntrinsicWidth();
        final int indicatorHeight = mIndicatorDrawable.getIntrinsicHeight();

        for (int i = 1; i <= mIndicatorCount; i++) {
            int left = (int) (progressLeft + i * indicatorStep);
            int top = progressBottom;
            int right = left + indicatorWidth;
            int bottom = top + indicatorHeight;
            Rect bounds = new Rect(left, top, right, bottom);
            mIndicatorDrawable.setBounds(bounds);
            mIndicatorDrawable.draw(canvas);
        }

        //calculate text baseline
        float ascent = mPaintText.ascent();
        final int outIndicatorHeight = mIndicatorDrawable.getIntrinsicHeight() - mProgressDrawablePadding;
        float baseline = paddingTop + getGrowthDrawableHeight();
        if (outIndicatorHeight > 0) {
            baseline += outIndicatorHeight;
        }
        baseline += mIndicatorPadding;
        baseline -= ascent;

        // draw text
        canvas.drawText(String.valueOf(mGrowthValues[0]), paddingLeft, baseline, mPaintText);

        final String nextGrowthValue = String.valueOf(mGrowthValues[mIndicatorCount + 1]);
        final float nextGrowthValueWidth = mPaintText.measureText(nextGrowthValue);
        canvas.drawText(nextGrowthValue, measuredWidth - paddingRight - nextGrowthValueWidth, baseline, mPaintText);

        for (int i = 1; i <= mIndicatorCount; i++) {
            final String growthValue = String.valueOf(mGrowthValues[i]);
            final float growthValueWidth = mPaintText.measureText(growthValue);
            float x = progressLeft + i * indicatorStep - growthValueWidth / 2;
            canvas.drawText(growthValue, x, baseline, mPaintText);
        }
    }

    private int getProgressDrawableHeight() {
        int progressDrawableHeight = mProgressDrawable.getIntrinsicHeight();
        int secondaryProgressDrawableHeight = mSecondaryProgressDrawable.getIntrinsicHeight();
        return Math.max(progressDrawableHeight, secondaryProgressDrawableHeight);
    }

    private int getGrowthDrawableHeight() {
        int leftGrowthDrawableHeight = mPreGrowthDrawable.getIntrinsicHeight();
        int rightGrowthDrawableHeight = mNextGrowthDrawable.getIntrinsicHeight();
        return Math.max(leftGrowthDrawableHeight, rightGrowthDrawableHeight);
    }

    private float secondProgressRatio() {
        int all = mAdapter.getNextGrowthValue() - mAdapter.getPreGrowthValue();
        int cur = mAdapter.getCurrentGrowthValue() - mAdapter.getPreGrowthValue();
        return cur * 1.0f / all;
    }
}
