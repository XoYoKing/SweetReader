package com.hm.sweetreader.show;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hm.sweetreader.DayNightHelper;
import com.hm.sweetreader.ScreenUtils;

/**
 * project：SweetReader
 * author： FLY
 * date：   2016/6/15
 * time：   15:40
 * purpose：
 */
public class ShowView extends ScrollView {

    private Context context;
    private TextView textView;

    public ShowView(Context context) {
        this(context,null);
    }

    public ShowView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ShowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    private void init(){
        textView=new TextView(context);
        if (DayNightHelper.newInstance(context).isDay()){
            textView.setTextColor(Color.BLACK);
            textView.setBackgroundColor(Color.WHITE);
            this.setBackgroundColor(Color.WHITE);
        }else {
            textView.setTextColor(Color.rgb(138,149,153));
            textView.setBackgroundColor(Color.rgb(63,63,63));
            this.setBackgroundColor(Color.rgb(63,63,63));
        }
        LayoutParams params=new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.addView(textView,params);

    }
    public void setText(String msg){
        if (textView==null){
            return;
        }
        textView.setText(msg);
    }
    public void setTextSize(int size){
        if (textView==null){
            return;
        }
        textView.setTextSize(size);
    }

}
