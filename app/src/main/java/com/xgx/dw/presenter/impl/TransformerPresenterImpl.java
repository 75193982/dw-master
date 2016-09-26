package com.xgx.dw.presenter.impl;

import com.xgx.dw.TransformerBean;
import com.xgx.dw.base.BasePresenter;
import com.xgx.dw.dao.TransformerBeanDaoHelper;
import com.xgx.dw.presenter.interfaces.ITransformerPresenter;
import com.xgx.dw.ui.view.interfaces.ICreateTransformerView;
import com.xgx.dw.ui.view.interfaces.ITransformerView;

public class TransformerPresenterImpl extends BasePresenter implements ITransformerPresenter {
    public void saveTransformer(ICreateTransformerView paramICreateTransformerView, TransformerBean paramTransformerBean, boolean paramBoolean) {
        if (isEmpty(paramTransformerBean.getStore_name(), paramICreateTransformerView, "请选择营业厅"))
            return;
        if (isEmpty(paramTransformerBean.getId(), paramICreateTransformerView, "台区编号不能为空")) return;
        if (isEmpty(paramTransformerBean.getName(), paramICreateTransformerView, "台区名称不能为空"))
            return;
        if (TransformerBeanDaoHelper.getInstance().hasKey(paramTransformerBean.getId()) && paramBoolean == true) {
            paramICreateTransformerView.showToast("已有相同编号的台区");
            paramICreateTransformerView.saveTransformer(false);
            return;
        }
        TransformerBeanDaoHelper.getInstance().addData(paramTransformerBean);
        paramICreateTransformerView.saveTransformer(true);
    }

    public void searchTransformer(ITransformerView paramITransformerView, TransformerBean paramTransformerBean) {
        paramITransformerView.searchTransformer(TransformerBeanDaoHelper.getInstance().getAllData());
    }
}
