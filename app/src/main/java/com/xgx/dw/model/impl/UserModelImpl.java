package com.xgx.dw.model.impl;

import android.content.Context;

import com.xgx.dw.base.BaseModel;
import com.xgx.dw.model.interfaces.IUserModel;
import com.xgx.dw.net.JsonTransactionListener;
import com.xgx.dw.net.URLs;
import com.xgx.dw.vo.request.LoginRequest;

public class UserModelImpl extends BaseModel implements IUserModel {
    public UserModelImpl(Context paramContext) {
        super(paramContext);
    }

    public void login(LoginRequest paramLoginRequest, JsonTransactionListener paramJsonTransactionListener) {
        post(getContext(), URLs.getURL(URLs.USER_SIGNIN), paramLoginRequest, paramJsonTransactionListener);
    }
}


