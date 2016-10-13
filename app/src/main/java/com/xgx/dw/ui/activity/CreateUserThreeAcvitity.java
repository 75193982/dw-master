package com.xgx.dw.ui.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.xgx.dw.R;
import com.xgx.dw.StoreBean;
import com.xgx.dw.TransformerBean;
import com.xgx.dw.UserBean;
import com.xgx.dw.app.G;
import com.xgx.dw.app.Setting;
import com.xgx.dw.base.BaseAppCompatActivity;
import com.xgx.dw.bean.LoginInformation;
import com.xgx.dw.dao.StoreBeanDaoHelper;
import com.xgx.dw.dao.TransformerBeanDaoHelper;
import com.xgx.dw.presenter.impl.UserPresenterImpl;
import com.xgx.dw.presenter.interfaces.IUserPresenter;
import com.xgx.dw.ui.view.interfaces.IUserView;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import fr.ganfra.materialspinner.MaterialSpinner;

public class CreateUserThreeAcvitity extends BaseAppCompatActivity implements IUserView, Toolbar.OnMenuItemClickListener {
    @Bind(R.id.imeTv)
    TextView imeTv;
    @Bind(R.id.store_spinner)
    MaterialSpinner storeSpinner;
    @Bind(R.id.store_layout)
    LinearLayout storeLayout;
    @Bind(R.id.transformer_spinner)
    MaterialSpinner transformerSpinner;
    @Bind(R.id.transformer_layout)
    LinearLayout transformerLayout;
    @Bind(R.id.user_id)
    MaterialEditText userId;
    @Bind(R.id.user_name)
    MaterialEditText userName;
    @Bind(R.id.voltageRatio)
    MaterialEditText voltageRatio;
    @Bind(R.id.currentRatio)
    MaterialEditText currentRatio;
    @Bind(R.id.price)
    MaterialEditText price;
    @Bind(R.id.meterNum)
    MaterialEditText meterNum;
    @Bind(R.id.phone)
    MaterialEditText phone;
    @Bind(R.id.action_save)
    RippleView actionSave;
    private IUserPresenter presenter;
    private List<StoreBean> storebeans;
    private UserBean bean;
    private List<TransformerBean> transformerBean;
    private String currentStoreId;
    private String currentStoreName;
    private boolean isFirst;


    public void initContentView() {
        baseSetContentView(R.layout.activity_create_user_three);
    }

    public void initPresenter() {
        presenter = new UserPresenterImpl();
    }

    public void initView() {
        getToolbar().setOnMenuItemClickListener(this);
        String ime = getIntent().getStringExtra("ime");
        imeTv.setText(checkText(ime));
        Setting setting = new Setting(this);
        currentStoreId = setting.loadString(G.currentStoreId);
        currentStoreName = setting.loadString(G.currentStoreName);
        initSpinnerData();
        initEditInfo();
    }

    private void initEditInfo() {
        isFirst = true;
        bean = ((UserBean) getIntent().getSerializableExtra("bean"));
        if (bean != null && !TextUtils.isEmpty(this.bean.getUserId())) {
            getSupportActionBar().setTitle("编辑二级用户");
            this.userId.setText(this.bean.getUserId());
            this.userId.setEnabled(false);
            userName.setText(checkText(this.bean.getUserName()));
            price.setText(checkText(bean.getPrice()));
            phone.setText(checkText(bean.getPhone()));
            meterNum.setText(checkText(bean.getMeterNum()));
            currentRatio.setText(checkText(bean.getCurrentRatio()));
            voltageRatio.setText(checkText(bean.getVoltageRatio()));
            imeTv.setText(checkText(bean.getIme()));
            try {
                for (int i = 0; i < storebeans.size(); i++) {
                    if (bean.getStoreId().equals(((StoreBean) storebeans.get(i)).getId())) {
                        storeSpinner.setSelection(i + 1);
                        storeSpinner.setEnabled(false);
                    }
                }
            } catch (Exception e) {

            }
            try {
                transformerBean = TransformerBeanDaoHelper.getInstance().testQueryBy(bean.getStoreId());
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
                    if (bean.getTransformerId().equals(((TransformerBean) transformerBean.get(i)).getId())) {
                        transformerSpinner.setSelection(i + 1);
                        transformerSpinner.setEnabled(false);
                    }
                }
            } catch (Exception e) {

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
            getSupportActionBar().setTitle("创建二级用户");
        }

    }

    private void setOnItemSelected() {
        storeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        //如果是10级用户 则直接拉去当前用户的营业厅，如果是11用户 则直接显示当前用户的营业厅和台区
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
        userBean.setVoltageRatio(voltageRatio.getText().toString());
        userBean.setCurrentRatio(currentRatio.getText().toString());
        userBean.setMeterNum(meterNum.getText().toString());
        userBean.setPhone(phone.getText().toString());
        userBean.setPrice(price.getText().toString());
        if ((bean == null) || (TextUtils.isEmpty(bean.getUserId()))) {
            userBean.setPassword(userId.getText().toString());
            this.presenter.saveUser(this, userBean, 20, true);
            return;
        }
        userBean.setPassword(userId.getText().toString());
        this.presenter.saveUser(this, userBean, 20, false);
    }

    public void saveTransformer(boolean paramBoolean) {
        hideProgress();
        if (paramBoolean) {
            showToast("保存成功");
            finish();
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
                startActivity(new Intent(this, TestGeneratectivity.class).putExtra("type", 3).putExtra("id", bean.getUserId()));
                break;
        }
        return true;
    }

}
