package com.szzgkon.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;

import com.szzgkon.mobilesafe.R;

public class Setup3Activity extends BaseSetupActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
    }

    @Override
    public void showPreviousPage() {
        startActivity(new Intent(this,Setup2Activity.class));
        finish();
        overridePendingTransition(R.anim.tran_previous_in,R.anim.tran_previous_out);
    }

    @Override
    public void showNextPage() {
        startActivity(new Intent(this,Setup4Activity.class));
        finish();
        //两个界面之间的切换动画
        overridePendingTransition(R.anim.tran_in,R.anim.tran_out);
    }


}
