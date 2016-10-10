package com.szzgkon.mobilesafe.service;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.text.format.Formatter;
import android.widget.RemoteViews;

import com.szzgkon.mobilesafe.R;
import com.szzgkon.mobilesafe.receiver.MyAppWidgetProvider;
import com.szzgkon.mobilesafe.utils.ServiceStatusUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * ===================================================
 *
 * 版权：深圳市中广控信息科技有限公司 版权所有（c）2016
 *
 * 作者：张勇柯
 *
 * 版本：1.0
 *
 * 创建日期：16/10/8 上午10:59
 *
 * 描述：清理桌面小控件的服务
 *
 * 修订历史：
 *
 * ===================================================
 **/

public class KillProcessWeiget extends Service {

    private AppWidgetManager widgetManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //桌面小控件的管理者
        widgetManager = AppWidgetManager.getInstance(this);



        //每隔5秒钟更新一次桌面
        //初始化定时器
        Timer timer = new Timer();
        //初始化一个定时任务
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                System.out.println("KillProcessWeiget");

                /**
                 * 初始化一个远程的view，因为桌面小控件是运行在launch程序里面
                 * （launch是另外的一个程序）
                 */
                RemoteViews views = new RemoteViews(getPackageName(), R.layout.process_widget);

                /**
                 * 需要注意，这个远程views中没有findviewById
                 */
                //设置当前文本里有多少个进程
                int processCount = ServiceStatusUtils.getProcessCount(KillProcessWeiget.this);
                System.out.println("正在运行的软件："+String.valueOf(processCount));

                views.setTextViewText(R.id.process_count, "正在运行的软件："+String.valueOf(processCount));
                //获取手机上的可用内存

                long availMem = ServiceStatusUtils.getAvailMem(KillProcessWeiget.this);

                views.setTextViewText(R.id.process_memory, "可用内存："+Formatter.formatFileSize(getApplicationContext(),availMem));


                //小控件的点击事件

                Intent intent = new Intent();
                //发送一个隐式意图
                intent.setAction("com.szzgkon.mobilesafe");

                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
                views.setOnClickPendingIntent(R.id.btn_clear,pendingIntent);


                //第一个参数表示上下文，第二个参数表示当前由哪一个广播去处理当前的桌面小控件

                ComponentName provider = new ComponentName(getApplicationContext(),MyAppWidgetProvider.class);

                //更细界面
                widgetManager.updateAppWidget(provider,views);
            }
        };
        //从0开始，每隔5秒钟更新一次
        timer.schedule(timerTask,0,5000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
