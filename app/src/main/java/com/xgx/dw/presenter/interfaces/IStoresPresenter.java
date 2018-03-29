package com.xgx.dw.presenter.interfaces;

import com.xgx.dw.bean.County;
import com.xgx.dw.ui.view.interfaces.ICreateStoresView;
import com.xgx.dw.ui.view.interfaces.IStoresView;

public interface IStoresPresenter {
    void saveStore(ICreateStoresView baseView, County county);

    void searchStores(IStoresView baseView, County county);
}
