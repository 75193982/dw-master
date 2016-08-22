package com.xgx.dw.presenter.interfaces;

import com.xgx.dw.ui.view.interfaces.ICreateStoresView;
import com.xgx.dw.ui.view.interfaces.IStoresView;
import com.xgx.dw.vo.request.StoresRequest;

public abstract interface IStoresPresenter
{
  public abstract void saveStore(ICreateStoresView paramICreateStoresView, StoresRequest paramStoresRequest, boolean paramBoolean);
  
  public abstract void searchStores(IStoresView paramIStoresView, StoresRequest paramStoresRequest);
}
