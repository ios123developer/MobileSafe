package com.szzgkon.mobilesafe.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.szzgkon.mobilesafe.R;

/**
 * 修改归属地显示位置
 */
public class DragViewActivity extends AppCompatActivity {
private TextView tvTop;
    private TextView tvBottom;
    private ImageView ivDrag;
    private int startX;
    private int startY;
    private SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_view);

       mPref =  getSharedPreferences("config",MODE_PRIVATE);

        tvTop = (TextView)findViewById(R.id.tv_top);
        tvBottom = (TextView)findViewById(R.id.tv_bottom);

        ivDrag = (ImageView)findViewById(R.id.iv_drag);

       int lastX = mPref.getInt("lastX",0);
        int lastY = mPref.getInt("lastY",0);
        ivDrag.layout(lastX,lastY,lastX+ivDrag.getWidth(),lastY + ivDrag.getHeight());



        ivDrag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                       switch(event.getAction()){
                           case MotionEvent.ACTION_DOWN:
                              //先获取起点坐标
                               startX = (int) event.getRawX();
                               startY = (int) event.getRawY();
                               break;
                           case MotionEvent.ACTION_MOVE:
                               int endX = (int)event.getRawX();
                               int endY = (int)event.getRawY();

                               //计算移动偏移量
                               int dx = endX - startX;
                               int dy = endY - startY;

                               //更新左上右下的坐标
                               int l = ivDrag.getLeft() + dx;
                               int r = ivDrag.getRight() + dx;

                               int t = ivDrag.getTop() + dy;
                               int b = ivDrag.getBottom() + dy;



                               //更新界面
                               ivDrag.layout(l,t,r,b);

                               //重新初始化起点坐标
                               startX = (int) event.getRawX();
                               startY = (int) event.getRawY();


                               break;
                           case MotionEvent.ACTION_UP:
                               //记录坐标点
                              SharedPreferences.Editor editor = mPref.edit();
                               editor.putInt("lastX",ivDrag.getLeft());
                               editor.putInt("lastY",ivDrag.getTop());
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
