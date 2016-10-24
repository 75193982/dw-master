package com.xgx.dw.ui.activity;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.xgx.dw.bean.UserAllInfo;
import com.xgx.dw.dao.StoreBeanDaoHelper;
import com.xgx.dw.dao.TransformerBeanDaoHelper;
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

public class LoginActivity extends BaseAppCompatActivity implements ILoginView, EasyPermissions.PermissionCallbacks, PopupMenu.OnMenuItemClickListener {
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

    private PopupMenu popupMenu;


    public void initContentView() {
        baseSetContentView(R.layout.activity_login);
    }

    public void initPresenter() {
        setToolbarTitle("登录");
        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                UserBean localUserBean = new UserBean("admin", "超级管理员", "888888", "admin", "867628025884339");
                UserBean localUserBean2 = new UserBean("666666", "超级管理员", "888888", "admin", "867567020720108");
//        UserBean localUserBean10 = new UserBean("4101001", "一级营业厅管理员", "888888", "10");
//        UserBean localUserBean11 = new UserBean("4101101", "一级台区管理员", "888888", "11");
//        UserBean localUserBean2 = new UserBean("4102001", "二级账户", "888888", "20");
//        UserBean localUserBean30 = new UserBean("4103001", "公司调试账户", "888888", "30");
//        UserBean localUserBean31 = new UserBean("4103101", "供电局调试1", "888888", "31");
//        UserBean localUserBean32 = new UserBean("4103201", "供电局调试2", "888888", "32");
                UserBeanDaoHelper.getInstance().addData(localUserBean);
                UserBeanDaoHelper.getInstance().addData(localUserBean2);
            }
        };
        t.start();
        this.loginPresenter = new LoginPresenterImpl();
    }

    public void initView() {
        Setting setting = new Setting(this);
        String username = setting.loadString(G.currentUsername);
        String password = setting.loadString(G.currentPassword);
        loginUsername.setText(checkText(username));
        loginPassword.setText(checkText(password));
    }

    public void loginCallback(UserBean userBean) {
        //登录成功后，将登录信息保存到偏好设置中
        Setting setting = new Setting(this);
        setting.saveString(G.currentUsername, userBean.getUserId());
        setting.saveString(G.currentUserType, userBean.getType());
        setting.saveString(G.currentStoreId, userBean.getStoreId());
        setting.saveString(G.currentStoreName, userBean.getStoreName());
        setting.saveString(G.currentTransformId, userBean.getTransformerId());
        setting.saveString(G.currentTransformName, userBean.getTransformerName());
        setting.saveString(G.currentPassword, userBean.getPassword());
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

    private final static int ONE = 0;
    private final static int TWO = 1;
    private final static int THREE = 2;

    @OnClick({R.id.login_btn, R.id.login_register, R.id.login_forget, R.id.login_from})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                LoginRequest localLoginRequest = new LoginRequest();
                localLoginRequest.bianhao = loginUsername.getText().toString();
                localLoginRequest.mima = loginPassword.getText().toString();
                this.loginPresenter.login(this, localLoginRequest);
                break;
            case R.id.login_register:

                popupMenu = new PopupMenu(this, view);
                Menu menu = popupMenu.getMenu();


                // 通过XML文件添加菜单项
                MenuInflater menuInflater = getMenuInflater();
                menuInflater.inflate(R.menu.popupmenu, menu);
                popupMenu.setOnMenuItemClickListener(this);
                popupMenu.show();
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
                    getResult(result);

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
                        getResult(result);
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

    private void getResult(String result) {
        String decryptString = "";
        try {
            decryptString = AES.decrypt(G.appsecret, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Logger.e(getContext(), "扫描结果：" + decryptString);
        UserAllInfo userAllInfo = new Gson().fromJson(decryptString, UserAllInfo.class);
        if (userAllInfo != null) {
            if (userAllInfo.getUser().getEcodeType().equals("3")) {
                //保存用户 方便登录
                UserBeanDaoHelper.getInstance().addData(userAllInfo.getUser());
                if (userAllInfo.getStoreBean().getId() != null) {
                    StoreBeanDaoHelper.getInstance().addData(userAllInfo.getStoreBean());
                }
                if (userAllInfo.getTransformerBean().getId() != null) {
                    TransformerBeanDaoHelper.getInstance().addData(userAllInfo.getTransformerBean());
                }
                //自动登录 比较 ime账号
                if (MyUtils.getuniqueId(getContext()).equals(userAllInfo.getUser().getIme())) {
                    //则登录成功 当前账号为 bean.getUserName
                    loginCallback(userAllInfo.getUser());

                }
            } else {
                showToast("二维码信息错误，请选择正确二维码");
            }
        } else {
            showToast("二维码信息错误，请选择正确二维码");
        }
    }


    private static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 666;

    int REQUEST_CODE = 1001;


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.one:
                startActivity(new Intent(this, TestGeneratectivity.class).putExtra("type", 0));
                popupMenu.dismiss();
                break;

            case R.id.two:
                startActivity(new Intent(this, TestGeneratectivity.class).putExtra("type", 1));
                popupMenu.dismiss();
                break;

            case R.id.three:
                startActivity(new Intent(this, TestGeneratectivity.class).putExtra("type", 2));
                popupMenu.dismiss();
                break;

        }
        return false;
    }
}

