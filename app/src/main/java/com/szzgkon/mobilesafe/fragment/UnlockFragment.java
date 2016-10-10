package com.szzgkon.mobilesafe.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.szzgkon.mobilesafe.R;
import com.szzgkon.mobilesafe.bean.AppInfo;
import com.szzgkon.mobilesafe.engine.AppInfos;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class UnlockFragment extends Fragment {


    private TextView tv_unlock;
    private ListView list_view;
    private View view;
    private List<AppInfo> appInfos;

    public UnlockFragment() {
        // Required empty public constructor
    }

    /**
     * 此方法只会被调用一次
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.item_unlock_fragment, null);

        list_view = (ListView) view.findViewById(R.id.list_view);

        tv_unlock = (TextView) view.findViewById(R.id.tv_unlock);


        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        appInfos = AppInfos.getAppInfos(getActivity());

        UnlockAdapter adapter = new UnlockAdapter();

        list_view.setAdapter(adapter);
    }

    public class UnlockAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return appInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return appInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            View view = null;

            if (convertView == null) {
                view = View.inflate(getActivity(), R.layout.item_unlock, null);
                holder = new ViewHolder();

                holder.iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
                holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
                holder.iv_unlock = (ImageView) view.findViewById(R.id.iv_unlock);
                view.setTag(holder);

            } else {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            }

            AppInfo appInfo = appInfos.get(position);

            holder.iv_icon.setImageDrawable(appInfo.getIcon());
            holder.tv_name.setText(appInfo.getApkName());

            return null;
        }
    }

    static class ViewHolder {
        ImageView iv_icon;
        TextView tv_name;
        ImageView iv_unlock;
    }
}
