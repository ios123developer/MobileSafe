package com.szzgkon.mobilesafe.engine;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.szzgkon.mobilesafe.bean.AppInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangyongke on 16/9/23.
 */
public class AppInfos {
    public static List<AppInfo> getAppInfos(Context context){
        ArrayList<AppInfo> packageAppInfos = new ArrayList<>();


        //获取到包的管理者
        PackageManager packageManager = context.getPackageManager();
        //获取到安装包
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);

        for (PackageInfo  installedPackage : installedPackages) {

            AppInfo appInfo = new AppInfo();

            //获取到应用程序的图片
            Drawable drawable = installedPackage.applicationInfo.loadIcon(packageManager);
            //获取到应用程序的名字
            String apkName = installedPackage.applicationInfo.loadLabel(packageManager).toString();
            //获取到应用程序的包名
            String packageName = installedPackage.packageName;
            //获取到apk资源的路径
            String sourceDir = installedPackage.applicationInfo.sourceDir;

            File file = new File(sourceDir);
            //apk的大小
            long apkSize = file.length();


            appInfo.setIcon(drawable);
            appInfo.setApkName(apkName);
            appInfo.setApkPackName(packageName);
            appInfo.setApkSize(apkSize);

            //用户应用的地址开头data/data/app    系统应用的地址开头system/app
            //获取到安装应用程序的标记
            int flags = installedPackage.applicationInfo.flags;
            if((flags & ApplicationInfo.FLAG_SYSTEM) != 0){
                //表示系统app
                appInfo.setUserApp(false);
            }else {
                //表示用户app
                appInfo.setUserApp(true);
            }
            if( (flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0){
                //表示在sd卡
                appInfo.setRom(false);
            }else {
                //表示在内存
                appInfo.setRom(true);
            }

            packageAppInfos.add(appInfo);

        }

        return packageAppInfos;
    }
}
