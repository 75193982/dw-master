package com.xgx.dw.upload;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;


import com.xgx.dw.vo.response.QueryResult;

import java.io.File;

public class DownloadService extends Service {

    /**
     * 安卓系统下载类
     **/
    DownloadManager manager;

    /**
     * 接收下载完的广播
     **/
    DownloadCompleteReceiver receiver;
    long lastDownloadId = -1;
    private String appname = "dw.apk";

    /**
     * 初始化下载器
     *
     * @param response
     **/
    private void initDownManager(QueryResult.UploadResponse response) {
        manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        receiver = new DownloadCompleteReceiver();
        // 设置下载地址
        Request down = new Request(Uri.parse(response.getApkPath()));
        down.setTitle("APP新版本" + response.getVersionName() + "更新");
        // 设置允许使用的网络类型，这里是移动网络和wifi都可以
        down.setAllowedNetworkTypes(Request.NETWORK_MOBILE | Request.NETWORK_WIFI);
        down.setMimeType("application/vnd.android.package-archive");
        // 下载时，通知栏显示途中
        down.setNotificationVisibility(Request.VISIBILITY_VISIBLE);
        // 显示下载界面
        down.setVisibleInDownloadsUi(true);

        // 设置下载后文件存放的位置
        down.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, appname);
        // 将下载请求放入队列
        // 判断本地是否已经存在 apk
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), appname);
        if (file.exists() && (file.length() + "").equals(response.getApkSize())) {
            installAPK(Uri.fromFile(file));
        } else {
            if (lastDownloadId == -1) {
                lastDownloadId = manager.enqueue(down);
            }

        }

        // 注册下载广播
        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            QueryResult.UploadResponse response = (QueryResult.UploadResponse) intent.getSerializableExtra("response");
            // 调用下载
            initDownManager(response);
        } catch (Exception e) {
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onDestroy() {

        // 注销下载广播
        if (receiver != null) unregisterReceiver(receiver);

        super.onDestroy();
    }

    // 接受下载完成后的intent
    class DownloadCompleteReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            // 判断是否下载完成的广播
            if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                try {
                    // 获取下载的文件id
                    // long downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

                    // 自动安装apk
                    File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), appname);
                    installAPK(Uri.fromFile(file));
                    // installAPK(manager.getUriForDownloadedFile(downId));

                    // 停止服务并关闭广播
                    DownloadService.this.stopSelf();
                } catch (Exception e) {
                }

            }
        }

    }

    /**
     * 安装apk文件
     */
    private void installAPK(Uri apk) {
        Intent localIntent = new Intent("android.intent.action.VIEW");
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        localIntent.setDataAndType(apk, "application/vnd.android.package-archive");
        startActivity(localIntent);
        // 通过Intent安装APK文件

    }
}