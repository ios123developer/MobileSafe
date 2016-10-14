package com.szzgkon.mobilesafe.activity;

import android.net.TrafficStats;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.szzgkon.mobilesafe.R;

public class TrafficManagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic_manager);

        //获取到手机的下载的流
        long mobileRxBytes = TrafficStats.getMobileRxBytes();
        //获取到手机的上传的流量
        long mobileTxBytes = TrafficStats.getMobileTxBytes();

    }
}
