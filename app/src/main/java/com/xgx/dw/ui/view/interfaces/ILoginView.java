package com.xgx.dw.ui.view.interfaces;

import com.xgx.dw.UserBean;
import com.xgx.dw.base.IBaseView;

public abstract interface ILoginView extends IBaseView {
    public abstract void loginCallback(UserBean userBean);
}

