package com.meituan.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
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

    private Drawable mLeftGrowthDrawable;
    private Drawable mRightGrowthDrawable;
    private Drawable[] mGrowthDrawables;
    private NinePatchDrawable mProgressDrawable;
    private NinePatchDrawable mSecondaryProgressDrawable;
    private int mDrawablePadding;
    private int mProgressDrawablePaddingBottom;

    private UserGrowthAdapter mAdapter;

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
        final float defaultProgressDrawablePaddingBottom = res.getDimension(R.dimen.default_user_growth_view_progress_drawable_padding_bottom);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.UserGrowthView, defStyle, 0);
        mGrowthDrawables = new Drawable[MAX_GROWTH + 1];
        mGrowthDrawables[0] = a.getDrawable(R.styleable.UserGrowthView_user_growth_drawable_0);
        mGrowthDrawables[1] = a.getDrawable(R.styleable.UserGrowthView_user_growth_drawable_1);
        mGrowthDrawables[2] = a.getDrawable(R.styleable.UserGrowthView_user_growth_drawable_2);
        mGrowthDrawables[3] = a.getDrawable(R.styleable.UserGrowthView_user_growth_drawable_3);
        mGrowthDrawables[4] = a.getDrawable(R.styleable.UserGrowthView_user_growth_drawable_4);
        mGrowthDrawables[5] = a.getDrawable(R.styleable.UserGrowthView_user_growth_drawable_5);
        mGrowthDrawables[6] = a.getDrawable(R.styleable.UserGrowthView_user_growth_drawable_6);

        Drawable drawable = a.getDrawable(R.styleable.UserGrowthView_user_growth_progress_drawable);
        mProgressDrawable = (NinePatchDrawable) (drawable == null ? res.getDrawable(R.drawable.user_growth_progress) :
                drawable);
        drawable = a.getDrawable(R.styleable.UserGrowthView_user_growth_secondary_progress_drawable);
        mSecondaryProgressDrawable = (NinePatchDrawable) (drawable == null ? res.getDrawable(R.drawable.user_growth_secondary_progress) :
                drawable);

        mDrawablePadding = (int)a.getDimension(R.styleable.UserGrowthView_android_drawablePadding, defaultDrawablePadding);
        mProgressDrawablePaddingBottom = (int)a.getDimension(R.styleable.UserGrowthView_user_growth_progress_drawable_padding_bottom,
                defaultProgressDrawablePaddingBottom);


    }

    public void setAdapter(UserGrowthAdapter adapter) {
        if (adapter == null) {
            return;
        }
        int currentGrowth = adapter.getCurrentGrowth();
        if (currentGrowth < 0 || currentGrowth > MAX_GROWTH) {
            throw new IllegalStateException(String.format("current growth %d not accepted", currentGrowth));
        }

        int curGrowthValue = adapter.getCurrentGrowthValue();
        int preGrowthValue = adapter.getPreGrowthValue();
        int nextGrowthValue = adapter.getNextGrowthValue();
        if (curGrowthValue < preGrowthValue || curGrowthValue > nextGrowthValue || preGrowthValue == nextGrowthValue) {
            throw new IllegalStateException(String.format("curGrowthValue:%d,preGrowthValue:%s,nextGrowthValue:%d,not accepted",
                    curGrowthValue, preGrowthValue, nextGrowthValue));
        }
        mAdapter = adapter;

        calculateGrowthDrawables();

        requestLayout();
        invalidate();
    }

    private void calculateGrowthDrawables() {
        final int currentGrowth = mAdapter.getCurrentGrowth();
        int leftGrowth, rightGrowth;
        if (currentGrowth < MAX_GROWTH) {
            leftGrowth = currentGrowth;
            rightGrowth = currentGrowth + 1;
        } else {
            leftGrowth = currentGrowth - 1;
            rightGrowth = currentGrowth;
        }
        mLeftGrowthDrawable = getGrowthDrawable(leftGrowth);
        mRightGrowthDrawable = getGrowthDrawable(rightGrowth);
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
        float height;
        final int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightSpecMode == MeasureSpec.EXACTLY) {
            height = MeasureSpec.getSize(heightMeasureSpec);
        } else {
            height = getPaddingTop() + getGrowthDrawableHeight() + getPaddingBottom();
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

        Bitmap leftGrowthBitmap = ((BitmapDrawable) mLeftGrowthDrawable).getBitmap();
        Bitmap rightGrowthBitmap = ((BitmapDrawable) mRightGrowthDrawable).getBitmap();

        canvas.drawBitmap(leftGrowthBitmap, paddingLeft, paddingTop, null);
        canvas.drawBitmap(rightGrowthBitmap, measuredWidth - paddingRight - mRightGrowthDrawable.getIntrinsicWidth(), paddingTop, null);

        int progressLeft = paddingLeft + mLeftGrowthDrawable.getIntrinsicWidth() + mDrawablePadding;
        int progressRight = measuredWidth - paddingRight - mRightGrowthDrawable.getIntrinsicWidth() - mDrawablePadding;
        int progressBottom = paddingTop + getGrowthDrawableHeight() - mProgressDrawablePaddingBottom;
        int progressTop = progressBottom - getProgressDrawableHeight();
        mProgressDrawable.setBounds(progressLeft, progressTop, progressRight, progressBottom);
        mProgressDrawable.draw(canvas);

        final int secondaryProgressRight = progressLeft + (int)(secondProgressRatio() * (progressRight - progressLeft));
        mSecondaryProgressDrawable.setBounds(progressLeft, progressTop, secondaryProgressRight, progressBottom);
        mSecondaryProgressDrawable.draw(canvas);


    }

    private int getProgressDrawableHeight() {
        int progressDrawableHeight = mProgressDrawable.getIntrinsicHeight();
        int secondaryProgressDrawableHeight = mSecondaryProgressDrawable.getIntrinsicHeight();
        return Math.max(progressDrawableHeight, secondaryProgressDrawableHeight);
    }

    private int getGrowthDrawableHeight() {
        int leftGrowthDrawableHeight = mLeftGrowthDrawable.getIntrinsicHeight();
        int rightGrowthDrawableHeight = mRightGrowthDrawable.getIntrinsicHeight();
        return Math.max(leftGrowthDrawableHeight, rightGrowthDrawableHeight);
    }


    private float secondProgressRatio() {
        int all = mAdapter.getNextGrowthValue() - mAdapter.getPreGrowthValue();
        int current = mAdapter.getCurrentGrowthValue() - mAdapter.getPreGrowthValue();
        return current * 1.0f / all;
    }
}
