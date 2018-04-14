package com.xgx.dw.ui.activity;

import android.text.TextUtils;
import android.widget.LinearLayout;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.xgx.dw.R;
import com.xgx.dw.base.BaseAppCompatActivity;
import com.xgx.dw.bean.SysDept;
import com.xgx.dw.presenter.impl.StorePresenterImpl;
import com.xgx.dw.presenter.interfaces.IStoresPresenter;
import com.xgx.dw.ui.view.interfaces.ICreateStoresView;

import butterknife.BindView;
import butterknife.OnClick;

public class CreateStoreActivity extends BaseAppCompatActivity implements ICreateStoresView {

    @BindView(R.id.store_id)
    MaterialEditText storeId;
    @BindView(R.id.normal_with_input_text)
    LinearLayout normalWithInputText;
    @BindView(R.id.store_name)
    MaterialEditText storeName;
    @BindView(R.id.store_address)
    MaterialEditText storeAddress;
    @BindView(R.id.store_linkname)
    MaterialEditText storeLinkname;
    @BindView(R.id.store_ContactWay)
    MaterialEditText storeContactWay;
    @BindView(R.id.action_save)
    LinearLayout actionSave;
    private IStoresPresenter presenter;
    private SysDept bean;

    @Override
    public void initContentView() {
        baseSetContentView(R.layout.activity_create_store);
    }

    @Override
    public void initPresenter() {
        presenter = new StorePresenterImpl();
    }

    @Override
    public void initView() {
        bean = ((SysDept) getIntent().getSerializableExtra("bean"));
        if (this.bean != null && !TextUtils.isEmpty(this.bean.getCountyid())) {
            getSupportActionBar().setTitle(R.string.upgrade_store);
            this.storeId.setText(checkText(this.bean.getCountyid()));
            this.storeId.setEnabled(false);
            this.storeName.setText(checkText(this.bean.getSimplename()));
            this.storeAddress.setText(checkText(this.bean.getAddress()));
            this.storeLinkname.setText(checkText(this.bean.getContact()));
            this.storeContactWay.setText(checkText(this.bean.getTel()));
            return;
        }
        getSupportActionBar().setTitle(R.string.create_store);
    }

    @OnClick({R.id.action_save})
    public void onSaveClick() {
        SysDept county = new SysDept();
        county.setCountyid(storeId.getText().toString());
        county.setSimplename(storeName.getText().toString());
        county.setAddress(storeAddress.getText().toString());
        county.setContact(storeLinkname.getText().toString());
        county.setTel(storeContactWay.getText().toString());
        if (bean != null && bean.getId() != null) {
            county.setId(bean.getId());
        }
        presenter.saveStore(this, county);
    }

    @Override
    public void saveStores(boolean paramBoolean) {
        hideProgress();
        if (paramBoolean) {
            showToast("保存成功");
            finish();
        }
    }

}
