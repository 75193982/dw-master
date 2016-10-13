package com.xgx.dw.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.andexert.library.RippleView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.xgx.dw.R;
import com.xgx.dw.SpotPricingBean;
import com.xgx.dw.StoreBean;
import com.xgx.dw.TransformerBean;
import com.xgx.dw.app.G;
import com.xgx.dw.app.Setting;
import com.xgx.dw.base.BaseAppCompatActivity;
import com.xgx.dw.bean.LoginInformation;
import com.xgx.dw.dao.StoreBeanDaoHelper;
import com.xgx.dw.dao.TransformerBeanDaoHelper;
import com.xgx.dw.presenter.impl.SpotPricingPresenterImpl;
import com.xgx.dw.ui.view.interfaces.ICreateSpotPricingView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.ganfra.materialspinner.MaterialSpinner;

public class CreateSpotPricingAcvitity extends BaseAppCompatActivity implements ICreateSpotPricingView {
    @Bind(R.id.spinner)
    MaterialSpinner spinner;
    @Bind(R.id.spotPricing_name)
    MaterialEditText spotPricingName;
    @Bind(R.id.spotPricing_id)
    MaterialEditText spotPricing_id;
    @Bind(R.id.spotpricing_priceCount)
    MaterialEditText spotpricingPriceCount;
    @Bind(R.id.spinner_type)
    MaterialSpinner spinnerType;
    @Bind(R.id.spotpricing_pointedPrice)
    MaterialEditText spotpricingPointedPrice;
    @Bind(R.id.spotpricing_peakPrice)
    MaterialEditText spotpricingPeakPrice;
    @Bind(R.id.spotpricing_flatPrice)
    MaterialEditText spotpricingFlatPrice;
    @Bind(R.id.spotpricing_valleyPrice)
    MaterialEditText spotpricingValleyPrice;
    @Bind(R.id.action_save)
    RippleView actionSave;
    private List<StoreBean> storebeans;
    private SpotPricingPresenterImpl spotPricingPresenter;
    private SpotPricingBean bean;

    public void initContentView() {
        baseSetContentView(R.layout.activity_create_spot_pricing);
    }

    public void initPresenter() {
        spotPricingPresenter = new SpotPricingPresenterImpl();
    }

