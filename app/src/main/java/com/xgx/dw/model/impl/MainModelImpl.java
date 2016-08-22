package com.xgx.dw.model.impl;

import android.content.Context;

import com.xgx.dw.base.BaseModel;
import com.xgx.dw.model.interfaces.IMainModel;
import com.xgx.dw.net.JsonTransactionListener;
import com.xgx.dw.net.URLs;
import com.xgx.dw.vo.request.UpdateVersionRequest;

public class MainModelImpl extends BaseModel implements IMainModel {
    public MainModelImpl(Context paramContext) {
        super(paramContext);
    }

    public void checkVersion(UpdateVersionRequest paramUpdateVersionRequest, JsonTransactionListener paramJsonTransactionListener) {
        post(getContext(), URLs.getURL(URLs.UPDATE_VERSION), paramUpdateVersionRequest, paramJsonTransactionListener);
    }
}


