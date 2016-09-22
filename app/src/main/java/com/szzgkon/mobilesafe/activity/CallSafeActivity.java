package com.szzgkon.mobilesafe.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.szzgkon.mobilesafe.R;
import com.szzgkon.mobilesafe.adapter.MyBaseAdapter;
import com.szzgkon.mobilesafe.bean.BlackNumberInfo;
import com.szzgkon.mobilesafe.db.dao.BlackNumberDao;

import java.util.List;

public class CallSafeActivity extends AppCompatActivity {

    private ListView list_view;
    private List<BlackNumberInfo> blackNumberInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_safe);

        initUI();
        initData();
    }

    private void initData() {


        BlackNumberDao dao = new BlackNumberDao(this);
        blackNumberInfos = dao.findAll();

        CallSafeAdapter adapter = new CallSafeAdapter(blackNumberInfos,this);
        list_view.setAdapter(adapter);
    }

    private void initUI() {
        list_view = (ListView) findViewById(R.id.list_view);
    }


    private class CallSafeAdapter extends MyBaseAdapter<BlackNumberInfo>{

        public CallSafeAdapter(List lists, Context mContext) {
            super(lists, mContext);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            /**
             * 最新的列表优化写法
             */

            ViewHolder holder;
            if(convertView == null){
                convertView = View.inflate(CallSafeActivity.this, R.layout.item_call_safe, null);
                 holder = new ViewHolder();
                holder.tv_number = (TextView) convertView.findViewById(R.id.tv_number);
                 holder.tv_mode = (TextView) convertView.findViewById(R.id.tv_mode);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }



            holder.tv_number.setText(lists.get(position).getNumber());

            String mode = lists.get(position).getMode();
            if(mode.equals("1")){
                holder.tv_mode.setText("全部拦截+短信");
            }else if(mode.equals("2")){
                holder.tv_mode.setText("电话拦截");
            }else if(mode.equals("3")){
                holder.tv_mode.setText("短信拦截");
            }


            return convertView;
        }
    }

    static  class ViewHolder{
        TextView tv_number;
        TextView tv_mode;
    }


}
