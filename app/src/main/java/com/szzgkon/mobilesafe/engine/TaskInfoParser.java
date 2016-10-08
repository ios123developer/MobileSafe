package com.szzgkon.mobilesafe.engine;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Debug;

import com.szzgkon.mobilesafe.R;
import com.szzgkon.mobilesafe.bean.TaskInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * ===================================================
 * 版权：深圳市中广控信息科技有限公司 版权所有（c）2016
 * 作者：zhangyongke
 * 版本：1.0
 * 创建日期：16/9/27 上午11:43
 * 描述：
 * 修订历史：
 * ===================================================
 **/
public class TaskInfoParser {

    public static List<TaskInfo> getTaskInfos(Context context) {

        PackageManager packageManager = context.getPackageManager();

        ArrayList<TaskInfo> TaskInfos = new ArrayList<TaskInfo>();

        //获取到进程管理器
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        //获取到手机上面所有运行的进程
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : appProcesses) {

            TaskInfo taskInfo = new TaskInfo();

            //获取到进程的名字
            String processName = runningAppProcessInfo.processName;

            taskInfo.setPackageName(processName);


            try {
                /**
                 * 这里的memoryInfo数组中共有一个元素
                 */
                Debug.MemoryInfo[] memoryInfo = activityManager.getProcessMemoryInfo(new int[]{runningAppProcessInfo.pid});

                //当前应用共占用多少内存
                int totalPrivateDirty = memoryInfo[0].getTotalPrivateDirty() * 1024;

                taskInfo.setMemorySize(totalPrivateDirty);


                PackageInfo packageInfo = packageManager.getPackageInfo(processName, 0);
                Drawable icon = packageInfo.applicationInfo.loadIcon(packageManager);
                String appName = packageInfo.applicationInfo.loadLabel(packageManager).toString();

                taskInfo.setIcon(icon);
                taskInfo.setAppName(appName);

                int flags = packageInfo.applicationInfo.flags;

                if((flags & ApplicationInfo.FLAG_SYSTEM) != 0){
                    //说明是系统应用
                    taskInfo.setUserApp(false);
                }else {
                    //用户应用
                    taskInfo.setUserApp(true);
                }



            } catch (Exception e) {
                e.printStackTrace();

                //系统核心库里面有些系统没有图标，必须给一个默认的图标
                taskInfo.setAppName("默认");
                taskInfo.setIcon(context.getResources().getDrawable(R.mipmap.ic_launcher));

            }

            TaskInfos.add(taskInfo);


        }


        return TaskInfos;
    }
}
