package com.xgx.dw.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.xgx.dw.PricingBean;
import com.xgx.dw.PricingBeanDao;
import com.xgx.dw.R;
import com.xgx.dw.TransformerBean;
import com.xgx.dw.UserBean;
import com.xgx.dw.base.BaseActivity;
import com.xgx.dw.base.BaseAppCompatActivity;
import com.xgx.dw.bean.LoginInformation;
import com.xgx.dw.bean.UserAllInfo;
import com.xgx.dw.dao.PricingDaoHelper;
import com.xgx.dw.utils.CommonUtils;

import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.ganfra.materialspinner.MaterialSpinner;

/**
 * Created by Administrator on 2016/10/15 0015.
 */
public class BuySpotActivity extends BaseAppCompatActivity {
    @Bind(R.id.userInfoTv)
    TextView userInfoTv;
    @Bind(R.id.userInfoBeilvTv)
    TextView userInfoBeilvTv;
    @Bind(R.id.spotTv)
    MaterialEditText spotTv;
    @Bind(R.id.spinner)
    MaterialSpinner spinner;
    private UserAllInfo userAllInfo;

    @Override
    public void initContentView() {
        baseSetContentView(R.layout.activity_buy_spot);
    }

    @Override
    public void initView() {
        setToolbarTitle("用户购电");
        userAllInfo = (UserAllInfo) getIntent().getSerializableExtra("userAllInfo");
        if (userAllInfo != null) {
            UserBean user = userAllInfo.getUser();
            if (TextUtils.isEmpty(user.getVoltageRatio()) && TextUtils.isEmpty(user.getCurrentRatio())
                    && TextUtils.isEmpty(user.getPrice())) {
                showToast("该用户的电压倍率/电流倍率/电价信息不完整，请联系管理员");
                finish();
                return;
            }
            userInfoTv.setText(user.getUserName());
            userInfoBeilvTv.setText("设备编号：" + user.getUserId() + "\n" + "电压倍率：" + user.getVoltageRatio() +
                    "\n" + "电流倍率：" + user.getCurrentRatio() +
                    "\n" + "电价：" + user.getPrice());
        }

        String[] arrayOfString = new String[]{"追加", "刷新"};
        ArrayAdapter localArrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, arrayOfString);
        localArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(localArrayAdapter);
    }

    @Override
    public void initPresenter() {

    }


    @OnClick(R.id.action_save)
    public void onClick() {
        if (TextUtils.isEmpty(spotTv.getText().toString())) {
            showToast("请输入购电金额");
            return;
        }
        //正在保存电价生成 二维码，并保存到表里
        PricingBean bean = new PricingBean();
        bean.setId(UUID.randomUUID().toString());
        bean.setPrice(spotTv.getText().toString());
        bean.setUserId(userAllInfo.getUser().getUserId());
        bean.setUserName(userAllInfo.getUser().getUserName());
        bean.setAdminName(LoginInformation.getInstance().getUser().getUserId());
        bean.setType((String) spinner.getSelectedItem());
        bean.setStoreId(LoginInformation.getInstance().getUser().getStoreId());
        bean.setStoreName(LoginInformation.getInstance().getUser().getStoreName());
        bean.setTransformerId(LoginInformation.getInstance().getUser().getTransformerId());
        bean.setTransformerName(LoginInformation.getInstance().getUser().getTransformerName());
        bean.setPid(userAllInfo.getUser().getPrice());
        bean.setCreateTime(CommonUtils.parseDateTime(System.currentTimeMillis()));
        if (PricingDaoHelper.getInstance().getAllData().size() == 0) {
            bean.setType("0");
        } else {
            bean.setType("1");
        }
        PricingDaoHelper.getInstance().addData(bean);
        //跳转到二维码界面
        startActivity(new Intent(this, TestGeneratectivity.class).putExtra("type", 5).putExtra("id", bean.getUserId()));

    }
}
