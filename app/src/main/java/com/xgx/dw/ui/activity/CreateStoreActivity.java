package com.xgx.dw.ui.activity;

import android.text.TextUtils;
import android.widget.LinearLayout;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.xgx.dw.R;
import com.xgx.dw.StoreBean;
import com.xgx.dw.base.BaseAppCompatActivity;
import com.xgx.dw.presenter.impl.StorePresenterImpl;
import com.xgx.dw.ui.view.interfaces.ICreateStoresView;
import com.xgx.dw.vo.request.StoresRequest;

import butterknife.Bind;
import butterknife.OnClick;

public class CreateStoreActivity extends BaseAppCompatActivity implements ICreateStoresView {

    @Bind(R.id.store_id)
    MaterialEditText storeId;
    @Bind(R.id.normal_with_input_text)
    LinearLayout normalWithInputText;
    @Bind(R.id.store_name)
    MaterialEditText storeName;
    @Bind(R.id.store_address)
    MaterialEditText storeAddress;
    @Bind(R.id.store_linkname)
    MaterialEditText storeLinkname;
    @Bind(R.id.store_ContactWay)
    MaterialEditText storeContactWay;
    @Bind(R.id.action_save)
    LinearLayout actionSave;
    private StorePresenterImpl presenter;
    private StoreBean bean;

    public void initContentView() {
        baseSetContentView(R.layout.activity_create_store);
    }

    public void initPresenter() {
        presenter = new StorePresenterImpl();
    }

    public void initView() {
        bean = ((StoreBean) getIntent().getSerializableExtra("bean"));
        if (this.bean != null && !TextUtils.isEmpty(this.bean.getId())) {
            getSupportActionBar().setTitle(R.string.upgrade_store);
            this.storeId.setText(this.bean.getId());
            this.storeId.setEnabled(false);
            this.storeName.setText(checkText(this.bean.getName()));
            this.storeAddress.setText(checkText(this.bean.getAddress()));
            this.storeLinkname.setText(checkText(this.bean.getLinkman()));
            this.storeContactWay.setText(checkText(this.bean.getContact_way()));
            return;
        }
        getSupportActionBar().setTitle(R.string.create_store);
    }

    @OnClick({R.id.action_save})
    public void onSaveClick() {
        showProgress("保存营业厅中...");
        StoresRequest localStoresRequest = new StoresRequest();
        localStoresRequest.storeId = this.storeId.getText().toString();
        localStoresRequest.storeName = this.storeName.getText().toString();
        localStoresRequest.storeAddress = this.storeAddress.getText().toString();
        localStoresRequest.storeLinkMan = this.storeLinkname.getText().toString();
        localStoresRequest.storeContactWay = this.storeContactWay.getText().toString();
        if ((this.bean == null) || (TextUtils.isEmpty(this.bean.getId()))) {
            this.presenter.saveStore(this, localStoresRequest, true);
            return;
        }
        this.presenter.saveStore(this, localStoresRequest, false);
    }

    public void saveStores(boolean paramBoolean) {
        hideProgress();
        if (paramBoolean) {
            showToast("保存成功");
            finish();
        }
    }

}
