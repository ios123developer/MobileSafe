package com.szzgkon.mobilesafe.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.szzgkon.mobilesafe.R;

public class Splash extends AppCompatActivity {
   private TextView tvVersion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvVersion = (TextView)findViewById(R.id.tv_version);
        tvVersion.setText("版本号：" + getVersionName());

    }
    private String getVersionName(){
       PackageManager packageManager = getPackageManager();
        try {
            //获取包的信息
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(),0);
            int versionCode = packageInfo.versionCode;
            String versionName = packageInfo.versionName;
            System.out.println("versionName=" + versionName + ":versionCode"+versionCode);
        return versionName;
        } catch (Exception e) {
            //没有找到包名的时候回走此异常
            e.printStackTrace();
        }
        return "";
    }
}
