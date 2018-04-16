package com.xgx.dw.ui.activity;

import android.content.Intent;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.blankj.utilcode.util.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.xgx.dw.R;
import com.xgx.dw.UserBean;
import com.xgx.dw.base.BaseAppCompatActivity;
import com.xgx.dw.bean.LoginInformation;
import com.xgx.dw.bean.Purchase;
import com.xgx.dw.net.DialogCallback;
import com.xgx.dw.net.LzyResponse;
import com.xgx.dw.net.URLs;
import com.xgx.dw.utils.NumberToCn;

import java.math.BigDecimal;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/10/15 0015.
 */
public class BuySpotActivity extends BaseAppCompatActivity {
    @BindView(R.id.userInfoTv)
    TextView userInfoTv;
    @BindView(R.id.userInfoBeilvTv)
    TextView userInfoBeilvTv;
    @BindView(R.id.spotTv)
    MaterialEditText spotTv;
    @BindView(R.id.bjPriceTv)
    MaterialEditText bjPriceTv;
    @BindView(R.id.isChangeSwitch)
    SwitchCompat isChangeSwitch;
    @BindView(R.id.isTrSwitch)
    SwitchCompat isTrSwitch;
    @BindView(R.id.buyTypeTv)
    TextView buyTypeTv;
    @BindView(R.id.action_save)
    LinearLayout actionSave;
    private UserBean user;

    @Override
    public void initContentView() {
        baseSetContentView(R.layout.activity_buy_spot);
    }

    @Override
    public void initView() {
        setToolbarTitle("用户购电");
        user = (UserBean) getIntent().getSerializableExtra("user");
        if (user != null) {
            if (TextUtils.isEmpty(user.getVoltageRatio()) && TextUtils.isEmpty(user.getCurrentRatio()) && TextUtils.isEmpty(user.getPrice())) {
                showToast("该用户的电压倍率/电流倍率/电价信息不完整，请联系管理员");
                finish();
                return;
            }
            userInfoTv.setText(user.getUserName());
            userInfoBeilvTv.setText("设备编号：" + user.getUserId() + "    " + "电压倍率：" + user.getVoltageRatio() +
                    "\n" + "电流倍率：" + user.getCurrentRatio() +
                    "    " + "电价id：" + user.getPrice());
        }
        buyTypeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ArrayList<String> strList = new ArrayList<>();
                strList.add("追加");
                strList.add("刷新");
                //条件选择器
                OptionsPickerView pvOptions = new OptionsPickerBuilder(BuySpotActivity.this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        String tx = strList.get(options1);
                        buyTypeTv.setText(tx);
                    }
                }).build();
                pvOptions.setPicker(strList);
                pvOptions.show();
            }
        });
        spotTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    BigDecimal numberOfMoney = new BigDecimal(s.toString());
                    String newStr = NumberToCn.number2CNMontrayUnit(numberOfMoney);
                    spotTv.setHelperText(newStr);
                    //  cnTv.setText(newStr);
                } catch (Exception e) {

                }

            }
        });
        bjPriceTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    BigDecimal numberOfMoney = new BigDecimal(s.toString());
                    String newStr = NumberToCn.number2CNMontrayUnit(numberOfMoney);
                    bjPriceTv.setHelperText(newStr);
                    //  cnTv.setText(newStr);
                } catch (Exception e) {

                }

            }
        });
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
        Purchase purchase = new Purchase();
        purchase.setAmt(checkText(spotTv.getText().toString()));
        purchase.setAmtbj(bjPriceTv.getText().toString());
        purchase.setCountyid(user.getStoreId());
        purchase.setCountyname(user.getStoreName());
        purchase.setTaiquname(user.getTransformerName());
        purchase.setTerminalcode(user.getUserId());
        purchase.setTerminalname(user.getUserName());
        purchase.setOptype(buyTypeTv.getText().toString());
        purchase.setOpuser(LoginInformation.getInstance().getUser().getUserName());
        purchase.setUsername(user.getUserName());
        purchase.setUserid(user.getUserId());
        purchase.setDybl(user.getVoltageRatio());
        purchase.setDlbl(user.getCurrentRatio());
        purchase.setIsBd(isTrSwitch.isChecked() ? 1 : 0);
        purchase.setIsBl(isChangeSwitch.isChecked() ? 1 : 0);
        //正在保存电价生成 二维码，并保存到表里
        OkGo.<LzyResponse<Purchase>>post(URLs.getURL(URLs.BUY_SPOT))
                .upJson(URLs.getRequstJsonString(purchase))
                .execute(new DialogCallback<LzyResponse<Purchase>>(this) {
                    @Override
                    public void onSuccess(Response<LzyResponse<Purchase>> response) {
                        ToastUtils.showShort("购电成功");
                    }

                    @Override
                    public void onError(Response<LzyResponse<Purchase>> response) {
                        super.onError(response);
                    }
                });
