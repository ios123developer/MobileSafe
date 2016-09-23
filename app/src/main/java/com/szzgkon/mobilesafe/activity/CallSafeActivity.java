package com.szzgkon.mobilesafe.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.szzgkon.mobilesafe.R;
import com.szzgkon.mobilesafe.adapter.MyBaseAdapter;
import com.szzgkon.mobilesafe.bean.BlackNumberInfo;
import com.szzgkon.mobilesafe.db.dao.BlackNumberDao;

import java.util.List;

public class CallSafeActivity extends AppCompatActivity {

    private ListView list_view;
    private List<BlackNumberInfo> blackNumberInfos;
    private LinearLayout ll_pb;

    private BlackNumberDao dao;
    //共有多少页
    private int totalPage;
    private CallSafeAdapter adapter;

    /**
     * 开始的位置
     */
    private int startIndex = 0;

    /**
     * 每页展示20条数据
     */
    private int maxCount = 20;
    private int totalNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_safe2);

        initUI();
        initData();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            ll_pb.setVisibility(View.INVISIBLE);
            if(adapter == null){
                adapter = new CallSafeAdapter(blackNumberInfos, CallSafeActivity.this);
                list_view.setAdapter(adapter);

            }else {
                adapter.notifyDataSetChanged();
            }

        }
    };

    private void initData() {
        dao = new BlackNumberDao(CallSafeActivity.this);
       //总数据条数
        totalNumber = dao.getTotalNumber();
        new Thread() {
            @Override
            public void run() {

                //分批加载数据
                if(blackNumberInfos == null){
                    blackNumberInfos = dao.findAllByBatch(startIndex,maxCount);

                }else {
                    //把后面的数据追加到blackNumberInfos中，防止黑名单被覆盖
                    blackNumberInfos.addAll(dao.findAllByBatch(startIndex,maxCount));
                }
                handler.sendEmptyMessage(0);
            }
        }.start();

    }

    /**
     * 添加黑名单
     * @param view
     */
    public void addBlackNumber(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();

        View dialog_view = View.inflate(this, R.layout.dialog_add_black_number, null);
        final EditText et_black_number =  (EditText) dialog_view.findViewById(R.id.et_black_number);

        Button btn_ok = (Button) dialog_view.findViewById(R.id.btn_ok);
        Button btn_cancel = (Button) dialog_view.findViewById(R.id.btn_cancel);

        final CheckBox cb_phone = (CheckBox)dialog_view.findViewById(R.id.cb_phone);
        final CheckBox cb_sms = (CheckBox)dialog_view.findViewById(R.id.cb_sms);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              String  str_number =   et_black_number.getText().toString().trim();
                if(TextUtils.isEmpty(str_number)){
                    Toast.makeText(CallSafeActivity.this,"请输入黑名单号码",Toast.LENGTH_SHORT).show();
                    return;
                }
                String mode = "";
                if(cb_phone.isChecked() && cb_sms.isChecked()){
                    mode = "1";
                }else if(cb_phone.isChecked()){
                    mode = "2";
                }else if(cb_sms.isChecked()){
                    mode = "3";
                }else {
                    Toast.makeText(CallSafeActivity.this,"请勾选拦截模式",Toast.LENGTH_SHORT).show();
                    return;
                }
                BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
                blackNumberInfo.setNumber(str_number);
                blackNumberInfo.setMode(mode);
                blackNumberInfos.add(0,blackNumberInfo);

                //把电话号码和拦截模式添加到数据库
                dao.add(str_number,mode);

              if(adapter == null){
                  adapter =  new CallSafeAdapter(blackNumberInfos,CallSafeActivity.this);
                  list_view.setAdapter(adapter);
              }else {
                  adapter.notifyDataSetChanged();
              }

               dialog.dismiss();
            }
        });
        dialog.setView(dialog_view);
        dialog.show();
    }

    private void initUI() {
        ll_pb = (LinearLayout) findViewById(R.id.ll_pb);
        //展示加载的圆圈
        ll_pb.setVisibility(View.VISIBLE);
        list_view = (ListView) findViewById(R.id.list_view);
        //设置listview的滚动监听
         list_view.setOnScrollListener(new AbsListView.OnScrollListener() {
             //状态改变时候 回调的方法
             @Override
             public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch(scrollState){
                case SCROLL_STATE_IDLE://闲置
                      //获取到最后一条显示的数据
                  int lastVisiblePosition = list_view.getLastVisiblePosition();

                    if(lastVisiblePosition == blackNumberInfos.size() - 1){

                        startIndex += maxCount;

                        if(lastVisiblePosition > totalNumber){
                            Toast.makeText(CallSafeActivity.this,"已经没有数据了",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        initData();
                    }
                     break;
                case SCROLL_STATE_TOUCH_SCROLL:

                     break;
                case SCROLL_STATE_FLING://惯性

                     break;

                default:
                     break;



                }

             }
              //lisiview滚动的时候调的方法
             @Override
             public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

             }
         });

    }


    private class CallSafeAdapter extends MyBaseAdapter<BlackNumberInfo> {

        public CallSafeAdapter(List lists, Context mContext) {
            super(lists, mContext);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            /**
             * 最新的列表优化写法
             */

            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(CallSafeActivity.this, R.layout.item_call_safe, null);
                holder = new ViewHolder();
                holder.tv_number = (TextView) convertView.findViewById(R.id.tv_number);
                holder.tv_mode = (TextView) convertView.findViewById(R.id.tv_mode);
                holder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            holder.tv_number.setText(lists.get(position).getNumber());

            String mode = lists.get(position).getMode();
            if (mode.equals("1")) {
                holder.tv_mode.setText("全部拦截+短信");
            } else if (mode.equals("2")) {
                holder.tv_mode.setText("电话拦截");
            } else if (mode.equals("3")) {
                holder.tv_mode.setText("短信拦截");
            }
           final BlackNumberInfo info = lists.get(position);
            holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String number = info.getNumber();
                   boolean result = dao.delete(number);
                    if(result){
                        Toast.makeText(CallSafeActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                         lists.remove(info);
                        adapter.notifyDataSetChanged();
                    }else {
                        Toast.makeText(CallSafeActivity.this,"删除失败",Toast.LENGTH_SHORT).show();

                    }
                }
            });

            return convertView;
        }
    }

    static class ViewHolder {
        TextView tv_number;
        TextView tv_mode;
        ImageView iv_delete;
    }




}
