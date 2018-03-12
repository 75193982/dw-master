package com.xgx.dw.ui.activity;

import android.content.Intent;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.xgx.dw.R;
import com.xgx.dw.StoreBean;
import com.xgx.dw.TransformerBean;
import com.xgx.dw.UserBean;
import com.xgx.dw.base.BaseAppCompatActivity;
import com.xgx.dw.bean.LoginInformation;
import com.xgx.dw.dao.StoreBeanDaoHelper;
import com.xgx.dw.dao.TransformerBeanDaoHelper;
import com.xgx.dw.presenter.impl.UserPresenterImpl;
import com.xgx.dw.presenter.interfaces.IUserPresenter;
import com.xgx.dw.ui.view.interfaces.IUserView;

import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;
import fr.ganfra.materialspinner.MaterialSpinner;

public class CreateUserTwoAcvitity extends BaseAppCompatActivity implements IUserView, Toolbar.OnMenuItemClickListener {
    @BindView(R.id.store_spinner)
    MaterialSpinner storeSpinner;
    @BindView(R.id.transformer_spinner)
    MaterialSpinner transformerSpinner;
    @BindView(R.id.buy_switch)
    SwitchCompat buySwitch;
    @BindView(R.id.test_switch)
    SwitchCompat testSwitch;
    @BindView(R.id.action_save)
    LinearLayout actionSave;
    @BindView(R.id.store_layout)
    LinearLayout storeLayout;
    @BindView(R.id.transformer_layout)
    LinearLayout transformerLayout;
    @BindView(R.id.user_id)
    MaterialEditText userId;
    @BindView(R.id.user_name)
    MaterialEditText userName;
    @BindView(R.id.imeTv)
    MaterialEditText imeTv;
    private IUserPresenter presenter;
    private List<StoreBean> storebeans;
    private UserBean bean;
    private List<TransformerBean> transformerBean;


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
        initSpinnerData();
        initEditInfo();
    }

    private void initEditInfo() {
        bean = ((UserBean) getIntent().getSerializableExtra("bean"));
        if (bean != null && !TextUtils.isEmpty(this.bean.getId())) {
            getSupportActionBar().setTitle("编辑台区管理员");
            this.userId.setText(this.bean.getUserId());
            imeTv.setText(checkText(bean.getIme()));
            imeTv.setEnabled(false);
            this.userId.setEnabled(false);
            this.userName.setText(checkText(this.bean.getUserName()));
            setSpinner(bean.getStoreId(), bean.getTransformerId());
            storeSpinner.setEnabled(false);
            transformerSpinner.setEnabled(false);
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
            if (!"admin,10".contains(LoginInformation.getInstance().getUser().getType())) {
                buySwitch.setEnabled(false);
                testSwitch.setEnabled(false);
            }
        } else {
            if (LoginInformation.getInstance().getUser().getType().equals("11")) {//台区管理员
                setSpinner(LoginInformation.getInstance().getUser().getStoreId(), LoginInformation.getInstance().getUser().getTransformerId());
                storeSpinner.setEnabled(false);
                transformerSpinner.setEnabled(false);
            } else if (LoginInformation.getInstance().getUser().getType().equals("10")) {//营业厅管理员
                setSpinner(LoginInformation.getInstance().getUser().getStoreId(), LoginInformation.getInstance().getUser().getTransformerId());
                storeSpinner.setEnabled(false);
                setOnItemSelected();
            } else {
                setOnItemSelected();
            }
            getSupportActionBar().setTitle("创建台区管理员");
        }
    }

    private void setOnItemSelected() {
        storeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int position, long paramAnonymousLong) {
                if (position != -1) {
                    String id = storebeans.get(position).getId();
                    transformerBean = TransformerBeanDaoHelper.getInstance().testQueryBy(id);
                    if ((transformerBean != null) && (transformerBean.size() > 0)) {
                        String[] arrayOfString = new String[transformerBean.size()];
                        for (int j = 0; j < transformerBean.size(); j++) {
                            arrayOfString[j] = ((TransformerBean) transformerBean.get(j)).getName();
                        }
                        ArrayAdapter localArrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, arrayOfString);
                        localArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        transformerSpinner.setAdapter(localArrayAdapter);
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> paramAnonymousAdapterView) {
            }
        });
    }

    private void setSpinner(String storeId, String transformerId) {
        try {
            for (int i = 0; i < storebeans.size(); i++) {
                if (storeId.equals(((StoreBean) storebeans.get(i)).getId())) {
                    storeSpinner.setSelection(i + 1);
                }
            }
        } catch (Exception e) {

        }
        if (!TextUtils.isEmpty(transformerId)) {
            try {
                transformerBean = TransformerBeanDaoHelper.getInstance().testQueryBy(storeId);
                if ((transformerBean != null) && (transformerBean.size() > 0)) {
                    String[] arrayOfString = new String[transformerBean.size()];
                    for (int j = 0; j < transformerBean.size(); j++) {
                        arrayOfString[j] = ((TransformerBean) transformerBean.get(j)).getName();
                    }
                    ArrayAdapter localArrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, arrayOfString);
                    localArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    transformerSpinner.setAdapter(localArrayAdapter);
                }
                for (int i = 0; i < transformerBean.size(); i++) {
                    if (transformerId.equals(((TransformerBean) transformerBean.get(i)).getId())) {
                        transformerSpinner.setSelection(i + 1);
                    }
                }
            } catch (Exception e) {

            }
        }

    }

    private void initSpinnerData() {
        storebeans = StoreBeanDaoHelper.getInstance().getAllData();
        if ((this.storebeans != null) && (this.storebeans.size() > 0)) {
            String[] arrayOfString = new String[this.storebeans.size()];
            for (int j = 0; j < this.storebeans.size(); j++) {
                arrayOfString[j] = ((StoreBean) this.storebeans.get(j)).getName();
            }
            ArrayAdapter localArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayOfString);
            localArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            this.storeSpinner.setAdapter(localArrayAdapter);
        }

    }

    @OnClick({R.id.action_save})
    public void onSaveClick() {
        showProgress("保存用户中...");
        UserBean userBean = new UserBean();
        userBean.setUserId(userId.getText().toString());
        userBean.setUserName(userName.getText().toString());
        try {
            StoreBean storeBean = (StoreBean) this.storebeans.get(storeSpinner.getSelectedItemPosition() - 1);
            userBean.setStoreId(storeBean.getId());
            userBean.setStoreName(storeBean.getName());
        } catch (Exception e) {
            userBean.setStoreId("");
            userBean.setStoreName("");
        }
        try {
            int y = this.transformerSpinner.getSelectedItemPosition();
            TransformerBean transFormerBean = (TransformerBean) this.transformerBean.get(y - 1);
            userBean.setTransformerId(transFormerBean.getId());
            userBean.setTransformerName(transFormerBean.getName());
        } catch (Exception e) {
        }
        userBean.setIme(imeTv.getText().toString());
        userBean.setIsBuy(buySwitch.isChecked() ? "1" : "0");
        userBean.setIsTest(testSwitch.isChecked() ? "1" : "0");
        userBean.setPassword(userId.getText().toString());
        if ((bean == null) || (TextUtils.isEmpty(bean.getUserId()))) {
            userBean.setPassword(userId.getText().toString());
            userBean.setId(UUID.randomUUID().toString());
            this.presenter.saveUser(this, userBean, 11, true);
            return;
        } else {
            userBean.setId(bean.getId());
        }

        this.presenter.saveUser(this, userBean, 11, false);
    }

    @Override
    public void saveTransformer(boolean paramBoolean, String id) {
        hideProgress();
        if (paramBoolean) {
            showToast("保存成功");
            startActivity(new Intent(this, TestGeneratectivity.class).putExtra("type", 3).putExtra("id", id));
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
        }
        return true;
    }

}
