package com.meituan.android.widget.sample.menu;

import android.app.Activity;
import android.os.Bundle;
import com.meituan.android.widget.menu.MenuView;
import com.meituan.android.widget.sample.R;

/**
 * Created with IntelliJ IDEA.
 * User: dingzhihu
 * Date: 13-3-14
 * Time: 下午4:18
 */
public class SampleMenu extends Activity {
    private MenuView mMenuView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        mMenuView = (MenuView) findViewById(R.id.menu);
        mMenuView.setAdapter(new SampleMenuAdapter(this));
//        mMenuView.setAdapter(new SampleMenuAdapter(this, "sample_menu_simple.json"));
    }
}
