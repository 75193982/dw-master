package com.xgx.dw.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xgx.dw.R;
import com.xgx.dw.StoreBean;

import java.util.List;

public class DeviceListAdapter extends BaseQuickAdapter<String> {
    public DeviceListAdapter(List<String> paramList) {
        super(R.layout.base_list_item, paramList);
    }

    protected void convert(BaseViewHolder paramBaseViewHolder, String paramStoreBean) {
        paramBaseViewHolder.setText(R.id.title, "设备：" + paramStoreBean);
    }
}
