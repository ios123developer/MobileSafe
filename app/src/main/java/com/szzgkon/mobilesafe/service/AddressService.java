package com.szzgkon.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.szzgkon.mobilesafe.R;
import com.szzgkon.mobilesafe.db.dao.AddressDao;

/**
 * 来电提醒的服务
 */
public class AddressService extends Service {

    private TelephonyManager tm;
    private MyListener listener;
    private OutCallRecevier recevier;
    private View view;
    private SharedPreferences mPref;
    private int startX;
    private int startY;
    private WindowManager mWM;
    private int winWidth;
    private int winHeight;

    public AddressService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
       mPref = getSharedPreferences("config",MODE_PRIVATE);

        //监听来电
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        listener = new MyListener();
        tm.listen(listener,PhoneStateListener.LISTEN_CALL_STATE);//监听来电的状态
        recevier = new OutCallRecevier();
        IntentFilter filter = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
        registerReceiver(recevier,filter);//动态注册广播

    }
    class MyListener extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch(state){
            case TelephonyManager.CALL_STATE_RINGING://电话铃声响了
                System.out.println("电话铃响...");
               String address = AddressDao.getAddress(incomingNumber);//根据来电号码来判断归属地
//                Toast.makeText(AddressService.this,address,Toast.LENGTH_LONG).show();
               showToast(address);
                 break;
            case TelephonyManager.CALL_STATE_IDLE://电话闲置状态
                if(mWM != null && view != null){
                    mWM.removeView(view);
                    view = null;
                }
                break;
            default:
                 break;

            }
            super.onCallStateChanged(state, incomingNumber);

        }
    }
    //动态注册去电广播接收者
    class OutCallRecevier extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String number = getResultData();//获取去电的电话号码
            String address = AddressDao.getAddress(number);
//            Toast.makeText(context,address,Toast.LENGTH_LONG).show();
           showToast(address);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        tm.listen(listener,PhoneStateListener.LISTEN_NONE);//停止来电监听
       unregisterReceiver(recevier);//注销广播
    }

    /**
     * 自定义归属地浮窗
     */
    public void showToast(String text){
        //可以在第三方app中弹出自己的浮窗
        mWM = (WindowManager)this
                .getSystemService(Context.WINDOW_SERVICE);
        //屏幕宽高
        winWidth = mWM.getDefaultDisplay().getWidth();
        winHeight = mWM.getDefaultDisplay().getHeight();


        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.gravity = Gravity.LEFT + Gravity.TOP;//将重心位置设置为左上方，
                                                // 也就是（0，0）从左上方开始，而不是
                                                //默认的重心位置
        params.setTitle("Toast");

        int lastX = mPref.getInt("lastX", 0);
        int lastY = mPref.getInt("lastY", 0);

        params.x = lastX;
        params.y = lastX;


        view =  View.inflate(this, R.layout.toast_address,null);
        int[] bgs = new int[]{R.drawable.call_locate_white,
                              R.drawable.call_locate_orange,
                              R.drawable.call_locate_blue,
                              R.drawable.call_locate_gray,
                              R.drawable.call_locate_green};

        int style =  mPref.getInt("address_style",0);

        view.setBackgroundResource(bgs[style]);//根据存储的样式更新背景


         TextView tvText = (TextView) view.findViewById(R.id.tv_number);
        tvText.setText(text);
        mWM.addView(view,params);//将view添加在屏幕上（window）
       view.setOnTouchListener(new View.OnTouchListener() {
          @Override
          public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    //初始化起点坐标
                    startX = (int) event.getRawX();
                    startY = (int) event.getRawY();
                     break;
                    case MotionEvent.ACTION_MOVE:
                        int endX = (int)event.getRawX();
                        int endY = (int)event.getRawY();

                        //计算移动偏移量
                        int dx = endX - startX;
                        int dy = endY - startY;
                        //更新浮窗
                        params.x += dx;
                        params.y += dy;


                        //防止坐标偏离屏幕
                        if(params.x < 0 ){
                            params.x = 0;
                        }
                        if(params.y < 0 ){
                            params.y = 0;
                        }
                        if(params.x > winWidth - view.getWidth()){
                            params.x = winWidth - view.getWidth();
                        }
                        if(params.y > winHeight - view.getHeight()){
                            params.y = winHeight - view.getHeight();
                        }
                        mWM.updateViewLayout(view,params);

                        //重新初始化起点坐标
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        //记录坐标点
                        SharedPreferences.Editor editor = mPref.edit();
                        editor.putInt("lastX",params.x);
                        editor.putInt("lastY",params.y);
                        editor.commit();
                        break;

                default:
                     break;
                }
              return true;
          }
      });
    }
}
