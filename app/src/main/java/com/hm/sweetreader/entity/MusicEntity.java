package com.hm.sweetreader.entity;

import android.support.annotation.NonNull;

/**
 * Purpose     : 音乐类实例
 * Description :
 * Author      : FLY
 * Date        : 2016.08.18 10:12
 */

public class MusicEntity {

    private String musicName;
    private String musicPlayer;
    private String musicYear;
    //专辑
    private String musicAlbums;
    //头像 或 专辑截图
    private String musicIcon;

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(@NonNull String musicName) {
        this.musicName = musicName;
    }

    public String getMusicPlayer() {
        return musicPlayer;
    }

    public void setMusicPlayer(String musicPlayer) {
        this.musicPlayer = musicPlayer;
    }

    public String getMusicYear() {
        return musicYear;
    }

    public void setMusicYear(String musicYear) {
        this.musicYear = musicYear;
    }

    public String getMusicAlbums() {
        return musicAlbums;
    }

    public void setMusicAlbums(String musicAlbums) {
        this.musicAlbums = musicAlbums;
    }

    public String getMusicIcon() {
        return musicIcon;
    }

    public void setMusicIcon(String musicIcon) {
        this.musicIcon = musicIcon;
    }
}
