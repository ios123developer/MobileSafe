package com.szzgkon.mobilesafe.receiver;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.List;
/**
 * ===================================================
 *
 * 版权：深圳市中广控信息科技有限公司 版权所有（c）2016
 *
 * 作者：张勇柯
 *
 * 版本：1.0
 *
 * 创建日期：16/10/8 下午3:40
 *
 * 描述：清理所有的进程
 *
 * 修订历史：
 *
 * ===================================================
 **/

public class KillProcessAllReceiver extends BroadcastReceiver {
    public KillProcessAllReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        //得到手机上面正在运行的进程
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : appProcesses) {

            //杀死所有的进程
            activityManager.killBackgroundProcesses(runningAppProcessInfo.processName);

        }

        Toast.makeText(context,"清理完毕",Toast.LENGTH_SHORT).show();
    }
}
