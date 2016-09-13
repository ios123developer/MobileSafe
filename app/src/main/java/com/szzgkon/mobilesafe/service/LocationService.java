package com.szzgkon.mobilesafe.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;

/**
 * 获取经纬度的service
 */
public class LocationService extends Service {
    private LocationManager lm;
    private myLocationListener listener;
    private Criteria criteria;
    private SharedPreferences mPref;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPref = getSharedPreferences("config", MODE_PRIVATE);
        /**
         * 获取系统的定位服务
         */
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        //标准
        criteria = new Criteria();
        criteria.setCostAllowed(true);//是否允许付费，比如使用3g网络定位
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
//        List<String> allProviders = lm.getAllProviders();//获取所有位置提供者
        listener = new myLocationListener();


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        /**
         * 参数一：表示位置提供者
         * 参数二：最短更新时间
         * 参数三：最短更新距离
         */
        lm.requestLocationUpdates(lm.getBestProvider(criteria, true), 0, 0, listener);

    }

    class myLocationListener implements LocationListener {
        //位置发生变化
        @Override
        public void onLocationChanged(Location location) {

            //将获取的经纬度保存在sp中
            mPref.edit()
                    .putString("location",
                            "longtitude:" + location.getLongitude() +
                                    ";latitude:" + location.getLatitude()).commit();

        }

        //状态发生变化
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            System.out.println("onStatusChanged");
        }

        //用户打开GPS
        @Override
        public void onProviderEnabled(String provider) {
            System.out.println("onProviderEnabled");
        }

        //用户关闭GPS
        @Override
        public void onProviderDisabled(String provider) {
            System.out.println("onProviderDisabled");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lm.removeUpdates(listener);//当activity销毁时，停止更新位置，节省电量
    }


}
