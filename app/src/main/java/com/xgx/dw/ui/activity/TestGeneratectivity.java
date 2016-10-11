package com.xgx.dw.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.xgx.dw.R;
import com.xgx.dw.UserBean;
import com.xgx.dw.app.G;
import com.xgx.dw.dao.UserBeanDaoHelper;
import com.xgx.dw.utils.AES;
import com.xgx.dw.utils.BitmapUtil;
import com.xgx.dw.utils.FileInfoUtils;
import com.xgx.dw.utils.MyUtils;
import com.xgx.dw.utils.SdCardUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class TestGeneratectivity extends AppCompatActivity {
    private ImageView mChineseIv;
    private ImageView mEnglishIv;
    private ImageView mChineseLogoIv;
    private ImageView mEnglishLogoIv;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_generate);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        type = getIntent().getIntExtra("type", -1);
        initView();
        createQRCode();
    }

    private void initView() {
        mChineseIv = (ImageView) findViewById(R.id.iv_chinese);
        mChineseLogoIv = (ImageView) findViewById(R.id.iv_chinese_logo);
        mEnglishIv = (ImageView) findViewById(R.id.iv_english);
        mEnglishLogoIv = (ImageView) findViewById(R.id.iv_english_logo);
    }

    private void createQRCode() {
        //根据不同的业务需求创建二维码
        UserBean bean = new UserBean();
        switch (type) {
            case 0:
                bean.setIme(MyUtils.getuniqueId(this));
                break;
            case 1:
                String id = getIntent().getStringExtra("id");
                bean = UserBeanDaoHelper.getInstance().getDataById(id);
                break;
        }
        bean.setEcodeType(type + "");
        createChineseQRCode(new Gson().toJson(bean));
    }

    private void createChineseQRCode(String s) {
        String encryptResultStr = null;
        try {
            encryptResultStr = AES.encrypt(G.appsecret, s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(encryptResultStr)) {
            Toast.makeText(this, "当前没有用户", Toast.LENGTH_SHORT).show();
            return;
        }
        Bitmap mBitmap = CodeUtils.createImage(encryptResultStr, 400, 400, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        mChineseIv.setImageBitmap(mBitmap);
    }


    public void decodeChinese(View v) {
        mChineseIv.setDrawingCacheEnabled(true);
        Bitmap bitmap = mChineseIv.getDrawingCache();
        decode(bitmap, "解析中文二维码失败");
    }


    private void decode(Bitmap bitmap, final String errorTip) {
        /*
        这里为了偷懒，就没有处理匿名 AsyncTask 内部类导致 Activity 泄漏的问题
        请开发在使用时自行处理匿名内部类导致Activity内存泄漏的问题，处理方式可参考 https://github.com/GeniusVJR/LearningNotes/blob/master/Part1/Android/Android%E5%86%85%E5%AD%98%E6%B3%84%E6%BC%8F%E6%80%BB%E7%BB%93.md
         */
        String image_dir = Environment.getExternalStorageDirectory() + "/dw/Cache/ecodeEr";
        File image_dirs = new File(image_dir);
        if (!image_dirs.exists()) {
            image_dirs.mkdirs();
        }

        String filenewpath = image_dir + "/ecodeEr.jpg";
        File imageFile = new File(filenewpath);

        if (!imageFile.exists()) {
            try {
                imageFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        boolean b = BitmapUtil.saveBitmap(bitmap, imageFile);
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + imageFile)));
        try {
            CodeUtils.analyzeBitmap(filenewpath, new CodeUtils.AnalyzeCallback() {
                @Override
                public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                    String decryptString = "";
                    try {
                        decryptString = AES.decrypt(G.appsecret, result);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(TestGeneratectivity.this, decryptString, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAnalyzeFailed() {
                    Toast.makeText(TestGeneratectivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
//        new AsyncTask<Void, Void, String>() {
//            @Override
//            protected String doInBackground(Void... params) {
//                return QRCodeDecoder.syncDecodeQRCode(bitmap);
//            }
//
//            @Override
//            protected void onPostExecute(String result) {
//                if (TextUtils.isEmpty(result)) {
//                    Toast.makeText(TestGeneratectivity.this, errorTip, Toast.LENGTH_SHORT).show();
//                } else {
//                    String decryptString = "";
//                    try {
//                        decryptString = AES.decrypt("1396198677119910", result);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                    Toast.makeText(TestGeneratectivity.this, decryptString, Toast.LENGTH_SHORT).show();
//                }
//            }
//        }.execute();
    }

    @SuppressLint("SimpleDateFormat")
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMAGE'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }
}