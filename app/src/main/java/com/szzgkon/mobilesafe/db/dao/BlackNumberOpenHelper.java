package com.szzgkon.mobilesafe.db.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zhangyongke on 16/9/21.
 */
public class BlackNumberOpenHelper extends SQLiteOpenHelper{
    public BlackNumberOpenHelper(Context context) {
        super(context, "safe.db", null, 1);
    }

    /**
     * blacknumber 表名
     * _id 主键自动增长
     * number 电话号码
     * mode 拦截模式， 电话拦截，短信拦截
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
    db.execSQL("create table blacknumber (_id integer primary key autoincrement,number varchar(20),mode varchar(2))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
