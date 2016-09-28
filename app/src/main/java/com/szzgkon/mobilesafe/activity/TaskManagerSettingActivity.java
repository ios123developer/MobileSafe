package com.szzgkon.mobilesafe.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.szzgkon.mobilesafe.R;
import com.szzgkon.mobilesafe.utils.SharedPreferensUtils;

/**
 * ===================================================
 *
 * 版权：深圳市中广控信息科技有限公司 版权所有（c）2016
 *
 * 作者：张勇柯
 *
 * 版本：1.0
 *
 * 创建日期：16/9/28 下午3:16
 *
 * 描述：进程管理的设置界面
 *
 * 修订历史：
 *
 * ===================================================
 **/


public class TaskManagerSettingActivity extends AppCompatActivity {

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
    }

    private void initUI() {
        setContentView(R.layout.activity_task_manager_setting);
        CheckBox cb_status = (CheckBox) findViewById(R.id.cb_status);

      cb_status.setChecked(SharedPreferensUtils.getBoolean(TaskManagerSettingActivity.this,"is_show_system",false));
        cb_status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    SharedPreferensUtils.saveBoolean(TaskManagerSettingActivity.this,"is_show_system",true);
                }else {
                    SharedPreferensUtils.saveBoolean(TaskManagerSettingActivity.this,"is_show_system",false);

                }
            }
        });
    }
}
