package com.xgx.dw.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xgx.dw.R;
import com.xgx.dw.SpotPricingBean;
import com.xgx.dw.bean.Price;

import java.util.List;

public class SpotPricingAdapter extends BaseQuickAdapter<Price, BaseViewHolder> {
    public SpotPricingAdapter(List<Price> paramList) {
        super(R.layout.base_list_item, paramList);
    }

    protected void convert(BaseViewHolder paramBaseViewHolder, Price price) {
        paramBaseViewHolder.setText(R.id.title, "价格名称：" + price.getPricename()).setText(R.id.subtitle, "所属营业厅：" + price.getCountyname() + "\n价格类型：" + price.getPricetype()).setText(R.id.content, "总电价(元)：" + price.getTotalprice() + "\n" + "尖电价(元)：" + price.getPricea() + "    峰电价(元)：" + price.getPriceb() + "\n" + "平电价(元)：" + price.getPricec() + "    谷电价(元)：" + price.getPriced());
    }
}
