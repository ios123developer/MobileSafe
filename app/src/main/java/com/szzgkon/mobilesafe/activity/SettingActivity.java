package com.szzgkon.mobilesafe.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.szzgkon.mobilesafe.R;
import com.szzgkon.mobilesafe.service.AddressService;
import com.szzgkon.mobilesafe.utils.ServiceStatusUtils;
import com.szzgkon.mobilesafe.view.SettingItemView;

public class SettingActivity extends AppCompatActivity {

    private SettingItemView sivUpdate;//设置升级
    private SettingItemView sivAddress;//归属地
    private SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mPref = getSharedPreferences("config",MODE_PRIVATE);

        initUpdateView();
        initAddressView();
    }

    /**
     * 初始化自动更新的开关
     */
    private void initUpdateView(){
        sivUpdate = (SettingItemView)findViewById(R.id.siv_update);

        //        sivUpdate.setTitle("自动更新设置");
        boolean autoUpdate = mPref.getBoolean("auto_update",true);
        if(autoUpdate){
//                sivUpdate.setDesc("自动更新已开启");
            sivUpdate.setChecked(true);
        } else {
//                sivUpdate.setDesc("自动更新已关闭");
            sivUpdate.setChecked(false);
        }

        sivUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断当前的勾选状态
                if(sivUpdate.isChecked()){
                    //设置不勾选
                    sivUpdate.setChecked(false);
//                    sivUpdate.setDesc("自动更新已关闭");
                    //将配置保存起来（SharePreferences）
                    mPref.edit().putBoolean("auto_update",false).commit();

                }else {
                    sivUpdate.setChecked(true);
//                    sivUpdate.setDesc("自动更新已开启");
                    mPref.edit().putBoolean("auto_update",true).commit();
                }
            }
        });
    }


    /**
     * 初始化归属地开关
     */
    private void initAddressView(){
       sivAddress = (SettingItemView)findViewById(R.id.siv_address);
        //根据归属地服务是否运行来更新checkBox
        boolean serviceRunning = ServiceStatusUtils.isServiceRunning(this, "com.szzgkon.mobilesafe.service.AddressService");
       if(serviceRunning){
           sivAddress.setChecked(true);
       }else {
           sivAddress.setChecked(false);
       }

        sivAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sivAddress.isChecked()){
                    sivAddress.setChecked(false);
                    stopService(new Intent(SettingActivity.this, AddressService.class));//停止归属地服务
                }else {
                    sivAddress.setChecked(true);
                    startService(new Intent(SettingActivity.this, AddressService.class));//打开归属地服务
                }
            }
        });
    }
}
