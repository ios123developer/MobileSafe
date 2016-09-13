package com.szzgkon.mobilesafe.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.szzgkon.mobilesafe.R;

/**
 * 手机防盗界面
 */
public class LostFindActivity extends AppCompatActivity {

    private SharedPreferences mPref;
    private TextView tvSafePhone;
    private ImageView ivProtect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       mPref = getSharedPreferences("config",MODE_PRIVATE);
       boolean configed = mPref.getBoolean("configed",false); //判断是否进入过设置向导

        if(configed){
            setContentView(R.layout.activity_lost_find);
            //根据sharePreferences更新安全号码
            tvSafePhone = (TextView)findViewById(R.id.tv_safe_phone);
            String phone = mPref.getString("safe_phone","");
            tvSafePhone.setText(phone);

            ivProtect = (ImageView)findViewById(R.id.iv_protect);
            boolean protect = mPref.getBoolean("protect",false);
            if(protect){
                ivProtect.setImageResource(R.drawable.lock);
            }else {
                ivProtect.setImageResource(R.drawable.unlock);
            }

        }else {
            //跳转设置向导页
          startActivity(new Intent(LostFindActivity.this,Setup1Activity.class ));
           finish();
        }
    }

    /**
     * 重新进入设置向导
     * @param view
     */
    public void reEnter(View view){
        startActivity(new Intent(this,Setup1Activity.class));
    }
}
