package com.szzgkon.mobilesafe.db.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * ===================================================
 * 版权：深圳市中广控信息科技有限公司 版权所有（c）2016
 * 作者：zhangyongke
 * 版本：1.0
 * 创建日期：16/10/11 上午10:48
 * 描述：
 * 修订历史：
 * ===================================================
 **/
public class AppLockOpenHelper extends SQLiteOpenHelper{
    public AppLockOpenHelper(Context context) {
        super(context, "applock.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
      db.execSQL("create table info (_id integer primary key autoincrement,packagename varchar(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
