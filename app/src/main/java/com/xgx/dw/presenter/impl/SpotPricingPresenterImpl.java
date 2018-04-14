package com.xgx.dw.presenter.impl;

import com.alibaba.fastjson.JSONArray;
import com.blankj.utilcode.util.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.xgx.dw.base.BasePresenter;
import com.xgx.dw.base.EventCenter;
import com.xgx.dw.bean.Price;
import com.xgx.dw.net.DialogCallback;
import com.xgx.dw.net.LzyResponse;
import com.xgx.dw.net.URLs;
import com.xgx.dw.presenter.interfaces.ISpotPricingPresenter;
import com.xgx.dw.ui.view.interfaces.ICreateSpotPricingView;
import com.xgx.dw.ui.view.interfaces.ISpotPricingView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class SpotPricingPresenterImpl extends BasePresenter implements ISpotPricingPresenter {
    public void saveSpotPricing(final ICreateSpotPricingView IBaseView, Price price) {

        if (isEmpty(price.getCountyid(), IBaseView, "请选择营业厅")) return;
        if (isEmpty(price.getPricename(), IBaseView, "电价名称不能为空")) return;
        if (isEmpty(price.getPricetype(), IBaseView, "请选择电价类型")) return;
        if (isEmpty(price.getTotalprice(), IBaseView, "电价总额不能为空")) return;
        OkGo.<LzyResponse<Price>>post(URLs.getURL(URLs.PRICE_SAVE)).params("id", checkIsNull(price.getId() + "")).params("pricename", checkIsNull(price.getPricename())).params("pricetype", checkIsNull(price.getPricetype())).params("countyid", checkIsNull(price.getCountyid())).params("countyname", checkIsNull(price.getCountyname())).params("totalprice", checkIsNull(price.getTotalprice())).params("pricea", checkIsNull(price.getPricea())).params("priceb", checkIsNull(price.getPriceb())).params("pricec", checkIsNull(price.getPricec())).params("priced", checkIsNull(price.getPriced())).execute(new DialogCallback<LzyResponse<Price>>(IBaseView.getContext()) {
            @Override
            public void onSuccess(Response<LzyResponse<Price>> response) {
                ToastUtils.showShort(response.body().message);
                IBaseView.close();
                EventBus.getDefault().post(new EventCenter<Price>(EventCenter.PRICE_SAVE));
            }

            @Override
            public void onError(Response<LzyResponse<Price>> response) {
                super.onError(response);
                ToastUtils.showShort(response.getException().getMessage());
            }
        });
    }

    public void searchSpotPricing(final ISpotPricingView paramISpotPricingView, Price price) {
        OkGo.<LzyResponse<Price>>post(URLs.getURL(URLs.PRICE_LIST)).params("pricename", checkIsNull(price.getPricename())).params("countyid", checkIsNull(price.getCountyid())).execute(new DialogCallback<LzyResponse<Price>>(paramISpotPricingView.getContext()) {
            @Override
            public void onSuccess(Response<LzyResponse<Price>> response) {
                List<Price> countyList = ((JSONArray) response.body().model).toJavaList(Price.class);
                paramISpotPricingView.searchSpotPricing(countyList);

            }

            @Override
            public void onError(Response<LzyResponse<Price>> response) {
                super.onError(response);
                ToastUtils.showShort(response.getException().getMessage());
            }
        });
    }
}


