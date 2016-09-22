package com.szzgkon.mobilesafe.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.szzgkon.mobilesafe.bean.BlackNumberInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangyongke on 16/9/21.
 */
public class BlackNumberDao {

    public   BlackNumberOpenHelper helper;

    public BlackNumberDao(Context context) {
        helper = new BlackNumberOpenHelper(context);
    }

    /**
     * @param number 黑名单号码
     * @param mode   拦截模式
     */
    public boolean add(String number, String mode) {

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("number", number);
        contentValues.put("mode", mode);

        long rowid = db.insert("blacknumber", null, contentValues);
        if (rowid == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 通过电话号码删除
     *
     * @param number
     */
    public boolean delete(String number) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int rowNumber = db.delete("blacknumber", "number=?", new String[]{number});
        if (rowNumber == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 通过电话号码修改拦截的模式
     *
     * @param number
     */
    public boolean changeNumberMode(String number, String mode) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("mode", mode);
        int rowNumber = db.update("blacknumber", contentValues, "number=?", new String[]{number});
        if (rowNumber == 0) {
            return false;
        } else {
            return true;
        }

    }

    /**
     * 返回一个黑名单号码的拦截模式
     *
     * @return
     */
    public String findNumber(String number) {
        String mode = "";
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query("blacknumber", new String[]{"mode"}, "number=?", new String[]{number}, null, null, null);
        if (cursor.moveToNext()) {
            mode = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return mode;
    }

    /**
     * 查询所有的黑名单
     *
     * @return
     */
    public List<BlackNumberInfo> findAll() {
        SQLiteDatabase db = helper.getWritableDatabase();
        ArrayList<BlackNumberInfo> blackNumberInfos = new ArrayList<>();
        Cursor cursor = db.query("blacknumber", new String[]{"number", "mode"}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
            blackNumberInfo.setNumber(cursor.getString(0));
            blackNumberInfo.setMode(cursor.getString(1));
            blackNumberInfos.add(blackNumberInfo);
        }
        cursor.close();
        db.close();

        return blackNumberInfos;
    }
}
