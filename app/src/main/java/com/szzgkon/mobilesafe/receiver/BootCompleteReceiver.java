package com.szzgkon.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
    SharedPreferences sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
    String sim = sp.getString("sim",null);
        if(!TextUtils.isEmpty(sim)) {
            //获取当前手机的sim卡
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                String currentSim = tm.getSimSerialNumber(); //拿到当前手机的sim卡
            if(sim.equals(currentSim)){
                System.out.println("手机安全");
            }else {
                System.out.println("sim卡已经变化，发送报警短信");
            }
        }
    }
}
