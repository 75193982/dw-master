package com.xgx.dw.bean;

import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class MySection extends SectionEntity<DeviceInfo> {
    private boolean isMore;

    public MySection(boolean isHeader, String header, boolean isMroe) {
        super(isHeader, header);
        this.isMore = isMroe;
    }

    public MySection(DeviceInfo t) {
        super(t);
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean mroe) {
        isMore = mroe;
    }
}
