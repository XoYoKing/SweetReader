package com.hm.sweetreader.entity;

/**
 * project：MyReader
 * author： FLY
 * date：   2016/5/26
 * time：   17:25
 * purpose：
 */
public class DataEntity {

    //书名
    private String name;
    //路径
    private String url;
    //封面
    private String icon;
    //缓存文件标识
    private String tag;
    private long page;
    private long pageTotal;

    public DataEntity(String name, String url, String icon, String tag, long page, long pageTotal) {
        this.name = name;
        this.url = url;
        this.icon = icon;
        this.tag = tag;
        this.page = page;
        this.pageTotal = pageTotal;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getIcon() {
        return icon;
    }

    public String getTag() {
        return tag;
    }

    public long getPage() {
        return page;
    }

    public long getPageTotal() {
        return pageTotal;
    }
}
