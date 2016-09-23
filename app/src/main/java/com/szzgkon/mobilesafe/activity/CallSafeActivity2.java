package com.szzgkon.mobilesafe.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
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

public class CallSafeActivity2 extends AppCompatActivity {

    private ListView list_view;
    private List<BlackNumberInfo> blackNumberInfos;
    private LinearLayout ll_pb;

    //当前页面
    private int mCurrentPageNumber = 0;
    //每页展示20条
    private int mPageSize = 20;
    private TextView tv_page_number;
    private BlackNumberDao dao;
    //共有多少页
    private int totalPage;
    private EditText et_pageNumber;
    private CallSafeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_safe);

        initUI();
        initData();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            ll_pb.setVisibility(View.INVISIBLE);
            adapter = new CallSafeAdapter(blackNumberInfos, CallSafeActivity2.this);
            list_view.setAdapter(adapter);
            tv_page_number.setText(mCurrentPageNumber + "/" + totalPage);

        }
    };

    private void initData() {

        new Thread() {
            @Override
            public void run() {
                super.run();
                dao = new BlackNumberDao(CallSafeActivity2.this);
                //通过总的记录数 / 每页多少条数据

                totalPage = dao.getTotalNumber() / mPageSize;


              blackNumberInfos = dao.findAllByPage(mCurrentPageNumber, mPageSize);
                handler.sendEmptyMessage(0);
            }
        }.start();

    }

    private void initUI() {
        ll_pb = (LinearLayout) findViewById(R.id.ll_pb);
        //展示加载的圆圈
        ll_pb.setVisibility(View.VISIBLE);
        list_view = (ListView) findViewById(R.id.list_view);

        tv_page_number = (TextView) findViewById(R.id.tv_page_number);
        et_pageNumber = (EditText) findViewById(R.id.et_pageNumber);
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
                convertView = View.inflate(CallSafeActivity2.this, R.layout.item_call_safe, null);
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
                        Toast.makeText(CallSafeActivity2.this,"删除成功",Toast.LENGTH_SHORT).show();
                         lists.remove(info);
                        adapter.notifyDataSetChanged();
                    }else {
                        Toast.makeText(CallSafeActivity2.this,"删除失败",Toast.LENGTH_SHORT).show();

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

    /**
     * 上一页
     *
     * @param view
     */
    public void prePage(View view) {
        if (mCurrentPageNumber <= 0) {
            Toast.makeText(this, "已经是第一页", Toast.LENGTH_SHORT).show();
            return;
        }
         mCurrentPageNumber--;
        initData();
    }

    /**
     * 下一页
     *
     * @param view
     */
    public void nextPage(View view) {
        //判断当前页码不能大于总的页数
        if (mCurrentPageNumber >= totalPage) {
            Toast.makeText(this, "已经是最后一页了", Toast.LENGTH_SHORT).show();
            return;
        }
        mCurrentPageNumber++;
        initData();

    }

    /**
     * 跳转
     *
     * @param view
     */
    public void jump(View view) {
        String str_page_number = et_pageNumber.getText().toString().trim();
            if(TextUtils.isEmpty(str_page_number)){
                Toast.makeText(this,"请输入正确的页码",Toast.LENGTH_SHORT).show();

            }else {
                int number = Integer.parseInt(str_page_number);
                if(number >= 0 && number <= totalPage){
                    mCurrentPageNumber = number;
                    initData();
                }else {
                    Toast.makeText(this,"请输入正确的页码",Toast.LENGTH_SHORT).show();

                }
            }
    }


}
