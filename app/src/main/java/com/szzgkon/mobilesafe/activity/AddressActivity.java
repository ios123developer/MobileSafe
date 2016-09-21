package com.szzgkon.mobilesafe.activity;

import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.szzgkon.mobilesafe.R;
import com.szzgkon.mobilesafe.db.dao.AddressDao;

/**
 * 归属地查询页面
 */
public class AddressActivity extends AppCompatActivity {
    private EditText etNumber;
    private TextView tvResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        etNumber = (EditText) findViewById(R.id.et_number);
        tvResult = (TextView)findViewById(R.id.tv_result);
        //监听edittext的变化
    etNumber.addTextChangedListener(new TextWatcher() {
        //文字变化前的回调
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }
        //文字发生变化时的回调
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String address = AddressDao.getAddress(s.toString());
            tvResult.setText(address);
        }
       //文字变化之后的回调
        @Override
        public void afterTextChanged(Editable s) {

        }
    });
    }

    /**
     * 开始查询
     * @param view
     */
    public void query(View view){
        String number = etNumber.getText().toString().trim();
        if(!TextUtils.isEmpty(number)){
            String address = AddressDao.getAddress(number);
            tvResult.setText(address);
        }else {
            Animation shake = AnimationUtils.loadAnimation(this,R.anim.shake);
//            shake.setInterpolator(new Interpolator() {
//                @Override
//                public float getInterpolation(float x) {
//                    //y = ax + b这里如要出现想要的结果，就要满足完成此效果的方程式
//                    return y;
//                }
//            });
            etNumber.startAnimation(shake);
            vibrate();
        }



    }
    /*
      手机振动需要权限 android.permission.VIBRATE
    */
    public void vibrate(){
       Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(2000);//振动两秒
//        vibrator.vibrate(new long[]{1000,2000,1000,3000},-1);//先等待1秒，再振动2秒，再等待1秒，再振动3秒
                                                              //参数2：-1表示只执行一次，不循环，0表示从头开始循环
                                                              //1表示从数组的第一个位置开始循环（参数2表示从数组的第几个位置开始循环）
//        vibrator.cancel();//取消振动
    }
}
