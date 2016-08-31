package com.hm.sweetreader;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Purpose     :
 * Description :
 * Author      : FLY
 * Date        : 2016.08.31 9:29
 */

public class DayNightHelper {
    static volatile DayNightHelper dayNightHelper;
    private SharedPreferences preferences;

    private DayNightHelper(Context context) {
        preferences=context.getSharedPreferences("DayNight",Context.MODE_PRIVATE);
    }

    public static DayNightHelper newInstance(Context context){
        if (dayNightHelper==null){
            synchronized (DayNightHelper.class){
                if (dayNightHelper==null){
                    dayNightHelper=new DayNightHelper(context);
                }
            }
        }
        return dayNightHelper;
    }

   public boolean isDay(){
       return preferences.getBoolean("theme",true);
   }
    public boolean isNight(){
        if (preferences.getBoolean("theme",true)){
            return false;
        }
        return true;
    }
    public void saveTheme(boolean isDay){
        preferences.edit().putBoolean("theme",isDay).apply();
    }
}
