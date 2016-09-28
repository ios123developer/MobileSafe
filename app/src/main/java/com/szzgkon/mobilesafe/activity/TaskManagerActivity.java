package com.szzgkon.mobilesafe.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.szzgkon.mobilesafe.R;
import com.szzgkon.mobilesafe.bean.TaskInfo;
import com.szzgkon.mobilesafe.engine.TaskInfoParser;
import com.szzgkon.mobilesafe.utils.ServiceStatusUtils;
import com.szzgkon.mobilesafe.utils.SharedPreferensUtils;
import com.szzgkon.mobilesafe.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class TaskManagerActivity extends Activity {
    //    @ViewInject(R.id.tv_task_process_count)
    private TextView tv_task_process_count;

    //    @ViewInject(R.id.tv_task_memory)
    private TextView tv_task_memory;

    //    @ViewInject(R.id.list_view)
    private ListView list_view;
    private long totalMem;
    private List<TaskInfo> taskInfos;
    private ArrayList<TaskInfo> userTaskInfos;
    private ArrayList<TaskInfo> systemTaskInfos;
    private Object object;
    private ViewHolder holder;
    private TaskManagerAdapter adapter;
    private int processCount;
    private long availMem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        initUI();
        initData();
    }


    class TaskManagerAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (SharedPreferensUtils.getBoolean(TaskManagerActivity.this, "is_show_system", false)) {
                return userTaskInfos.size() + 1 + systemTaskInfos.size() + 1;
            } else {
                return userTaskInfos.size() + 1;
            }

        }

        @Override
        public Object getItem(int position) {
            if (position == 0) {
                return null;
            } else if (position == userTaskInfos.size() + 1) {
                return null;
            }
            TaskInfo taskInfo;

            if (position < (userTaskInfos.size() + 1)) {
                //用户程序
                taskInfo = userTaskInfos.get(position - 1);//多了一个textview的标签，位置需要减一

            } else {
                //系统程序
                int location = position - 1 - userTaskInfos.size() - 1;
                taskInfo = systemTaskInfos.get(location);
            }

            return taskInfo;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //如果当前的position等于0 表示应用程序
            if (position == 0) {

                TextView textView = new TextView(TaskManagerActivity.this);

                textView.setTextColor(Color.WHITE);

                textView.setBackgroundColor(Color.GRAY);

                textView.setText("用户程序(" + userTaskInfos.size() + ")");

                return textView;
                //表示系统程序
            } else if (position == userTaskInfos.size() + 1) {


                TextView textView = new TextView(TaskManagerActivity.this);


                textView.setTextColor(Color.WHITE);

                textView.setBackgroundColor(Color.GRAY);

                textView.setText("系统程序(" + systemTaskInfos.size() + ")");

                return textView;

            }

            ViewHolder holder;
            View view;
            if (convertView != null && convertView instanceof LinearLayout) {
                view = convertView;
                holder = (ViewHolder) view.getTag();

            } else {
                view = View.inflate(TaskManagerActivity.this, R.layout.item_task_manager, null);
                holder = new ViewHolder();

                holder.iv_app_icon = (ImageView) view.findViewById(R.id.iv_app_icon);
                holder.tv_app_name = (TextView) view.findViewById(R.id.tv_app_name);
                holder.tv_app_memory_size = (TextView) view.findViewById(R.id.tv_app_memory_size);
                holder.cb_app_status = (CheckBox) view.findViewById(R.id.cb_app_status);

                view.setTag(holder);

            }


            TaskInfo taskInfo;

            if (position < (userTaskInfos.size() + 1)) {
                //用户程序
                taskInfo = userTaskInfos.get(position - 1);//多了一个textview的标签，位置需要减一

            } else {
                //系统程序
                int location = position - 1 - userTaskInfos.size() - 1;
                taskInfo = systemTaskInfos.get(location);
            }

            holder.iv_app_icon.setImageDrawable(taskInfo.getIcon());

            holder.tv_app_name.setText(taskInfo.getAppName());

            holder.tv_app_memory_size.setText("内存占用：" + Formatter.formatFileSize(TaskManagerActivity.this, taskInfo.getMemorySize()));

            if (taskInfo.isChecked()) {
                holder.cb_app_status.setChecked(true);

            } else {
                holder.cb_app_status.setChecked(false);

            }
            return view;
        }
    }

    static class ViewHolder {
        ImageView iv_app_icon;
        TextView tv_app_name;
        TextView tv_app_memory_size;
        CheckBox cb_app_status;
    }

    //初始化数据
    private void initData() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                taskInfos = TaskInfoParser.getTaskInfos(TaskManagerActivity.this);

                userTaskInfos = new ArrayList<TaskInfo>();
                systemTaskInfos = new ArrayList<TaskInfo>();

                for (TaskInfo taskInfo : taskInfos) {
                    if (taskInfo.isUserApp()) {
                        userTaskInfos.add(taskInfo);
                    } else {
                        systemTaskInfos.add(taskInfo);
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new TaskManagerAdapter();
                        list_view.setAdapter(adapter);
                    }
                });

            }
        }.start();

    }

    /**
     * PackageManager 和 ActivityManager的区别
     * ActivityManager ：活动管理器（任务管理器）
     * PackageManager ：包管理器
     */


    //初始化UI
    private void initUI() {
        ViewUtils.inject(this);
        setContentView(R.layout.activity_task_manager);

        tv_task_process_count = (TextView) findViewById(R.id.tv_task_process_count);
        tv_task_memory = (TextView) findViewById(R.id.tv_task_memory);
        list_view = (ListView) findViewById(R.id.list_view);
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                object = list_view.getItemAtPosition(position);

                holder = (ViewHolder) view.getTag();

                if (object != null && object instanceof TaskInfo) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TaskInfo taskInfo = (TaskInfo) object;

                            if (taskInfo.isChecked()) {
                                taskInfo.setChecked(false);
                                holder.cb_app_status.setChecked(false);
                            } else {
                                taskInfo.setChecked(true);
                                holder.cb_app_status.setChecked(true);

                            }
                        }
                    });
                }
            }
        });

        processCount = ServiceStatusUtils.getProcessCount(this);

        tv_task_process_count.setText("进程：" + processCount + "个");

        //可用内存
        availMem = ServiceStatusUtils.getAvailMem(this);
        //总内存
        totalMem = ServiceStatusUtils.getTotalMem(this);
        //总内存
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {


            tv_task_memory.setText("剩余/总内存：" +
                    Formatter.formatFileSize(TaskManagerActivity.this, availMem) +
                    "/" + Formatter.formatFileSize(TaskManagerActivity.this,
                    this.totalMem));

        }
    }

    /**
     * 全选功能
     *
     * @param view
     */
    public void selectAll(View view) {
        for (TaskInfo taskInfo : userTaskInfos) {

            //判断当前的用户程序是不是自己的程序，如果是自己的程序就把checkbox隐藏掉

            taskInfo.setChecked(true);

            if (taskInfo.getPackageName().equals(getPackageName())) {

                continue;
            }
        }
        for (TaskInfo taskInfo : systemTaskInfos) {
            taskInfo.setChecked(true);
        }

        //一定要刷新下页面，不然无法显示

        adapter.notifyDataSetChanged();
    }

    /**
     * 反选功能
     *
     * @param view
     */
    public void selectOpposite(View view) {
        for (TaskInfo taskInfo : userTaskInfos) {
            taskInfo.setChecked(!taskInfo.isChecked());

            if (taskInfo.getPackageName().equals(getPackageName())) {

                continue;
            }
        }
        for (TaskInfo taskInfo : systemTaskInfos) {
            taskInfo.setChecked(!taskInfo.isChecked());

        }

        //一定要刷新下页面，不然无法显示

        adapter.notifyDataSetChanged();
    }

    /**
     * 清理进程
     *
     * @param view
     */
    public void killProcess(View view) {
        //如果要杀死进程，首先必须得到进程管理器
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        //清理进程的集合
        ArrayList<TaskInfo> killLists = new ArrayList<>();


        //清理进程的总个数

        int totalCount = 0;

        //清理的进程的大小
        int killAvailMem = 0;

        for (TaskInfo taskInfo : userTaskInfos) {

            if (taskInfo.isChecked()) {
                killLists.add(taskInfo);
                totalCount++;
                killAvailMem += taskInfo.getMemorySize();

                //杀死进程 参数是包名
                activityManager.killBackgroundProcesses(taskInfo.getPackageName());
            }

        }

        for (TaskInfo taskInfo : systemTaskInfos) {

            if (taskInfo.isChecked()) {
                killLists.add(taskInfo);
                totalCount++;
                killAvailMem += taskInfo.getMemorySize();
                //杀死进程 参数是包名
                activityManager.killBackgroundProcesses(taskInfo.getPackageName());
            }
        }


        /**
         * 注意：当集合在快速遍历的时候，不能修改集合的大小（ 例如删除元素），解决方法是放入另外一个数组
         */
        for (TaskInfo taskInfo : killLists) {
            //判断是否是用户app
            if (taskInfo.isUserApp()) {
                userTaskInfos.remove(taskInfo);
                //杀死进程 参数是包名
                activityManager.killBackgroundProcesses(taskInfo.getPackageName());
            } else {
                systemTaskInfos.remove(taskInfo);
                //杀死进程 参数是包名
                activityManager.killBackgroundProcesses(taskInfo.getPackageName());
            }


        }

        ToastUtils.showToast(TaskManagerActivity.this, "共清理了" + totalCount + "个进程，释放了" +
                Formatter.formatFileSize(TaskManagerActivity.this, killAvailMem) + "内存");

        //清理过后剩余的进程
        processCount -= totalCount;
        tv_task_process_count.setText("进程：" + processCount + "个");

        availMem += killAvailMem;
        tv_task_memory.setText("剩余/总内存：" +
                Formatter.formatFileSize(TaskManagerActivity.this, availMem) +
                "/" + Formatter.formatFileSize(TaskManagerActivity.this,
                this.totalMem));

        //刷新界面
        adapter.notifyDataSetChanged();
    }

    /**
     * 打开设置界面
     *
     * @param view
     */
    public void openSetting(View view) {
        Intent intent = new Intent(this, TaskManagerSettingActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}
