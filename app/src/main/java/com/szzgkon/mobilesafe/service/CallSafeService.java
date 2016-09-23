package com.szzgkon.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.SmsMessage;

import com.szzgkon.mobilesafe.db.dao.BlackNumberDao;

public class CallSafeService extends Service {

    private BlackNumberDao dao;

    public CallSafeService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        dao = new BlackNumberDao(this);
        //初始化短信的广播
        InnerReceiver innerReceiver = new InnerReceiver();
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        intentFilter.setPriority(Integer.MAX_VALUE);
        registerReceiver(innerReceiver,intentFilter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private class InnerReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Object[] objects = (Object[]) intent.getExtras().get("pdus");
            for (Object object : objects) {//短信最多是140个字节，超出的话会分为多条发送，所以是一个数组，因为短信指令很短，所以只执行一次
                SmsMessage message = SmsMessage.createFromPdu((byte[]) object);
                String originationAddress = message.getOriginatingAddress();//短信来源号码
                String messageBody = message.getMessageBody();//短信内容

                //通过短信的电话号码查询拦截模式
                String mode = dao.findNumber(originationAddress);
                /**
                 * 黑名单拦截模式
                 * 1 全部拦截 电话拦截+短信拦截
                 * 2 电话拦截
                 * 3 短信拦截
                 */

                if(mode.equals("1")){
                   abortBroadcast();
                }else if(mode.equals("3")){
                    abortBroadcast();
                }

                //智能拦截模式
                if(messageBody.contains("nimeide")){
                    abortBroadcast();
                }
            }
        }
    }
}
