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

    private static long totalMem;

    /**
     * 检测服务是否正在运行
     *
     * @return
     */
    public static boolean isServiceRunning(Context ctx, String serviceName) {
        ActivityManager am = (ActivityManager) ctx.
                getSystemService(Context.ACTIVITY_SERVICE);
        //获取所有系统正在运行的服务，最多返回100个
        List<ActivityManager.RunningServiceInfo> runningServices = am.getRunningServices(100);
        //打印正在运行的服务的名称
        for (ActivityManager.RunningServiceInfo runningServiceInfo : runningServices) {
            String className = runningServiceInfo.service.getClassName();
            System.out.println(className);

            if (className.equals(serviceName)) {//服务存在
                return true;
            }
        }

        return false;
    }

    /**
     * 返回进程的总个数
     *
     * @param context
     * @return
     */
    public static int getProcessCount(Context context) {
        //得到进程管理者
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //获取当前手机上面所有运行的进程
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();

        //获取手机上面一共有多少个运行的进程

        return runningAppProcesses.size();
    }

    public static long getAvailMem(Context context) {
        //得到进程管理者
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();

        //获取到内存的基本信息
        activityManager.getMemoryInfo(memoryInfo);

        //获取到剩余内存
        return memoryInfo.availMem;


    }

    public static long getTotalMem(Context context) {
        //得到进程管理者
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();


        //获取到内存的基本信息
        activityManager.getMemoryInfo(memoryInfo);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {

            //这句代码不能直接跑到低版本的手机上面
//
            totalMem = memoryInfo.totalMem;


        }

        return totalMem;
    }
}
