package com.xgx.dw.presenter.impl;

import com.alibaba.fastjson.JSONArray;
import com.blankj.utilcode.util.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.nostra13.universalimageloader.utils.L;
import com.xgx.dw.StoreBean;
import com.xgx.dw.TransformerBean;
import com.xgx.dw.base.BasePresenter;
import com.xgx.dw.base.EventCenter;
import com.xgx.dw.bean.County;
import com.xgx.dw.bean.Taiqu;
import com.xgx.dw.dao.TransformerBeanDaoHelper;
import com.xgx.dw.net.DialogCallback;
import com.xgx.dw.net.LzyResponse;
import com.xgx.dw.net.URLs;
import com.xgx.dw.presenter.interfaces.ITransformerPresenter;
import com.xgx.dw.ui.view.interfaces.ICreateTransformerView;
import com.xgx.dw.ui.view.interfaces.ITransformerView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class TransformerPresenterImpl extends BasePresenter implements ITransformerPresenter {
    public void saveTransformer(final ICreateTransformerView IBaseView, Taiqu taiqu) {
        if (isEmpty(taiqu.getCountyid(), IBaseView, "请选择营业厅")) return;
        if (isEmpty(taiqu.getCode(), IBaseView, "台区编号不能为空")) return;
        if (isEmpty(taiqu.getName(), IBaseView, "台区名称不能为空")) return;
        OkGo.<LzyResponse<Taiqu>>post(URLs.getURL(URLs.TAIQU_SAVE)).params("id", checkIsNull(taiqu.getId() + "")).params("countyid", checkIsNull(taiqu.getCountyid())).
                params("countyname", checkIsNull(taiqu.getCountyname())).params("code", checkIsNull(taiqu.getCode())).params("name", checkIsNull(taiqu.getName())).execute(new DialogCallback<LzyResponse<Taiqu>>(IBaseView.getContext()) {
            @Override
            public void onSuccess(Response<LzyResponse<Taiqu>> response) {
                ToastUtils.showShort(response.body().message);
                IBaseView.close();
                EventBus.getDefault().post(new EventCenter<Taiqu>(EventCenter.TAIQU_SAVE));
            }

            @Override
            public void onError(Response<LzyResponse<Taiqu>> response) {
                super.onError(response);
                ToastUtils.showShort(response.getException().getMessage());
            }
        });
    }

    public void searchTransformer(final ITransformerView paramITransformerView, Taiqu taiqu) {

        OkGo.<LzyResponse<Taiqu>>post(URLs.getURL(URLs.TAIQU_LIST))
                .params("name", checkIsNull(taiqu.getName()))
                .params("countyid", checkIsNull(taiqu.getCountyid()))
                .execute(new DialogCallback<LzyResponse<Taiqu>>(paramITransformerView.getContext()) {
            @Override
            public void onSuccess(Response<LzyResponse<Taiqu>> response) {
                List<Taiqu> taiquList = ((JSONArray) response.body().model).toJavaList(Taiqu.class);
                paramITransformerView.searchTransformer(taiquList);

            }

            @Override
            public void onError(Response<LzyResponse<Taiqu>> response) {
                super.onError(response);
                ToastUtils.showShort(response.getException().getMessage());
            }
        });
        paramITransformerView.searchTransformer(TransformerBeanDaoHelper.getInstance().getAllData());
    }
}
