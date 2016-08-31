package com.hm.sweetreader.map;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.hm.sweetreader.R;

/**
 * 准备地图
 */
public class MapActivity extends AppCompatActivity {

    private MapView mMapView = null;
    private BaiduMap mBaiduMap;
    //模式
    private MyLocationConfiguration.LocationMode mCurrentMode;
    // 定位相关
    private LocationClient mLocClient;
    private boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_map);
        init();
        getLocalAddress();
    }

    private void init() {
        mMapView = (MapView) findViewById(R.id.bmapview);
        mMapView.showZoomControls(false);

        mBaiduMap = mMapView.getMap();

        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

        //卫星地图
//        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);

        //空白地图, 基础地图瓦片将不会被渲染。在地图类型中设置为NONE，将不会使用流量下载基础地图瓦片图层。使用场景：与瓦片图层一起使用，节省流量，提升自定义瓦片图下载速度。
//        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NONE);

    }

    private void getLocalAddress() {
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);


        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(bdLocation.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(0).latitude(bdLocation.getLatitude())
                        .longitude(bdLocation.getLongitude()).build();
                mBaiduMap.setMyLocationData(locData);
                BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
                        .fromResource(R.drawable.icon_geo);
                mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
                MyLocationConfiguration config = new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker);
                mBaiduMap.setMyLocationConfigeration(config);
                if (isFirst) {
                    isFirst = false;
                    LatLng ll = new LatLng(bdLocation.getLatitude(),
                            bdLocation.getLongitude());
                    MapStatus.Builder builder = new MapStatus.Builder();
                    builder.target(ll).zoom(1000.0f);
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                }
            }
        });
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
        // 构造定位数据
//        MyLocationData locData = new MyLocationData.Builder()
//                .accuracy(location.getRadius())
//                // 此处设置开发者获取到的方向信息，顺时针0-360
//                .direction(100).latitude(location.getLatitude())
//                .longitude(location.getLongitude()).build();
//        // 设置定位数据
//        mBaiduMap.setMyLocationData(locData);
//        // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
//        BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
//                .fromResource(R.drawable.icon_geo);
//        mCurrentMode= MyLocationConfiguration.LocationMode.NORMAL;
//        MyLocationConfiguration config = new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker);
//        mBaiduMap.setMyLocationConfiguration(config);
        // 当不需要定位图层时关闭定位图层
//        mBaiduMap.setMyLocationEnabled(false);

        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.e("test","this is map click for LatLng");
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                Log.e("test","this is map click for MapPoi");
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

}
