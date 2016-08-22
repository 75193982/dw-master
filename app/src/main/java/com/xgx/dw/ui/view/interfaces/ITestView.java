package com.xgx.dw.ui.view.interfaces;

import com.xgx.dw.base.IBaseView;
import com.xgx.dw.vo.response.QueryResult;

public abstract interface ITestView
  extends IBaseView
{
  public abstract void queryResult(QueryResult paramQueryResult);
}

