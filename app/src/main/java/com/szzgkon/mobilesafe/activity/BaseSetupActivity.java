package com.szzgkon.mobilesafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by zhangyongke on 16/9/12.
 */

/**
 * 设置引导页的基类，不需要再清单文件中注册，因为不需要展示
 */
public abstract class BaseSetupActivity extends Activity {
    private GestureDetector mDectector;
    public SharedPreferences mPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPref = getSharedPreferences("config",MODE_PRIVATE);
        //手势识别器
        mDectector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){
            /**
             * 监听手势滑动事件
             * @param e1 表示滑动的起点
             * @param e2 表示滑动的终点
             * @param velocityX 表示水平速度
             * @param velocityY 表示垂直速度
             * @return
             */
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

                //判断纵向滑动幅度是否过大，过大的话不允许切换界面
                if(Math.abs(e2.getRawY() - e1.getRawY())>100){
                    Toast.makeText(BaseSetupActivity.this,"不能这样划哦",Toast.LENGTH_SHORT).show();
                    return true;
                }
                if(Math.abs(velocityX) < 100){
                    Toast.makeText(BaseSetupActivity.this,"滑动的太慢哦",Toast.LENGTH_SHORT).show();
                 return true;
                }
                if(e2.getRawX() - e1.getRawX() > 200 ){
                    showPreviousPage();
                    return true;

                }else if(e1.getRawX() - e2.getRawX() >200){
                    showNextPage();
                    return true;
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }
    /**
     * 展示上一页,抽象方法，子类必须实现
     */
    public abstract void showPreviousPage();

    /**
     * 展示下一页
     */
    public abstract void showNextPage();
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDectector.onTouchEvent(event);//委托手势识别器处理触摸事件
        return super.onTouchEvent(event);
    }
    //下一页
    public void next(View view){
        showNextPage();
    }
    //上一页
    public void previous(View view){
        showPreviousPage();
    }
}
