package com.xgx.dw.model.interfaces;

import com.xgx.dw.net.JsonTransactionListener;
import com.xgx.dw.vo.request.UpdateVersionRequest;

public abstract interface IMainModel {
    public abstract void checkVersion(UpdateVersionRequest paramUpdateVersionRequest, JsonTransactionListener paramJsonTransactionListener);
}


