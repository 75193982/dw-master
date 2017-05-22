package com.xgx.dw.presenter.impl;

import com.xgx.dw.R;
import com.xgx.dw.UserBean;
import com.xgx.dw.base.BasePresenter;
import com.xgx.dw.dao.UserBeanDaoHelper;
import com.xgx.dw.presenter.interfaces.ILoginPresenter;
import com.xgx.dw.ui.view.interfaces.ILoginView;
import com.xgx.dw.utils.MyUtils;
import com.xgx.dw.vo.request.LoginRequest;

import java.util.UUID;

import javax.xml.validation.Schema;

public class LoginPresenterImpl extends BasePresenter implements ILoginPresenter {
    public void login(ILoginView paramILoginView, LoginRequest request) {
        if (isEmpty(request.bianhao, paramILoginView, "用户名不能为空")) return;
        if (isEmpty(request.mima, paramILoginView, "密码不能为空")) return;
        //这里不用验证了 ，只在购电的时候进行测试就可以，如果没有这个用户 则创建一个用户，
        UserBean localUserBean = UserBeanDaoHelper.getInstance().queryByTransFormUserId(request.bianhao);
        if (localUserBean == null) {
            UserBean newUser = new UserBean(UUID.randomUUID().toString(), request.bianhao, "二级账户-" + request.bianhao, request.mima, "20", MyUtils.getuniqueId(paramILoginView.getContext()));
            UserBeanDaoHelper.getInstance().addData(newUser);
            paramILoginView.loginCallback(newUser);
            return;
        }
        if (request.mima.equals(localUserBean.getPassword()) && localUserBean.getIme().contains(MyUtils.getuniqueId(paramILoginView.getContext()))) {
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
