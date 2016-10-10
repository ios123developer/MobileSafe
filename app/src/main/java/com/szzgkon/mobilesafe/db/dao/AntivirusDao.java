package com.szzgkon.mobilesafe.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * ===================================================
 * 版权：深圳市中广控信息科技有限公司 版权所有（c）2016
 * 作者：zhangyongke
 * 版本：1.0
 * 创建日期：16/10/9 上午9:30
 * 描述：
 * 修订历史：
 * ===================================================
 **/
public class AntivirusDao {

    /**
     * 检查当前md5值是否在病毒数据库中
     * @param md5
     * @return
     */
    private static final String PATH = "data/data/com.szzgkon.mobilesafe/files/antivirus.db";

    public static String checkFileVirus(String md5){

        String desc = "";

        //获取数据库对象
        SQLiteDatabase database = SQLiteDatabase.
                openDatabase(PATH,null,SQLiteDatabase.OPEN_READONLY);

        //查询当前传过来的md5是否在病毒数据库中
        Cursor cursor = database.rawQuery("select desc from datable where md5 = ?", new String[]{md5});

        //判断当前的游标是否可以移动
        if(cursor.moveToNext()){
         desc = cursor.getString(0);
        }

        cursor.close();
        return desc;

    }

    /**
     * 添加病毒数据库
     * @param md5 特征码
     * @param desc 描述信息
     */
    public static void addVirus(String md5,String desc){
        //获取数据库对象
        SQLiteDatabase database = SQLiteDatabase.
                openDatabase(PATH,null,SQLiteDatabase.OPEN_READWRITE);

        ContentValues contentValues = new ContentValues();
        contentValues.put("md5",md5);
        contentValues.put("desc",desc);
        contentValues.put("type",6);
        contentValues.put("name","Android.Troj.AirAD.a");

        database.insert("datable",null,contentValues);

        database.close();
    }

}
