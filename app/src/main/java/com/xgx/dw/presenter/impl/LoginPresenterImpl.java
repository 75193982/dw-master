package com.xgx.dw.presenter.impl;

import com.xgx.dw.UserBean;
import com.xgx.dw.base.BasePresenter;
import com.xgx.dw.dao.UserBeanDaoHelper;
import com.xgx.dw.presenter.interfaces.ILoginPresenter;
import com.xgx.dw.ui.view.interfaces.ILoginView;
import com.xgx.dw.utils.MyUtils;
import com.xgx.dw.vo.request.LoginRequest;

import javax.xml.validation.Schema;

public class LoginPresenterImpl extends BasePresenter implements ILoginPresenter {
    public void login(ILoginView paramILoginView, LoginRequest request) {
        if (isEmpty(request.bianhao, paramILoginView, "用户名不能为空")) return;
        if (isEmpty(request.mima, paramILoginView, "密码不能为空")) return;
        UserBean localUserBean = UserBeanDaoHelper.getInstance().getDataById(request.bianhao);
        if (localUserBean == null) {
            paramILoginView.showToast("当前用户不存在");
            paramILoginView.hideProgress();
            return;
        }
        if (request.mima.equals(localUserBean.getPassword()) && localUserBean.getIme().equals(MyUtils.getuniqueId(paramILoginView.getContext()))) {
            paramILoginView.loginCallback(localUserBean);
            // return;
            // } else {
            //     paramILoginView.showToast("当前密码错误");
            // }
            // paramILoginView.hideProgress();
        } else {
            paramILoginView.showToast("账号不能在该手机上使用");
        }
    }

}
