package com.xgx.dw.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xgx.dw.R;
import com.xgx.dw.StoreBean;
import com.xgx.dw.bean.County;

import java.util.List;

public class StoresAdapter extends BaseQuickAdapter<County, BaseViewHolder> {
    public StoresAdapter(List<County> paramList) {
        super(R.layout.base_list_item, paramList);
    }

    protected void convert(BaseViewHolder paramBaseViewHolder, County paramStoreBean) {
        paramBaseViewHolder.setText(R.id.title, "编号：" + paramStoreBean.getCountyid() + "   名称：" + paramStoreBean.getCountyname()).setText(R.id.subtitle, "地址：" + paramStoreBean.getAddress()).setText(R.id.content, "联系人：" + paramStoreBean.getContact() + "   联系电话：" + paramStoreBean.getTel());
    }
}
