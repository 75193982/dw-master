package com.xgx.dw.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.andexert.library.RippleView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.xgx.dw.R;
import com.xgx.dw.StoreBean;
import com.xgx.dw.TransformerBean;
import com.xgx.dw.UserBean;
import com.xgx.dw.app.G;
import com.xgx.dw.app.Setting;
import com.xgx.dw.base.BaseAppCompatActivity;
import com.xgx.dw.dao.StoreBeanDaoHelper;
import com.xgx.dw.dao.TransformerBeanDaoHelper;
import com.xgx.dw.presenter.impl.UserPresenterImpl;
import com.xgx.dw.presenter.interfaces.IUserPresenter;
import com.xgx.dw.ui.view.interfaces.ICreateTransformerView;
import com.xgx.dw.ui.view.interfaces.IUserView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.ganfra.materialspinner.MaterialSpinner;

public class CreateUserOneAcvitity extends BaseAppCompatActivity implements IUserView {

    @Bind(R.id.spinner)
    MaterialSpinner spinner;
    @Bind(R.id.buy_switch)
    SwitchCompat buySwitch;
    @Bind(R.id.test_switch)
    SwitchCompat testSwitch;
    @Bind(R.id.action_save)
    RippleView actionSave;
    @Bind(R.id.store_layout)
    LinearLayout storeLayout;
    @Bind(R.id.user_id)
    MaterialEditText userId;
    @Bind(R.id.user_name)
    MaterialEditText userName;
    private IUserPresenter presenter;
    private List<StoreBean> storebeans;
    private UserBean bean;
    private List<TransformerBean> transformerBean;


    public void initContentView() {
        baseSetContentView(R.layout.activity_create_user_one);
    }

    public void initPresenter() {
        presenter = new UserPresenterImpl();
    }

    public void initView() {


        initSpinnerData();
        initEditInfo();
    }

    private void initEditInfo() {
        bean = (UserBean) getIntent().getSerializableExtra("bean");
        if ((this.bean != null) && (!TextUtils.isEmpty(this.bean.getUserId()))) {
            getSupportActionBar().setTitle("修改营业厅管理员");
            this.userId.setText(this.bean.getUserId());
            this.userId.setEnabled(false);
            this.userName.setText(checkText(this.bean.getUserName()));
            try {
                for (int i = 0; i < this.storebeans.size(); i++) {
                    if (this.bean.getStoreId().equals(((StoreBean) this.storebeans.get(i)).getId())) {
                        this.spinner.setSelection(i + 1);
                        this.spinner.setEnabled(false);
                    }
                }
            } catch (Exception e) {

            }

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
        } else {
            getSupportActionBar().setTitle(R.string.create_userone);
        }
    }

    private void initSpinnerData() {
        bean = ((UserBean) getIntent().getSerializableExtra("bean"));
        storebeans = StoreBeanDaoHelper.getInstance().getAllData();
        if ((this.storebeans != null) && (this.storebeans.size() > 0)) {
            String[] arrayOfString = new String[this.storebeans.size()];
            for (int j = 0; j < this.storebeans.size(); j++) {
                arrayOfString[j] = ((StoreBean) this.storebeans.get(j)).getName();
            }
            ArrayAdapter localArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayOfString);
            localArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            this.spinner.setAdapter(localArrayAdapter);
        }
    }

    @OnClick({R.id.action_save})
    public void onSaveClick() {
        showProgress("保存用户中...");
        UserBean userBean = new UserBean();
        userBean.setUserId(userId.getText().toString());
        userBean.setUserName(userName.getText().toString());

        try {
            int i = this.spinner.getSelectedItemPosition();
            StoreBean localStoreBean = (StoreBean) this.storebeans.get(i - 1);
            userBean.setStoreId(localStoreBean.getId());
            userBean.setStoreName(localStoreBean.getName());
        } catch (Exception e) {

        }

        if ((bean == null) || (TextUtils.isEmpty(bean.getUserId()))) {
            userBean.setPassword(userId.getText().toString());
            this.presenter.saveUser(this, userBean, 10, true);
            return;
        }
        userBean.setPassword(userId.getText().toString());
        userBean.setIsBuy(buySwitch.isChecked() ? "1" : "0");
        userBean.setIsTest(testSwitch.isChecked() ? "1" : "0");
        this.presenter.saveUser(this, userBean, 10, false);
    }

    public void saveTransformer(boolean paramBoolean) {
        hideProgress();
        if (paramBoolean) {
            showToast("保存成功");
            finish();
        }
    }

}
