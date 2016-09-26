package com.xgx.dw.presenter.impl;

import com.xgx.dw.UserBean;
import com.xgx.dw.base.BasePresenter;
import com.xgx.dw.dao.UserBeanDaoHelper;
import com.xgx.dw.presenter.interfaces.ILoginPresenter;
import com.xgx.dw.ui.view.interfaces.ILoginView;
import com.xgx.dw.vo.request.LoginRequest;

import javax.xml.validation.Schema;

public class LoginPresenterImpl extends BasePresenter implements ILoginPresenter {
    public void login(ILoginView paramILoginView, LoginRequest paramLoginRequest) {
        if (isEmpty(paramLoginRequest.bianhao, paramILoginView, "用户名不能为空")) return;
        if (isEmpty(paramLoginRequest.mima, paramILoginView, "密码不能为空")) return;
        UserBean localUserBean = UserBeanDaoHelper.getInstance().getDataById(paramLoginRequest.bianhao);
        if (localUserBean == null) {
            paramILoginView.showToast("当前用户不存在");
            paramILoginView.hideProgress();
            return;
        }
        if (paramLoginRequest.mima.equals(localUserBean.getPassword())) {
            paramILoginView.loginCallback(localUserBean);
            // return;
            // } else {
            //     paramILoginView.showToast("当前密码错误");
            // }
            // paramILoginView.hideProgress();
        }
    }

}
