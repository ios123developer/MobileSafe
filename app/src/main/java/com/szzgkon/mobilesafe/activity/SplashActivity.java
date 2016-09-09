package com.szzgkon.mobilesafe.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.szzgkon.mobilesafe.R;
import com.szzgkon.mobilesafe.utils.StreamUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SplashActivity extends AppCompatActivity {
    private static final int CODE_UPDATE_DIALOG = 0;
    private static final int CODE_NET_ERROR = 1;
    private static final int CODE_JSON_ERROR = 2;
    private static final int CODE_ENTER_HOME = 3;//进入主界面

    private TextView tvVersion;
    private TextView tvProgress;//下载进度展示

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
                    enterHome();
                    break;
                case CODE_JSON_ERROR:
                    Toast.makeText(SplashActivity.this,"json解析错误",Toast.LENGTH_SHORT).show();
                        enterHome();
                    break;
                case CODE_ENTER_HOME:
                    enterHome();
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

        tvProgress = (TextView)findViewById(R.id.tv_progress);

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
        final long startTime = System.currentTimeMillis();

        final Thread thread =  new Thread(){
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
                            }else {
                                //没有版本更新
                                msg.what = CODE_ENTER_HOME;
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
                    long endTime = System.currentTimeMillis();
                    long timeUsed = endTime - startTime;//访问网络花费的时间
                    //强制休眠一段时间，保证闪屏页展示2秒钟
                     if(timeUsed < 2000){
                         try {
                             Thread.sleep(2000-timeUsed);
                         } catch (InterruptedException e) {
                             e.printStackTrace();
                         }
                     }
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
//        builder.setCancelable(false);//不让用户取消对话框，用户体验太差
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("立即更新");
                download();

            }
        });
        builder.setNegativeButton("以后在说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enterHome();
            }
        });
        //设置取消的监听，用户返回键时会触发
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
            enterHome();
            }
        });
        builder.show();
    }
    /**
     * 下载apk文件
     */
    private void download(){
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            tvProgress.setVisibility(View.VISIBLE);//显示进度
            String target = Environment.getExternalStorageDirectory() + "/update.apk";
            //xutis
            HttpUtils utils = new HttpUtils();
            utils.download(mDownloadUrl, target, new RequestCallBack<File>() {
                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    Toast.makeText(SplashActivity.this,"下载成功",Toast.LENGTH_SHORT).show();
                       //跳转到系统下载界面
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.setDataAndType(Uri.fromFile(responseInfo.result),"application/vnd.android.package-archive");
//                    startActivity(intent);
                    startActivityForResult(intent,0);//如果用户取消安装的话会返回结果,然后回调onActivityResult方法
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    Toast.makeText(SplashActivity.this,"下载失败",Toast.LENGTH_SHORT).show();
                }

                /**
                 * 下载文件的进度函数
                 * @param total
                 * @param current
                 * @param isUploading
                 */
                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    super.onLoading(total, current, isUploading);
                    System.out.println("下载进度："+current+"/"+total);
                    tvProgress.setText("下载进度："+current * 100/total +"%");
                }
            });
        }else {
            Toast.makeText(SplashActivity.this,"sd卡没有就绪",Toast.LENGTH_SHORT).show();
        }

    }
     //如果用户取消安装的话，回调此方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        enterHome();
    }

    /**
     *进入主界面
     */
    private void enterHome(){
        Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);
    }
}
