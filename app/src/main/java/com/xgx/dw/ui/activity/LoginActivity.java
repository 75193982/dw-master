package com.xgx.dw.ui.activity;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.xgx.dw.R;
import com.xgx.dw.UserBean;
import com.xgx.dw.app.G;
import com.xgx.dw.app.Setting;
import com.xgx.dw.base.BaseAppCompatActivity;
import com.xgx.dw.bean.LoginInformation;
import com.xgx.dw.dao.UserBeanDaoHelper;
import com.xgx.dw.presenter.impl.LoginPresenterImpl;
import com.xgx.dw.presenter.impl.UserPresenterImpl;
import com.xgx.dw.presenter.interfaces.ILoginPresenter;
import com.xgx.dw.presenter.interfaces.IUserPresenter;
import com.xgx.dw.ui.view.interfaces.ILoginView;
import com.xgx.dw.utils.AES;
import com.xgx.dw.utils.Logger;
import com.xgx.dw.utils.MyUtils;
import com.xgx.dw.vo.request.LoginRequest;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class LoginActivity extends BaseAppCompatActivity implements ILoginView, EasyPermissions.PermissionCallbacks {
    ILoginPresenter loginPresenter;
    @Bind(R.id.login_username)
    EditText loginUsername;
    @Bind(R.id.login_password)
    EditText loginPassword;
    @Bind(R.id.login_btn)
    Button loginBtn;
    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;
    @Bind(R.id.login_register)
    TextView loginRegister;
    @Bind(R.id.login_forget)
    TextView loginForget;
    @Bind(R.id.login_from)
    TextView loginFrom;


    public void initContentView() {
        baseSetContentView(R.layout.activity_login);
    }

    public void initPresenter() {
        setToolbarTitle("登录");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        UserBean localUserBean = new UserBean("admin", "超级管理员", "888888", "admin", MyUtils.getuniqueId(getContext()));
//        UserBean localUserBean10 = new UserBean("4101001", "一级营业厅管理员", "888888", "10");
//        UserBean localUserBean11 = new UserBean("4101101", "一级台区管理员", "888888", "11");
//        UserBean localUserBean2 = new UserBean("4102001", "二级账户", "888888", "20");
//        UserBean localUserBean30 = new UserBean("4103001", "公司调试账户", "888888", "30");
//        UserBean localUserBean31 = new UserBean("4103101", "供电局调试1", "888888", "31");
//        UserBean localUserBean32 = new UserBean("4103201", "供电局调试2", "888888", "32");
        UserBeanDaoHelper.getInstance().addData(localUserBean);
        this.loginPresenter = new LoginPresenterImpl();
    }

    public void initView() {
    }

    public void loginCallback(UserBean userBean) {
        hideProgress();
        //登录成功后，将登录信息保存到偏好设置中
        Setting setting = new Setting(this);
        setting.saveString(G.currentUsername, userBean.getUserId());
        setting.saveString(G.currentUserType, userBean.getType());
        setting.saveString(G.currentStoreId, userBean.getStoreId());
        setting.saveString(G.currentStoreName, userBean.getStoreName());
        setting.saveString(G.currentTransformId, userBean.getTransformerId());
        setting.saveString(G.currentTransformName, userBean.getTransformerName());
        setting.saveString("user", new Gson().toJson(userBean));
        LoginInformation.getInstance().setUser(userBean);
        startActivity(new Intent(this, MainActivity.class));
    }


    @Override
    protected void onStart() {
        super.onStart();
        requestCodeQrcodePermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
    }

    @AfterPermissionGranted(REQUEST_CODE_QRCODE_PERMISSIONS)
    private void requestCodeQrcodePermissions() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "扫描二维码需要打开相机和散光灯的权限", REQUEST_CODE_QRCODE_PERMISSIONS, perms);
        }
    }


    @OnClick({R.id.login_btn, R.id.login_register, R.id.login_forget, R.id.login_from})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                showProgress(getString(R.string.login_progress));
                LoginRequest localLoginRequest = new LoginRequest();
                localLoginRequest.bianhao = loginUsername.getText().toString();
                localLoginRequest.mima = loginPassword.getText().toString();
                this.loginPresenter.login(this, localLoginRequest);
                break;
            case R.id.login_register:
                startActivity(new Intent(this, TestGeneratectivity.class).putExtra("type", 0));
                break;
            case R.id.login_forget:
                Intent intent = new Intent(this, CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.login_from:
                startActivityForResult(BGAPhotoPickerActivity.newIntent(this, null, 1, null), REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    String decryptString = "";
                    try {
                        decryptString = AES.decrypt(G.appsecret, result);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Logger.e(getContext(), "扫描结果：" + decryptString);
                    UserBean bean = new Gson().fromJson(decryptString, UserBean.class);
                    if (bean.getEcodeType().equals("1")) {
                        //保存用户 方便登录
                        UserBeanDaoHelper.getInstance().addData(bean);
                        //自动登录 比较 ime账号
                        if (MyUtils.getuniqueId(getContext()).equals(bean.getIme())) {
                            //则登录成功 当前账号为 bean.getUserName
                            loginCallback(bean);

                        }
                    } else {
                        showToast("二维码信息错误，请选择正确二维码");
                    }
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY) {
            final String picturePath = BGAPhotoPickerActivity.getSelectedImages(data).get(0);
            try {
                CodeUtils.analyzeBitmap(picturePath, new CodeUtils.AnalyzeCallback() {
                    @Override
                    public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                        String decryptString = "";
                        try {
                            decryptString = AES.decrypt("1396198677119910", result);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(LoginActivity.this, decryptString, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAnalyzeFailed() {
                        Toast.makeText(LoginActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(LoginActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
            }
        }
    }


    private static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 666;

    int REQUEST_CODE = 1001;
}

