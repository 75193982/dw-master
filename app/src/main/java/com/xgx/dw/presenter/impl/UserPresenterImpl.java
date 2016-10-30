package com.xgx.dw.presenter.impl;

import android.text.TextUtils;
import android.view.View;

import com.xgx.dw.TransformerBean;
import com.xgx.dw.UserBean;
import com.xgx.dw.app.G;
import com.xgx.dw.app.Setting;
import com.xgx.dw.base.BasePresenter;
import com.xgx.dw.dao.TransformerBeanDaoHelper;
import com.xgx.dw.dao.UserBeanDaoHelper;
import com.xgx.dw.presenter.interfaces.ITransformerPresenter;
import com.xgx.dw.presenter.interfaces.IUserPresenter;
import com.xgx.dw.ui.view.interfaces.ICreateTransformerView;
import com.xgx.dw.ui.view.interfaces.ITransformerView;
import com.xgx.dw.ui.view.interfaces.IUserListView;
import com.xgx.dw.ui.view.interfaces.IUserView;
import com.xgx.dw.utils.Logger;
import com.xgx.dw.utils.MyUtils;

public class UserPresenterImpl extends BasePresenter implements IUserPresenter {
    public void saveTransformer(ICreateTransformerView paramICreateTransformerView, TransformerBean paramTransformerBean, boolean paramBoolean) {
        if (isEmpty(paramTransformerBean.getStore_name(), paramICreateTransformerView, "请选择营业厅"))
            return;
        if (isEmpty(paramTransformerBean.getId(), paramICreateTransformerView, "台区编号不能为空")) return;
        if (isEmpty(paramTransformerBean.getName(), paramICreateTransformerView, "台区名称不能为空"))
            return;
        if (TransformerBeanDaoHelper.getInstance().hasKey(paramTransformerBean.getId())) {
            paramICreateTransformerView.showToast("已有相同编号的台区");
            paramICreateTransformerView.saveTransformer(false);
            return;
        }
        TransformerBeanDaoHelper.getInstance().addData(paramTransformerBean);
        paramICreateTransformerView.saveTransformer(true);
    }

    public void searchTransformer(ITransformerView paramITransformerView, TransformerBean paramTransformerBean) {
        paramITransformerView.searchTransformer(TransformerBeanDaoHelper.getInstance().getAllData());
    }

    @Override
    public void saveUser(IUserView IBaseView, UserBean userBean, int type, boolean isSave) {
        try {
            if (isEmpty(userBean.getUserId(), IBaseView, "用户编号不能为空")) return;
            if (isEmpty(userBean.getUserName(), IBaseView, "用户名称不能为空")) return;
            if (isEmpty(userBean.getStoreName(), IBaseView, "请选择营业厅")) return;
            if (type == 11) {
                if (isEmpty(userBean.getTransformerId(), IBaseView, "台区名称不能为空")) return;
            } else if (type == 20) {
                if (isEmpty(userBean.getPrice(), IBaseView, "请选择电价")) return;
            }
            userBean.setType(type + "");
            //查询有没有这个设备编号的
            IBaseView.showProgress("保存用户中...");
            UserBean tempUser = UserBeanDaoHelper.getInstance().queryByTransFormUserId(userBean.getUserId());
            if (isSave == true) {//表示保存
                if (tempUser == null) {//新建用户
                    UserBeanDaoHelper.getInstance().addData(userBean);
                    IBaseView.saveTransformer(true);
                } else {
                    if (tempUser.getIme().contains(userBean.getIme())) {//说明已经存在
                        IBaseView.showToast("已经存在该用户");
                        IBaseView.saveTransformer(false);
                    } else {
                        userBean.setId(tempUser.getId());
                        StringBuilder sb = new StringBuilder();
                        sb.append(tempUser.getIme());
                        if (!TextUtils.isEmpty(userBean.getIme())) {
                            sb.append("," + userBean.getIme());
                        }
                        userBean.setIme(sb.toString());
                        UserBeanDaoHelper.getInstance().addData(userBean);
                        IBaseView.saveTransformer(true);
                    }
                }
            } else {//编辑状态 直接保存即可
                UserBeanDaoHelper.getInstance().addData(userBean);
                IBaseView.saveTransformer(true);
            }
            IBaseView.hideProgress();
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }


    }

    @Override
    public void searchUser(IUserListView IBaseView) {
        if (G.currentUserType.equals("10")) {//营业厅管理员 查看当前营业厅下的人
            IBaseView.getUserList(UserBeanDaoHelper.getInstance().queryByStoreId(G.currentStoreId));
        } else if (G.currentUserType.equals("11")) {
            IBaseView.getUserList(UserBeanDaoHelper.getInstance().queryByTransFormId(G.currentTransformId));
        } else {
            IBaseView.getUserList(UserBeanDaoHelper.getInstance().getAllData());
        }
    }

    @Override
    public void saveOrUpdateUser(UserBean bean) {
        try {
            UserBean tempUser = UserBeanDaoHelper.getInstance().queryByTransFormUserId(bean.getUserId());
            if (tempUser == null) {//新建用户
                UserBeanDaoHelper.getInstance().addData(bean);
            } else {
                if (!tempUser.getIme().contains(bean.getIme())) {//说明已经存在
                    bean.setId(tempUser.getId());
                    StringBuilder sb = new StringBuilder();
                    sb.append(tempUser.getIme());
                    if (!TextUtils.isEmpty(bean.getIme())) {
                        sb.append("," + bean.getIme());
                    }
                    bean.setIme(sb.toString());
                    UserBeanDaoHelper.getInstance().addData(bean);
                }
            }
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }

    }
}
