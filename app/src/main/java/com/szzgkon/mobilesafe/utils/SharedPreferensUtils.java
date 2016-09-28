package com.szzgkon.mobilesafe.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * ===================================================
 * 版权：深圳市中广控信息科技有限公司 版权所有（c）2016
 * 作者：zhangyongke
 * 版本：1.0
 * 创建日期：16/9/28 下午4:09
 * 描述：
 * 修订历史：
 * ===================================================
 **/
public class SharedPreferensUtils {
    public static final String SP_NAME = "config";
    public static void saveBoolean(Context context,String key,Boolean value){
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key,value).commit();

    }

    public static boolean getBoolean(Context context,String key,boolean defValue){
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(key,defValue);
    }
}
