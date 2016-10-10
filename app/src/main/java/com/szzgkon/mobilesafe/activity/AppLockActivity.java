package com.szzgkon.mobilesafe.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.szzgkon.mobilesafe.R;
import com.szzgkon.mobilesafe.fragment.LockFragment;
import com.szzgkon.mobilesafe.fragment.UnlockFragment;

public class AppLockActivity extends FragmentActivity implements View.OnClickListener {

    private FrameLayout fl_content;
    private TextView tv_unlock;
    private TextView tv_lock;
    private FragmentManager fragmentManager;
    private UnlockFragment unlockFragment;
    private LockFragment lockFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            initUI();
    }

    private void initUI() {
        setContentView(R.layout.activity_app_lock);

        fl_content = (FrameLayout) findViewById(R.id.fl_content);

        tv_unlock = (TextView) findViewById(R.id.tv_unlock);
        tv_lock = (TextView) findViewById(R.id.tv_lock);

        tv_unlock.setOnClickListener(this);

        tv_lock.setOnClickListener(this);

        //获取到fragment的管理者
        fragmentManager = getSupportFragmentManager();
        //开启事务
        FragmentTransaction mTransaction = fragmentManager.beginTransaction();

        unlockFragment = new UnlockFragment();

        lockFragment = new LockFragment();

        mTransaction.replace(R.id.fl_content, unlockFragment).commit();

    }

    @Override
    public void onClick(View v) {

        FragmentTransaction ft = fragmentManager.beginTransaction();

        switch (v.getId()){
            case R.id.tv_unlock:
                //没有加锁
                tv_unlock.setBackgroundResource(R.drawable.tab_left_pressed);
                tv_lock.setBackgroundResource(R.drawable.tab_right_default);

                ft.replace(R.id.fl_content,lockFragment);
                break;
            case R.id.tv_lock:
                tv_unlock.setBackgroundResource(R.drawable.tab_left_default);
                tv_lock.setBackgroundResource(R.drawable.tab_right_pressed);
                ft.replace(R.id.fl_content,unlockFragment);

                break;

        }
        ft.commit();
    }
}
