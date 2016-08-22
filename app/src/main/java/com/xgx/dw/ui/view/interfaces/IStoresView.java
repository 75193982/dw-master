package com.xgx.dw.ui.view.interfaces;

import com.xgx.dw.StoreBean;
import com.xgx.dw.base.IBaseView;
import java.util.List;

public abstract interface IStoresView
  extends IBaseView
{
  public abstract void searchStores(List<StoreBean> paramList);
}

