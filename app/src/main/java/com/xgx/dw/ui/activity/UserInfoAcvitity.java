package com.xgx.dw.ui.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.xgx.dw.R;
import com.xgx.dw.StoreBean;
import com.xgx.dw.TransformerBean;
import com.xgx.dw.UserBean;
import com.xgx.dw.app.G;
import com.xgx.dw.app.Setting;
import com.xgx.dw.base.BaseAppCompatActivity;
import com.xgx.dw.dao.StoreBeanDaoHelper;
import com.xgx.dw.dao.TransformerBeanDaoHelper;
import com.xgx.dw.presenter.impl.UserPresenterImpl;
import com.xgx.dw.presenter.interfaces.IUserPresenter;
import com.xgx.dw.ui.view.interfaces.IUserView;

import java.util.List;

import butterknife.Bind;
import fr.ganfra.materialspinner.MaterialSpinner;

public class UserInfoAcvitity extends BaseAppCompatActivity implements IUserView, Toolbar.OnMenuItemClickListener {
    @Bind(R.id.imeTv)
    TextView imeTv;
    @Bind(R.id.store_spinner)
    TextView storeSpinner;
    @Bind(R.id.store_layout)
    LinearLayout storeLayout;
    @Bind(R.id.transformer_spinner)
    TextView transformerSpinner;
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


    public void initContentView() {
        baseSetContentView(R.layout.activity_user_info);
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
        initEditInfo();
    }

    private void initEditInfo() {
        isFirst = true;
        bean = ((UserBean) getIntent().getSerializableExtra("bean"));
        if (bean != null && !TextUtils.isEmpty(this.bean.getUserId())) {
            getSupportActionBar().setTitle("个人资料");
            this.userId.setText(this.bean.getUserId());
            this.userId.setEnabled(false);
            userName.setText(checkText(this.bean.getUserName()));
            price.setText(checkText(bean.getPrice()));
            phone.setText(checkText(bean.getPhone()));
            currentRatio.setText(checkText(bean.getCurrentRatio()));
            voltageRatio.setText(checkText(bean.getVoltageRatio()));
            imeTv.setText(checkText(bean.getIme()));
            storeSpinner.setText(bean.getStoreName());
            transformerSpinner.setText(bean.getTransformerName());
//            try {
//                transformerBean = TransformerBeanDaoHelper.getInstance().testQueryBy(bean.getStoreId());
//                if ((transformerBean != null) && (transformerBean.size() > 0)) {
//                    String[] arrayOfString = new String[transformerBean.size()];
//                    for (int j = 0; j < transformerBean.size(); j++) {
//                        arrayOfString[j] = ((TransformerBean) transformerBean.get(j)).getName();
//                    }
//                    ArrayAdapter localArrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, arrayOfString);
//                    localArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    transformerSpinner.setAdapter(localArrayAdapter);
//                }
//                for (int i = 0; i < transformerBean.size(); i++) {
//                    if (bean.getTransformerId().equals(((TransformerBean) transformerBean.get(i)).getId())) {
//                        transformerSpinner.setSelection(i + 1);
//                        transformerSpinner.setEnabled(false);
//                    }
//                }
//            } catch (Exception e) {
//
//            }
        }
        storeSpinner.setEnabled(false);
        transformerSpinner.setEnabled(false);
        userId.setEnabled(false);
        userName.setEnabled(false);
        voltageRatio.setEnabled(false);
        currentRatio.setEnabled(false);
        price.setEnabled(false);
        phone.setEnabled(false);
        actionSave.setVisibility(View.GONE);
    }

    public void saveTransformer(boolean paramBoolean, String id) {
        hideProgress();
        if (paramBoolean) {
            showToast("保存成功");
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
                intent.putExtra("id", bean.getUserId());
                startActivity(intent);
                break;
        }
        return true;
    }


}
