package com.xgx.dw.presenter.interfaces;

import com.xgx.dw.ui.view.interfaces.ILoginView;
import com.xgx.dw.vo.request.LoginRequest;

public abstract interface ILoginPresenter
{
  public abstract void login(ILoginView paramILoginView, LoginRequest paramLoginRequest);
}

