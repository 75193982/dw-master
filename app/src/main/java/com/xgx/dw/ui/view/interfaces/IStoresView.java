package com.xgx.dw.ui.view.interfaces;

import com.xgx.dw.base.IBaseView;
import com.xgx.dw.bean.SysDept;

import java.util.List;

public abstract interface IStoresView extends IBaseView {
    public abstract void searchStores(List<SysDept> paramList);
}

