package com.xgx.dw.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xgx.dw.R;
import com.xgx.dw.StoreBean;
import com.xgx.dw.UserBean;
import com.xgx.dw.app.G;

import java.util.List;

public class UserAdapter extends BaseQuickAdapter<UserBean, BaseViewHolder> {
    public UserAdapter(List<UserBean> paramList) {
        super(R.layout.base_list_item, paramList);
    }

    protected void convert(BaseViewHolder paramBaseViewHolder, UserBean userBean) {
        paramBaseViewHolder.setText(R.id.title, "用户编号：" + userBean.getUserId() + "   名称：" + userBean.getUserName());
        if (userBean.getType().equals(G.adminRole)) {
            paramBaseViewHolder.setText(R.id.subtitle, "用户类型：超级管理员");
            paramBaseViewHolder.setText(R.id.content, " ");
        } else if (userBean.getType().equals(G.depRole)) {
            paramBaseViewHolder.setText(R.id.subtitle, "用户类型：营业厅管理员");
            paramBaseViewHolder.setText(R.id.content, "所属营业厅：" + userBean.getStoreName());
        } else if (userBean.getType().equals(G.taiquRole)) {
            paramBaseViewHolder.setText(R.id.subtitle, "用户类型：台区管理员");
            paramBaseViewHolder.setText(R.id.content, "所属营业厅：" + userBean.getStoreName() + "\n" + "所属台区：" + userBean.getTransformerName());
        } else if (userBean.getType().equals(G.userRole)) {
            paramBaseViewHolder.setText(R.id.subtitle, "用户类型：2级用户");
            paramBaseViewHolder.setText(R.id.content, "所属营业厅：" + userBean.getStoreName() + "\n" + "所属台区：" + userBean.getTransformerName() + "\n" + "电压倍率：" + userBean.getVoltageRatio() + "\n" + "电流倍率：" + userBean.getCurrentRatio() + "\n" + "电价：" + userBean.getPriceName() + "\n" + "用户号码：" + userBean.getPhone() + "\n" + "用户表号：" + userBean.getId());
        } else {

        }

    }
}
