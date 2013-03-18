package com.meituan.android.widget.menu;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import com.meituan.android.widget.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dingzhihu
 * Date: 13-3-14
 * Time: 下午3:39
 */
public class MenuView extends View {

    private MenuAdapter mAdapter;
    private Paint mPaintTitle;
    private Paint mPaintContent;
    private float mSepPadding;
    private float mTitlePadding;
    private float mContentPadding;
    private float mPricePadding;

    public MenuView(Context context) {
        this(context, null);
    }

    public MenuView(Context context, AttributeSet attrs) {
        this(context, attrs, R.styleable.MTWidget_mtMenuViewStyle);

    }

    public MenuView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        final Resources res = context.getResources();
        final float defaultTextSize = res.getDimension(R.dimen.default_menu_view_text_size);
        final int defaultTitleColor = res.getColor(R.color.default_menu_view_title_color);
        final int defaultContentColor = res.getColor(R.color.default_menu_view_content_color);
        final float defaultSepPadding = res.getDimension(R.dimen.default_menu_view_sep_padding);
        final float defaultTitlePadding = res.getDimension(R.dimen.default_menu_view_title_padding);
        final float defaultContentPadding = res.getDimension(R.dimen.default_menu_view_content_padding);
        final float defaultPricePadding = res.getDimension(R.dimen.default_menu_view_price_padding);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MenuView, defStyle, 0);

        mSepPadding = a.getDimension(R.styleable.MenuView_sepPadding, defaultSepPadding);
        mTitlePadding = a.getDimension(R.styleable.MenuView_titlePadding, defaultTitlePadding);
        mContentPadding = a.getDimension(R.styleable.MenuView_contentPadding, defaultContentPadding);
        mPricePadding = a.getDimension(R.styleable.MenuView_pricePadding, defaultPricePadding);

        mPaintTitle = new Paint();
        mPaintTitle.setAntiAlias(true);
        mPaintTitle.setTextSize(a.getDimension(R.styleable.MenuView_titleSize, defaultTextSize));
        mPaintTitle.setColor(a.getColor(R.styleable.MenuView_titleColor, defaultTitleColor));

        mPaintContent = new Paint();
        mPaintContent.setAntiAlias(true);
        mPaintContent.setTextSize(a.getDimension(R.styleable.MenuView_contentSize, defaultTextSize));
        mPaintContent.setColor(a.getColor(R.styleable.MenuView_contentColor, defaultContentColor));

        a.recycle();
    }

    public void setAdapter(MenuAdapter adapter) {
        if (adapter == null) {
            return;
        }
        mAdapter = adapter;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mAdapter == null) {
            setMeasuredDimension(0, 0);
            return;
        }
        final int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);

        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();
        final int paddingTop = getPaddingTop();
        final int paddingBottom = getPaddingBottom();

        float height = 0;
        height += paddingTop;
        for (int i = 0; i < mAdapter.getCount(); i++) {
            MenuItem item = mAdapter.getMenuItem(i);
            if (item instanceof SepMenuItem) {
                height += mSepPadding;
            } else if (item instanceof TitleMenuItem) {
                TitleMenuItem titleMenuItem = (TitleMenuItem) item;
                height += measureTexts(titleMenuItem.getTitle(), measuredWidth - paddingLeft - paddingRight, mPaintTitle);

                if (i == 0 || i == mAdapter.getCount() - 1) {
                    height += mTitlePadding;
                } else {
                    height += 2 * mTitlePadding;
                }
            } else if (item instanceof ContentMenuItem) {
                ContentMenuItem contentMenuItem = (ContentMenuItem) item;
                final float priceWidth = mPaintContent.measureText(contentMenuItem.getPrice());
                height += measureTexts(contentMenuItem.getContent(), measuredWidth - paddingLeft - mPricePadding - priceWidth - paddingRight, mPaintContent);
                if (i == 0 || i == mAdapter.getCount() - 1) {
                    height += mContentPadding;
                } else {
                    height += 2 * mContentPadding;
                }
            }
        }
        height += paddingBottom;
        final int measuredHeight = (int) height;

        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mAdapter == null) {
            return;
        }
        final int measuredWidth = getMeasuredWidth();
        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();

        float height = getPaddingTop();
        for (int i = 0; i < mAdapter.getCount(); i++) {
            MenuItem item = mAdapter.getMenuItem(i);
            if (item instanceof SepMenuItem) {
                height += mSepPadding;
            } else if (item instanceof TitleMenuItem) {
                TitleMenuItem titleMenuItem = (TitleMenuItem) item;
                final String title = titleMenuItem.getTitle();
                final List<String> texts = breakText(title, measuredWidth - paddingLeft - paddingRight, mPaintTitle);
                float y = 0;
                if (i > 0) {
                    y += mTitlePadding;
                }
                for (String text : texts) {
                    canvas.drawText(text, paddingLeft, height - mPaintTitle.ascent() + y, mPaintTitle);
                    y += (mPaintTitle.descent() - mPaintTitle.ascent());
                }
                height += y;
                if (i < mAdapter.getCount() - 1) {
                    height += mTitlePadding;
                }

            } else if (item instanceof ContentMenuItem) {
                ContentMenuItem contentMenuItem = (ContentMenuItem) item;
                final String content = contentMenuItem.getContent();
                final String price = contentMenuItem.getPrice();
                final float priceWidth = mPaintContent.measureText(price);
                final List<String> texts = breakText(content, measuredWidth - paddingLeft - mPricePadding - priceWidth - paddingRight, mPaintContent);
                canvas.drawText(price, measuredWidth - paddingLeft - priceWidth, height - mPaintContent.ascent(), mPaintContent);
                float y = 0;
                if (i > 0) {
                    y += mContentPadding;
                }
                for (String text : texts) {
                    canvas.drawText(text, paddingLeft, height - mPaintContent.ascent() + y, mPaintContent);
                    y += (mPaintContent.descent() - mPaintContent.ascent());
                }
                height += y;
                if (i < mAdapter.getCount() - 1) {
                    height += mContentPadding;
                }
            }
        }
    }

    private float measureTexts(String text, float maxWidth, Paint paint) {
        List<String> texts = breakText(text, maxWidth, paint);
        final float height = paint.descent() - paint.ascent();
        return height * texts.size();
    }

    private List<String> breakText(String text, float maxWidth, Paint paint) {
        List<String> texts = new ArrayList<String>();
        final int end = text.length();
        int next = 0;
        float[] measuredWidth = {0};
        while (next < end) {
            int breakPosition = paint.breakText(text, next, end, true, maxWidth,
                    measuredWidth);
            int carriageReturn = 0;
            carriageReturn = text.substring(next, next + breakPosition).indexOf('\n');
            if (carriageReturn <= 0) {
                texts.add(text.substring(next, next + breakPosition));
                next += breakPosition;
            } else {
                texts.add(text.substring(next, next + carriageReturn));
                next += carriageReturn + 1;
            }
        }
        if (texts.size() > 1) {
            String last = texts.get(texts.size() - 1);
            if ("）".equals(last) || ")".equals(last)) {
                texts.remove(texts.size() - 1);
                String lastButOne = texts.remove(texts.size() - 1);
                String newLine = lastButOne + last;
                texts.add(newLine);
            }
        }
        return texts;
    }
}
