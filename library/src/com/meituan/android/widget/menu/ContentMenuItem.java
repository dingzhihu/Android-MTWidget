package com.meituan.android.widget.menu;

/**
 * Created with IntelliJ IDEA.
 * User: dingzhihu
 * Date: 13-3-14
 * Time: 下午5:06
 */
public class ContentMenuItem implements MenuItem{
    private String content;

    private String price;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
