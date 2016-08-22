package com.xgx.dw.model.interfaces;

import com.xgx.dw.net.JsonTransactionListener;
import com.xgx.dw.vo.request.LoginRequest;

public abstract interface IUserModel
{
  public abstract void login(LoginRequest paramLoginRequest, JsonTransactionListener paramJsonTransactionListener);
}


