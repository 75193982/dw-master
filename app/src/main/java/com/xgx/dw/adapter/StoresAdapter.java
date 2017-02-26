package com.xgx.dw.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xgx.dw.R;
import com.xgx.dw.StoreBean;

import java.util.List;

public class StoresAdapter
        extends BaseQuickAdapter<StoreBean, BaseViewHolder> {
    public StoresAdapter(List<StoreBean> paramList) {
        super(R.layout.base_list_item, paramList);
    }

    protected void convert(BaseViewHolder paramBaseViewHolder, StoreBean paramStoreBean) {
        paramBaseViewHolder.setText(R.id.title, "编号：" + paramStoreBean.getId() + "   名称：" + paramStoreBean.getName()).setText(R.id.subtitle, "地址：" + paramStoreBean.getAddress()).setText(R.id.content, "联系人：" + paramStoreBean.getLinkman() + "   联系电话：" + paramStoreBean.getContact_way());
    }
}
