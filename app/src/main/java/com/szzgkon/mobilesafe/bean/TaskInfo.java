package com.szzgkon.mobilesafe.bean;


import android.graphics.drawable.Drawable;

/**
 * ===================================================
 * 版权：深圳市中广控信息科技有限公司 版权所有（c）2016
 * 作者：zhangyongke
 * 版本：1.0
 * 创建日期：16/9/27 上午11:44
 * 描述：
 * 修订历史：
 * ===================================================
 **/
public class TaskInfo {

    private Drawable icon;

    private String packageName;

    private int memorySize;

    private String appName;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    private boolean checked;

    /**
     * 是否是用户进程
     */
    private boolean userApp;



    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public long getMemorySize() {
        return memorySize;
    }

    public void setMemorySize(int memorySize) {
        this.memorySize = memorySize;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public boolean isUserApp() {
        return userApp;
    }

    public void setUserApp(boolean userApp) {
        this.userApp = userApp;
    }

    @Override
    public String toString() {
        return "TaskInfo{" +
                "packageName='" + packageName + '\'' +
                ", memorySize=" + memorySize +
                ", appName='" + appName + '\'' +
                ", userApp=" + userApp +
                '}';
    }
}
