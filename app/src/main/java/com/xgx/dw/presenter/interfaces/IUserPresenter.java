package com.xgx.dw.presenter.interfaces;

import com.xgx.dw.TransformerBean;
import com.xgx.dw.UserBean;
import com.xgx.dw.ui.activity.UserMgrActivity;
import com.xgx.dw.ui.view.interfaces.ICreateTransformerView;
import com.xgx.dw.ui.view.interfaces.ITransformerView;
import com.xgx.dw.ui.view.interfaces.IUserListView;
import com.xgx.dw.ui.view.interfaces.IUserView;

public abstract interface IUserPresenter {
    public abstract void saveUser(IUserView IBaseView, UserBean userBean, int type, boolean saveAndUpdate);

    void searchUser(IUserListView IBaseView);
}


