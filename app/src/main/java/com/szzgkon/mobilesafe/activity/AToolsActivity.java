package com.szzgkon.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.szzgkon.mobilesafe.R;

/**
 * 高级工具的界面
 */
public class AToolsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atools);
    }

    /**
     * 归属地查询
     * @param view
     */
    public void numberAddressQuery(View view){
       startActivity(new Intent(this, AddressActivity.class));
    }
}
