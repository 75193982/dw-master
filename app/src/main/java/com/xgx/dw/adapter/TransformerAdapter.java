package com.xgx.dw.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xgx.dw.R;
import com.xgx.dw.TransformerBean;
import com.xgx.dw.bean.Taiqu;

import java.util.List;

public class TransformerAdapter extends BaseQuickAdapter<Taiqu, BaseViewHolder> {
    public TransformerAdapter(List<Taiqu> paramList) {
        super(R.layout.base_list_item, paramList);
    }

    protected void convert(BaseViewHolder paramBaseViewHolder, Taiqu taiqu) {
        paramBaseViewHolder.setText(R.id.title, "编号：" + taiqu.getCode() + "   名称：" + taiqu.getName()).setText(R.id.content, "所属营业厅：" + taiqu.getCountyname());
    }
}
