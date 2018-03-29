package com.xgx.dw.ui.view.interfaces;

import com.xgx.dw.SpotPricingBean;
import com.xgx.dw.base.IBaseView;
import com.xgx.dw.bean.Price;

import java.util.List;

public abstract interface ISpotPricingView extends IBaseView {
    public abstract void searchSpotPricing(List<Price> paramList);
}

