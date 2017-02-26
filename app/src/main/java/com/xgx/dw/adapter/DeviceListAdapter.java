package com.xgx.dw.adapter;

/**
 * Created by Administrator on 2016/8/29 0029.
 */

import android.annotation.TargetApi;
import android.os.Build;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xgx.dw.R;
import com.xgx.dw.bean.DeviceInfo;
import com.xgx.dw.bean.MySection;

import java.util.List;


public class DeviceListAdapter extends BaseSectionQuickAdapter<MySection, BaseViewHolder> {
    public DeviceListAdapter(int layoutResId, int sectionHeadResId, List data) {
        super(layoutResId, sectionHeadResId, data);
    }

    @Override
    protected void convertHead(BaseViewHolder helper, final MySection item) {
        helper.setText(R.id.title, item.header);
    }


    @Override
    protected void convert(final BaseViewHolder helper, MySection item) {
        DeviceInfo menu = (DeviceInfo) item.t;
        helper.setText(R.id.contentTv, menu.getName());
    }
}
