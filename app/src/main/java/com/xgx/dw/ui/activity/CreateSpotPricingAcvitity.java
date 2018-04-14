package com.xgx.dw.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.xgx.dw.R;
import com.xgx.dw.app.G;
import com.xgx.dw.app.Setting;
import com.xgx.dw.base.BaseEventBusActivity;
import com.xgx.dw.base.EventCenter;
import com.xgx.dw.bean.LoginInformation;
import com.xgx.dw.bean.Price;
import com.xgx.dw.bean.SysDept;
import com.xgx.dw.presenter.impl.SpotPricingPresenterImpl;
import com.xgx.dw.presenter.interfaces.ISpotPricingPresenter;
import com.xgx.dw.ui.view.interfaces.ICreateSpotPricingView;

import butterknife.BindView;
import butterknife.OnClick;
import fr.ganfra.materialspinner.MaterialSpinner;

public class CreateSpotPricingAcvitity extends BaseEventBusActivity implements ICreateSpotPricingView {
    @BindView(R.id.contyTv)
    TextView contyTv;
    @BindView(R.id.spotPricing_name)
    MaterialEditText spotPricingName;
    @BindView(R.id.spotPricing_id)
    MaterialEditText spotPricing_id;
    @BindView(R.id.spotpricing_priceCount)
    MaterialEditText spotpricingPriceCount;
    @BindView(R.id.spinner_type)
    MaterialSpinner spinnerType;
    @BindView(R.id.spotpricing_pointedPrice)
    MaterialEditText spotpricingPointedPrice;
    @BindView(R.id.spotpricing_peakPrice)
    MaterialEditText spotpricingPeakPrice;
    @BindView(R.id.spotpricing_flatPrice)
    MaterialEditText spotpricingFlatPrice;
    @BindView(R.id.spotpricing_valleyPrice)
    MaterialEditText spotpricingValleyPrice;
    @BindView(R.id.action_save)
    LinearLayout actionSave;
    private ISpotPricingPresenter spotPricingPresenter;
    private Price bean;

    @Override
    public void initContentView() {
        baseSetContentView(R.layout.activity_create_spot_pricing);
    }

    @Override
    public void initPresenter() {
        spotPricingPresenter = new SpotPricingPresenterImpl();
    }

    @Override
    public void initView() {
        Setting setting = new Setting(this);
        String currentUserType = LoginInformation.getInstance().getUser().getType();
        String currentStoreId = LoginInformation.getInstance().getUser().getStoreId();
        String currentStoreName = LoginInformation.getInstance().getUser().getStoreName();
        ArrayAdapter localArrayAdapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, new String[]{"普通", "分时"});
        localArrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(localArrayAdapter1);
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong) {
                if (paramAnonymousInt == 1) {
                    spotpricingPointedPrice.setEnabled(true);
                    spotpricingPeakPrice.setEnabled(true);
                    spotpricingFlatPrice.setEnabled(true);
                    spotpricingValleyPrice.setEnabled(true);
                    spotpricingPriceCount.setEnabled(false);
                    spotpricingPriceCount.setText("0");
                }
                while (paramAnonymousInt != 0) {
                    return;
                }
                spotpricingPointedPrice.setEnabled(false);
                spotpricingPeakPrice.setEnabled(false);
                spotpricingFlatPrice.setEnabled(false);
                spotpricingValleyPrice.setEnabled(false);
                spotpricingPriceCount.setEnabled(true);
                spotpricingPointedPrice.setText("0.00");
                spotpricingPeakPrice.setText("0.00");
                spotpricingFlatPrice.setText("0.00");
                spotpricingValleyPrice.setText("0.00");
            }

            public void onNothingSelected(AdapterView<?> paramAnonymousAdapterView) {
            }
        });
        bean = ((Price) getIntent().getSerializableExtra("bean"));
        if (bean != null && bean.getId() != null) {
            spotPricingName.setText(checkText(bean.getPricename()));
            spotpricingPriceCount.setText(checkText(bean.getTotalprice()));
            if ("普通".equals(bean.getPricetype())) {
                spinnerType.setSelection(1);
            }
            if ("分时".equals(bean.getPricetype())) {
                spinnerType.setSelection(2);
            }
            spotpricingPointedPrice.setText(checkText(bean.getPricea()));
            spotpricingPeakPrice.setText(checkText(bean.getPriceb()));
            spotpricingFlatPrice.setText(checkText(bean.getPricec()));
            spotpricingValleyPrice.setText(checkText(bean.getPriced()));
            spotpricingPriceCount.setText(checkText(bean.getTotalprice()));
            contyTv.setText(checkText(bean.getCountyname()));
            contyTv.setContentDescription(checkText(bean.getCountyid()));
            getSupportActionBar().setTitle(R.string.upgrade_spotpricing);
        } else {
            if (!currentUserType.equals(G.adminRole)) {
                contyTv.setText(checkText(currentStoreName));
                contyTv.setContentDescription(currentStoreId);
            }
            getSupportActionBar().setTitle(R.string.create_spotpricing);
        }
        if (currentUserType.equals(G.adminRole)) {
            contyTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(CreateSpotPricingAcvitity.this, StoresMgrActivity.class);
                    intent.putExtra("isSelect", true);
                    startActivity(intent);
                }
            });
        }
    }


    @OnClick({R.id.action_save})
    public void onSaveClick() {
        if (bean == null) {
            bean = new Price();
        }
        bean.setPricename(spotPricingName.getText().toString());
        bean.setPricea(spotpricingPointedPrice.getText().toString());
        bean.setPriceb(spotpricingPeakPrice.getText().toString());
        bean.setPricec(spotpricingFlatPrice.getText().toString());
        bean.setPriced(spotpricingValleyPrice.getText().toString());
        bean.setTotalprice(spotpricingPriceCount.getText().toString());
        bean.setPricetype(spinnerType.getSelectedItem().toString());
        bean.setCountyid(contyTv.getContentDescription() == null ? "" : contyTv.getContentDescription().toString());
        bean.setCountyname(contyTv.getText().toString());
        spotPricingPresenter.saveSpotPricing(this, bean);
    }

    public void saveSpotPricing(boolean paramBoolean) {
        hideProgress();
        if (paramBoolean) {
            showToast("保存成功");
            finish();
        }


    }

    @Override
    protected void onEventComming(EventCenter eventCenter) {
        if (EventCenter.COUNTY_SELECT == eventCenter.getEventCode()) {
            try {
                SysDept county = (SysDept) eventCenter.getData();
                contyTv.setText(checkText(county.getSimplename()));
                contyTv.setContentDescription(checkText(county.getCountyid()));
            } catch (Exception e) {

            }
        }
    }

    @Override
    public boolean isBindEventBusHere() {
        return true;
    }
}
