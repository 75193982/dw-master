package com.xgx.dw.presenter.impl;

import com.xgx.dw.SpotPricingBean;
import com.xgx.dw.base.BasePresenter;
import com.xgx.dw.dao.SpotPricingBeanDaoHelper;
import com.xgx.dw.presenter.interfaces.ISpotPricingPresenter;
import com.xgx.dw.ui.view.interfaces.ICreateSpotPricingView;
import com.xgx.dw.ui.view.interfaces.ISpotPricingView;

import java.util.List;

public class SpotPricingPresenterImpl extends BasePresenter implements ISpotPricingPresenter {
    public void saveSpotPricing(ICreateSpotPricingView baseView, SpotPricingBean paramSpotPricingBean, boolean paramBoolean) {
        baseView.showProgress("保存电价中...");

        if (isEmpty(paramSpotPricingBean.getStorename(), baseView, "请选择营业厅"))
            return;
        if (isEmpty(paramSpotPricingBean.getId(), baseView, "电价编号不能为空")) return;
        if (isEmpty(paramSpotPricingBean.getName(), baseView, "电价名称不能为空"))
            return;
        if (isEmpty(paramSpotPricingBean.getType(), baseView, "请选择电价类型")) return;
        if (isEmpty(paramSpotPricingBean.getPrice_count(), baseView, "电价总额不能为空"))
            return;
        List localList = SpotPricingBeanDaoHelper.getInstance().testQueryBy(paramSpotPricingBean.getName(), paramSpotPricingBean.getStore_id());
        if (localList != null && localList.size() > 0 && paramBoolean) {
            baseView.showToast("该电价已经存在");
            baseView.saveSpotPricing(false);
            baseView.hideProgress();

            return;
        }
        SpotPricingBeanDaoHelper.getInstance().addData(paramSpotPricingBean);
        baseView.saveSpotPricing(true);
        baseView.hideProgress();
    }

    public void searchSpotPricing(ISpotPricingView paramISpotPricingView, SpotPricingBean paramSpotPricingBean) {
        paramISpotPricingView.searchSpotPricing(SpotPricingBeanDaoHelper.getInstance().getAllData());
    }
}


