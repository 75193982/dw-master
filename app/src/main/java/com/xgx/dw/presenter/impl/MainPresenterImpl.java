package com.xgx.dw.presenter.impl;

import android.widget.Toast;

import com.google.gson.Gson;
import com.xgx.dw.R;
import com.xgx.dw.base.BasePresenter;
import com.xgx.dw.base.BaseResponse;
import com.xgx.dw.model.impl.MainModelImpl;
import com.xgx.dw.model.interfaces.IMainModel;
import com.xgx.dw.net.JsonTransactionListener;
import com.xgx.dw.presenter.interfaces.IMainPresenter;
import com.xgx.dw.ui.view.interfaces.IMainView;
import com.xgx.dw.upload.UploadResponse;
import com.xgx.dw.utils.CommonUtils;
import com.xgx.dw.vo.request.UpdateVersionRequest;
import com.xgx.dw.vo.response.UpdateVersionResult;

public class MainPresenterImpl extends BasePresenter implements IMainPresenter {
    private IMainModel mMainModel;
    private IMainView mainView;

    public MainPresenterImpl(IMainView paramIMainView) {
        this.mainView = paramIMainView;
        this.mMainModel = new MainModelImpl(paramIMainView.getContext());
    }


    public void checkVersion(UpdateVersionRequest paramUpdateVersionRequest, final int paramInt) {
        this.mMainModel.checkVersion(paramUpdateVersionRequest, new JsonTransactionListener() {
            public void onFailure(int paramAnonymousInt) {
                super.onFailure(paramAnonymousInt);
            }

            public void onSuccess(BaseResponse respones) {
                if (respones.isSuccess()) {
                    UploadResponse localUpdateVersionResult = (UploadResponse) CommonUtils.getGson().fromJson(respones.getData(), UploadResponse.class);
                    MainPresenterImpl.this.mainView.checkVersionCallBack(localUpdateVersionResult);
                }
            }
        });
    }

    public void switchNavigation(int paramInt) {
        this.mainView.switchAlpha(paramInt);
        switch (paramInt) {
            case R.id.ll_wx:
                this.mainView.switchWX();
                break;
            case R.id.ll_address:
                this.mainView.switchAddressBook();
                break;
            case R.id.ll_find:
                this.mainView.switchFind();
                break;
            case R.id.ll_me:
                this.mainView.switchMe();
                break;
        }
    }
}



/* Location:           C:\Users\Administrator\Desktop\work\AndroidKiller_V1.2 正式版\projects\1469979611154app-release\ProjectSrc\smali\
 * Qualified Name:     com.xgx.dw.presenter.impl.MainPresenterImpl
 * JD-Core Version:    0.7.1
 */