package com.xgx.dw.presenter.interfaces;

import com.xgx.dw.ui.view.interfaces.ITestView;
import com.xgx.dw.vo.request.QueryParameter;

public abstract interface ITestPresenter {
    public abstract void attributionToInquiries(ITestView paramITestView, QueryParameter paramQueryParameter);
}
