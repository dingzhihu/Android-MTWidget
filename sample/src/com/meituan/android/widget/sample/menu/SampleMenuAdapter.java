package com.meituan.android.widget.sample.menu;

import android.content.Context;
import com.meituan.android.widget.menu.MenuAdapter;
import com.meituan.android.widget.menu.MenuItem;
import roboguice.util.Ln;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dingzhihu
 * Date: 13-3-14
 * Time: 下午5:09
 */
public class SampleMenuAdapter extends MenuAdapter {
    private List<MenuItem> mData;

    public SampleMenuAdapter(Context context) {
        this(context, "sample_menu.json");
    }

    public SampleMenuAdapter(Context context, String jsonFile) {
        InputStream in;
        try {
            in = context.getResources().getAssets().open(jsonFile);
            mData = MenuUtils.pareMenu(in);

        } catch (IOException e) {
            mData = null;
            Ln.e(e);
        }

    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public MenuItem getMenuItem(int i) {
        return mData.get(i);
    }
}
