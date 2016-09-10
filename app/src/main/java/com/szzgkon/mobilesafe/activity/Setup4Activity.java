package com.szzgkon.mobilesafe.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.szzgkon.mobilesafe.R;

public class Setup4Activity extends AppCompatActivity {

    private SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
       mPref =  getSharedPreferences("config",MODE_PRIVATE);

    }
    //下一页
    public void next(View view){

        startActivity(new Intent(this,LostFindActivity.class));
        finish();
        mPref.edit().putBoolean("configed",true).commit();//更新sharePreference，表示已经展示过设置向导了
    }
    //上一页
    public void previous(View view){
        startActivity(new Intent(this,Setup3Activity.class));
        finish();
    }
}
