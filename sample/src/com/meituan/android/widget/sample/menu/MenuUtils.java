package com.meituan.android.widget.sample.menu;

import com.meituan.android.widget.menu.ContentMenuItem;
import com.meituan.android.widget.menu.MenuItem;
import com.meituan.android.widget.menu.SepMenuItem;
import com.meituan.android.widget.menu.TitleMenuItem;
import org.json.JSONArray;
import org.json.JSONObject;
import roboguice.util.Ln;
import roboguice.util.Strings;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dingzhihu
 * Date: 13-3-14
 * Time: 下午6:39
 */
final public class MenuUtils {
    public static List<MenuItem> pareMenu(InputStream in) {
        List<MenuItem> data;

        try {
            data = new ArrayList<MenuItem>();
            String source = Strings.toString(in);
            JSONArray parent = new JSONArray(source);
            for (int i = 0; i < parent.length(); i++) {
                JSONArray child = parent.getJSONArray(i);

                for (int j = 0; j < child.length(); j++) {
                    JSONObject obj = child.getJSONObject(j);
                    String type = obj.getString("type");
                    if ("0".equals(type)) {
                        String title = obj.getString("content");
                        TitleMenuItem item = new TitleMenuItem();
                        item.setTitle(title);
                        data.add(item);
                    } else {
                        String content = obj.getString("content");
                        String spec = obj.getString("specification");
                        String price = obj.getString("price");
                        ContentMenuItem item = new ContentMenuItem();
                        item.setContent(content + "（" + spec + "）");
                        item.setPrice(price+"元");
                        data.add(item);
                    }

                }

                if (i != parent.length() - 1) {
                    MenuItem item = new SepMenuItem();
                    data.add(item);
                }

            }
        } catch (Exception e) {
            data = null;
            Ln.e(e);
        }
        return data;


    }
}
