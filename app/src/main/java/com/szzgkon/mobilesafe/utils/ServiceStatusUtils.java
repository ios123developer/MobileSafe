package com.szzgkon.mobilesafe.utils;

/**
 * Created by zhangyongke on 16/9/20.
 */

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * 判断服务状态工具类
 */
public class ServiceStatusUtils {
    /**
     * 检测服务是否正在运行
     * @return
     */
    public static boolean isServiceRunning(Context ctx, String serviceName){
        ActivityManager am = (ActivityManager) ctx.
                getSystemService(Context.ACTIVITY_SERVICE);
        //获取所有系统正在运行的服务，最多返回100个
        List<ActivityManager.RunningServiceInfo> runningServices = am.getRunningServices(100);
        //打印正在运行的服务的名称
        for (ActivityManager.RunningServiceInfo runningServiceInfo: runningServices) {
            String className = runningServiceInfo.service.getClassName();
            System.out.println(className);

            if(className.equals(serviceName)){//服务存在
                return true;
            }
        }

        return false;
    }
}
