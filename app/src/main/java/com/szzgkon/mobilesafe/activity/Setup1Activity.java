package com.szzgkon.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;

import com.szzgkon.mobilesafe.R;

public class Setup1Activity extends BaseSetupActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
    }

    @Override
    public void showPreviousPage() {

    }

    @Override
    public void showNextPage() {
        startActivity(new Intent(this,Setup2Activity.class));
        finish();

        //两个界面之间的切换动画
        overridePendingTransition(R.anim.tran_in,R.anim.tran_out);
    }

}
