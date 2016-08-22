package com.xgx.dw.presenter.interfaces;

import com.xgx.dw.TransformerBean;
import com.xgx.dw.ui.view.interfaces.ICreateTransformerView;
import com.xgx.dw.ui.view.interfaces.ITransformerView;

public abstract interface ITransformerPresenter {
    public abstract void saveTransformer(ICreateTransformerView paramICreateTransformerView, TransformerBean paramTransformerBean, boolean paramBoolean);

    public abstract void searchTransformer(ITransformerView paramITransformerView, TransformerBean paramTransformerBean);
}


