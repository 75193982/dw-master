package com.xgx.dw.presenter.interfaces;

import com.xgx.dw.vo.request.UpdateVersionRequest;

public abstract interface IMainPresenter {

    public abstract void checkVersion(UpdateVersionRequest paramUpdateVersionRequest, int paramInt);

    public abstract void switchNavigation(int paramInt);
}

