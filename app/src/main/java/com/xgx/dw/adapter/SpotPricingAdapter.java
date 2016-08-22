package com.xgx.dw.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xgx.dw.R;
import com.xgx.dw.SpotPricingBean;

import java.util.List;

public class SpotPricingAdapter
        extends BaseQuickAdapter<SpotPricingBean> {
    public SpotPricingAdapter(List<SpotPricingBean> paramList) {
        super(R.layout.base_list_item, paramList);
    }

    protected void convert(BaseViewHolder paramBaseViewHolder, SpotPricingBean paramSpotPricingBean) {
        paramBaseViewHolder.setText(R.id.title, "价格名称：" + paramSpotPricingBean.getName())
                .setText(R.id.subtitle, "所属营业厅：" + paramSpotPricingBean.getStorename() + "\n价格类型：" +
                        paramSpotPricingBean.getType()).setText(R.id.content, "总电价(元)：" +
                paramSpotPricingBean.getPrice_count() + "\n" + "尖电价(元)：" + paramSpotPricingBean.getPointed_price() +
                "    峰电价(元)：" + paramSpotPricingBean.getPeek_price() + "\n" + "平电价(元)：" +
                paramSpotPricingBean.getFlat_price() + "    谷电价(元)：" + paramSpotPricingBean.getValley_price());
    }
}
