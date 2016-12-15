package com.xgx.dw.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.xgx.dw.PricingBean;
import com.xgx.dw.R;
import com.xgx.dw.SpotPricingBean;
import com.xgx.dw.StoreBean;
import com.xgx.dw.TransformerBean;
import com.xgx.dw.UserBean;
import com.xgx.dw.app.G;
import com.xgx.dw.app.Setting;
import com.xgx.dw.base.BaseAppCompatActivity;
import com.xgx.dw.bean.LoginInformation;
import com.xgx.dw.bean.UserAllInfo;
import com.xgx.dw.dao.PricingDaoHelper;
import com.xgx.dw.dao.SpotPricingBeanDaoHelper;
import com.xgx.dw.dao.StoreBeanDaoHelper;
import com.xgx.dw.dao.TransformerBeanDaoHelper;
import com.xgx.dw.dao.UserBeanDaoHelper;
import com.xgx.dw.presenter.impl.UserPresenterImpl;
import com.xgx.dw.presenter.interfaces.IUserPresenter;
import com.xgx.dw.ui.view.interfaces.IUserView;
import com.xgx.dw.utils.Logger;
import com.xgx.dw.utils.MyUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.ganfra.materialspinner.MaterialSpinner;

public class CreateUserThreeAcvitity extends BaseAppCompatActivity implements IUserView, Toolbar.OnMenuItemClickListener {
    @Bind(R.id.imeTv)
    MaterialEditText imeTv;
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
    TextView price;
    @Bind(R.id.phone)
    MaterialEditText phone;
    @Bind(R.id.action_save)
    LinearLayout actionSave;
    private IUserPresenter presenter;
    private List<StoreBean> storebeans;
    private UserBean bean;
    private List<TransformerBean> transformerBean;
    private String currentStoreId;
    private String currentStoreName;
    private boolean isFirst;
    private boolean isSave;

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
        bean = ((UserBean) getIntent().getSerializableExtra("bean"));
        isFirst = true;
        initEditInfo(bean);
        userId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                UserBean tempUser = UserBeanDaoHelper.getInstance().queryByTransFormUserId(s.toString());
                isSave = true;
                if (tempUser != null && tempUser.getType().equals("20")) {
                    initUserInfo(tempUser);
                    userId.setTag(tempUser.getId());
                } else {
                    userId.setTag("");
                }
            }
        });
    }

    private void initEditInfo(UserBean bean) {
        try {
            if (bean != null && !TextUtils.isEmpty(bean.getUserId())) {
                imeTv.setText(checkText(bean.getIme()));
                userId.setTag(bean.getId());
                userId.setText(bean.getUserId());
                initUserInfo(bean);
                isSave = false;
            } else {
                isSave = true;
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
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }


    }

    private void initUserInfo(UserBean bean) {
        imeTv.setEnabled(false);
        getSupportActionBar().setTitle("编辑二级用户");
        userName.setText(checkText(bean.getUserName()));
        SpotPricingBean spotPricingBean = SpotPricingBeanDaoHelper.getInstance().getDataById(bean.getPrice());
        price.setText(checkText(spotPricingBean.getName()));
        price.setTag(checkText(spotPricingBean.getId()));
        phone.setText(checkText(bean.getPhone()));
        currentRatio.setText(checkText(bean.getCurrentRatio()));
        voltageRatio.setText(checkText(bean.getVoltageRatio()));
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
        userBean.setPhone(phone.getText().toString());
        userBean.setPrice(price.getTag() == null ? "" : price.getTag().toString());
        if (userId.getTag() == null || TextUtils.isEmpty(userId.getTag().toString())) {
            userBean.setId(UUID.randomUUID().toString());
        } else {
            userBean.setId(userId.getTag().toString());
        }
        userBean.setPassword(userId.getText().toString());
        this.presenter.saveUser(this, userBean, 20, isSave);
    }

    public void saveTransformer(boolean paramBoolean, String id) {
        hideProgress();
        if (paramBoolean) {
            showToast("保存成功");
            if (isSave) {
                //进入购电界面

                UserAllInfo userAllInfo = new UserAllInfo();
                UserBean bean = new UserBean();
                StoreBean storebean = new StoreBean();
                TransformerBean transbean = new TransformerBean();
                List<PricingBean> pricings = new ArrayList<>();
                SpotPricingBean spotPricingBeans = new SpotPricingBean();
                bean = UserBeanDaoHelper.getInstance().getDataById(id);
                bean.setIme(MyUtils.getuniqueId(this));
                storebean = StoreBeanDaoHelper.getInstance().getDataById(bean.getStoreId());
                transbean = TransformerBeanDaoHelper.getInstance().getDataById(bean.getTransformerId());
                try {
                    spotPricingBeans = SpotPricingBeanDaoHelper.getInstance().getDataById(bean.getPrice());
                } catch (Exception e) {
                    Logger.e(e.getMessage());
                }
                userAllInfo.setSpotBeans(spotPricingBeans);
                userAllInfo.setStoreBean(storebean);
                userAllInfo.setTransformerBean(transbean);
                pricings = PricingDaoHelper.getInstance().queryByUserId(bean.getId());
                userAllInfo.setPricingSize(pricings.size());
                bean.setEcodeType(6 + "");
                userAllInfo.setUser(bean);
                startActivity(new Intent(getContext(), BuySpotActivity.class).putExtra("userAllInfo", userAllInfo));

            }

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
                startActivity(new Intent(this, TestGeneratectivity.class).putExtra("type", 3).putExtra("id", bean.getId()));
                break;
            case R.id.action_spotInfo:
                Intent intent = new Intent(getContext(), AdminSpotListActivity.class);
                intent.putExtra("id", bean.getId());
                startActivity(intent);
                break;
        }
        return true;
    }


    @OnClick(R.id.price)
    public void onClickToPrice() {
        Intent intent = new Intent(getContext(), SearchSpotPricingListActivity.class);
        startActivityForResult(intent, 1001);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == 1001) {
            SpotPricingBean bean = (SpotPricingBean) data.getSerializableExtra("entity");
            price.setText(bean.getName());
            price.setTag(bean.getId());
        }
    }
}
