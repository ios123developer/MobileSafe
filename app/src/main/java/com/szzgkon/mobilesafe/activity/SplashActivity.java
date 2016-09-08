package com.szzgkon.mobilesafe.activity;

import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.szzgkon.mobilesafe.R;
import com.szzgkon.mobilesafe.utils.StreamUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SplashActivity extends AppCompatActivity {
    private static final int CODE_UPDATE_DIALOG = 0;
    private static final int CODE_NET_ERROR = 1;
    private static final int CODE_JSON_ERROR = 2;
    private TextView tvVersion;
    private String mVrsionName;//版本名字
    private int mVrsionCode;//版本号
    private String mDesc;//版本描述
    private String mDownloadUrl;//下载路径
    private Thread thread;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
            case CODE_UPDATE_DIALOG:
                showUpdateDialog();
                 break;
                case CODE_NET_ERROR:
                    Toast.makeText(SplashActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
                    break;
                case CODE_JSON_ERROR:
                    Toast.makeText(SplashActivity.this,"json解析错误",Toast.LENGTH_SHORT).show();

                    break;

            default:
                 break;



            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        tvVersion = (TextView)findViewById(R.id.tv_version);
        tvVersion.setText("版本号：" + getVersionName());
        checkVerson();
    }
    private String getVersionName(){
       PackageManager packageManager = getPackageManager();
        try {
            //获取包的信息
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(),0);
            int versionCode = packageInfo.versionCode;
            String versionName = packageInfo.versionName;
            System.out.println("versionName=" + versionName + ":versionCode"+versionCode);
        return versionName;
        } catch (Exception e) {
            //没有找到包名的时候回走此异常
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 本地app的versionCode
     * @return
     */
    private int getVersionCode(){
        PackageManager packageManager = getPackageManager();
        try {
            //获取包的信息
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(),0);
            int versionCode = packageInfo.versionCode;
            return versionCode;
        } catch (Exception e) {
            //没有找到包名的时候回走此异常
            e.printStackTrace();
        }
        return -1;
    }
    /**
     * 从服务器获取版本信息进行校验
     */
    private void checkVerson(){
        Thread thread =  new Thread(){
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                HttpURLConnection conn = null;
                try {
                    URL url = new URL("http://10.0.3.2:8080/update.json");
                    conn = (HttpURLConnection)url.openConnection();
                    conn.setRequestMethod("GET");//设置请求方法
                    conn.setConnectTimeout(5000);//设置连接超时
                    conn.setReadTimeout(5000);//设置响应超时，连接上了，但是服务器迟迟不给响应
                    conn.connect();
                    int responseCode = conn.getResponseCode();
                    if(responseCode == 200){
                     InputStream inputStream = conn.getInputStream();
                    String result = StreamUtils.readFromStream(inputStream);
                        //json解析
                        try {
                            JSONObject jo = new JSONObject(result);
                            mVrsionName = jo.getString("versionName");
                            mVrsionCode = jo.getInt("versionCode");
                            mDesc = jo.getString("description");
                            mDownloadUrl = jo.getString("downloadUrl");

//                            System.out.println("版本描述:" + mDesc);
                            if(mVrsionCode > getVersionCode()){//判断是否有更新
                                //服务的VersionCode大于本地的VersionCode
                                //说明有更新，弹出升级对话框
                                msg.what = CODE_UPDATE_DIALOG;
                            }
                        } catch (JSONException e) {
                            msg.what = CODE_NET_ERROR;
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    msg.what = CODE_JSON_ERROR;

                    e.printStackTrace();
                }finally {
                    handler.sendMessage(msg);
                    if(conn != null){
                        conn.disconnect();
                    }
                }
            }
        };
        thread.start();


    }

    public void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("最新版本" + mVrsionName);
        builder.setMessage(mDesc);
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("立即更新");
            }
        });
        builder.setNegativeButton("以后在说",null);
        builder.show();
    }

    /**
     *进入主界面
     */
    private void enterHome(){

    }
}
