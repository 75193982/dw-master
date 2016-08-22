package com.xgx.dw.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.xgx.dw.R;
import com.xgx.dw.upload.DownloadService;
import com.xgx.dw.utils.MyUtils;
import com.xgx.dw.vo.response.QueryResult;

import java.io.File;

public class UploadDialogActivity extends Activity {
    private TextView umeng_update_content;
    private Button umeng_update_id_ok;
    private Button umeng_update_id_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.umeng_update_dialog);
        final QueryResult.UploadResponse response = (QueryResult.UploadResponse) getIntent().getSerializableExtra("response");
        umeng_update_content = (TextView) findViewById(R.id.umeng_update_content);
        umeng_update_id_ok = (Button) findViewById(R.id.umeng_update_id_ok);
        umeng_update_id_cancel = (Button) findViewById(R.id.umeng_update_id_cancel);
        if (response != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("最新版本：" + response.getVersionName());
            sb.append("\n");
            try {
                sb.append("新版本大小：" + MyUtils.convertFileSize(Long.valueOf(response.getApkSize())));
            } catch (Exception e) {
            }
            sb.append("\n");
            sb.append("更新内容");
            sb.append("\n");
            sb.append(response.getRemark());
            umeng_update_content.setText(sb.toString());
            umeng_update_id_ok.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    String s = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/" + response.getApkName();
                    File file = new File(s);
                    try {
                        if (file.exists() && String.valueOf(file.length()) == response.getApkSize()) {
                            Intent localIntent = new Intent("android.intent.action.VIEW");
                            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            localIntent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                            startActivity(localIntent);
                            finish();
                            // 通过Intent安装APK文件
                        } else {
                            // 处理升级版本
                            // 比较是否 有下载好的文件 则直接安装
                            Intent service = new Intent(UploadDialogActivity.this, DownloadService.class);
                            service.putExtra("response", response);
                            startService(service);
                            finish();
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                    }

                }
            });
            umeng_update_id_cancel.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }
}
