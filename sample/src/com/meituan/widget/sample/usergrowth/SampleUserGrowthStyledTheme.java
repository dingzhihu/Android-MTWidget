package com.meituan.widget.sample.usergrowth;

import android.app.Activity;
import android.os.Bundle;
import com.meituan.widget.UserGrowthView;
import com.meituan.widget.sample.R;

/**
 * Created with IntelliJ IDEA.
 * User: dingzhihu
 * Date: 13-3-8
 * Time: 上午11:55
 */
public class SampleUserGrowthStyledTheme extends Activity {
    private UserGrowthView mUserGrowthView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_user_growth);

        mUserGrowthView = (UserGrowthView) findViewById(R.id.user_growth);
        mUserGrowthView.setAdapter(new SampleUserGrowthAdapter());

    }
}
