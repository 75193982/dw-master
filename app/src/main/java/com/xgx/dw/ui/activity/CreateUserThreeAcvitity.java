package com.xgx.dw.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
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
import com.xgx.dw.dao.TransformerBeanDaoHelper;
import com.xgx.dw.presenter.impl.UserPresenterImpl;
import com.xgx.dw.presenter.interfaces.IUserPresenter;
import com.xgx.dw.ui.view.interfaces.IUserView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.ganfra.materialspinner.MaterialSpinner;

public class CreateUserThreeAcvitity extends BaseAppCompatActivity implements IUserView {
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


    public void initContentView() {
        baseSetContentView(R.layout.activity_create_user_three);
    }

    public void initPresenter() {
        presenter = new UserPresenterImpl();
    }

    public void initView() {
        Setting setting = new Setting(this);
        currentStoreId = setting.loadString(G.currentStoreId);
        currentStoreName = setting.loadString(G.currentStoreName);
        initSpinnerData();
        initEditInfo();
    }

    private void initEditInfo() {
        bean = ((UserBean) getIntent().getSerializableExtra("bean"));
        if (bean != null && !TextUtils.isEmpty(this.bean.getUserId())) {
            getSupportActionBar().setTitle("编辑二级用户");
            this.userId.setText(this.bean.getUserId());
            this.userId.setEnabled(false);
            this.userName.setText(checkText(this.bean.getUserName()));
            try {
                for (int i = 0; i < this.transformerBean.size(); i++) {
                    if (this.bean.getTransformerId().equals(((TransformerBean) this.transformerBean.get(i)).getId())) {
                        this.transformerSpinner.setSelection(i + 1);
                        this.transformerSpinner.setEnabled(false);
                    }
                }
            } catch (Exception e) {

            }
        } else {
            getSupportActionBar().setTitle("创建二级用户");
            bean = new UserBean();
            bean.setStoreId(currentStoreId);
            bean.setStoreId(currentStoreName);
        }
    }

    private void initSpinnerData() {
        //如果是10级用户 则直接拉去当前用户的营业厅，如果是11用户 则直接显示当前用户的营业厅和台区
        transformerBean = TransformerBeanDaoHelper.getInstance().testQueryBy(currentStoreId);
        if ((this.transformerBean != null) && (this.transformerBean.size() > 0)) {
            String[] arrayOfString = new String[this.transformerBean.size()];
            for (int j = 0; j < this.transformerBean.size(); j++) {
                arrayOfString[j] = ((TransformerBean) this.transformerBean.get(j)).getName();
            }
            ArrayAdapter localArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayOfString);
            localArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            this.transformerSpinner.setAdapter(localArrayAdapter);
        }
    }

    @OnClick({R.id.action_save})
    public void onSaveClick() {
        showProgress("保存用户中...");
        UserBean userBean = new UserBean();
        userBean.setUserId(userId.getText().toString());
        userBean.setUserName(userName.getText().toString());
        userBean.setStoreId(bean.getStoreId());
        userBean.setStoreName(bean.getStoreName());
        try {
            int y = this.transformerSpinner.getSelectedItemPosition();
            TransformerBean transFormerBean = (TransformerBean) this.transformerBean.get(y - 1);
            userBean.setTransformerId(transFormerBean.getId());
            userBean.setTransformerName(transFormerBean.getName());
        } catch (Exception e) {

        }


        if ((bean == null) || (TextUtils.isEmpty(bean.getUserId()))) {
            userBean.setPassword(userId.getText().toString());
            this.presenter.saveUser(this, userBean, 11, true);
            return;
        }
        userBean.setPassword(userId.getText().toString());
        this.presenter.saveUser(this, userBean, 11, false);
    }

    public void saveTransformer(boolean paramBoolean) {
        hideProgress();
        if (paramBoolean) {
            showToast("保存成功");
            finish();
        }
    }

}
