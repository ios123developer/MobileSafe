package com.szzgkon.mobilesafe.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.szzgkon.mobilesafe.R;
import com.szzgkon.mobilesafe.service.AddressService;
import com.szzgkon.mobilesafe.service.CallSafeService;
import com.szzgkon.mobilesafe.utils.ServiceStatusUtils;
import com.szzgkon.mobilesafe.view.SettingClickView;
import com.szzgkon.mobilesafe.view.SettingItemView;

public class SettingActivity extends AppCompatActivity {

    private SettingItemView sivUpdate;//设置升级
    private SettingItemView sivAddress;//归属地
    private SettingClickView scvAddressStyle;//修改风格
    private SettingClickView scvAddressLocation;//显示位置

    private SharedPreferences mPref;
    private SettingItemView siv_callSafe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mPref = getSharedPreferences("config",MODE_PRIVATE);

        initUpdateView();
        initAddressView();
        initAddressStyle();
        initAddressLocation();
        initBlackView();
    }

    /**
     * 初始化黑名单
     */
    private void initBlackView() {
        siv_callSafe = (SettingItemView)findViewById(R.id.siv_callSafe);
        //根据归属地服务是否运行来更新checkBox
        boolean serviceRunning = ServiceStatusUtils.isServiceRunning(this, "com.szzgkon.mobilesafe.service.CallSafeService");
        if(serviceRunning){
            siv_callSafe.setChecked(true);
        }else {
            siv_callSafe.setChecked(false);
        }

        siv_callSafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(siv_callSafe.isChecked()){
                    siv_callSafe.setChecked(false);
                    stopService(new Intent(SettingActivity.this, CallSafeService.class));//停止归属地服务
                }else {
                    siv_callSafe.setChecked(true);
                    startService(new Intent(SettingActivity.this, CallSafeService.class));//打开归属地服务
                }
            }
        });

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

    /**
     * 修改提示框的显示风格
     */
    private void initAddressStyle(){
        scvAddressStyle = (SettingClickView)findViewById(R.id.scv_address_style);
         scvAddressStyle.setTitle("归属地提示框风格");
         scvAddressStyle.setDesc("活力橙");
        scvAddressStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSingleChooseDialog();
            }
        });
    }
    final String[] items = new String[]{"半透明","活力橙","卫士蓝","金属灰","苹果绿"};

    /**
     * 弹出选择风格的单选框
     */
    private void showSingleChooseDialog() {
        final AlertDialog.Builder builder = new  AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("归属地提示框风格");

       int style =  mPref.getInt("address_style",0);//读取保存的style

        scvAddressStyle.setDesc(items[style]);

        builder.setSingleChoiceItems(items, style, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPref.edit().putInt("address_style",which).commit();//保存选择的风格
                dialog.dismiss();//让dialog消失

                scvAddressStyle.setDesc(items[which]);//更新组合控件的描述
            }
        });
            builder.setNegativeButton("取消", null);
        builder.show();
    }

    /**
     * 修改归属地显示位置
     */
    private void initAddressLocation(){
        scvAddressLocation = (SettingClickView)findViewById(R.id.scv_address_location);
        scvAddressLocation.setTitle("归属地提示框显示位置");
        scvAddressLocation.setDesc("设置归属地提示框的显示位置");

        scvAddressLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this,DragViewActivity.class));
            }
        });
    }
}
