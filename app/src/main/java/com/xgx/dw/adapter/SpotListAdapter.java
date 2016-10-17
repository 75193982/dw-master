package com.xgx.dw.adapter;

import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xgx.dw.PricingBean;
import com.xgx.dw.R;
import com.xgx.dw.SpotPricingBean;
import com.xgx.dw.utils.MyUtils;

import java.util.List;

public class SpotListAdapter extends BaseQuickAdapter<PricingBean> {
    public SpotListAdapter(List<PricingBean> paramList) {
        super(R.layout.base_list_item, paramList);
    }

    protected void convert(BaseViewHolder baseViewHolder, PricingBean bean) {
        baseViewHolder.setText(R.id.title, "购电金额：" + bean.getPrice());
        String type = bean.getType().equals("2") ? "已完成" : "未上传设备";
        baseViewHolder.setText(R.id.subtitle, type);
        baseViewHolder.setTextColor(R.id.subtitle, ContextCompat.getColor(mContext, bean.getType().equals("2") ? android.R.color.holo_blue_light : android.R.color.holo_red_light));
        baseViewHolder.setText(R.id.content, "购电时间：" + bean.getCreateTime());
    }
}
