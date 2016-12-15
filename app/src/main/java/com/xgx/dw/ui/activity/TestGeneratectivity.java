package com.xgx.dw.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.xgx.dw.PricingBean;
import com.xgx.dw.R;
import com.xgx.dw.SpotPricingBean;
import com.xgx.dw.StoreBean;
import com.xgx.dw.TransformerBean;
import com.xgx.dw.UserBean;
import com.xgx.dw.app.G;
import com.xgx.dw.base.BaseAppCompatActivity;
import com.xgx.dw.bean.LoginInformation;
import com.xgx.dw.bean.UserAllInfo;
import com.xgx.dw.dao.PricingDaoHelper;
import com.xgx.dw.dao.SpotPricingBeanDaoHelper;
import com.xgx.dw.dao.StoreBeanDaoHelper;
import com.xgx.dw.dao.TransformerBeanDaoHelper;
import com.xgx.dw.dao.UserBeanDaoHelper;
import com.xgx.dw.utils.AES;
import com.xgx.dw.utils.BitmapUtil;
import com.xgx.dw.utils.Logger;
import com.xgx.dw.utils.MyUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class TestGeneratectivity extends BaseAppCompatActivity {
    @Bind(R.id.iv_chinese)
    ImageView mChineseIv;
    @Bind(R.id.iv_english)
    ImageView mEnglishIv;
    @Bind(R.id.iv_chinese_logo)
    ImageView mChineseLogoIv;
    @Bind(R.id.iv_english_logo)
    ImageView mEnglishLogoIv;
    @Bind(R.id.decode_isbn)
    TextView decodeIsbn;
    private int type;


    @Override
    public void initContentView() {
        baseSetContentView(R.layout.activity_test_generate);
    }

    @Override
    public void initView() {

    }


    @Override
    public void initPresenter() {
        type = getIntent().getIntExtra("type", -1);
        createQRCode();
    }

    private void createQRCode() {
        //根据不同的业务需求创建二维码
        UserAllInfo userAllInfo = new UserAllInfo();
        String id = getIntent().getStringExtra("id");

        UserBean bean = new UserBean();
        StoreBean storebean = new StoreBean();
        TransformerBean transbean = new TransformerBean();
        List<PricingBean> pricings = new ArrayList<>();
        SpotPricingBean spotPricingBeans = new SpotPricingBean();
        switch (type) {
            case 0://1级账号
                setToolbarTitle(getResources().getString(R.string.create_userone));
                bean.setIme(MyUtils.getuniqueId(this));
                break;
            case 1://台区管理员账号
                setToolbarTitle(getResources().getString(R.string.create_usertwo));
                bean.setIme(MyUtils.getuniqueId(this));
                break;
            case 2://二级账号
                setToolbarTitle(getResources().getString(R.string.create_userThree));
                bean.setIme(MyUtils.getuniqueId(this));
                break;
            case 3:
                setToolbarTitle("账号信息");
                bean = UserBeanDaoHelper.getInstance().getDataById(id);
                if (bean.getType().equals("10")) {
                    storebean = StoreBeanDaoHelper.getInstance().getDataById(bean.getStoreId());
                } else if (bean.getType().equals("11")) {
                    storebean = StoreBeanDaoHelper.getInstance().getDataById(bean.getStoreId());
                    transbean = TransformerBeanDaoHelper.getInstance().getDataById(bean.getTransformerId());
                } else if (bean.getType().equals("20")) {
                    storebean = StoreBeanDaoHelper.getInstance().getDataById(bean.getStoreId());
                    transbean = TransformerBeanDaoHelper.getInstance().getDataById(bean.getTransformerId());
                    try {
                        spotPricingBeans = SpotPricingBeanDaoHelper.getInstance().getDataById(bean.getPrice());
                    } catch (Exception e) {
                        Logger.e(e.getMessage());
                    }
                }
                userAllInfo.setSpotBeans(spotPricingBeans);
                userAllInfo.setStoreBean(storebean);
                userAllInfo.setTransformerBean(transbean);
                break;
            case 4:
                setToolbarTitle("申请购电者信息");
                bean = UserBeanDaoHelper.getInstance().getDataById(id);
                bean.setIme(MyUtils.getuniqueId(this));
                storebean = StoreBeanDaoHelper.getInstance().getDataById(bean.getStoreId());
                transbean = TransformerBeanDaoHelper.getInstance().getDataById(bean.getTransformerId());
                try {
                    spotPricingBeans = SpotPricingBeanDaoHelper.getInstance().getDataById(bean.getPrice());
                } catch (Exception e) {
                    Logger.e(e.getMessage());
                }
                userAllInfo.setSpotBeans(spotPricingBeans);
                userAllInfo.setStoreBean(storebean);
                userAllInfo.setTransformerBean(transbean);
                pricings = PricingDaoHelper.getInstance().queryByUserId(bean.getId());
                userAllInfo.setPricingSize(pricings.size());
                break;
            case 5:
                setToolbarTitle("返回购电用户信息");
                bean = UserBeanDaoHelper.getInstance().getDataById(id);
                pricings = PricingDaoHelper.getInstance().queryByUserId(bean.getId());
                spotPricingBeans = SpotPricingBeanDaoHelper.getInstance().getDataById(bean.getPrice());
                if (pricings.size() > 0) {
                    userAllInfo.setPricings(pricings.get(0));
                }
                userAllInfo.setSpotBeans(spotPricingBeans);
                userAllInfo.setPricingSize(pricings.size());
                break;
            case 6:
                setToolbarTitle("返回购电用户信息");

                bean = UserBeanDaoHelper.getInstance().getDataById(id);
                if (bean.getType().equals("10")) {
                    storebean = StoreBeanDaoHelper.getInstance().getDataById(bean.getStoreId());
                } else if (bean.getType().equals("11")) {
                    storebean = StoreBeanDaoHelper.getInstance().getDataById(bean.getStoreId());
                    transbean = TransformerBeanDaoHelper.getInstance().getDataById(bean.getTransformerId());
                } else if (bean.getType().equals("20")) {
                    storebean = StoreBeanDaoHelper.getInstance().getDataById(bean.getStoreId());
                    transbean = TransformerBeanDaoHelper.getInstance().getDataById(bean.getTransformerId());
                    try {
                        spotPricingBeans = SpotPricingBeanDaoHelper.getInstance().getDataById(bean.getPrice());
                    } catch (Exception e) {
                        Logger.e(e.getMessage());
                    }
                }
                userAllInfo.setSpotBeans(spotPricingBeans);
                userAllInfo.setStoreBean(storebean);
                userAllInfo.setTransformerBean(transbean);
                pricings = PricingDaoHelper.getInstance().queryByUserId(bean.getId());
                if (pricings.size() > 0) {
                    userAllInfo.setPricings(pricings.get(0));
                }
                userAllInfo.setPricingSize(pricings.size());
                break;
        }
        bean.setEcodeType(type + "");
        userAllInfo.setUser(bean);
        createChineseQRCode(new Gson().toJson(userAllInfo));
    }

    private void createChineseQRCode(String s) {
        String encryptResultStr = s;
        try {
            // encryptResultStr = AES.encrypt(G.appsecret, s);
            //encryptResultStr = AES.toHex(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(encryptResultStr)) {
            Toast.makeText(this, "当前没有用户", Toast.LENGTH_SHORT).show();
            return;
        }
        Bitmap mBitmap = CodeUtils.createImage(encryptResultStr, 800, 800, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
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