package com.szzgkon.mobilesafe.activity;

import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.szzgkon.mobilesafe.R;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * ===================================================
 * <p/>
 * 版权：深圳市中广控信息科技有限公司 版权所有（c）2016
 * <p/>
 * 作者：张勇柯
 * <p/>
 * 版本：1.0
 * <p/>
 * 创建日期：16/10/12 下午2:54
 * <p/>
 * 描述：缓存清理
 * <p/>
 * 修订历史：
 * <p/>
 * ===================================================
 **/


public class CleanCacheActivity extends AppCompatActivity {

    private PackageManager packageManager;
    private ArrayList<CacheInfo> cacheLists;
    private ListView list_view;
    private CacheAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();

    }

    private void initUI() {
        setContentView(R.layout.activity_clean_cache);
        list_view = (ListView)findViewById(R.id.list_view);

        //缓存的集合
        cacheLists = new ArrayList<CacheInfo>();
        packageManager = getPackageManager();
        /**
         * 接收2个参数
         * 第一个参数接收一个包名
         * 第二个参数接收aidl的对象
         */
        /*
        * @hide
       出现@hide就是被系统隐藏了，要想拿到这个方法，就得使用反射的方法
        public abstract void getPackageSizeInfo(String packageName, int userHandle,
        IPackageStatsObserver observer);
       */

        new Thread() {
            @Override
            public void run() {
                super.run();

                List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
                for (PackageInfo packageInfo : installedPackages) {
                    getCacheSize(packageInfo);
                }
                handler.sendEmptyMessage(0);
            }
        }.start();


    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            adapter = new CacheAdapter();

            list_view.setAdapter(adapter);

        }
    };

    private class CacheAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return cacheLists.size();
        }

        @Override
        public Object getItem(int position) {
            return cacheLists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                view = View.inflate(CleanCacheActivity.this, R.layout.item_clean_cache, null);
                holder.icon = (ImageView) view.findViewById(R.id.iv_icon);
                holder.name = (TextView) view.findViewById(R.id.tv_name);
                holder.cacheSize = (TextView) view.findViewById(R.id.tv_cache_size);

                view.setTag(holder);

            }else {
                view = convertView;

                holder = (ViewHolder)view.getTag();
            }
            holder.icon.setImageDrawable(cacheLists.get(position).icon);
            holder.name.setText(cacheLists.get(position).appName);
            holder.cacheSize.setText("缓存大小" + Formatter.formatFileSize(CleanCacheActivity.this,
                                        cacheLists.get(position).cacheSize));


            return view;
        }
    }

    static class ViewHolder {
        ImageView icon;
        TextView name;
        TextView cacheSize;
    }

    //获取到缓存的大小
    private void getCacheSize(PackageInfo packageInfo) {
        try {
//            Class<?> clazz = getClassLoader().loadClass("packageManager");

            //通过反射获取到当前的方法
            Method method = PackageManager.class.getDeclaredMethod("getPackageSizeInfo",
                    String.class, IPackageStatsObserver.class);

            /**
             * 第一个参数表示当前的这个方法由谁调用的
             *
             * 第二个参数表示包名
             */
            method.invoke(packageManager, packageInfo.applicationInfo.packageName, new MyIPackageStatsObserver(packageInfo));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class MyIPackageStatsObserver extends IPackageStatsObserver.Stub {
        private PackageInfo packageInfo;

        public MyIPackageStatsObserver(PackageInfo packageInfo) {
            this.packageInfo = packageInfo;
        }

        @Override
        public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {

            //获取到当前手机应用的缓存大小
            long cacheSize = pStats.cacheSize;

            //如果当前的缓存大小大于0的话，说明有缓存
            if (cacheSize > 0) {
                System.out.println("当前应用的名字" +
                        packageInfo.applicationInfo.loadLabel(packageManager) + "缓存大小" + cacheSize);

                CacheInfo cacheInfo = new CacheInfo();

                Drawable icon = packageInfo.applicationInfo.loadIcon(packageManager);

                cacheInfo.icon = icon;
                String appName = packageInfo.applicationInfo.loadLabel(packageManager).toString();
                cacheInfo.appName = appName;

                cacheInfo.cacheSize = cacheSize;


                cacheLists.add(cacheInfo);

            }

        }

    }

    static class CacheInfo {
        Drawable icon;
        long cacheSize;
        String appName;
    }

    /**
     * 全部清除
     */
//    public void cleanAll(View view){
//        //获取到当前应用程序里面所有的方法
//        Method[] methods = PackageManager.class.getMethods();
//
//        for (Method  method: methods) {
//            //判断当前的方法名字
//            if(method.getName().equals("freeStorageAndNotify")){
//                method.invoke(packageManager,Integer.MAX_VALUE,)
//            }
//        }
//    }

}
