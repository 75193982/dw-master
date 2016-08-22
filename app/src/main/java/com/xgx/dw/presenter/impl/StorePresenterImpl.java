package com.xgx.dw.presenter.impl;

import com.xgx.dw.StoreBean;
import com.xgx.dw.base.BasePresenter;
import com.xgx.dw.dao.StoreBeanDaoHelper;
import com.xgx.dw.presenter.interfaces.IStoresPresenter;
import com.xgx.dw.ui.view.interfaces.ICreateStoresView;
import com.xgx.dw.ui.view.interfaces.IStoresView;
import com.xgx.dw.vo.request.StoresRequest;

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

    public void searchStores(IStoresView paramIStoresView, StoresRequest paramStoresRequest) {
        paramIStoresView.searchStores(StoreBeanDaoHelper.getInstance().getAllData());
    }
}

