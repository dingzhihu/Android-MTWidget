package com.meituan.widget.sample.usergrowth;

import com.meituan.widget.UserGrowthAdapter;

/**
 * Created with IntelliJ IDEA.
 * User: dingzhihu
 * Date: 13-3-8
 * Time: 上午11:04
 */
public class SampleUserGrowthAdapter extends UserGrowthAdapter {
    @Override
    public int getCurrentGrowth() {
        return 3;
    }

    @Override
    public int getPreGrowthValue() {
        return 3000;
    }

    @Override
    public int getCurrentGrowthValue() {
        return 3800;
    }

    @Override
    public int getNextGrowthValue() {
        return 10000;
    }
}
