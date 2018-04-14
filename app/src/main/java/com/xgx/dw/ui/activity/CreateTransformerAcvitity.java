package com.xgx.dw.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.xgx.dw.R;
import com.xgx.dw.StoreBean;
import com.xgx.dw.app.G;
import com.xgx.dw.app.Setting;
import com.xgx.dw.base.BaseEventBusActivity;
import com.xgx.dw.base.EventCenter;
import com.xgx.dw.bean.LoginInformation;
import com.xgx.dw.bean.SysDept;
import com.xgx.dw.bean.Taiqu;
import com.xgx.dw.presenter.impl.TransformerPresenterImpl;
import com.xgx.dw.presenter.interfaces.ITransformerPresenter;
import com.xgx.dw.ui.view.interfaces.ICreateTransformerView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

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
        bean = ((Taiqu) getIntent().getSerializableExtra("bean"));
        if ((this.bean != null) && (!TextUtils.isEmpty(this.bean.getCode()))) {
            getSupportActionBar().setTitle(R.string.upgrade_transformer);
            this.transformerId.setText(this.bean.getCode());
            this.transformerId.setEnabled(false);
            this.transformerName.setText(checkText(this.bean.getName()));
            contyTv.setText(checkText(bean.getCountyname()));
            contyTv.setContentDescription(bean.getCountyid());

        } else {
            if (currentUserType.equals(G.depRole)) {
                contyTv.setText(LoginInformation.getInstance().getUser().getStoreName());
                contyTv.setContentDescription(LoginInformation.getInstance().getUser().getStoreId());
            }
            getSupportActionBar().setTitle(R.string.create_transformer);
        }
        if (!currentUserType.equals(G.depRole)) {
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
                SysDept county = (SysDept) eventCenter.getData();
                contyTv.setText(checkText(county.getSimplename()));
                contyTv.setContentDescription(checkText(county.getId() + ""));
            } catch (Exception e) {

            }
        }
    }

    @Override
    public boolean isBindEventBusHere() {
        return true;
    }
}
