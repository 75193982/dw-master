package com.xgx.dw.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.xgx.dw.R;
import com.xgx.dw.StoreBean;
import com.xgx.dw.TransformerBean;
import com.xgx.dw.UserBean;
import com.xgx.dw.app.G;
import com.xgx.dw.app.Setting;
import com.xgx.dw.base.BaseAppCompatActivity;
import com.xgx.dw.bean.LoginInformation;
import com.xgx.dw.dao.UserBeanDaoHelper;
import com.xgx.dw.presenter.impl.UserPresenterImpl;
import com.xgx.dw.presenter.interfaces.IUserPresenter;
import com.xgx.dw.ui.view.interfaces.IUserView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserInfoAcvitity extends BaseAppCompatActivity implements IUserView, Toolbar.OnMenuItemClickListener {
    @BindView(R.id.imeTv)
    TextView imeTv;
    @BindView(R.id.store_spinner)
    TextView storeSpinner;
    @BindView(R.id.store_layout)
    LinearLayout storeLayout;
    @BindView(R.id.transformer_spinner)
    TextView transformerSpinner;
    @BindView(R.id.transformer_layout)
    LinearLayout transformerLayout;
    @BindView(R.id.user_id)
    MaterialEditText userId;
    @BindView(R.id.user_name)
    MaterialEditText userName;
    @BindView(R.id.voltageRatio)
    MaterialEditText voltageRatio;
    @BindView(R.id.currentRatio)
    MaterialEditText currentRatio;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.phone)
    MaterialEditText phone;
    @BindView(R.id.action_save)
    LinearLayout actionSave;
    private UserBean bean;


    @Override
    public void initContentView() {
        baseSetContentView(R.layout.activity_user_info);
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        getToolbar().setOnMenuItemClickListener(this);
        String ime = getIntent().getStringExtra("ime");
        imeTv.setText(checkText(ime));
        initEditInfo();
    }

    private void initEditInfo() {
        bean = ((UserBean) getIntent().getSerializableExtra("bean"));
        if (bean != null && !TextUtils.isEmpty(this.bean.getUserId())) {
            getSupportActionBar().setTitle("个人资料");
            this.userId.setText(this.bean.getUserId());
            this.userId.setEnabled(false);
            userName.setText(checkText(this.bean.getUserName()));
            price.setText(checkText(bean.getPrice()));
            phone.setText(checkText(bean.getPhone()));
            currentRatio.setText(checkText(bean.getCurrentRatio()));
            voltageRatio.setText(checkText(bean.getVoltageRatio()));
            imeTv.setText(checkText(bean.getIme()));
            storeSpinner.setText(bean.getStoreName());
            transformerSpinner.setText(bean.getTransformerName());
        }
        storeSpinner.setEnabled(false);
        transformerSpinner.setEnabled(false);
        userId.setEnabled(false);
        userName.setEnabled(false);
        voltageRatio.setEnabled(false);
        currentRatio.setEnabled(false);
        price.setEnabled(false);
        phone.setEnabled(false);
    }

    @Override
    public void saveTransformer(boolean paramBoolean, String id) {
        hideProgress();
        if (paramBoolean) {
            showToast("保存成功");
            finish();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu paramMenu) {
        if (bean != null && !TextUtils.isEmpty(bean.getUserId())) {
            getMenuInflater().inflate(R.menu.menu_erweima, paramMenu);
        }
        return true;
    }


    @Override
    public boolean onMenuItemClick(MenuItem paramMenuItem) {
        switch (paramMenuItem.getItemId()) {
            case R.id.action_showerweima:
                startActivity(new Intent(this, TestGeneratectivity.class).putExtra("type", 3).putExtra("id", bean.getId()));
                break;
            case R.id.action_spotInfo:
                Intent intent = new Intent(getContext(), AdminSpotListActivity.class);
                intent.putExtra("id", bean.getUserId());
                startActivity(intent);
                break;
        }
        return true;
    }


    @OnClick(R.id.action_save)
    public void onViewClicked() {
        Setting setting = new Setting(this);
        setting.clearLoginInfomation();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        ActivityUtils.finishAllActivities();
    }
}
