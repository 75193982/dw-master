package com.xgx.dw.ui.checkversion;

import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.allenliu.versionchecklib.core.AVersionService;
import com.blankj.utilcode.util.ToastUtils;
import com.xgx.dw.app.BaseApplication;
import com.xgx.dw.app.Setting;
import com.xgx.dw.upload.UploadResponse;

public class CheckVersionService extends AVersionService {
    public CheckVersionService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onResponses(AVersionService service, String response) {
        Log.e("DemoService", response);
        //可以在判断版本之后在设置是否强制更新或者VersionParams
        //eg
        // versionParams.isForceUpdate=true;
        try {
            JSONObject jsonObject = JSON.parseObject(response);
            Setting setting = new Setting(BaseApplication.getInstance());

            if (jsonObject.getString("result").equals("001")) {
                setting.saveBoolean("isNewVersion", true);
                UploadResponse uploadResonse = JSON.parseObject(jsonObject.getString("model"), UploadResponse.class);
                showVersionDialog(uploadResonse.getApkPath(), "检测到新版本", uploadResonse.getRemark());
            } else {
                setting.saveBoolean("isNewVersion", false);
                if (CustomVersionDialogActivity.isShowToast == 1) {
                    ToastUtils.showShort("当前已经是最新版本");
                }
            }
        } catch (Exception e) {

        }
    }
}
