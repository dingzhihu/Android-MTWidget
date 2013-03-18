package com.meituan.android.widget.sample.alphabet;

import android.app.Activity;
import android.os.Bundle;
import com.meituan.android.widget.AlphabetView;
import com.meituan.android.widget.sample.R;

/**
 * Created with IntelliJ IDEA.
 * User: dingzhihu
 * Date: 13-3-18
 * Time: 上午10:36
 */
public class SampleAlphabetDefault extends Activity implements AlphabetView.OnAlphabetChangeListener {
    private AlphabetView mAlphabetView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_alphabet);
        mAlphabetView = (AlphabetView) findViewById(R.id.alphabet);
        mAlphabetView.setOnAlphabetChangeListener(this);

        final String[] src = "#$ABCDEFGHIJKLMNOPQRSTUVWXYZ".split("");
        final String[] chars = new String[src.length - 1];
        System.arraycopy(src, 1, chars, 0, src.length - 1);
        mAlphabetView.setCharacters(chars);
    }

    @Override
    public void onAlphabetChange(String character) {
        if (character != null) {
            System.out.println(character);
        }
    }
}
