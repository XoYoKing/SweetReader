package com.hm.sweetreader.weather;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hm.sweetreader.Contents;
import com.hm.sweetreader.R;
import com.hm.sweetreader.weather.weather_model.WeatherInfo;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;

/**
 * 天气主界面
 */
public class WeatherActivity extends FragmentActivity implements View.OnClickListener, WeatherFragment.OnFragmentInteractionListener {

    private String TAG = WeatherActivity.class.getSimpleName();
    private ArrayList<String> weatherInfoToday = new ArrayList<>();
    private ArrayList<String> weatherInfoOtherDay;
    private ArrayList<Fragment> fragmentList;
    private ViewPagerAdapter adapter;
    private ViewPager viewPager;
    private TextView threeDay, fourDay;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);

        init();
        getInfo("厦门");

    }

    private void init() {
        progressBar=(ProgressBar)findViewById(R.id.weather_main_progress);
        RelativeLayout tab_one = (RelativeLayout) findViewById(R.id.weather_tab_one);
        RelativeLayout tab_two = (RelativeLayout) findViewById(R.id.weather_tab_two);
        RelativeLayout tab_three = (RelativeLayout) findViewById(R.id.weather_tab_three);
        RelativeLayout tab_four = (RelativeLayout) findViewById(R.id.weather_tab_four);
        tab_one.setOnClickListener(this);
        tab_two.setOnClickListener(this);
        tab_three.setOnClickListener(this);
        tab_four.setOnClickListener(this);
        threeDay = (TextView) findViewById(R.id.weather_tab_text_three);
        fourDay = (TextView) findViewById(R.id.weather_tab_text_four);

        viewPager = (ViewPager) findViewById(R.id.weather_main_viewpager);
    }

    public void getInfo(String cityName) {
        String url = Contents.WeatherBaseUrl + cityName;
        OkHttpUtils
                .get()
                .url(url)
//                .addParams("username", "hyman")
//                .addParams("password", "123")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
//                        Log.e("response ", "response is " + response);
                        try {
                            InputStream in_nocode = new ByteArrayInputStream(response.getBytes());
                            WeatherInfo weatherInfo = WeatherUtil.handleWeatherResponse(in_nocode);
                            Log.e(TAG, "size is " + weatherInfo.getWeatherDaysForecast().size());
                            progressBar.setVisibility(View.GONE);
                            setData(weatherInfo);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    private void setData(WeatherInfo weatherInfo) {

        threeDay.setText(weatherInfo.getWeatherDaysForecast().get(3).getDate().split("日")[1]);
        fourDay.setText(weatherInfo.getWeatherDaysForecast().get(4).getDate().split("日")[1]);

        fragmentList = new ArrayList<>();

        weatherInfoToday.add(weatherInfo.getCity());
        weatherInfoToday.add(weatherInfo.getTemperature());
        weatherInfoToday.add(weatherInfo.getWeatherDaysForecast().get(1).getTypeDay());
        weatherInfoToday.add(weatherInfo.getWeatherDaysForecast().get(1).getHigh());
        weatherInfoToday.add(weatherInfo.getWeatherDaysForecast().get(1).getLow());
        weatherInfoToday.add(weatherInfo.getAQI());
        weatherInfoToday.add(weatherInfo.getHumidity());
        Calendar cal = Calendar.getInstance();
        String date = cal.get(Calendar.MONTH) + 1 + "-" + weatherInfo.getWeatherDaysForecast().get(1).getDate();
        int mouth = cal.get(Calendar.MONTH) + 1;
        weatherInfoToday.add(date);
        weatherInfoToday.add(weatherInfo.getWindDirection());
        weatherInfoToday.add(weatherInfo.getWindPower());
        weatherInfoToday.add(weatherInfo.getUpdateTime());

        fragmentList.add(WeatherFragment.newInstance(weatherInfoToday, getTemperatureHighList(weatherInfo), getTemperatureLowList(weatherInfo),getDaysList(weatherInfo)));

        for (int i = 2; i < 5; i++) {
            weatherInfoOtherDay = new ArrayList<>();
            weatherInfoOtherDay.add(weatherInfo.getCity());
            weatherInfoOtherDay.add("");
            weatherInfoOtherDay.add(weatherInfo.getWeatherDaysForecast().get(i).getTypeDay());
            weatherInfoOtherDay.add(weatherInfo.getWeatherDaysForecast().get(i).getHigh());
            weatherInfoOtherDay.add(weatherInfo.getWeatherDaysForecast().get(i).getLow());
            weatherInfoOtherDay.add("");
            weatherInfoOtherDay.add("");

            int day = Integer.parseInt(weatherInfo.getWeatherDaysForecast().get(i).getDate().split("日")[0]);
            if (day == 1) {
                mouth += 1;

            }
            date = mouth + "-" + weatherInfo.getWeatherDaysForecast().get(i).getDate();
            weatherInfoOtherDay.add(date);

            weatherInfoOtherDay.add(weatherInfo.getWeatherDaysForecast().get(i).getWindDirectionDay());
            weatherInfoOtherDay.add(weatherInfo.getWeatherDaysForecast().get(i).getWindPowerDay());
            weatherInfoOtherDay.add(weatherInfo.getUpdateTime());
            fragmentList.add(WeatherFragment.newInstance(weatherInfoOtherDay, getTemperatureHighList(weatherInfo), getTemperatureLowList(weatherInfo),getDaysList(weatherInfo)));

        }
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);
    }

    private ArrayList<Integer> getTemperatureHighList(WeatherInfo weatherInfo) {
        ArrayList<Integer> listHight = new ArrayList<>();
        for (int i = 1; i < weatherInfo.getWeatherDaysForecast().size(); i++) {
            String s = weatherInfo.getWeatherDaysForecast().get(i).getHigh().substring(3, 5);
            listHight.add(Integer.parseInt(s));
        }
        return listHight;
    }

    private ArrayList<Integer> getTemperatureLowList(WeatherInfo weatherInfo) {
        ArrayList<Integer> listLow = new ArrayList<>();
        for (int i = 1; i < weatherInfo.getWeatherDaysForecast().size(); i++) {
            String s = weatherInfo.getWeatherDaysForecast().get(i).getLow().substring(3, 5);
            listLow.add(Integer.parseInt(s));
        }
        return listLow;
    }

    private ArrayList<String> getDaysList(WeatherInfo weatherInfo) {
        ArrayList<String> listDay = new ArrayList<>();
        int mouth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        listDay.add("今天");
        for (int i = 2; i < weatherInfo.getWeatherDaysForecast().size(); i++) {
            int day = Integer.parseInt(weatherInfo.getWeatherDaysForecast().get(i).getDate().split("日")[0]);
            if (day == 1) {
                mouth += 1;

            }
            listDay.add(mouth + "-" + (weatherInfo.getWeatherDaysForecast().get(i).getDate().split("日")[0]));
        }
        return listDay;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.weather_tab_one:
                viewPager.setCurrentItem(0);
                break;
            case R.id.weather_tab_two:
                viewPager.setCurrentItem(1);
                break;
            case R.id.weather_tab_three:
                viewPager.setCurrentItem(2);
                break;
            case R.id.weather_tab_four:
                viewPager.setCurrentItem(3);
                break;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
