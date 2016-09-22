package com.szzgkon.mobilesafe.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.szzgkon.mobilesafe.R;

public class CallSafeActivity extends AppCompatActivity {

    private ListView list_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_safe);

        initUI();
    }

    private void initUI() {
        list_view = (ListView) findViewById(R.id.list_view);
    }


}
