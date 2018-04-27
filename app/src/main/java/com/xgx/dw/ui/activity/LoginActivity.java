package com.xgx.dw.ui.activity;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.Response;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.xgx.dw.R;
import com.xgx.dw.UserBean;
import com.xgx.dw.app.BaseApplication;
import com.xgx.dw.app.G;
import com.xgx.dw.app.Setting;
import com.xgx.dw.base.BaseAppCompatActivity;
import com.xgx.dw.bean.LoginInformation;
import com.xgx.dw.bean.SysUser;
import com.xgx.dw.bean.UserAllInfo;
import com.xgx.dw.dao.PricingDaoHelper;
import com.xgx.dw.dao.SpotPricingBeanDaoHelper;
import com.xgx.dw.dao.StoreBeanDaoHelper;
import com.xgx.dw.dao.TransformerBeanDaoHelper;
import com.xgx.dw.dao.UserBeanDaoHelper;
import com.xgx.dw.net.DialogCallback;
import com.xgx.dw.net.JsonCallback;
import com.xgx.dw.net.LzyResponse;
import com.xgx.dw.net.URLs;
import com.xgx.dw.presenter.impl.LoginPresenterImpl;
import com.xgx.dw.presenter.impl.UserPresenterImpl;
import com.xgx.dw.presenter.interfaces.ILoginPresenter;
import com.xgx.dw.presenter.interfaces.IUserPresenter;
import com.xgx.dw.ui.view.interfaces.ILoginView;
import com.xgx.dw.utils.Logger;
import com.xgx.dw.utils.MyUtils;
import com.xgx.dw.vo.request.LoginRequest;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class LoginActivity extends BaseAppCompatActivity implements ILoginView, EasyPermissions.PermissionCallbacks, PopupMenu.OnMenuItemClickListener {
    ILoginPresenter loginPresenter;
    @BindView(R.id.login_username)
    EditText loginUsername;
    @BindView(R.id.login_password)
    EditText loginPassword;
    @BindView(R.id.login_btn)
    Button loginBtn;
    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;
    @BindView(R.id.login_register)
    TextView loginRegister;
    @BindView(R.id.login_forget)
    TextView loginForget;
    @BindView(R.id.login_from)
    TextView loginFrom;

    private PopupMenu popupMenu;


    @Override
    public void initContentView() {
        baseSetContentView(R.layout.activity_login);
    }

    @Override
    public void initPresenter() {
        setToolbarTitle("登录");
        this.loginPresenter = new LoginPresenterImpl();

    }

    @Override
    public void initView() {
        Setting setting = new Setting(this);
        String username = setting.loadString(G.currentUsername);
        String password = setting.loadString(G.currentPassword);
        loginUsername.setText(checkText(username));
        loginPassword.setText(checkText(password));
        try {
            UserBean bean = JSON.parseObject(setting.loadString("user"), UserBean.class);
            if (bean != null && bean.getUserId() != null) {

                OkGo.<LzyResponse<SysUser>>post(URLs.getURL(URLs.USER_SIGNIN)).params("userName", bean.getUserId()).params("password", bean.getPassword()).execute(new JsonCallback<LzyResponse<SysUser>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<SysUser>> response) {
                        String token = response.body().token;
                        String randomKey = response.body().randomKey;
                        new Setting(LoginActivity.this).saveString("token", token);
                        new Setting(LoginActivity.this).saveString("randomKey", randomKey);
                        HttpHeaders headerstemp = new HttpHeaders();
                        headerstemp.put(BaseApplication.token, "Bearer " + token);
                        OkGo.getInstance().addCommonHeaders(headerstemp);

                    }

                    @Override
                    public void onError(Response<LzyResponse<SysUser>> response) {
                        super.onError(response);
                        ToastUtils.showShort(response.getException().getMessage());
                    }
                });
                setLoginInfomation(bean);
            }
        } catch (Exception e) {

        }


    }

    @Override
    public void loginCallback(UserBean userBean) {
        //登录成功后，将登录信息保存到偏好设置中
        setLoginInfomation(userBean);
    }

    private void setLoginInfomation(UserBean userBean) {
        Setting setting = new Setting(this);
        setting.saveString(G.currentUsername, userBean.getUserId());
        setting.saveString(G.currentUserType, userBean.getType());
        setting.saveString(G.currentStoreId, userBean.getStoreId());
        setting.saveString(G.currentStoreName, userBean.getStoreName());
        setting.saveString(G.currentTransformId, userBean.getTransformerId());
        setting.saveString(G.currentTransformName, userBean.getTransformerName());
        setting.saveString(G.currentPassword, userBean.getPassword());
        setting.saveString("user", new Gson().toJson(userBean));
        UserBeanDaoHelper.getInstance().addData(userBean);
        LoginInformation.getInstance().setUser(userBean);
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();

    }


    @Override
    protected void onStart() {
        super.onStart();
        requestCodeQrcodePermissions();
        //检查SD卡写权限授予情况
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
                if (StringUtils.isEmpty(loginUsername.getText().toString())) {
                    showToast("用户名不能为空");
                    return;
                }
                if (StringUtils.isEmpty(loginPassword.getText().toString())) {
                    showToast("密码不能为空");
                    return;
                }
                OkGo.<LzyResponse<SysUser>>post(URLs.getURL(URLs.USER_SIGNIN)).params("userName", loginUsername.getText().toString()).params("password", loginPassword.getText().toString()).execute(new DialogCallback<LzyResponse<SysUser>>(this) {
                    @Override
                    public void onSuccess(Response<LzyResponse<SysUser>> response) {
                        String token = response.body().token;
                        String userType = response.body().userType;
                        String randomKey = response.body().randomKey;
                        new Setting(LoginActivity.this).saveString("token", token);
                        new Setting(LoginActivity.this).saveString("randomKey", randomKey);
                        HttpHeaders headerstemp = new HttpHeaders();
                        headerstemp.put(BaseApplication.token, "Bearer " + token);
                        OkGo.getInstance().addCommonHeaders(headerstemp);
                        if (G.adminRole.equals(userType)) {
                            SysUser temp = ((JSONObject) response.body().model).toJavaObject(SysUser.class);
                            if (temp != null) {
                                UserBean bean = new UserBean();
                                bean.setId(temp.getId() + "");
                                bean.setUserName(temp.getName());
                                bean.setUserId(temp.getAccount());
                                bean.setStoreId(temp.getCountyid());
                                bean.setStoreName(temp.getCountyname());
                                bean.setTransformerId(temp.getTaiquid());
                                bean.setTransformerName(temp.getTaiquname());
                                bean.setRemark(temp.getEmail());
                                bean.setIsBuy(temp.getIsBuy());
                                bean.setIsTest(temp.getIsTest());
                                bean.setType(temp.getUserType() + "");
                                bean.setPhone(temp.getPhone());
                                bean.setPassword(loginPassword.getText().toString());

                                setLoginInfomation(bean);
                            } else {
                                ToastUtils.showShort("账号不存在");
                            }
                        } else {
                            UserBean user = ((JSONObject) response.body().model).toJavaObject(UserBean.class);
                            if (user != null && !TextUtils.isEmpty(user.getPhone())) {
                                Setting setting = new Setting(LoginActivity.this);
                                boolean isValide = setting.loadBoolean(user.getUserId() + user.getPhone() + setting.loadString(G.currentUserLoginPhone));
                                if (isValide) {
                                    setLoginInfomation(user);
                                } else {
                                    //进入手机验证阶段
                                    startActivity(new Intent(LoginActivity.this, PhoneCodeActivity.class).putExtra("user", user));
                                }
                            } else {
                                ToastUtils.showShort("账号不存在");
                            }
                        }


                    }

                    @Override
                    public void onError(Response<LzyResponse<SysUser>> response) {
                        super.onError(response);
                        ToastUtils.showShort(response.getException().getMessage());
                    }
                });
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
            decryptString = result;

            Logger.e(getContext(), "扫描结果：" + decryptString);
            UserAllInfo userAllInfo = new Gson().fromJson(decryptString, UserAllInfo.class);
            if (userAllInfo != null) {
                if (userAllInfo.getEcodeType().equals("3") || userAllInfo.getEcodeType().equals("6")) {
                    //保存用户 方便登录
                    IUserPresenter presenter = new UserPresenterImpl();
                    presenter.saveOrUpdateUser(userAllInfo.getUser());
                    if (userAllInfo.getTransformerBean().getId() != null) {
                        TransformerBeanDaoHelper.getInstance().addData(userAllInfo.getTransformerBean());
                    }
                    if (userAllInfo.getSpotBeans().getId() != null) {
                        SpotPricingBeanDaoHelper.getInstance().addData(userAllInfo.getSpotBeans());
                    }
                    if (userAllInfo.getSpotBeans().getId() != null) {
                        SpotPricingBeanDaoHelper.getInstance().addData(userAllInfo.getSpotBeans());
                    }
                    if (userAllInfo.getPricings().getId() != null) {
                        PricingDaoHelper.getInstance().addData(userAllInfo.getPricings());
                    }

                    UserBean temp = UserBeanDaoHelper.getInstance().queryByTransFormUserId(userAllInfo.getUser().getUserId());
                    //自动登录 比较 ime账号
                    if (temp.getIme().contains(MyUtils.getuniqueId(getContext()))) {
                        //则登录成功 当前账号为 bean.getUserName
                        loginCallback(temp);
                    } else {
                        showToast("该账户无法在您手机登录，请联系管理员");
                    }
                } else {
                    showToast("二维码信息错误，请选择正确二维码");
                }
            } else {
                showToast("二维码信息错误，请选择正确二维码");
            }
        } catch (Exception e) {
            e.printStackTrace();
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

