package com.xgx.dw.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;

import com.andexert.library.RippleView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.xgx.dw.R;
import com.xgx.dw.StoreBean;
import com.xgx.dw.TransformerBean;
import com.xgx.dw.base.BaseAppCompatActivity;
import com.xgx.dw.dao.StoreBeanDaoHelper;
import com.xgx.dw.presenter.impl.TransformerPresenterImpl;
import com.xgx.dw.ui.view.interfaces.ICreateTransformerView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.ganfra.materialspinner.MaterialSpinner;

public class CreateTransformerAcvitity extends BaseAppCompatActivity implements ICreateTransformerView {

    @Bind(R.id.spinner)
    MaterialSpinner spinner;
    @Bind(R.id.transformer_id)
    MaterialEditText transformerId;
    @Bind(R.id.transformer_name)
    MaterialEditText transformerName;
    @Bind(R.id.action_save)
    RippleView actionSave;
    private TransformerPresenterImpl presenter;
    private List<StoreBean> storebeans;
    private TransformerBean bean;


    public void initContentView() {
        baseSetContentView(R.layout.activity_create_transformer);
    }

    public void initPresenter() {
        presenter = new TransformerPresenterImpl();
    }

    public void initView() {
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
        bean = ((TransformerBean) getIntent().getSerializableExtra("bean"));
        if ((this.bean != null) && (!TextUtils.isEmpty(this.bean.getId()))) {
            getSupportActionBar().setTitle(R.string.upgrade_transformer);
            this.transformerId.setText(this.bean.getId());
            this.transformerId.setEnabled(false);
            this.transformerName.setText(checkText(this.bean.getName()));
            for (int i = 0; i < this.storebeans.size(); i++) {
                if (this.bean.getStore_id().equals(((StoreBean) this.storebeans.get(i)).getId())) {
                    this.spinner.setSelection(i + 1);
                    this.spinner.setEnabled(false);
                }
            }
        } else {
            getSupportActionBar().setTitle(R.string.create_transformer);

        }
    }

    @OnClick({R.id.action_save})
    public void onSaveClick() {
        showProgress("保存台区中...");
        TransformerBean localTransformerBean = new TransformerBean();
        localTransformerBean.setId(this.transformerId.getText().toString());
        localTransformerBean.setName(this.transformerName.getText().toString());
        int i = this.spinner.getSelectedItemPosition();
        StoreBean localStoreBean = (StoreBean) this.storebeans.get(i - 1);
        localTransformerBean.setStore_id(localStoreBean.getId());
        localTransformerBean.setStore_name(localStoreBean.getName());
        if ((localTransformerBean == null) || (TextUtils.isEmpty(localTransformerBean.getId()))) {
            this.presenter.saveTransformer(this, localTransformerBean, true);
            return;
        }
        this.presenter.saveTransformer(this, localTransformerBean, false);
    }

    public void saveTransformer(boolean paramBoolean) {
        hideProgress();
        if (paramBoolean) {
            showToast("保存成功");
            finish();
        }
    }

}
