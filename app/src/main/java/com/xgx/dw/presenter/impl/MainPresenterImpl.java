package com.xgx.dw.presenter.impl;

import com.xgx.dw.R;
import com.xgx.dw.base.BasePresenter;
import com.xgx.dw.presenter.interfaces.IMainPresenter;
import com.xgx.dw.ui.view.interfaces.IMainView;

public class MainPresenterImpl extends BasePresenter implements IMainPresenter {
    private IMainView mainView;

    public MainPresenterImpl(IMainView paramIMainView) {
        this.mainView = paramIMainView;
    }

    public void switchNavigation(int paramInt) {
        this.mainView.switchAlpha(paramInt);
        switch (paramInt) {
            case R.id.ll_wx:
                this.mainView.switchWX();
                break;
            case R.id.ll_address:
                this.mainView.switchAddressBook();
                break;
            case R.id.ll_find:
                this.mainView.switchFind();
                break;
            case R.id.ll_me:
                this.mainView.switchMe();
                break;
        }
    }
}


