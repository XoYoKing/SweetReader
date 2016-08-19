package com.hm.sweetreader;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.hm.sweetreader.file_manager.FileMangerActivity;
import com.hm.sweetreader.map.MapActivity;
import com.hm.sweetreader.music.MusicMainActivity;
import com.hm.sweetreader.weather.WeatherActivity;

/**
 * project：SweetReader
 * author： FLY
 * date：   2016/6/24
 * time：   10:19
 * purpose：主界面左侧右滑界面
 */
public class MainFragment extends Fragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.menu_main, container, false);
        init(root);
        return root;
    }

    private void init(View root) {
        RelativeLayout one = (RelativeLayout) root.findViewById(R.id.menu_one);
        one.setOnClickListener(this);
        RelativeLayout two = (RelativeLayout) root.findViewById(R.id.menu_two);
        two.setOnClickListener(this);
        RelativeLayout three = (RelativeLayout) root.findViewById(R.id.menu_three);
        three.setOnClickListener(this);
        RelativeLayout four = (RelativeLayout) root.findViewById(R.id.menu_four);
        four.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            //导入书籍
            case R.id.menu_one:
                intent = new Intent(getActivity(), FileMangerActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
            //今日天气
            case R.id.menu_two:
                intent = new Intent(getActivity(), WeatherActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
            //背景音乐
            case R.id.menu_three:
                intent = new Intent(getActivity(), MusicMainActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.menu_four:
                intent = new Intent(getActivity(), MapActivity.class);
                startActivity(intent);
                break;
        }
    }
}
