package com.xgx.dw.presenter.interfaces;

import com.xgx.dw.SpotPricingBean;
import com.xgx.dw.ui.view.interfaces.ICreateSpotPricingView;
import com.xgx.dw.ui.view.interfaces.ISpotPricingView;

public abstract interface ISpotPricingPresenter
{
  public abstract void saveSpotPricing(ICreateSpotPricingView paramICreateSpotPricingView, SpotPricingBean paramSpotPricingBean, boolean paramBoolean);
  
  public abstract void searchSpotPricing(ISpotPricingView paramISpotPricingView, SpotPricingBean paramSpotPricingBean);
}

