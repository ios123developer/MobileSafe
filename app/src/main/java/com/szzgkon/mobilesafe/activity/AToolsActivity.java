package com.szzgkon.mobilesafe.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szzgkon.mobilesafe.R;
import com.szzgkon.mobilesafe.utils.SmsUtils;
import com.szzgkon.mobilesafe.utils.ToastUtils;


/**
 * ===================================================
 * 版权：深圳市中广控信息科技有限公司 版权所有（c）2016
 * 作者：张勇柯
 * 版本：1.0
 * 创建日期：16/9/26
 * 描述：高级工具的界面
 * 修订历史：
 * ===================================================
 **/

public class AToolsActivity extends AppCompatActivity {
    @ViewInject(R.id.progressBar)
    private ProgressBar progressBar;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atools);
        ViewUtils.inject(this);
    }

    /**
     * 归属地查询
     * @param view
     */
    public void numberAddressQuery(View view){
       startActivity(new Intent(this, AddressActivity.class));
    }

    /**
     * 备份短信
     * @param view
     */
    public void backUpsms(View view) {
        //初始化一个进度条的对话框
        pd = new ProgressDialog(AToolsActivity.this);
        pd.setTitle("提示");
        pd.setMessage("请稍等，正在备份");

        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.show();




        new Thread() {
            @Override
            public void run() {
                super.run();

                boolean result = SmsUtils.backUp(AToolsActivity.this, new SmsUtils.BackUpCallBackSms() {
                    @Override
                    public void before(int count) {
                        pd.setMax(count);
                    }

                    @Override
                    public void onBackUpSms(int progress) {
                        pd.setProgress(progress);
                    }
                });

                if (result) {
                    ToastUtils.showToast(AToolsActivity.this,"备份成功");
                } else {

                    ToastUtils.showToast(AToolsActivity.this,"备份失败");

                }
                pd.dismiss();
            }
        }.start();

    }
}
