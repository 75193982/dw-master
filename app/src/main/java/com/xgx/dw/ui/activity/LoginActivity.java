package com.xgx.dw.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xgx.dw.R;
import com.xgx.dw.UserBean;
import com.xgx.dw.base.BaseActivity;
import com.xgx.dw.base.BaseAppCompatActivity;
import com.xgx.dw.dao.UserBeanDaoHelper;
import com.xgx.dw.presenter.impl.LoginPresenterImpl;
import com.xgx.dw.presenter.interfaces.ILoginPresenter;
import com.xgx.dw.ui.view.interfaces.ILoginView;
import com.xgx.dw.vo.request.LoginRequest;
import com.xgx.dw.vo.response.UserInfo;

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
        UserBean localUserBean = new UserBean();
        localUserBean.setBianhao("admin");
        localUserBean.setXingming("admin");
        localUserBean.setMima("888888");
        UserBeanDaoHelper.getInstance().addData(localUserBean);
        this.loginPresenter = new LoginPresenterImpl();
    }

    public void initView() {
    }

    public void loginCallback(UserInfo paramUserInfo) {
        hideProgress();
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
