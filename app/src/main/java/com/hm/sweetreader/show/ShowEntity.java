package com.hm.sweetreader.show;

/**
 * project：SweetReader
 * author： FLY
 * date：   2016/6/16
 * time：   14:43
 * purpose：
 */
public class ShowEntity {
    private long tag;
    private String show;

    public ShowEntity(long tag, String show) {
        this.tag = tag;
        this.show = show;
    }

    public long getTag() {
        return tag;
    }

    public void setTag(long tag) {
        this.tag = tag;
    }

    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }
}
