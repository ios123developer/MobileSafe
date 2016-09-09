package com.szzgkon.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.szzgkon.mobilesafe.R;

public class HomeActivity extends AppCompatActivity {
     private GridView gvHome;
    private String[] mItems = new String[]{"手机防盗","通讯卫士",
                                            "软件管理","进程管理",
                                            "流量统计","手机杀毒",
                                            "缓存清理","高级工具",
                                                    "设置中心"};
    private int[] mPics = new int[]{R.drawable.home_safe,R.drawable.home_callmsgsafe,
                                    R.drawable.home_apps,R.drawable.home_taskmanager,
                                    R.drawable.home_callmsgsafe,R.drawable.home_trojan,
                                    R.drawable.home_sysoptimize,R.drawable.home_tools,
                                        R.drawable.home_settings};
    private   AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        gvHome = (GridView)findViewById(R.id.gv_home);
        gvHome.setAdapter(new HomeAdapter());
        //gridview的点击事件
        gvHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                case 0:
                     //手机防盗
                    showPasswordDialog();
                     break;
                case 8:
                    startActivity(new Intent(HomeActivity.this,SettingActivity.class));
                     break;

                default:
                     break;



                }
            }
        });
    }
    protected void showPasswordDialog(){
        //判断是否设置密码
        //如果没有设置过，弹出设置密码的弹窗
        showPasswordSetDialog();
    }

    /**
     * 设置密码的弹窗
     */
    private void showPasswordSetDialog() {
        AlertDialog.Builder builder =  new AlertDialog.Builder(this);
        dialog = builder.create();
        View view = View.inflate(this,R.layout.dialog_set_password,null);
//        dialog.setView(view);//将自定义的布局文件设置给dialog
        dialog.setView(view,0,0,0,0);//设置边距为0，保证在2.x的版本上也兼容
        Button btnOk = (Button)view.findViewById(R.id.btn_ok);
        Button btnCancel = (Button)view.findViewById(R.id.btn_cancel);
        final EditText etPassword = (EditText)findViewById(R.id.et_password);
        final EditText etPasswordConfirm = (EditText)findViewById(R.id.et_password_confirm);

        //对话框确定按钮
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             String password = etPassword.getText().toString();
                String passwordConfirm = etPasswordConfirm.getText().toString();
                if(!TextUtils.isEmpty(password) && !TextUtils.isEmpty(passwordConfirm)){

                }else {
                    Toast.makeText(HomeActivity.this,"输入内容不能为空",Toast.LENGTH_SHORT).show();
                }

            }
        });
         //取消按钮
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              dialog.dismiss();//隐藏对话框
            }
        });

        dialog.show();
    }

    class HomeAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mItems.length;
        }

        @Override
        public Object getItem(int position) {
            return mItems[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view  = View.inflate(HomeActivity.this,R.layout.home_list_item,null);
            ImageView ivItem = (ImageView)view.findViewById(R.id.iv_item);
            TextView tvItem = (TextView)view.findViewById(R.id.tv_item);
            tvItem.setText(mItems[position]);
            ivItem.setImageResource(mPics[position]);
            return view;
        }
    }
}
