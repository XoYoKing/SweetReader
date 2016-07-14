package com.hm.sweetreader;

import android.os.Environment;

import java.io.File;

/**
 * project：MyReader
 * author： FLY
 * date：   2016/5/26
 * time：   14:52
 * purpose：
 */
public class Contents {

    public static String sharePageKey = "pageNum";
    public static String shareBookNameKey = "bookName";

    public static String rootSDCardPath=Environment.getExternalStorageDirectory().getAbsolutePath();
    public static String rootFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +
            "books";
    public static String cacheFilePath = rootFilePath + File.separator + "tmp";

    public static int byteInPage=500;

    public static String WeatherBaseUrl="http://wthrcdn.etouch.cn/WeatherApi?city=";
    /**
     * base64编码
     */
    public static final String BASE64 = "base64";

    //MusicBox接收器所能响应的Action
    public static final String MUSICBOX_ACTION="com.jph.musicbox.MUSICBOX_ACTION";
    public static final String MUSICBOX_ACTION_PROGRESS="com.jph.musicbox.MUSICBOX_ACTION_PROGRESS";
    //MusicService接收器所能响应的Action
    public static final String MUSICSERVICE_ACTION_STATE="com.jph.musicbox.MUSICSERVICE_ACTION";
    public static final String MUSICSERVICE_ACTION_PROGRESS="com.jph.musicbox.MUSICSERVICE_ACTION_PROGRESS";
    //初始化flag
    public static final int STATE_NON=0x122;
    //播放的flag
    public static final int STATE_PLAY=0x123;
    //暂停的flag
    public static final int STATE_PAUSE=0x124;
    //停止放的flag
    public static final int STATE_STOP=0x125;
    //播放上一首的flag
    public static final int STATE_PREVIOUS=0x126;
    //播放下一首的flag
    public static final int STATE_NEXT=0x127;
    //菜单关于选项的itemId
    public static final int MENU_ABOUT=0x200;
    //菜单退出选的项的itemId
    public static final int MENU_EXIT=0x201;
    //音乐播放器旋转一圈所要时间 单位 ms 毫秒
    public static final int MUSIC_TIEM=10000;
}
