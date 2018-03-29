package com.xgx.dw.presenter.interfaces;

import com.xgx.dw.bean.Taiqu;
import com.xgx.dw.ui.view.interfaces.ICreateTransformerView;
import com.xgx.dw.ui.view.interfaces.ITransformerView;

public interface ITransformerPresenter {
    void saveTransformer(ICreateTransformerView baseView, Taiqu taiqu);

    void searchTransformer(ITransformerView paramITransformerView, Taiqu taiqu);
}


