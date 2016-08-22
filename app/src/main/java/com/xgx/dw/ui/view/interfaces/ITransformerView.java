package com.xgx.dw.ui.view.interfaces;

import com.xgx.dw.TransformerBean;
import com.xgx.dw.base.IBaseView;

import java.util.List;

public abstract interface ITransformerView
        extends IBaseView {
    public abstract void searchTransformer(List<TransformerBean> paramList);
}

