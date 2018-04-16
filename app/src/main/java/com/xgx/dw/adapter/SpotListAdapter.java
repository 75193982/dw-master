package com.xgx.dw.adapter;

import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xgx.dw.PricingBean;
import com.xgx.dw.R;
import com.xgx.dw.SpotPricingBean;
import com.xgx.dw.UserBean;
import com.xgx.dw.app.G;
import com.xgx.dw.bean.LoginInformation;
import com.xgx.dw.bean.Purchase;
import com.xgx.dw.dao.SpotPricingBeanDaoHelper;
import com.xgx.dw.dao.UserBeanDaoHelper;
import com.xgx.dw.utils.AES;
import com.xgx.dw.utils.MyStringUtils;
import com.xgx.dw.utils.MyUtils;

import java.util.List;

public class SpotListAdapter extends BaseQuickAdapter<Purchase, BaseViewHolder> {
    public SpotListAdapter(List<Purchase> paramList) {
        super(R.layout.base_list_item, paramList);
    }

    protected void convert(BaseViewHolder baseViewHolder, Purchase bean) {
        String priceNum = "";
        try {
            priceNum = bean.getAmt();
        } catch (Exception e) {
            priceNum = "";
        }
        baseViewHolder.setText(R.id.title, "购电单号：" + bean.getOpcode() +
                "\n购电用户名称：" + bean.getUsername() +
                "\n营业厅名称：" + bean.getCountyname() +
                "\n购电金额(元)：" + priceNum +
                "\n购电前金额(元)：" + bean.getAmtbefore() +
                "\n购电金额(元)：" + bean.getAmtafter() +
                "\n购电类型：" + bean.getMetatype() +
                "\n操作员名称：" + bean.getOpuser() +
                "\n联系人名称：" + "" +
                "\n联系人电话：" + "");
        String type = "";
        if (bean.getStatus() != null) {
            if (bean.getStatus() == 0) {
                type = "未上传至设备";


            } else if (bean.getStatus() == 1) {
                type = "未上传至设备--需要更新倍率和电价";
            } else {
                type = "已完成";
            }
            if (bean.getStatus() == 3) {
                type = type + "（保电投入）";
            }
            baseViewHolder.setTextColor(R.id.subtitle, ContextCompat.getColor(mContext, bean.getStatus() == 2 ? android.R.color.holo_blue_light : android.R.color.holo_red_light));
        } else {
            type = "未上传至设备";
        }

        baseViewHolder.setText(R.id.subtitle, type);
        baseViewHolder.setText(R.id.content, "购电时间：" + bean.getCreatetime());
    }
}
