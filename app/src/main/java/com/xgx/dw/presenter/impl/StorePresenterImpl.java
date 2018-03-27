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

import java.util.ArrayList;
import java.util.List;

public class StorePresenterImpl extends BasePresenter implements IStoresPresenter {
    public void saveStore(ICreateStoresView paramICreateStoresView, StoresRequest paramStoresRequest, boolean paramBoolean) {
        if (isEmpty(paramStoresRequest.storeId, paramICreateStoresView, "营业厅编号不能为空")) return;
        if (isEmpty(paramStoresRequest.storeName, paramICreateStoresView, "营业厅名称不能为空")) return;
        if ((StoreBeanDaoHelper.getInstance().getDataById(paramStoresRequest.storeId) != null) && (paramBoolean)) {
            paramICreateStoresView.showToast("已经存在该营业厅");
            paramICreateStoresView.saveStores(false);
            return;
        }
        StoreBean localStoreBean = new StoreBean();
        localStoreBean.setId(paramStoresRequest.storeId);
        localStoreBean.setName(paramStoresRequest.storeName);
        localStoreBean.setAddress(paramStoresRequest.storeAddress);
        localStoreBean.setLinkman(paramStoresRequest.storeLinkMan);
        localStoreBean.setContact_way(paramStoresRequest.storeContactWay);
        StoreBeanDaoHelper.getInstance().addData(localStoreBean);
        paramICreateStoresView.saveStores(true);
    }

    public void searchStores(final IStoresView paramIStoresView, StoresRequest paramStoresRequest) {
        OkGo.<LzyResponse<County>>post(URLs.getURL(URLs.COUNTY_LIST)).params("countyname", paramStoresRequest.storeName).execute(new DialogCallback<LzyResponse<County>>(paramIStoresView.getContext()) {
            @Override
            public void onSuccess(Response<LzyResponse<County>> response) {
                List<County> countyList = ((JSONArray) response.body().model).toJavaList(County.class);
                List<StoreBean> beans = new ArrayList<>();
                for (int i = 0; i < countyList.size(); i++) {
                    StoreBean bean = new StoreBean();
                    bean.setId(countyList.get(i).getCountyid() + "");
                    bean.setName(countyList.get(i).getCountyname() + "");
                    bean.setAddress(countyList.get(i).getAddress());
                    bean.setContact_way(countyList.get(i).getTel());
                    bean.setLinkman(countyList.get(i).getContact());
                    beans.add(bean);
                }
                paramIStoresView.searchStores(beans);

            }

            @Override
            public void onError(Response<LzyResponse<County>> response) {
                super.onError(response);
                ToastUtils.showShort(response.getException().getMessage());
            }
        });
    }
}

