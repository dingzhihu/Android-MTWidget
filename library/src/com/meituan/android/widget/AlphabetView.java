package com.meituan.android.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created with IntelliJ IDEA.
 * User: dingzhihu
 * Date: 13-3-18
 * Time: 上午10:23
 */
public class AlphabetView extends View {

    private String[] mChars;
    private String mLastCharacter;
    private boolean mLastCharacterChanged;

    private OnAlphabetChangeListener mListener;

    private Paint mPaintText;
    private Paint mPaintPath;
    private float mAlphabetPadding;
    private boolean mIsTouching;

    public AlphabetView(Context context) {
        this(context, null);
    }

    public AlphabetView(Context context, AttributeSet attrs) {
        this(context, attrs, R.styleable.MTWidget_mtAlphabetViewStyle);
    }

    public AlphabetView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        final Resources res = context.getResources();
        final float defaultAlphabetPadding = res.getDimension(R.dimen.default_alphabet_view_alphabet_padding);
        final float defaultTextSize = res.getDimension(R.dimen.default_alphabet_view_text_size);
        final int defaultTextColor = res.getColor(R.color.default_alphabet_view_text_color);
        final int defaultPathColor = res.getColor(R.color.default_alphabet_view_path_color);
        final float defaultPathRadius = res.getDimension(R.dimen.default_alphabet_view_path_radius);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AlphabetView, defStyle, 0);
        mAlphabetPadding = a.getDimension(R.styleable.AlphabetView_alphabetPadding, defaultAlphabetPadding);

        mPaintText = new Paint();
        mPaintText.setAntiAlias(true);
        mPaintText.setTypeface(Typeface.DEFAULT_BOLD);
        mPaintText.setTextSize(a.getDimension(R.styleable.AlphabetView_android_textSize, defaultTextSize));
        mPaintText.setColor(a.getColor(R.styleable.AlphabetView_android_textColor, defaultTextColor));

        mPaintPath = new Paint();
        mPaintPath.setAntiAlias(true);
        mPaintPath.setColor(a.getColor(R.styleable.AlphabetView_pathColor, defaultPathColor));
        final float pathRadius = a.getDimension(R.styleable.AlphabetView_pathRadius, defaultPathRadius);
        mPaintPath.setPathEffect(new CornerPathEffect(pathRadius));

        a.recycle();
    }

    public void setCharacters(String[] chars) {
        mChars = chars;
        requestLayout();
    }

    public void setOnAlphabetChangeListener(OnAlphabetChangeListener listener) {
        mListener = listener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mChars == null || mChars.length == 0) {
            setMeasuredDimension(0, 0);
            return;
        }
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int widthMeasureSpec) {
        float result = 0;
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = getPaddingLeft() + getPaddingRight() + maxCharWidth();
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return (int) FloatMath.ceil(result);
    }

    private int measureHeight(int heightMeasureSpec) {
        float result = 0;
        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        int specSize = MeasureSpec.getSize(heightMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = getPaddingTop() + getPaddingBottom();
            final float charHeight = mPaintText.descent() - mPaintText.ascent();
            final float alphabetHeight = mChars.length * charHeight + (mChars.length - 1) * mAlphabetPadding;
            result += alphabetHeight;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return (int) FloatMath.ceil(result);
    }

    private float maxCharWidth() {
        float maxWidth = -1;
        for (String chr : mChars) {
            float width = mPaintText.measureText(chr);
            if (width > maxWidth) {
                maxWidth = width;
            }
        }
        return maxWidth;
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (mChars == null) {
            return;
        }
        // draw path
        if (mIsTouching) {
            canvas.drawPath(makePath(), mPaintPath);
        }

        //draw alphabet
        final float paddingLeft = getPaddingLeft();
        final float centerX = paddingLeft + (getMeasuredWidth() - paddingLeft - getPaddingRight()) / 2.0f;
        float y = getPaddingTop() - mPaintText.ascent();

        for (String chr : mChars) {
            float charWidth = mPaintText.measureText(chr);
            float x = centerX - charWidth / 2;
            canvas.drawText(chr, x, y, mPaintText);

            y += (mPaintText.descent() - mPaintText.ascent());
            y += mAlphabetPadding;
        }
    }

    private Path makePath() {
        final int w = getMeasuredWidth();
        final int h = getMeasuredHeight();

        Path p = new Path();
        p.moveTo(0, 0);
        p.lineTo(w, 0);
        p.lineTo(w, h);
        p.lineTo(0, h);
        p.lineTo(0, 0);
        p.lineTo(w, 0);

        return p;
    }

    private String getCharacter(float y) {
        final float height = (mPaintText.descent() - mPaintText.ascent()) + mAlphabetPadding;
        int position = (int) ((y - getPaddingTop()) / height);
        if (position >= 0 && position < mChars.length) {
            return mChars[position];
        }
        return null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mIsTouching = true;
                mLastCharacterChanged = true;
                mLastCharacter = getCharacter(y);
                break;
            case MotionEvent.ACTION_MOVE:
                String character = getCharacter(y);
                if (mLastCharacter != character) {
                    mLastCharacterChanged = true;
                    mLastCharacter = character;
                }else {
                    mLastCharacterChanged = false;
                }

                break;
            case MotionEvent.ACTION_UP:
                mIsTouching = false;
                mLastCharacterChanged = true;
                mLastCharacter = null;
                break;
        }
        if (mLastCharacterChanged && mListener != null) {
            mListener.onAlphabetChange(mLastCharacter);
        }

        invalidate();
        return true;
    }

    public interface OnAlphabetChangeListener {
        void onAlphabetChange(String character);
    }

}
