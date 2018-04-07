package com.xgx.dw.ui.activity;

import android.content.Intent;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.xgx.dw.R;
import com.xgx.dw.UserBean;
import com.xgx.dw.base.BaseEventBusActivity;
import com.xgx.dw.base.EventCenter;
import com.xgx.dw.bean.County;
import com.xgx.dw.bean.LoginInformation;
import com.xgx.dw.presenter.impl.UserPresenterImpl;
import com.xgx.dw.presenter.interfaces.IUserPresenter;
import com.xgx.dw.ui.view.interfaces.IUserView;

import butterknife.BindView;
import butterknife.OnClick;

public class CreateUserOneAcvitity extends BaseEventBusActivity implements IUserView, Toolbar.OnMenuItemClickListener {
    @BindView(R.id.imeTv)
    MaterialEditText imeTv;
    @BindView(R.id.buy_switch)
    SwitchCompat buySwitch;
    @BindView(R.id.test_switch)
    SwitchCompat testSwitch;
    @BindView(R.id.action_save)
    LinearLayout actionSave;
    @BindView(R.id.user_id)
    MaterialEditText userId;
    @BindView(R.id.user_name)
    MaterialEditText userName;
    @BindView(R.id.contyTv)
    TextView contyTv;
    private IUserPresenter presenter;
    private UserBean bean;

    @Override
    public void initContentView() {
        baseSetContentView(R.layout.activity_create_user_one);
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
    }

    private void initEditInfo() {
        bean = (UserBean) getIntent().getSerializableExtra("bean");
        if (bean != null && !TextUtils.isEmpty(bean.getUserId())) {
            getSupportActionBar().setTitle("修改营业厅管理员");
            imeTv.setEnabled(false);
            userId.setText(bean.getUserId());
            imeTv.setText(checkText(bean.getIme()));
            userId.setEnabled(false);
            userName.setText(checkText(bean.getUserName()));
            contyTv.setText(checkText(bean.getStoreName()));
            contyTv.setContentDescription(bean.getStoreId());
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
            if (!LoginInformation.getInstance().getUser().getType().equals("0")) {
                buySwitch.setEnabled(false);
                testSwitch.setEnabled(false);
            }
        } else {
            getSupportActionBar().setTitle(R.string.create_userone);
        }
    }

    @OnClick({R.id.action_save})
    public void onSaveClick() {
        UserBean userBean = new UserBean();
        userBean.setUserId(userId.getText().toString());
        userBean.setUserName(userName.getText().toString());
        userBean.setIme(imeTv.getText().toString());
        userBean.setIsBuy(buySwitch.isChecked() ? "1" : "0");
        userBean.setIsTest(testSwitch.isChecked() ? "1" : "0");
        userBean.setPassword(userId.getText().toString());
        userBean.setStoreName(contyTv.getText().toString());
        userBean.setStoreId(contyTv.getContentDescription() != null ? contyTv.getContentDescription().toString() : "");
        userBean.setType("10");
        if (bean != null && !TextUtils.isEmpty(bean.getUserId())) {
            userBean.setId(bean.getId());
        }
        presenter.saveUser(this, userBean, 10);

    }


    @Override
    protected void onEventComming(EventCenter eventCenter) {
        if (EventCenter.COUNTY_SELECT == eventCenter.getEventCode()) {
            try {
                County county = (County) eventCenter.getData();
                contyTv.setText(checkText(county.getCountyname()));
                contyTv.setContentDescription(checkText(county.getCountyid()));
            } catch (Exception e) {

            }
        }
    }

    @Override
    public boolean isBindEventBusHere() {
        return true;
    }

    @OnClick({R.id.contyTv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.contyTv:
                if (LoginInformation.getInstance().getUser().getType().equals("0")) {
                    Intent intent = new Intent(CreateUserOneAcvitity.this, StoresMgrActivity.class);
                    intent.putExtra("isSelect", true);
                    startActivity(intent);
                }
                break;
        }
    }

    public boolean onCreateOptionsMenu(Menu paramMenu) {
        if (bean != null && !TextUtils.isEmpty(bean.getUserId())) {
            getMenuInflater().inflate(R.menu.menu_erweima, paramMenu);
        }
        return true;
    }


    public boolean onMenuItemClick(MenuItem paramMenuItem) {
        switch (paramMenuItem.getItemId()) {
            case R.id.action_showerweima:
                startActivity(new Intent(this, TestGeneratectivity.class).putExtra("type", 3).putExtra("id", bean.getId()));
                break;
        }
        return true;
    }

    @Override
    public void saveTransformer(boolean b, String id) {
        hideProgress();
        if (b) {
            showToast("保存成功");
            startActivity(new Intent(this, TestGeneratectivity.class).putExtra("type", 3).putExtra("id", id));
            finish();
        }
    }
}
