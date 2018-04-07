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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.xgx.dw.PricingBean;
import com.xgx.dw.R;
import com.xgx.dw.SpotPricingBean;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateUserThreeAcvitity extends BaseEventBusActivity implements IUserView, Toolbar.OnMenuItemClickListener {
    @BindView(R.id.imeTv)
    MaterialEditText imeTv;
    @BindView(R.id.user_id)
    MaterialEditText userId;
    @BindView(R.id.user_name)
    MaterialEditText userName;
    @BindView(R.id.voltageRatio)
    MaterialEditText voltageRatio;
    @BindView(R.id.currentRatio)
    MaterialEditText currentRatio;
    @BindView(R.id.price)
    TextView priceTv;
    @BindView(R.id.phone)
    MaterialEditText phone;
    @BindView(R.id.mobileTv)
    MaterialEditText mobileTv;
    @BindView(R.id.action_save)
    LinearLayout actionSave;
    @BindView(R.id.contyTv)
    TextView contyTv;
    @BindView(R.id.transformerTv)
    TextView transformerTv;
    private IUserPresenter presenter;
    private UserBean bean;
    private String currentStoreId;
    private String currentStoreName;
    private boolean isFirst;
    private boolean isSave;
    private String currentTransformId;
    private String currentTransformName;

    @Override
    public void initContentView() {
        baseSetContentView(R.layout.activity_create_user_three);
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
        Setting setting = new Setting(this);
        currentStoreId = setting.loadString(G.currentStoreId);
        currentStoreName = setting.loadString(G.currentStoreName);
        currentTransformId = setting.loadString(G.currentTransformId);
        currentTransformName = setting.loadString(G.currentTransformName);
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
                if (LoginInformation.getInstance().getUser().getType().equals("11")) {//台区管理员
                    contyTv.setText(currentStoreName);
                    contyTv.setContentDescription(currentStoreId);
                    transformerTv.setText(currentTransformName);
                    transformerTv.setContentDescription(currentTransformId);
                } else if (LoginInformation.getInstance().getUser().getType().equals("10")) {//营业厅管理员
                    contyTv.setText(currentStoreName);
                    contyTv.setContentDescription(currentStoreId);
                } else {
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
        priceTv.setText(checkText(bean.getPriceName()));
        priceTv.setTag(checkText(bean.getPrice()));
        phone.setText(checkText(bean.getPhone()));
        mobileTv.setText(checkText(bean.getMobile()));
        currentRatio.setText(checkText(bean.getCurrentRatio()));
        voltageRatio.setText(checkText(bean.getVoltageRatio()));
        contyTv.setText(bean.getStoreName());
        contyTv.setContentDescription(bean.getStoreId());
        transformerTv.setText(bean.getTransformerName());
        transformerTv.setContentDescription(bean.getTransformerId());
    }


    @OnClick({R.id.action_save})
    public void onSaveClick() {
        UserBean userBean = new UserBean();
        userBean.setUserId(userId.getText().toString());
        userBean.setUserName(userName.getText().toString());
        userBean.setIme(imeTv.getText().toString());
        userBean.setVoltageRatio(voltageRatio.getText().toString());
        userBean.setCurrentRatio(currentRatio.getText().toString());
        userBean.setPhone(phone.getText().toString());
        userBean.setMobile(mobileTv.getText().toString());
        userBean.setPrice(priceTv.getTag() == null ? "" : priceTv.getTag().toString());
        userBean.setPriceName(priceTv.getText().toString());
        if (userId.getTag() != null && !TextUtils.isEmpty(userId.getTag().toString())) {
            userBean.setId(userId.getTag().toString());
        }
        userBean.setPassword(userId.getText().toString());
        userBean.setTransformerId(transformerTv.getContentDescription() != null ? transformerTv.getContentDescription().toString() : "");
        userBean.setTransformerName(transformerTv.getText().toString());
        userBean.setStoreId(contyTv.getContentDescription() != null ? contyTv.getContentDescription().toString() : "");
        userBean.setStoreName(contyTv.getText().toString());
        this.presenter.saveUser(this, userBean, 20);
    }

    @Override
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
                //  bean.setEcodeType(6 + "");
                userAllInfo.setUser(bean);
                //startActivity(new Intent(getContext(), BuySpotActivity.class).putExtra("userAllInfo", userAllInfo));
                startActivity(new Intent(this, TestGeneratectivity.class).putExtra("type", 3).putExtra("id", id));
            }

            finish();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu paramMenu) {
        if (bean != null && !TextUtils.isEmpty(bean.getUserId())) {
            getMenuInflater().inflate(R.menu.menu_erweima1, paramMenu);
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
                intent.putExtra("id", bean.getId());
                startActivity(intent);
                break;
            case R.id.action_buy:
                UserAllInfo userAllInfo = new UserAllInfo();
                StoreBean storebean = StoreBeanDaoHelper.getInstance().getDataById(bean.getStoreId());
                TransformerBean transbean = TransformerBeanDaoHelper.getInstance().getDataById(bean.getTransformerId());
                SpotPricingBean spotPricingBeans = null;
                try {
                    spotPricingBeans = SpotPricingBeanDaoHelper.getInstance().getDataById(bean.getPrice());
                } catch (Exception e) {
                    Logger.e(e.getMessage());
                }
                userAllInfo.setSpotBeans(spotPricingBeans);
                userAllInfo.setStoreBean(storebean);
                userAllInfo.setTransformerBean(transbean);
                List<PricingBean> pricings = PricingDaoHelper.getInstance().queryByUserId(bean.getId());
                userAllInfo.setPricingSize(pricings.size());
                bean.setEcodeType(4 + "");
                userAllInfo.setUser(bean);
                userAllInfo.setEcodeType("4");
                startActivity(new Intent(getContext(), BuySpotActivity.class).putExtra("userAllInfo", userAllInfo));
                break;
        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == 1001) {
            SpotPricingBean bean = (SpotPricingBean) data.getSerializableExtra("entity");
            priceTv.setText(bean.getName());
            priceTv.setTag(bean.getId());
        }
    }

    @Override
    protected void onEventComming(EventCenter eventCenter) {
        if (EventCenter.COUNTY_SELECT == eventCenter.getEventCode()) {
            try {
                County county = (County) eventCenter.getData();
                contyTv.setText(checkText(county.getCountyname()));
                contyTv.setContentDescription(checkText(county.getCountyid()));
                transformerTv.setText("");
                transformerTv.setContentDescription("");
            } catch (Exception e) {

            }
        }
        if (EventCenter.TAIQU_SELECT == eventCenter.getEventCode()) {
            try {
                Taiqu taiqu = (Taiqu) eventCenter.getData();
                transformerTv.setText(checkText(taiqu.getName()));
                transformerTv.setContentDescription(checkText(taiqu.getCode()));
            } catch (Exception e) {

            }
        }
        if (EventCenter.PRICE_SELECT == eventCenter.getEventCode()) {
            try {
                Price price = (Price) eventCenter.getData();
                priceTv.setText(checkText(price.getPricename()));
                priceTv.setTag(checkText(price.getId() + ""));
            } catch (Exception e) {

            }
        }
    }

    @Override
    public boolean isBindEventBusHere() {
        return true;
    }

    @OnClick({R.id.contyTv, R.id.transformerTv, R.id.price})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.price:
                if (contyTv.getContentDescription() != null && !TextUtils.isEmpty(contyTv.getContentDescription().toString())) {

                    Intent priceintent = new Intent(CreateUserThreeAcvitity.this, SpotPricingActivity.class);
                    priceintent.putExtra("isSelect", true);
                    priceintent.putExtra("countyid", contyTv.getContentDescription().toString());
                    startActivity(priceintent);
                } else {
                    ToastUtils.showShort("请选择营业厅");
                }
                break;
            case R.id.contyTv:
                if (LoginInformation.getInstance().getUser().getType().equals("0")) {
                    Intent intent = new Intent(CreateUserThreeAcvitity.this, StoresMgrActivity.class);
                    intent.putExtra("isSelect", true);

                    startActivity(intent);
                }

                break;
            case R.id.transformerTv:

                if (LoginInformation.getInstance().getUser().getType().equals("0") || LoginInformation.getInstance().getUser().getType().equals("10")) {
                    if (contyTv.getContentDescription() != null && !TextUtils.isEmpty(contyTv.getContentDescription().toString())) {
                        Intent taiquIntent = new Intent(CreateUserThreeAcvitity.this, TransformerActivity.class);
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
