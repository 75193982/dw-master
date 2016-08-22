package com.xgx.dw.model.impl;

import android.content.Context;
import com.xgx.dw.base.BaseModel;
import com.xgx.dw.model.interfaces.IStoreModel;
import com.xgx.dw.net.JsonTransactionListener;
import com.xgx.dw.vo.request.StoresRequest;

public class StoreModelImpl
  extends BaseModel
  implements IStoreModel
{
  public StoreModelImpl(Context paramContext)
  {
    super(paramContext);
  }
  
  public void saveStore(StoresRequest paramStoresRequest, JsonTransactionListener paramJsonTransactionListener) {}
}

