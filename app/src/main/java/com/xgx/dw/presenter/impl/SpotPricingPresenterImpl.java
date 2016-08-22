package com.xgx.dw.presenter.impl;

import com.xgx.dw.SpotPricingBean;
import com.xgx.dw.base.BasePresenter;
import com.xgx.dw.dao.SpotPricingBeanDaoHelper;
import com.xgx.dw.presenter.interfaces.ISpotPricingPresenter;
import com.xgx.dw.ui.view.interfaces.ICreateSpotPricingView;
import com.xgx.dw.ui.view.interfaces.ISpotPricingView;

import java.util.List;

public class SpotPricingPresenterImpl extends BasePresenter implements ISpotPricingPresenter {
    public void saveSpotPricing(ICreateSpotPricingView paramICreateSpotPricingView, SpotPricingBean paramSpotPricingBean, boolean paramBoolean) {
        if (isEmpty(paramSpotPricingBean.getStorename(), paramICreateSpotPricingView, "请选择营业厅"))
            return;
        if (isEmpty(paramSpotPricingBean.getId(), paramICreateSpotPricingView, "电价编号不能为空")) return;
        if (isEmpty(paramSpotPricingBean.getName(), paramICreateSpotPricingView, "电价名称不能为空"))
            return;
        if (isEmpty(paramSpotPricingBean.getType(), paramICreateSpotPricingView, "请选择电价类型")) return;
        if (isEmpty(paramSpotPricingBean.getPrice_count(), paramICreateSpotPricingView, "电价总额不能为空"))
            return;
        List localList = SpotPricingBeanDaoHelper.getInstance().testQueryBy(paramSpotPricingBean.getName(), paramSpotPricingBean.getStore_id());
        if (localList != null && localList.size() > 0 && paramBoolean) {
            paramICreateSpotPricingView.showToast("该台区已经存在");
            paramICreateSpotPricingView.saveSpotPricing(false);
            return;
        }
        SpotPricingBeanDaoHelper.getInstance().addData(paramSpotPricingBean);
        paramICreateSpotPricingView.saveSpotPricing(true);
    }

    public void searchSpotPricing(ISpotPricingView paramISpotPricingView, SpotPricingBean paramSpotPricingBean) {
        paramISpotPricingView.searchSpotPricing(SpotPricingBeanDaoHelper.getInstance().getAllData());
    }
}


