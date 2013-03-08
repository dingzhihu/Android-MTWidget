package com.meituan.widget.sample.usergrowth;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import com.meituan.widget.UserGrowthView;

/**
 * Created with IntelliJ IDEA.
 * User: dingzhihu
 * Date: 13-3-6
 * Time: 下午2:26
 */
public class SampleUserGrowthDefault extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UserGrowthView view = new UserGrowthView(this);
        view.setAdapter(new SampleUserGrowthAdapter());

        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(view, lp);

    }

}
