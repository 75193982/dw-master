package com.xgx.dw.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xgx.dw.R;
import com.xgx.dw.UserBean;
import com.xgx.dw.app.G;
import com.xgx.dw.app.Setting;
import com.xgx.dw.base.BaseActivity;
import com.xgx.dw.base.BaseAppCompatActivity;
import com.xgx.dw.dao.UserBeanDaoHelper;
import com.xgx.dw.presenter.impl.LoginPresenterImpl;
import com.xgx.dw.presenter.interfaces.ILoginPresenter;
import com.xgx.dw.ui.view.interfaces.ILoginView;
import com.xgx.dw.vo.request.LoginRequest;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseAppCompatActivity implements ILoginView {
    ILoginPresenter loginPresenter;
    @Bind(R.id.login_username)
    EditText loginUsername;
    @Bind(R.id.login_password)
    EditText loginPassword;
    @Bind(R.id.login_btn)
    Button loginBtn;

    public void initContentView() {
        baseSetContentView(R.layout.activity_login);
    }

    public void initPresenter() {
        setToolbarTitle("登录");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        UserBean localUserBean = new UserBean("admin", "超级管理员", "888888", "admin");

        UserBean localUserBean10 = new UserBean("4101001", "一级营业厅管理员", "888888", "10");

        UserBean localUserBean11 = new UserBean("4101101", "一级台区管理员", "888888", "11");

        UserBean localUserBean2 = new UserBean("4102001", "二级账户", "888888", "2");

        UserBean localUserBean30 = new UserBean("4103001", "公司调试账户", "888888", "30");

        UserBean localUserBean31 = new UserBean("4103101", "供电局调试1", "888888", "31");

        UserBean localUserBean32 = new UserBean("4103201", "供电局调试2", "888888", "32");
        UserBeanDaoHelper.getInstance().addData(localUserBean);
        UserBeanDaoHelper.getInstance().addData(localUserBean10);
        UserBeanDaoHelper.getInstance().addData(localUserBean11);
        UserBeanDaoHelper.getInstance().addData(localUserBean2);
        UserBeanDaoHelper.getInstance().addData(localUserBean30);
        UserBeanDaoHelper.getInstance().addData(localUserBean31);
        UserBeanDaoHelper.getInstance().addData(localUserBean32);
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
        startActivity(new Intent(this, MainActivity.class));
    }


    @OnClick(R.id.login_btn)
    public void onClick() {
        showProgress(getString(R.string.login_progress));
        LoginRequest localLoginRequest = new LoginRequest();
        localLoginRequest.bianhao = loginUsername.getText().toString();
        localLoginRequest.mima = loginPassword.getText().toString();
        this.loginPresenter.login(this, localLoginRequest);

    }
}
