package com.xgx.dw.model.interfaces;

import com.xgx.dw.net.StringTransactionListener;
import com.xgx.dw.vo.request.QueryParameter;

public abstract interface ITestModel
{
  public abstract void attributionToInquiries(QueryParameter paramQueryParameter, StringTransactionListener paramStringTransactionListener);
}


