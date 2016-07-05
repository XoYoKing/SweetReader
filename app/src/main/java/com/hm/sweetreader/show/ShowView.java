package com.hm.sweetreader.show;

import android.app.Activity;
import android.content.Context;
import android.text.Layout;
import android.util.AttributeSet;
import android.widget.TextView;

import com.hm.sweetreader.ScreenUtils;

/**
 * project：SweetReader
 * author： FLY
 * date：   2016/6/15
 * time：   15:40
 * purpose：
 */
public class ShowView extends TextView {

    private Context context;

    public ShowView(Context context) {
        super(context);
        this.context = context;
    }

    public ShowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public ShowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        resize();
    }

    /**
     * 去除当前页无法显示的字
     *
     * @return 去掉的字数
     */
    public int resize() {
        CharSequence oldContent = getText();
        CharSequence newContent = oldContent.subSequence(0, getCharNum());
        setText(newContent);
        return oldContent.length() - newContent.length();
    }

    /**
     * 获取当前页总字数
     */
    public int getCharNum() {
        return getLayout().getLineEnd(getLineNum());
    }

    /**
     * 获取当前页总行数
     */
    public int getLineNum() {
        Layout layout = getLayout();
        int topOfLastLine = getHeight() - getPaddingTop() - getPaddingBottom() - getLineHeight();
        return layout.getLineForVertical(topOfLastLine);
    }

    public int getCharHeighPX() {
        return ScreenUtils.dip2px(context, getTextSize());
    }

    public int getLineTotal(Activity act) {
        return ScreenUtils.screenHeigh(act) / getCharHeighPX();
    }
}