    public void initView() {
        storebeans = new ArrayList<>();
        Setting setting = new Setting(this);
        String currentUserType = LoginInformation.getInstance().getUser().getType();
        String currentStoreId = LoginInformation.getInstance().getUser().getStoreId();
        String currentStoreName = LoginInformation.getInstance().getUser().getStoreName();
        if (currentUserType.equals("10")) {
            storebeans.add(new StoreBean(currentStoreId, currentStoreName));
        } else {
            storebeans = StoreBeanDaoHelper.getInstance().getAllData();
        }
        if ((this.storebeans != null) && (this.storebeans.size() > 0)) {
            String[] arrayOfString = new String[this.storebeans.size()];
            for (int j = 0; j < this.storebeans.size(); j++) {
                arrayOfString[j] = ((StoreBean) this.storebeans.get(j)).getName();
            }
            ArrayAdapter localArrayAdapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayOfString);
            localArrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            this.spinner.setAdapter(localArrayAdapter2);
        }
        ArrayAdapter localArrayAdapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, new String[]{"普通", "分时"});
        localArrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinnerType.setAdapter(localArrayAdapter1);
        this.spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong) {
                if (paramAnonymousInt == 1) {
                    CreateSpotPricingAcvitity.this.spotpricingPointedPrice.setEnabled(true);
                    CreateSpotPricingAcvitity.this.spotpricingPeakPrice.setEnabled(true);
                    CreateSpotPricingAcvitity.this.spotpricingFlatPrice.setEnabled(true);
                    CreateSpotPricingAcvitity.this.spotpricingValleyPrice.setEnabled(true);
                }
                while (paramAnonymousInt != 0) {
                    return;
                }
                CreateSpotPricingAcvitity.this.spotpricingPointedPrice.setEnabled(false);
                CreateSpotPricingAcvitity.this.spotpricingPeakPrice.setEnabled(false);
                CreateSpotPricingAcvitity.this.spotpricingFlatPrice.setEnabled(false);
                CreateSpotPricingAcvitity.this.spotpricingValleyPrice.setEnabled(false);
                CreateSpotPricingAcvitity.this.spotpricingPointedPrice.setText("0.00");
                CreateSpotPricingAcvitity.this.spotpricingPeakPrice.setText("0.00");
                CreateSpotPricingAcvitity.this.spotpricingFlatPrice.setText("0.00");
                CreateSpotPricingAcvitity.this.spotpricingValleyPrice.setText("0.00");
            }

            public void onNothingSelected(AdapterView<?> paramAnonymousAdapterView) {
            }
        });
        bean = ((SpotPricingBean) getIntent().getSerializableExtra("bean"));
        if ((this.bean != null) && (!TextUtils.isEmpty(this.bean.getId()))) {
            getSupportActionBar().setTitle(R.string.create_spotpricing);
            this.spotPricingName.setText(checkText(this.bean.getName()));
            spotPricing_id.setText(bean.getId());
            this.spotpricingPriceCount.setText(checkText(this.bean.getPrice_count()));
            for (int i = 0; i < this.storebeans.size(); i++) {
                if (this.bean.getStore_id().equals(((StoreBean) this.storebeans.get(i)).getId())) {
                    this.spinner.setSelection(i + 1);
                    this.spinner.setEnabled(false);
                }
            }
            if (!checkText(this.bean.getType()).equals("普通")) {
                this.spinnerType.setSelection(1);
            }
            if (checkText(this.bean.getType()).equals("分时")) {
                this.spinnerType.setSelection(2);
            }
            this.spotpricingPointedPrice.setText(checkText(this.bean.getPointed_price()));
            this.spotpricingPeakPrice.setText(checkText(this.bean.getPeek_price()));
            this.spotpricingFlatPrice.setText(checkText(this.bean.getFlat_price()));
            this.spotpricingValleyPrice.setText(checkText(this.bean.getValley_price()));
            getSupportActionBar().setTitle(R.string.upgrade_spotpricing);
        } else {
            getSupportActionBar().setTitle(R.string.create_spotpricing);
        }
    }


    @OnClick({R.id.action_save})
    public void onSaveClick() {
        SpotPricingBean bean = new SpotPricingBean();
        bean.setName(this.spotPricingName.getText().toString());
        bean.setPeek_price(this.spotpricingPeakPrice.getText().toString());
        bean.setValley_price(this.spotpricingValleyPrice.getText().toString());
        bean.setPointed_price(this.spotpricingPointedPrice.getText().toString());
        bean.setFlat_price(this.spotpricingFlatPrice.getText().toString());
        bean.setPrice_count(this.spotpricingPriceCount.getText().toString());
        int i = this.spinner.getSelectedItemPosition();
        try {
            StoreBean localStoreBean = (StoreBean) this.storebeans.get(i - 1);
            bean.setStore_id(localStoreBean.getId());
            bean.setStorename(localStoreBean.getName());
        } catch (Exception e) {
            bean.setStore_id("");
            bean.setStorename("");
        }

        bean.setType(this.spinnerType.getSelectedItem().toString());
        if (bean != null && !TextUtils.isEmpty(this.bean.getId())) {
            bean.setId(spotPricing_id.getText().toString());
            this.spotPricingPresenter.saveSpotPricing(this, bean, true);
            return;
        }
        this.spotPricingPresenter.saveSpotPricing(this, bean, false);
    }

    public void saveSpotPricing(boolean paramBoolean) {
        hideProgress();
        if (paramBoolean) {
            showToast("保存成功");
            finish();
        }


    }

}
