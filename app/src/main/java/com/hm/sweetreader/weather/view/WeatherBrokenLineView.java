package com.hm.sweetreader.weather.view;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.hm.sweetreader.R;

import java.util.ArrayList;
import java.util.List;

/**
 * project：SweetReader
 * author： FLY
 * date：   2016/6/29
 * time：   9:55
 * purpose：气温折线图界面
 */
public class WeatherBrokenLineView extends View {

    private String TAG = WeatherBrokenLineView.class.getSimpleName();

    private Paint paintHigh, paintLow, paintHighText, paintLowText, paintDayText;
    private int width, height;
    private List<Integer> first, second;
    private List<String> day, weather;

    public WeatherBrokenLineView(Context context) {
        super(context);
    }

    public WeatherBrokenLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WeatherBrokenLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setData(List<Integer> high, List<Integer> low, List<String> days) {
        first = high;
        second = low;
        day = days;
        weather = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            weather.add("");
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        /*
        EXACTLY：一般是设置了明确的值或者是MATCH_PARENT
        AT_MOST：表示子布局限制在一个最大值内，一般为WARP_CONTENT
        UNSPECIFIED：表示子布局想要多大就多大，很少使用
         */
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = getWidth();
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = getHeight();
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initPaint();
        drawDayAndWeather(canvas);
        drawLine(canvas);

    }

    private void initPaint() {
        paintHigh = new Paint();
        paintHigh.setStyle(Paint.Style.STROKE);
        paintHigh.setAntiAlias(true);
        paintHigh.setColor(Color.YELLOW);
        paintHigh.setStrokeWidth(10);

        paintHighText = new Paint();
        paintHighText.setStyle(Paint.Style.STROKE);
        paintHighText.setAntiAlias(true);
        paintHighText.setColor(Color.BLACK);
        paintHighText.setTextSize(50);

        paintLow = new Paint();
        paintLow.setStyle(Paint.Style.STROKE);
        paintLow.setAntiAlias(true);
        paintLow.setColor(Color.GREEN);
        paintLow.setStrokeWidth(10);

        paintLowText = new Paint();
        paintLowText.setStyle(Paint.Style.STROKE);
        paintLowText.setAntiAlias(true);
        paintLowText.setColor(Color.GREEN);
        paintLowText.setTextSize(50);

        paintDayText = new Paint();
        paintDayText.setStyle(Paint.Style.STROKE);
        paintDayText.setAntiAlias(true);
        paintDayText.setColor(Color.BLACK);
        paintDayText.setTextSize(50);
    }

    private void drawDayAndWeather(Canvas canvas) {
        float widthEvery = 0f;
        widthEvery = width / day.size();
        float day_x = getX();
        float day_y = getY();
        float weather_x = getX()-25;
        float weather_y = getY() + 100;
        for (int i = 0; i < day.size(); i++) {
            canvas.drawText(day.get(i), day_x, day_y, paintDayText);
            day_x = getX() + (i + 1) * widthEvery;
        }

//        Matrix matrix = new Matrix();
        for (int i = 0; i < weather.size(); i++) {
            canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher), weather_x, weather_y, paintLow);
            weather_x = (getX()-25) + (i + 1) * widthEvery;
        }


    }

    private void drawLine(Canvas canvas) {
        float widthEvery = 0;
        if (first.size() > second.size()) {
            widthEvery = width / first.size();
        } else {
            widthEvery = width / second.size();
        }
        //TODO 需要再处理 (根据数值判断是否需要零下温度)
        float heightEvery = (float) (height * 0.7 / 50);
        float tag_heith_x = getX();
        float tag_heith_y = getY() + height - first.get(0) * heightEvery;
        float tag_low_x = getX();
        float tag_low_y = getY() + height - second.get(0) * heightEvery;

        for (int i = 0; i < second.size(); i++) {

            canvas.drawLine(tag_heith_x, tag_heith_y, getX() + i * widthEvery, getY() + height - first.get(i) * heightEvery, paintHigh);
            canvas.drawCircle(getX() + i * widthEvery, getY() + height - first.get(i) * heightEvery, 10, paintHigh);
            canvas.drawText(String.valueOf(first.get(i)), getX() + i * widthEvery - 10, getY() + height - first.get(i) * heightEvery - 20, paintHighText);
            tag_heith_x = (getX() + i * widthEvery);
            tag_heith_y = (getY() + height - first.get(i) * heightEvery);

            canvas.drawLine(tag_low_x, tag_low_y, getX() + i * widthEvery, getY() + height - second.get(i) * heightEvery, paintLow);
            canvas.drawCircle(getX() + i * widthEvery, getY() + height - second.get(i) * heightEvery, 10, paintLow);
            canvas.drawText(String.valueOf(second.get(i)), getX() + i * widthEvery - 10, getY() + height - second.get(i) * heightEvery - 20, paintHighText);
            tag_low_x = getX() + i * widthEvery;
            tag_low_y = getY() + height - second.get(i) * heightEvery;
        }
    }
}
