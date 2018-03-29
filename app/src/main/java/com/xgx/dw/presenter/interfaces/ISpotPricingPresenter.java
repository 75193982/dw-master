package com.xgx.dw.presenter.interfaces;

import com.xgx.dw.SpotPricingBean;
import com.xgx.dw.bean.Price;
import com.xgx.dw.ui.view.interfaces.ICreateSpotPricingView;
import com.xgx.dw.ui.view.interfaces.ISpotPricingView;

public interface ISpotPricingPresenter {
    void saveSpotPricing(ICreateSpotPricingView IBaseView, Price price);

    void searchSpotPricing(ISpotPricingView paramISpotPricingView, Price price);
}

