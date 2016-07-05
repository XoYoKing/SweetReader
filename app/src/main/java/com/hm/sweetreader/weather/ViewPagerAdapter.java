package com.hm.sweetreader.weather;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * project：SweetReader
 * author： FLY
 * date：   2016/6/28
 * time：   14:40
 * purpose：
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    List<Fragment> list;

    public ViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> lists) {
        super(fm);
        list = lists;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
