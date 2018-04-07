package com.xgx.dw.presenter.impl;

import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.blankj.utilcode.util.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.xgx.dw.TransformerBean;
import com.xgx.dw.UserBean;
import com.xgx.dw.app.G;
import com.xgx.dw.app.Setting;
import com.xgx.dw.base.BasePresenter;
import com.xgx.dw.base.EventCenter;
import com.xgx.dw.bean.County;
import com.xgx.dw.bean.LoginInformation;
import com.xgx.dw.bean.Taiqu;
import com.xgx.dw.dao.TransformerBeanDaoHelper;
import com.xgx.dw.dao.UserBeanDaoHelper;
import com.xgx.dw.net.DialogCallback;
import com.xgx.dw.net.LzyResponse;
import com.xgx.dw.net.URLs;
import com.xgx.dw.presenter.interfaces.ITransformerPresenter;
import com.xgx.dw.presenter.interfaces.IUserPresenter;
import com.xgx.dw.ui.view.interfaces.ICreateTransformerView;
import com.xgx.dw.ui.view.interfaces.ITransformerView;
import com.xgx.dw.ui.view.interfaces.IUserListView;
import com.xgx.dw.ui.view.interfaces.IUserView;
import com.xgx.dw.utils.Logger;
import com.xgx.dw.utils.MyUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

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
    public void saveUser(final IUserView IBaseView, final UserBean userBean, int type) {
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
            OkGo.<LzyResponse<UserBean>>post(URLs.getURL(URLs.MTUSER_SAVE))
                    .params("id", checkIsNull(userBean.getId() + ""))
                    .params("userId", checkIsNull(userBean.getUserId()))
                    .params("userName", checkIsNull(userBean.getUserName()))
                    .params("password", checkIsNull(userBean.getPassword()))
                    .params("type",type+"")
                    .params("storeId", checkIsNull(userBean.getStoreId()))
                    .params("storeName", checkIsNull(userBean.getStoreName()))
                    .params("isBuy", checkIsNull(userBean.getIsBuy()))
                    .params("isTest", checkIsNull(userBean.getIsTest()))
                    .params("transformerId", checkIsNull(userBean.getTransformerId()))
                    .params("transformerName", checkIsNull(userBean.getTransformerName()))
                    .params("voltageRatio", checkIsNull(userBean.getVoltageRatio()))
                    .params("currentRatio", checkIsNull(userBean.getCurrentRatio()))
                    .params("price", checkIsNull(userBean.getPrice()))
                    .params("mobile", checkIsNull(userBean.getMobile()))
                    .params("priceName", checkIsNull(userBean.getPriceName()))
                    .params("phone", checkIsNull(userBean.getPhone()))
                    .params("remark", checkIsNull(userBean.getRemark()))
                    .params("ime", checkIsNull(userBean.getIme()))
                    .params("companyName", checkIsNull(userBean.getCompanyName()))
                    .params("ecodeType", checkIsNull(userBean.getEcodeType())).execute(new DialogCallback<LzyResponse<UserBean>>(IBaseView.getContext()) {
                @Override
                public void onSuccess(Response<LzyResponse<UserBean>> response) {
                    ToastUtils.showShort(response.body().message);
                    EventBus.getDefault().post(new EventCenter<Taiqu>(EventCenter.USER_SAVE));
                    IBaseView.close();
                }

                @Override
                public void onError(Response<LzyResponse<UserBean>> response) {
                    super.onError(response);
                    ToastUtils.showShort(response.getException().getMessage());
                }
            });
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }


    }

    @Override
    public void searchUser(final IUserListView IBaseView) {
        Setting setting = new Setting(IBaseView.getContext());
        OkGo.<LzyResponse<UserBean>>post(URLs.getURL(URLs.MTUSER_LIST))
                .params("countyid",  LoginInformation.getInstance().getUser().getStoreId())
                .params("taiqubh", LoginInformation.getInstance().getUser().getTransformerId())
                .params("audiotype",    LoginInformation.getInstance().getUser().getType())
                .execute(new DialogCallback<LzyResponse<UserBean>>(IBaseView.getContext()) {
            @Override
            public void onSuccess(Response<LzyResponse<UserBean>> response) {
                List<UserBean> countyList = ((JSONArray) response.body().model).toJavaList(UserBean.class);
                IBaseView.getUserList(countyList);

            }

            @Override
            public void onError(Response<LzyResponse<UserBean>> response) {
                super.onError(response);
                ToastUtils.showShort(response.getException().getMessage());
            }
        });

//        if (G.currentUserType.equals("10")) {//营业厅管理员 查看当前营业厅下的人
//            IBaseView.getUserList(UserBeanDaoHelper.getInstance().queryByStoreId(G.currentStoreId));
//        } else if (G.currentUserType.equals("11")) {
//            IBaseView.getUserList(UserBeanDaoHelper.getInstance().queryByTransFormId(G.currentTransformId));
//        } else {
//
//        }
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
                } else {
                    bean.setId(tempUser.getId());
                    bean.setIme(tempUser.getIme());
                    UserBeanDaoHelper.getInstance().addData(bean);
                }
            }
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }

    }
}
