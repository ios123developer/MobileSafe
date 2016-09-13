package com.szzgkon.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.szzgkon.mobilesafe.R;

public class Setup4Activity extends BaseSetupActivity {

   private CheckBox cbProtect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
        cbProtect = (CheckBox)findViewById(R.id.cb_protect);

        boolean protect = mPref.getBoolean("protect",false);
        if(protect){
            cbProtect.setText("防盗保护已经开启");
            cbProtect.setChecked(true);
        }else {
            cbProtect.setText("防盗保护没有开启");
            cbProtect.setChecked(false);
        }

        cbProtect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    cbProtect.setText("防盗保护已经开启");
                    mPref.edit().putBoolean("protect",true).commit();
                }else {
                    cbProtect.setText("防盗保护没有开启");
                    mPref.edit().putBoolean("protect",false).commit();

                }
            }
        });
    }

    @Override
    public void showPreviousPage() {
        startActivity(new Intent(this,Setup3Activity.class));
        finish();
        overridePendingTransition(R.anim.tran_previous_in,R.anim.tran_previous_out);
    }

    @Override
    public void showNextPage() {
        startActivity(new Intent(this,LostFindActivity.class));
        finish();
        //两个界面之间的切换动画
        overridePendingTransition(R.anim.tran_in,R.anim.tran_out);
        mPref.edit().putBoolean("configed",true).commit();//更新sharePreference，表示已经展示过设置向导了
    }

}
