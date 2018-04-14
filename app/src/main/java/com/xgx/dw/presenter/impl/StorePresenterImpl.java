package com.xgx.dw.presenter.impl;

import com.alibaba.fastjson.JSONArray;
import com.blankj.utilcode.util.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.Response;
import com.xgx.dw.base.BasePresenter;
import com.xgx.dw.base.EventCenter;
import com.xgx.dw.bean.LoginInformation;
import com.xgx.dw.bean.SysDept;
import com.xgx.dw.net.DialogCallback;
import com.xgx.dw.net.LzyResponse;
import com.xgx.dw.net.URLs;
import com.xgx.dw.presenter.interfaces.IStoresPresenter;
import com.xgx.dw.ui.view.interfaces.ICreateStoresView;
import com.xgx.dw.ui.view.interfaces.IStoresView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class StorePresenterImpl extends BasePresenter implements IStoresPresenter {
    public void saveStore(final ICreateStoresView baseView, SysDept county) {
        if (isEmpty(county.getCountyid(), baseView, "营业厅编号不能为空")) return;
        if (isEmpty(county.getSimplename(), baseView, "营业厅名称不能为空")) return;
        OkGo.<LzyResponse<SysDept>>post(URLs.getURL(URLs.COUNTY_SAVE)).params("id", checkIsNull(county.getId() + "")).params("countyid", checkIsNull(county.getCountyid())).params("simplename", checkIsNull(county.getSimplename())).params("address", checkIsNull(county.getAddress())).params("tel", checkIsNull(county.getTel())).params("contact", checkIsNull(county.getContact())).execute(new DialogCallback<LzyResponse<SysDept>>(baseView.getContext()) {
            @Override
            public void onSuccess(Response<LzyResponse<SysDept>> response) {
                ToastUtils.showShort(response.body().message);
                baseView.close();
                EventBus.getDefault().post(new EventCenter<SysDept>(EventCenter.COUNTY_SAVE));
            }

            @Override
            public void onError(Response<LzyResponse<SysDept>> response) {
                super.onError(response);
                ToastUtils.showShort(response.getException().getMessage());
            }
        });
    }

    public void searchStores(final IStoresView paramIStoresView, SysDept conty) {
        OkGo.<LzyResponse<SysDept>>post(URLs.getURL(URLs.COUNTY_LIST)).params("simplename", checkIsNull(conty.getSimplename())).tag(paramIStoresView.getContext()).cacheKey(URLs.COUNTY_LIST + LoginInformation.getInstance().getUser().getUserId()).cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST).execute(new DialogCallback<LzyResponse<SysDept>>(paramIStoresView.getContext()) {
            @Override
            public void onSuccess(Response<LzyResponse<SysDept>> response) {
                List<SysDept> countyList = ((JSONArray) response.body().model).toJavaList(SysDept.class);
                paramIStoresView.searchStores(countyList);

            }

            @Override
            public void onCacheSuccess(Response<LzyResponse<SysDept>> response) {
                super.onCacheSuccess(response);
                onSuccess(response);
            }

            @Override
            public void onError(Response<LzyResponse<SysDept>> response) {
                super.onError(response);
                ToastUtils.showShort(response.getException().getMessage());
            }
        });
    }
}

