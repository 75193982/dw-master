package com.xgx.dw.ui.view.interfaces;

import com.xgx.dw.base.IBaseView;
import com.xgx.dw.vo.response.UserInfo;

public abstract interface ILoginView
  extends IBaseView
{
  public abstract void loginCallback(UserInfo paramUserInfo);
}

