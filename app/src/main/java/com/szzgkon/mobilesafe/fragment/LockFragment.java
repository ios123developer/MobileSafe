package com.szzgkon.mobilesafe.fragment;


import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.szzgkon.mobilesafe.R;
import com.szzgkon.mobilesafe.bean.AppInfo;
import com.szzgkon.mobilesafe.db.dao.AppLockDao;
import com.szzgkon.mobilesafe.engine.AppInfos;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
/**
 * ===================================================
 *
 * 版权：深圳市中广控信息科技有限公司 版权所有（c）2016
 *
 * 作者：张勇柯
 *
 * 版本：1.0
 *
 * 创建日期：16/10/11 下午4:05
 *
 * 描述：已经加锁的软件界面
 *
 * 修订历史：
 *
 * ===================================================
 **/

public class LockFragment extends Fragment {


    private ListView list_view;
    private TextView tv_lock;
    private ArrayList<AppInfo> lockLists;
    private AppLockDao dao;
    private LockAdapter adapter;

    public LockFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_lock_fragment, null);
        list_view = (ListView) view.findViewById(R.id.list_view);
        tv_lock = (TextView) view.findViewById(R.id.tv_lock);


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    //拿到所有的应用程序
        List<AppInfo> appInfos = AppInfos.getAppInfos(getActivity());

        //初始化一个加锁的集合
        lockLists = new ArrayList<AppInfo>();


        dao = new AppLockDao(getActivity());

        for (AppInfo  appInfo: appInfos) {
             //如果能找到当前的包名说明在程序锁的数据库里面
            if(dao.find(appInfo.getApkPackName())){
               lockLists.add(appInfo);
            }else {

            }
        }


        adapter = new LockAdapter();
        list_view.setAdapter(adapter);
    }

    private class LockAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            tv_lock.setText("已加锁（" + lockLists.size() + "）个");

            return lockLists.size();
        }

        @Override
        public Object getItem(int position) {
            return lockLists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
          final   View view ;
            ViewHolder holder = null;

            if(convertView == null){
                holder = new ViewHolder();
                view = View.inflate(getActivity(), R.layout.item_lock, null);
                 holder.iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
                holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
                holder.iv_lock = (ImageView) view.findViewById(R.id.iv_lock);

                view.setTag(holder);

            }else {
                view = convertView;
                holder = (ViewHolder)view.getTag();

            }

         final    AppInfo appInfo = lockLists.get(position);
            holder.iv_icon.setImageDrawable(appInfo.getIcon());
            holder.tv_name.setText(appInfo.getApkName());
            holder.iv_lock.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    // 初始化一个位移动画


                    TranslateAnimation translateAnimation = new TranslateAnimation(
                            Animation.RELATIVE_TO_SELF, 0,
                            Animation.RELATIVE_TO_SELF, -1.0f,
                            Animation.RELATIVE_TO_SELF, 0,
                            Animation.RELATIVE_TO_SELF, 0);
//                    // 设置动画时间
                    translateAnimation.setDuration(1000);
//                    // 开始动画
                    view.startAnimation(translateAnimation);


                    new Thread() {
                        public void run() {
                            SystemClock.sleep(1000);

                            getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    // TODO Auto-generated method stub
                                    // 添加到数据库里面
                                    dao.delete(appInfo.getApkPackName());
                                    // 从当前的页面移除对象
                                    lockLists.remove(position);
                                    // 刷新界面
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }

                        ;
                    }.start();


                }
            });


            return view;
        }
    }

    static class ViewHolder{
        ImageView iv_icon;
        TextView tv_name;
        ImageView iv_lock;
    }
}
