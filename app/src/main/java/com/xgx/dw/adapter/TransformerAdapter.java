package com.xgx.dw.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xgx.dw.R;
import com.xgx.dw.TransformerBean;

import java.util.List;

public class TransformerAdapter
        extends BaseQuickAdapter<TransformerBean> {
    public TransformerAdapter(List<TransformerBean> paramList) {
        super(R.layout.base_list_item, paramList);
    }

    protected void convert(BaseViewHolder paramBaseViewHolder, TransformerBean paramTransformerBean) {
        paramBaseViewHolder.setText(R.id.title, "编号：" + paramTransformerBean.getId() + "   名称：" + paramTransformerBean.getName())
                .setText(R.id.content, "所属营业厅：" + paramTransformerBean.getStore_name());
    }
}
