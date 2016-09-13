package com.szzgkon.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.szzgkon.mobilesafe.R;
import com.szzgkon.mobilesafe.utils.ToastUtils;

public class Setup3Activity extends BaseSetupActivity {
     private EditText etPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
        etPhone = (EditText)findViewById(R.id.et_phone);

       String phone = mPref.getString("safe_phone","");
        etPhone.setText(phone);

    }

    @Override
    public void showPreviousPage() {
        startActivity(new Intent(this,Setup2Activity.class));
        finish();
        overridePendingTransition(R.anim.tran_previous_in,R.anim.tran_previous_out);

    }

    @Override
    public void showNextPage() {
        String phone = etPhone.getText().toString().trim();//trim() 是过滤空格
        if(TextUtils.isEmpty(phone)){
             //自定义的工具类
            ToastUtils.showToast(this,"安全号码不能为空");
            return;
        }
        mPref.edit().putString("safe_phone",phone).commit();//保存安全号码
        startActivity(new Intent(this,Setup4Activity.class));
        finish();
        //两个界面之间的切换动画
        overridePendingTransition(R.anim.tran_in,R.anim.tran_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            String phone = data.getStringExtra("phone");

            phone = phone.replace("-","").replaceAll(" ","");
            etPhone.setText(phone);//把电话号码设置给输入框

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 选择联系人
     * @param view
     */
    public void selectContact(View view){
        Intent intent = new Intent(this,ContactActivity.class);
        startActivityForResult(intent,0);
    }


}
