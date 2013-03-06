package com.meituan.widget.sample;

import android.R;
import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.text.Collator;
import java.util.*;

/**
 * ref: https://github.com/JakeWharton/Android-ViewPagerIndicator/blob/master/sample/src/com/viewpagerindicator/sample/ListSamples.java
 */
public class MainActivity extends ListActivity {

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String path = intent.getStringExtra("com.meituan.widget.sample.Path");

        if (null == path) {
            path = "";
        }

        setListAdapter(new SimpleAdapter(this, getData(path),
                R.layout.simple_list_item_1, new String[]{"title"},
                new int[]{R.id.text1}));

    }

    protected List<Map<String, Object>> getData(String prefix) {
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory("com.meituan.widget.sample.SAMPLE");

        PackageManager pm = getPackageManager();
        List<ResolveInfo> list = pm.queryIntentActivities(mainIntent, 0);

        if (null == list) {
            return data;
        }

        String[] prefixPath;
        String prefixWithSlash = prefix;

        if ("".equals(prefix)) {
            prefixPath = null;
        } else {
            prefixPath = prefix.split("/");
            prefixWithSlash = prefix + "/";
        }

        int len = list.size();

        Map<String, Boolean> entries = new HashMap<String, Boolean>();
        for (int i = 0; i < len; i++) {
            ResolveInfo info = list.get(i);
            CharSequence labelSeq = info.loadLabel(pm);
            String label = labelSeq != null
                    ? labelSeq.toString() : info.activityInfo.name;

            if (prefixWithSlash.length() == 0 || label.startsWith(prefixWithSlash)) {
                String[] labelPath = label.split("/");
                String nextLabel = prefixPath == null ? labelPath[0] : labelPath[prefixPath.length];

                if ((prefixPath != null ? prefixPath.length : 0) == labelPath.length - 1) {
                    addItem(data, nextLabel, activityIntent(
                            info.activityInfo.applicationInfo.packageName,
                            info.activityInfo.name));

                } else {

                    if (entries.get(nextLabel) == null) {
                        addItem(data, nextLabel, browseIntent(prefix.equals("") ? nextLabel : prefix + "/" + nextLabel));
                        entries.put(nextLabel, true);
                    }

                }

            }
        }

        Collections.sort(data, NAME_COMPARATOR);
        return data;
    }

    protected Intent browseIntent(String path) {
        Intent result = new Intent();
        result.setClass(this, MainActivity.class);
        result.putExtra("com.meituan.widget.sample.Path", path);
        return result;
    }

    protected void addItem(List<Map<String, Object>> data, String name, Intent intent) {
        Map<String, Object> temp = new HashMap<String, Object>();
        temp.put("title", name);
        temp.put("intent", intent);
        data.add(temp);
    }

    protected Intent activityIntent(String pkg, String componentName) {
        Intent result = new Intent();
        result.setClassName(pkg, componentName);
        return result;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Map<String, Object> map = (Map<String, Object>)l.getItemAtPosition(position);

        Intent intent = (Intent) map.get("intent");
        startActivity(intent);
    }

    private final static Comparator<Map<String, Object>> NAME_COMPARATOR = new Comparator<Map<String, Object>>() {
        private final Collator collector = Collator.getInstance();

        @Override
        public int compare(Map<String, Object> map1, Map<String, Object> map2) {
            return collector.compare(map1.get("title"), map2.get("title"));
        }
    };
}
