package com.szzgkon.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.szzgkon.mobilesafe.R;

public class Setup2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
    }
        //下一页
    public void next(View view){
            startActivity(new Intent(this,Setup3Activity.class));
        finish();
    }
    //上一页
    public void previous(View view){
            startActivity(new Intent(this,Setup1Activity.class));
        finish();
    }
}
