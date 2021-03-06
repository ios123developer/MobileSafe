package com.szzgkon.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;
import com.szzgkon.mobilesafe.db.dao.BlackNumberDao;

import java.lang.reflect.Method;

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
        //获取到系统的电话服务
        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        MyphoneStateListener listener = new MyphoneStateListener();

        tm.listen(listener,PhoneStateListener.LISTEN_CALL_STATE);

        //初始化短信的广播
        InnerReceiver innerReceiver = new InnerReceiver();
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        intentFilter.setPriority(Integer.MAX_VALUE);
        registerReceiver(innerReceiver, intentFilter);

    }

    private class MyphoneStateListener extends PhoneStateListener {

        //电话状态改变的监听
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                //电话铃响的状态
                case TelephonyManager.CALL_STATE_RINGING:

                    String mode = dao.findNumber(incomingNumber);
                    if(mode.equals("1") || mode.equals("2")){

                        Uri uri = Uri.parse("content://call_log/calls");
                        getContentResolver().registerContentObserver(uri,true,new MyContentObserver(new Handler(),incomingNumber));

                        //挂断电话
                        endCall();
                    }


                    break;
                //电话闲置
                case TelephonyManager.CALL_STATE_IDLE:

                    break;
                //电话接通的状态
                case TelephonyManager.CALL_STATE_OFFHOOK:

                    break;
                default:
                    break;


            }
        }
    }

    private class MyContentObserver extends ContentObserver {
       String incomingNumber;

        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         * @param incomingNumber
         */
        public MyContentObserver(Handler handler, String incomingNumber) {
            super(handler);
            this.incomingNumber = incomingNumber;
        }

        /**
         * 当数据改变的时候调用的方法
         * @param selfChange
         */
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);

            getContentResolver().unregisterContentObserver(this);

            deleteCallLog(incomingNumber);
        }
    }
    //删除通话记录
    private void deleteCallLog(String incomingNumber) {
        Uri uri = Uri.parse("content://call_log/calls");
        getContentResolver().delete(uri,"number=?",new String[]{incomingNumber});

    }

    /**
     * 挂断电话方法
     */
    private void endCall() {
        try {
            //通过类加载器加载ServiceManager
            Class<?> clazz = getClassLoader().loadClass("android.os.ServiceManager");
            //通过反射得到当前的方法
            Method method = clazz.getDeclaredMethod("getService", String.class);

            IBinder iBinder = (IBinder) method.invoke(null, TELEPHONY_SERVICE);

            ITelephony iTelephony = ITelephony.Stub.asInterface(iBinder);

            iTelephony.endCall();

        } catch (Exception e) {
            e.printStackTrace();
        }

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

                if (mode.equals("1")) {
                    abortBroadcast();
                } else if (mode.equals("3")) {
                    abortBroadcast();
                }

                //智能拦截模式
                if (messageBody.contains("nimeide")) {
                    abortBroadcast();
                }
            }
        }
    }


}
