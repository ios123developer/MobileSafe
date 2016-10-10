package com.szzgkon.mobilesafe.receiver;

import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import com.szzgkon.mobilesafe.service.KillProcessWeiget;

/**
 * 创建桌面小部件的步骤
 * 1.需要在清单文件里面配置元数据
 * 2.需要配置当前元数据里面要用到xml
 * res/xml
 * 3.需要配置一个广播接受者
 * 4.实现一个桌面小部件的xml(布局桌面小控件)
 */

public class MyAppWidgetProvider extends AppWidgetProvider {

    /**
     * 第一次创建的时候才会调用此声明周期方法
     *
     * 当前的广播的生命周期只有10秒钟
     * 不能做耗时的操作
     * @param context
     */

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        System.out.println("onEnabled");

        Intent intent = new Intent(context, KillProcessWeiget.class);
        context.startService(intent);
    }

    /**
     * 当桌面上面所有的桌面小控件都删除的时候才调用
     *
     * @param context
     */
    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        System.out.println("onDisabled");

        Intent intent = new Intent(context, KillProcessWeiget.class);
        context.stopService(intent);
    }


}
