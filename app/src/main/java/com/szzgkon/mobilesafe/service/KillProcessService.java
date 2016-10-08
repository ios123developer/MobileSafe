package com.szzgkon.mobilesafe.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class KillProcessService extends Service {

    private LockScreenReceiver receiver;

    public KillProcessService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private class LockScreenReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);

            //获取手机上面所有正在运行的进程
            List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : appProcesses) {
                activityManager.killBackgroundProcesses(runningAppProcessInfo.processName);

            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //锁屏的广播
        receiver = new LockScreenReceiver();

        //锁屏的过滤器
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);

        registerReceiver(receiver,filter);


        Timer timer = new Timer();
        TimerTask task = new TimerTask(){

            @Override
            public void run() {
                System.out.println("我被调用了");
            }
        };
        //进行定时调度
        /**
         * 第一个参数
         */
        timer.schedule(task,1000,1000);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();

        //当程序退出的时候，需要把广播反注册掉
        unregisterReceiver(receiver);
        //手动回收
        receiver = null;
    }
}
