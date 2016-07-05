package com.hm.sweetreader.weather.weather_model;

/**
 * project：SweetReader
 * author： FLY
 * date：   2016/6/27
 * time：   17:53
 * purpose：
 */
public class WeatherLifeIndex {
    private static final long serialVersionUID = -3927234831261262032L;
    /**
     * 指数名
     */
    private String mIndexName;

    /**
     * 指数建议
     */
    private String mIndexValue;

    /**
     * 指数详细
     */
    private String mIndexDetail;

    public String getIndexDetail() {
        return mIndexDetail;
    }

    public void setIndexDetail(String indexDetail) {
        mIndexDetail = indexDetail;
    }

    public String getIndexName() {
        return mIndexName;
    }

    public void setIndexName(String indexName) {
        mIndexName = indexName;
    }

    public String getIndexValue() {
        return mIndexValue;
    }

    public void setIndexValue(String indexValue) {
        mIndexValue = indexValue;
    }
}
