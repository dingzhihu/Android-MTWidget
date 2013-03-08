package com.meituan.widget.sample.usergrowth;

import android.app.Activity;
import android.os.Bundle;
import com.meituan.widget.UserGrowthView;
import com.meituan.widget.sample.R;

/**
 * Created with IntelliJ IDEA.
 * User: dingzhihu
 * Date: 13-3-8
 * Time: 上午11:07
 */
public class SampleUserGrowthStyledLayout extends Activity {

    private UserGrowthView mUserGrowthView;

    private UserGrowthView mUserGrowthViewAnother;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.styled_user_growth);
        mUserGrowthView = (UserGrowthView) findViewById(R.id.user_growth);
        mUserGrowthViewAnother = (UserGrowthView) findViewById(R.id.user_growth_another);
        SampleUserGrowthAdapter adapter = new SampleUserGrowthAdapter();

        mUserGrowthView.setAdapter(adapter);
        mUserGrowthViewAnother.setAdapter(adapter);

    }
}
