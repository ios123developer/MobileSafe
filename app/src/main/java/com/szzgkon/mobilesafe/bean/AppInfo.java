package com.szzgkon.mobilesafe.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by zhangyongke on 16/9/23.
 */
public class AppInfo {
    /**
     * 图片的icon
     */
    private Drawable icon;
    /**
     * 程序的名字
     */
    private String apkName;
    /**
     * 程序的大小
     */
    private long apkSize;
    /**
     * 区分用户app还是系统app
     * 如果是true 就是用户app
     */
    private boolean userApp;
    /**
     * 放置的位置
     */
    private boolean isRom;
    /**
     * 包名
     */
    private String apkPackName;

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getApkName() {
        return apkName;
    }

    public void setApkName(String apkName) {
        this.apkName = apkName;
    }

    public long getApkSize() {
        return apkSize;
    }

    public void setApkSize(long apkSize) {
        this.apkSize = apkSize;
    }

    public boolean isUserApp() {
        return userApp;
    }

    public void setUserApp(boolean userApp) {
        this.userApp = userApp;
    }

    public boolean isRom() {
        return isRom;
    }

    public void setRom(boolean rom) {
        isRom = rom;
    }

    public String getApkPackName() {
        return apkPackName;
    }

    public void setApkPackName(String apkPackName) {
        this.apkPackName = apkPackName;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "apkName='" + apkName + '\'' +
                ", apkSize=" + apkSize +
                ", userApp=" + userApp +
                ", isRom=" + isRom +
                ", apkPackName='" + apkPackName + '\'' +
                '}';
    }
}
