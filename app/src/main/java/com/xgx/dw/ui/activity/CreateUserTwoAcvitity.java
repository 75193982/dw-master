package com.xgx.dw.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.xgx.dw.R;
import com.xgx.dw.StoreBean;
import com.xgx.dw.TransformerBean;
import com.xgx.dw.UserBean;
import com.xgx.dw.app.G;
import com.xgx.dw.app.Setting;
import com.xgx.dw.base.BaseEventBusActivity;
import com.xgx.dw.base.EventCenter;
import com.xgx.dw.bean.County;
import com.xgx.dw.bean.LoginInformation;
import com.xgx.dw.bean.Price;
import com.xgx.dw.bean.Taiqu;
import com.xgx.dw.presenter.impl.UserPresenterImpl;
import com.xgx.dw.presenter.interfaces.IUserPresenter;
import com.xgx.dw.ui.view.interfaces.IUserView;

import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateUserTwoAcvitity extends BaseEventBusActivity implements IUserView, Toolbar.OnMenuItemClickListener {
    @BindView(R.id.buy_switch)
    SwitchCompat buySwitch;
    @BindView(R.id.test_switch)
    SwitchCompat testSwitch;
    @BindView(R.id.user_id)
    MaterialEditText userId;
    @BindView(R.id.user_name)
    MaterialEditText userName;
    @BindView(R.id.imeTv)
    MaterialEditText imeTv;
    @BindView(R.id.contyTv)
    TextView contyTv;
    @BindView(R.id.taiquTv)
    TextView taiquTv;
    private IUserPresenter presenter;
    private UserBean bean;
    private String currentStoreId;
    private String currentStoreName;


    @Override
    public void initContentView() {
        baseSetContentView(R.layout.activity_create_user_two);
    }

    @Override
    public void initPresenter() {
        presenter = new UserPresenterImpl();
    }

    @Override
    public void initView() {
        getToolbar().setOnMenuItemClickListener(this);
        String ime = getIntent().getStringExtra("ime");
        imeTv.setText(checkText(ime));
        initEditInfo();
        Setting setting = new Setting(this);
        currentStoreId = setting.loadString(G.currentStoreId);
        currentStoreName = setting.loadString(G.currentStoreName);
    }

    private void initEditInfo() {
        bean = ((UserBean) getIntent().getSerializableExtra("bean"));
        if (bean != null && !TextUtils.isEmpty(bean.getId())) {
            getSupportActionBar().setTitle("编辑台区管理员");
            userId.setText(bean.getUserId());
            imeTv.setText(checkText(bean.getIme()));
            imeTv.setEnabled(false);
            userId.setEnabled(false);
            userName.setText(checkText(bean.getUserName()));
            if (bean.getIsBuy().equals("0")) {
                buySwitch.setChecked(false);
            } else if (bean.getIsBuy().equals("1")) {
                buySwitch.setChecked(true);
            }
            if (bean.getIsTest().equals("0")) {
                testSwitch.setChecked(false);
            } else if (bean.getIsTest().equals("1")) {
                testSwitch.setChecked(true);
            }
            if (!"10".equals(LoginInformation.getInstance().getUser().getType()) || "0".equals(LoginInformation.getInstance().getUser().getType())) {
                buySwitch.setEnabled(false);
                testSwitch.setEnabled(false);
            }

            contyTv.setText(bean.getStoreName());
            contyTv.setContentDescription(bean.getStoreId());
            taiquTv.setText(bean.getTransformerName());
            taiquTv.setContentDescription(bean.getTransformerId());
        } else {
            if (LoginInformation.getInstance().getUser().getType().equals("10")) {//营业厅管理员
                contyTv.setText(LoginInformation.getInstance().getUser().getStoreName());
                contyTv.setContentDescription(LoginInformation.getInstance().getUser().getStoreId());
            }
            getSupportActionBar().setTitle("创建台区管理员");
        }
    }


    @OnClick({R.id.action_save})
    public void onSaveClick() {
        showProgress("保存用户中...");
        UserBean userBean = new UserBean();
        userBean.setUserId(userId.getText().toString());
        userBean.setUserName(userName.getText().toString());
        userBean.setIme(imeTv.getText().toString());
        userBean.setIsBuy(buySwitch.isChecked() ? "1" : "0");
        userBean.setIsTest(testSwitch.isChecked() ? "1" : "0");
        userBean.setPassword(userId.getText().toString());
        userBean.setTransformerId(taiquTv.getContentDescription() != null ? taiquTv.getContentDescription().toString() : "");
        userBean.setTransformerName(taiquTv.getText().toString());
        userBean.setStoreId(contyTv.getContentDescription() != null ? contyTv.getContentDescription().toString() : "");
        userBean.setStoreName(contyTv.getText().toString());
        if (bean != null && !TextUtils.isEmpty(bean.getUserId())) {
            userBean.setId(bean.getId());
        }
        this.presenter.saveUser(this, userBean, 11);
    }

    @Override
    public void saveTransformer(boolean paramBoolean, String id) {
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
        }
        return true;
    }


    @Override
    protected void onEventComming(EventCenter eventCenter) {
        if (EventCenter.COUNTY_SELECT == eventCenter.getEventCode()) {
            try {
                County county = (County) eventCenter.getData();
                contyTv.setText(checkText(county.getCountyname()));
                contyTv.setContentDescription(checkText(county.getCountyid()));
                taiquTv.setText("");
                taiquTv.setContentDescription("");
            } catch (Exception e) {

            }
        }
        if (EventCenter.TAIQU_SELECT == eventCenter.getEventCode()) {
            try {
                Taiqu taiqu = (Taiqu) eventCenter.getData();
                taiquTv.setText(checkText(taiqu.getName()));
                taiquTv.setContentDescription(checkText(taiqu.getCode()));
            } catch (Exception e) {

            }
        }
    }

    @Override
    public boolean isBindEventBusHere() {
        return true;
    }

    @OnClick({R.id.contyTv, R.id.taiquTv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.contyTv:
                if (LoginInformation.getInstance().getUser().getType().equals("0")) {
                    Intent intent = new Intent(CreateUserTwoAcvitity.this, StoresMgrActivity.class);
                    intent.putExtra("isSelect", true);
                    startActivity(intent);
                }

                break;
            case R.id.taiquTv:

                if (LoginInformation.getInstance().getUser().getType().equals("0") || LoginInformation.getInstance().getUser().getType().equals("10")) {
                    if (contyTv.getContentDescription() != null && !TextUtils.isEmpty(contyTv.getContentDescription().toString())) {
                        Intent taiquIntent = new Intent(CreateUserTwoAcvitity.this, TransformerActivity.class);
                        taiquIntent.putExtra("isSelect", true);
                        taiquIntent.putExtra("countyid", contyTv.getContentDescription().toString());
                        startActivity(taiquIntent);
                    } else {
                        ToastUtils.showShort("请选择营业厅");
                    }

                }

                break;
        }
    }

}
