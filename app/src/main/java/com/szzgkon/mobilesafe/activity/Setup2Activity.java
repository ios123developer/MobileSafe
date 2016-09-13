package com.szzgkon.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;

import com.szzgkon.mobilesafe.R;
import com.szzgkon.mobilesafe.utils.ToastUtils;
import com.szzgkon.mobilesafe.view.SettingItemView;

public class Setup2Activity extends BaseSetupActivity {


    private SettingItemView sivSim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
        sivSim = (SettingItemView)findViewById(R.id.siv_sim);
        String sim = mPref.getString("sim",null);
        if(!TextUtils.isEmpty(sim)){
            sivSim.setChecked(true);
        }else {
            sivSim.setChecked(false);
        }

        sivSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sivSim.isChecked()){
                    sivSim.setChecked(false);
                    mPref.edit().remove("sim").commit();//删除已绑定的sim卡
                }else {
                    sivSim.setChecked(true);
                    //保存sim卡信息
                    TelephonyManager tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
                    String simSerialNumber = tm.getSimSerialNumber();//获取sim卡序列号
                    System.out.println("sim卡序列号" + simSerialNumber);

                    mPref.edit().putString("sim",simSerialNumber).commit();//将sim卡保存在SharePreferences中

                }
            }
        });

    }

    @Override
    public void showPreviousPage() {
        startActivity(new Intent(this,Setup1Activity.class));
        finish();
        overridePendingTransition(R.anim.tran_previous_in,R.anim.tran_previous_out);
    }

    @Override
    public void showNextPage() {
        //如果sim卡没有绑定，就不允许进入下一个页面
        String sim = mPref.getString("sim",null);
        if(!TextUtils.isEmpty(sim)){
            startActivity(new Intent(this,Setup3Activity.class));
            finish();
            //两个界面之间的切换动画
            overridePendingTransition(R.anim.tran_in,R.anim.tran_out);
        }else {
            ToastUtils.showToast(this,"sim卡还未绑定哦");
        }

    }

}
