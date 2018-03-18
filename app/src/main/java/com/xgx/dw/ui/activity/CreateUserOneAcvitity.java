package com.xgx.dw.ui.activity;

import android.content.Intent;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.xgx.dw.R;
import com.xgx.dw.StoreBean;
import com.xgx.dw.TransformerBean;
import com.xgx.dw.UserBean;
import com.xgx.dw.base.BaseAppCompatActivity;
import com.xgx.dw.bean.LoginInformation;
import com.xgx.dw.dao.StoreBeanDaoHelper;
import com.xgx.dw.presenter.impl.UserPresenterImpl;
import com.xgx.dw.presenter.interfaces.IUserPresenter;
import com.xgx.dw.ui.view.interfaces.IUserView;

import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;
import fr.ganfra.materialspinner.MaterialSpinner;

public class CreateUserOneAcvitity extends BaseAppCompatActivity implements IUserView, Toolbar.OnMenuItemClickListener {
    @BindView(R.id.imeTv)
    MaterialEditText imeTv;
    @BindView(R.id.spinner)
    MaterialSpinner spinner;
    @BindView(R.id.buy_switch)
    SwitchCompat buySwitch;
    @BindView(R.id.test_switch)
    SwitchCompat testSwitch;
    @BindView(R.id.action_save)
    LinearLayout actionSave;
    @BindView(R.id.store_layout)
    LinearLayout storeLayout;
    @BindView(R.id.user_id)
    MaterialEditText userId;
    @BindView(R.id.user_name)
    MaterialEditText userName;
    private IUserPresenter presenter;
    private List<StoreBean> storebeans;
    private UserBean bean;
    private List<TransformerBean> transformerBean;


    public void initContentView() {
        baseSetContentView(R.layout.activity_create_user_one);
    }

    public void initPresenter() {
        presenter = new UserPresenterImpl();
    }

    public void initView() {
        getToolbar().setOnMenuItemClickListener(this);
        String ime = getIntent().getStringExtra("ime");
        imeTv.setText(checkText(ime));
        initSpinnerData();
        initEditInfo();
    }

    private void initEditInfo() {
        bean = (UserBean) getIntent().getSerializableExtra("bean");
        if ((this.bean != null) && (!TextUtils.isEmpty(this.bean.getUserId()))) {
            getSupportActionBar().setTitle("修改营业厅管理员");
            imeTv.setEnabled(false);
            this.userId.setText(this.bean.getUserId());
            imeTv.setText(checkText(bean.getIme()));
            this.userId.setEnabled(false);
            this.userName.setText(checkText(this.bean.getUserName()));
            try {
                for (int i = 0; i < this.storebeans.size(); i++) {
                    if (this.bean.getStoreId().equals(((StoreBean) this.storebeans.get(i)).getId())) {
                        this.spinner.setSelection(i + 1);
                        this.spinner.setEnabled(false);
                    }
                }
            } catch (Exception e) {

            }

            if (bean.getIsBuy().equals("0")) {
                buySwitch.setChecked(false);
            } else if (bean.getIsBuy().equals("1")) {
                buySwitch.setChecked(true);
            }
            if (bean.getIsTest().equals("0")) {
                testSwitch.setChecked(false);
            } else if (bean.getIsTest().equals("1")) {
                testSwitch.setChecked(true);
            }
            if (!LoginInformation.getInstance().getUser().getType().equals("admin")) {
                buySwitch.setEnabled(false);
                testSwitch.setEnabled(false);
            }
        } else {
            getSupportActionBar().setTitle(R.string.create_userone);
        }
    }

    private void initSpinnerData() {
        bean = ((UserBean) getIntent().getSerializableExtra("bean"));
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
    }

    @OnClick({R.id.action_save})
    public void onSaveClick() {
        UserBean userBean = new UserBean();
        userBean.setUserId(userId.getText().toString());
        userBean.setUserName(userName.getText().toString());
        userBean.setIme(imeTv.getText().toString());
        try {
            int i = this.spinner.getSelectedItemPosition();
            StoreBean localStoreBean = (StoreBean) this.storebeans.get(i - 1);
            userBean.setStoreId(localStoreBean.getId());
            userBean.setStoreName(localStoreBean.getName());
        } catch (Exception e) {

        }
        userBean.setIsBuy(buySwitch.isChecked() ? "1" : "0");
        userBean.setIsTest(testSwitch.isChecked() ? "1" : "0");
        userBean.setPassword(userId.getText().toString());

        if ((bean == null) || (TextUtils.isEmpty(bean.getUserId()))) {
            userBean.setId(UUID.randomUUID().toString());
            this.presenter.saveUser(this, userBean, 10, true);

            return;
        } else {
            userBean.setId(bean.getId());
        }
        this.presenter.saveUser(this, userBean, 10, false);
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
        }
        return true;
    }

    @Override
    public void saveTransformer(boolean b, String id) {
        hideProgress();
        if (b) {
            showToast("保存成功");
            startActivity(new Intent(this, TestGeneratectivity.class).putExtra("type", 3).putExtra("id", id));
            finish();
        }
    }
}