//        try {
//            PricingBean bean = new PricingBean();
//            String price = "";
//            try {
//                price = AES.encrypt(G.appsecret, spotTv.getText().toString());
//            } catch (Exception e) {
//                price = "";
//            }
//            bean.setPrice(price);
//            bean.setBjprice(bjPriceTv.getText().toString());
//            bean.setUserId(user.getUserId());
//            bean.setUserPrimaryid(user.getId());
//            bean.setUserName(user.getUserName());
//            bean.setAdminPhone(LoginInformation.getInstance().getUser().getPhone());
//            bean.setAdminName(LoginInformation.getInstance().getUser().getUserId());
//            bean.setAdminNickName(LoginInformation.getInstance().getUser().getUserName());
//            bean.setType(buyTypeTv.getText().toString());
//            bean.setStoreId(user.getStoreId());
//            bean.setStoreName(user.getStoreName());
//            //根据storeid 获取store详情
//            StoreBean storeBean = StoreBeanDaoHelper.getInstance().getDataById(user.getStoreId());
//            if (storeBean != null) {
//                bean.setStoreAddress(storeBean.getAddress());
//            }
//            bean.setTransformerId(user.getTransformerId());
//            bean.setTransformerName(user.getTransformerName());
//            bean.setPid(user.getPrice());
//            bean.setCreateTime(CommonUtils.parseDateTime(System.currentTimeMillis()));
//            //获取本地的电价列表
//            bean.setId(UUID.randomUUID().toString());
//            List<PricingBean> pricingBeanList = PricingDaoHelper.getInstance().queryByUserDeviceId(user.getUserId());
//            if (pricingBeanList != null && pricingBeanList.size() > 0) {
//                bean.setSpotpriceId((pricingBeanList.size() + 1) + "");
//            } else {
//                bean.setSpotpriceId("1");
//            }
//            if (isChangeSwitch.isChecked()) {
//
//                if (isTrSwitch.isChecked()) {
//                    bean.setFinishtype("1,3");
//                } else {
//                    bean.setFinishtype("1");
//                }
//            } else {
//
//                if (isTrSwitch.isChecked()) {
//                    bean.setFinishtype("0,3");
//                } else {
//                    bean.setFinishtype("0");
//                }
//            }
//            //这里要注意的是 需要带入 二维码传过来的ime
//            bean.setIme(user.getIme());
//            PricingDaoHelper.getInstance().addData(bean);
//
//            int type = 5;
//            if (user.getEcodeType().equals("6")) {
//                type = 6;
//            }
//            startActivity(new Intent(this, TestGeneratectivity.class).putExtra("type", type).putExtra("id", bean.getUserPrimaryid()));
//
//            finish();
//        } catch (Exception e) {
//            Logger.e(e.getMessage());
//        }

        //获得spotpriceId
        //跳转到二维码界面

    }


    @OnClick(R.id.userInfoTv)
    public void onEditClick() {
        //获取用户资料
//        UserBean user = UserBeanDaoHelper.getInstance().getDataById(userAllInfo.getUser().getId());
//        Intent localIntent = new Intent();
//        localIntent.putExtra("bean", user);
//        localIntent.setClass(getContext(), CreateUserThreeAcvitity.class);
//        startActivityForResult(localIntent, 1001);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
//            UserBean user = UserBeanDaoHelper.getInstance().getDataById(userAllInfo.getUser().getId());
//            if (TextUtils.isEmpty(user.getVoltageRatio()) && TextUtils.isEmpty(user.getCurrentRatio()) && TextUtils.isEmpty(user.getPrice())) {
//                showToast("该用户的电压倍率/电流倍率/电价信息不完整，请联系管理员");
//                finish();
//                return;
//            }
            userInfoTv.setText(user.getUserName());
            userInfoBeilvTv.setText("设备编号：" + user.getUserId() + "\n" + "电压倍率：" + user.getVoltageRatio() +
                    "\n" + "电流倍率：" + user.getCurrentRatio() +
                    "\n" + "电价id：" + user.getPrice());

        }
    }

}
