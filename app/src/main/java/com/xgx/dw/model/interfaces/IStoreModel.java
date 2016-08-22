package com.xgx.dw.model.interfaces;

import com.xgx.dw.net.JsonTransactionListener;
import com.xgx.dw.vo.request.StoresRequest;

public abstract interface IStoreModel
{
  public abstract void saveStore(StoresRequest paramStoresRequest, JsonTransactionListener paramJsonTransactionListener);
}


