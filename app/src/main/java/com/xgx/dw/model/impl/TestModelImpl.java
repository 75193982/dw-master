package com.xgx.dw.model.impl;

import android.content.Context;
import com.xgx.dw.base.BaseModel;
import com.xgx.dw.model.interfaces.ITestModel;
import com.xgx.dw.net.StringTransactionListener;
import com.xgx.dw.net.URLs;
import com.xgx.dw.vo.request.QueryParameter;

public class TestModelImpl
  extends BaseModel
  implements ITestModel
{
  public TestModelImpl(Context paramContext)
  {
    super(paramContext);
  }
  
  public void attributionToInquiries(QueryParameter paramQueryParameter, StringTransactionListener paramStringTransactionListener)
  {
    get(getContext(), URLs.getURL("get"), paramQueryParameter, paramStringTransactionListener);
  }
}


