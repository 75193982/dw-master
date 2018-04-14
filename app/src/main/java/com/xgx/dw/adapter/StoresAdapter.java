package com.xgx.dw.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xgx.dw.R;
import com.xgx.dw.bean.SysDept;

import java.util.List;

public class StoresAdapter extends BaseQuickAdapter<SysDept, BaseViewHolder> {
    public StoresAdapter(List<SysDept> paramList) {
        super(R.layout.base_list_item, paramList);
    }

    protected void convert(BaseViewHolder paramBaseViewHolder, SysDept paramStoreBean) {
        paramBaseViewHolder.setText(R.id.title, "编号：" + paramStoreBean.getCountyid() + "   名称：" + paramStoreBean.getSimplename()).setText(R.id.subtitle, "地址：" + paramStoreBean.getAddress()).setText(R.id.content, "联系人：" + paramStoreBean.getContact() + "   联系电话：" + paramStoreBean.getTel());
    }
}
