package com.xgx.dw.presenter.impl;

import android.content.Intent;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.Response;
import com.xgx.dw.StoreBean;
import com.xgx.dw.UserBean;
import com.xgx.dw.app.BaseApplication;
import com.xgx.dw.app.Setting;
import com.xgx.dw.base.BasePresenter;
import com.xgx.dw.base.EventCenter;
import com.xgx.dw.bean.County;
import com.xgx.dw.bean.SysUser;
import com.xgx.dw.dao.StoreBeanDaoHelper;
import com.xgx.dw.net.DialogCallback;
import com.xgx.dw.net.LzyResponse;
import com.xgx.dw.net.URLs;
import com.xgx.dw.presenter.interfaces.IStoresPresenter;
import com.xgx.dw.ui.activity.LoginActivity;
import com.xgx.dw.ui.activity.MainActivity;
import com.xgx.dw.ui.view.interfaces.ICreateStoresView;
import com.xgx.dw.ui.view.interfaces.IStoresView;
import com.xgx.dw.vo.request.StoresRequest;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class StorePresenterImpl extends BasePresenter implements IStoresPresenter {
    public void saveStore(final ICreateStoresView baseView, County county) {
        if (isEmpty(county.getCountyid(), baseView, "营业厅编号不能为空")) return;
        if (isEmpty(county.getCountyname(), baseView, "营业厅名称不能为空")) return;
        OkGo.<LzyResponse<County>>post(URLs.getURL(URLs.COUNTY_SAVE)).params("id", checkIsNull(county.getId() + "")).params("countyid", checkIsNull(county.getCountyid())).params("countyname", checkIsNull(county.getCountyname())).params("address", checkIsNull(county.getAddress())).params("tel", checkIsNull(county.getTel())).params("contact", checkIsNull(county.getContact())).execute(new DialogCallback<LzyResponse<County>>(baseView.getContext()) {
            @Override
            public void onSuccess(Response<LzyResponse<County>> response) {
                ToastUtils.showShort(response.body().message);
                baseView.close();
                EventBus.getDefault().post(new EventCenter<County>(EventCenter.COUNTY_SAVE));
            }

            @Override
            public void onError(Response<LzyResponse<County>> response) {
                super.onError(response);
                ToastUtils.showShort(response.getException().getMessage());
            }
        });
    }

    public void searchStores(final IStoresView paramIStoresView, County conty) {
        OkGo.<LzyResponse<County>>post(URLs.getURL(URLs.COUNTY_LIST)).params("countyname", checkIsNull(conty.getCountyname())).execute(new DialogCallback<LzyResponse<County>>(paramIStoresView.getContext()) {
            @Override
            public void onSuccess(Response<LzyResponse<County>> response) {
                List<County> countyList = ((JSONArray) response.body().model).toJavaList(County.class);
                paramIStoresView.searchStores(countyList);

            }

            @Override
            public void onError(Response<LzyResponse<County>> response) {
                super.onError(response);
                ToastUtils.showShort(response.getException().getMessage());
            }
        });
    }
}

