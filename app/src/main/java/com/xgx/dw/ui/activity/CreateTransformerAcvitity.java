package com.xgx.dw.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialAutoCompleteTextView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.xgx.dw.R;
import com.xgx.dw.StoreBean;
import com.xgx.dw.TransformerBean;
import com.xgx.dw.app.G;
import com.xgx.dw.app.Setting;
import com.xgx.dw.base.BaseAppCompatActivity;
import com.xgx.dw.base.BaseEventBusActivity;
import com.xgx.dw.base.EventCenter;
import com.xgx.dw.bean.County;
import com.xgx.dw.bean.Taiqu;
import com.xgx.dw.dao.StoreBeanDaoHelper;
import com.xgx.dw.presenter.impl.TransformerPresenterImpl;
import com.xgx.dw.presenter.interfaces.ITransformerPresenter;
import com.xgx.dw.ui.view.interfaces.ICreateTransformerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.xgx.dw.app.G.currentStoreId;

public class CreateTransformerAcvitity extends BaseEventBusActivity implements ICreateTransformerView {

    @BindView(R.id.transformer_id)
    MaterialEditText transformerId;
    @BindView(R.id.transformer_name)
    MaterialEditText transformerName;
    @BindView(R.id.action_save)
    LinearLayout actionSave;
    @BindView(R.id.contyTv)
    TextView contyTv;
    private ITransformerPresenter presenter;
    private List<StoreBean> storebeans;
    private Taiqu bean;


    @Override
    public void initContentView() {
        baseSetContentView(R.layout.activity_create_transformer);
    }

    @Override
    public void initPresenter() {
        presenter = new TransformerPresenterImpl();
    }

    @Override
    public void initView() {
        Setting setting = new Setting(this);
        String currentUserType = setting.loadString(G.currentUserType);
        String currentStoreId = setting.loadString(G.currentStoreId);
        String currentStoreName = setting.loadString(G.currentStoreName);
//        if (currentUserType.equals("10")) {
//            storebeans = new ArrayList<>();
//            storebeans.add(new StoreBean(currentStoreId, currentStoreName));
//            spinner.setEnabled(false);
//        } else {
//            storebeans = StoreBeanDaoHelper.getInstance().getAllData();
//        }
//        if ((this.storebeans != null) && (this.storebeans.size() > 0)) {
//            String[] arrayOfString = new String[this.storebeans.size()];
//            for (int j = 0; j < this.storebeans.size(); j++) {
//                arrayOfString[j] = ((StoreBean) this.storebeans.get(j)).getName();
//            }
//            ArrayAdapter localArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayOfString);
//            localArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            this.spinner.setAdapter(localArrayAdapter);
//        }
        bean = ((Taiqu) getIntent().getSerializableExtra("bean"));
        if ((this.bean != null) && (!TextUtils.isEmpty(this.bean.getCode()))) {
            getSupportActionBar().setTitle(R.string.upgrade_transformer);
            this.transformerId.setText(this.bean.getCode());
            this.transformerId.setEnabled(false);
            this.transformerName.setText(checkText(this.bean.getName()));
            contyTv.setText(checkText(bean.getCountyname()));
            contyTv.setContentDescription(bean.getCountyid());

        } else {
            if (currentUserType.equals("10")) {
                contyTv.setText(checkText(currentStoreName));
                contyTv.setContentDescription(currentStoreId);
            }
            getSupportActionBar().setTitle(R.string.create_transformer);
        }
        if (!currentUserType.equals("10")) {
            contyTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(CreateTransformerAcvitity.this, StoresMgrActivity.class);
                    intent.putExtra("isSelect", true);
                    startActivity(intent);
                }
            });
        }
    }

    @OnClick({R.id.action_save})
    public void onSaveClick() {
        Taiqu taiqu = new Taiqu();
        taiqu.setCountyid(contyTv.getContentDescription() == null ? "" : contyTv.getContentDescription().toString());
        taiqu.setCountyname(contyTv.getText().toString());
        taiqu.setCode(transformerId.getText().toString());
        taiqu.setName(transformerName.getText().toString());
        if (bean != null && bean.getId() != null) {
            taiqu.setId(bean.getId());
        }
        this.presenter.saveTransformer(this, taiqu);
    }

    @Override
    public void saveTransformer(boolean paramBoolean) {
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
                County county = (County) eventCenter.getData();
                contyTv.setText(checkText(county.getCountyname()));
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
