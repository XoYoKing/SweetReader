package com.hm.sweetreader;

import android.app.Activity;

/**
 * Purpose     :
 * Description :
 * Author      : FLY
 * Date        : 2016.08.31 10:55
 */

public class ThemeUtils {

    public static void initTheme(Activity act){
        if (DayNightHelper.newInstance(act).isDay()){
            act.setTheme(R.style.DayTheme);
        }else {
            act.setTheme(R.style.NightTheme);
        }
    }
}
