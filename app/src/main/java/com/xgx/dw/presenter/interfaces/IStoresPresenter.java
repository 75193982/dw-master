package com.xgx.dw.presenter.interfaces;

import com.xgx.dw.bean.SysDept;
import com.xgx.dw.ui.view.interfaces.ICreateStoresView;
import com.xgx.dw.ui.view.interfaces.IStoresView;

public interface IStoresPresenter {
    void saveStore(ICreateStoresView baseView, SysDept county);

    void searchStores(IStoresView baseView, SysDept county);
}
