package com.szzgkon.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.szzgkon.mobilesafe.R;

/**
 * Created by zhangyongke on 16/9/9.
 */

/**
 * 设置中心的自定义控件
 */
public class SettingItemView extends RelativeLayout{

    private TextView tvTitle;
    private TextView tvDesc;
    private CheckBox cbStatus;

    public SettingItemView(Context context) {
        super(context);
        initView();
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();

    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }
    /**
     * 初始化布局
     */
    private void initView(){
        //将自定义好的布局文件设置给当前的SettingItemView
        View.inflate(getContext(), R.layout.view_setting_item,this);
        tvTitle = (TextView)findViewById(R.id.tv_title);
        tvDesc = (TextView)findViewById(R.id.tv_desc);
        cbStatus = (CheckBox)findViewById(R.id.cb_status);

    }
    public void setTitle(String title){
        tvTitle.setText(title);
    }
    public void setDesc(String desc){
        tvDesc.setText(desc);
    }
    //判断当前勾选状态
    public boolean isChecked(){
        return cbStatus.isChecked();
    }
    public void setChecked(boolean check){
       cbStatus.setChecked(check);
    }
}
