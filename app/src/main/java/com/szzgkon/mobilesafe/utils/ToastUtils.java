package com.szzgkon.mobilesafe.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by zhangyongke on 16/9/12.
 */
public class ToastUtils {
    public static void showToast(Context ctx,String text){
        Toast.makeText(ctx,text,Toast.LENGTH_SHORT).show();
    }
}
