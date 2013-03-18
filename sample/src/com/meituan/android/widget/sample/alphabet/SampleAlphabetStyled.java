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
public class SampleAlphabetStyled extends Activity {
    private static final String[] ALPHABET = {"#","$","A","B","C","d","e","f","安","卓"};
    private AlphabetView mAlphabetView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.styled_alphabet);
        mAlphabetView = (AlphabetView) findViewById(R.id.alphabet);
        mAlphabetView.setCharacters(ALPHABET);

    }
}
