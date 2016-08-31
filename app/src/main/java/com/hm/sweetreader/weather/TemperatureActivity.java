package com.hm.sweetreader.weather;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hm.sweetreader.R;
import com.hm.sweetreader.ThemeUtils;
import com.hm.sweetreader.weather.view.WeatherBrokenLineView;

/**
 * 气温折线图主界面
 */
public class TemperatureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtils.initTheme(this);
        setContentView(R.layout.activity_test);

        WeatherBrokenLineView myView=(WeatherBrokenLineView) findViewById(R.id.my_view);

        myView.setData(getIntent().getIntegerArrayListExtra("high"),getIntent().getIntegerArrayListExtra("low"),getIntent().getStringArrayListExtra("day"));
    }
}
