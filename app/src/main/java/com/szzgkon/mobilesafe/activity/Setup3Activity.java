package com.szzgkon.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.szzgkon.mobilesafe.R;

public class Setup3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
    }
    //下一页
    public void next(View view){
        startActivity(new Intent(this,Setup4Activity.class));
        finish();
    }
    //上一页
    public void previous(View view){
        startActivity(new Intent(this,Setup2Activity.class));
        finish();
    }
}
