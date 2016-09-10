package com.szzgkon.mobilesafe.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.szzgkon.mobilesafe.R;

/**
 * 手机防盗界面
 */
public class LostFindActivity extends AppCompatActivity {

    private SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       mPref = getSharedPreferences("config",MODE_PRIVATE);
       boolean configed = mPref.getBoolean("configed",false); //判断是否进入过设置向导

        if(configed){
            setContentView(R.layout.activity_lost_find);

        }else {
            //跳转设置向导页
          startActivity(new Intent(LostFindActivity.this,Setup1Activity.class ));
           finish();
        }
    }
}
