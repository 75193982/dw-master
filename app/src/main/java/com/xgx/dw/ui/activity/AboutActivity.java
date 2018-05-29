package com.xgx.dw.ui.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xgx.dw.R;
import com.xgx.dw.base.BaseAppCompatActivity;
import com.xgx.dw.utils.MyUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by xgx on 2016/10/29 0029 for dw-master
 */
public class AboutActivity extends BaseAppCompatActivity {
    @BindView(R.id.flowTv)
    TextView flowTv;
    @BindView(R.id.btn_update)
    TextView btnUpdate;

    @Override
    public void initContentView() {
        baseSetContentView(R.layout.activity_about);
    }

    @Override
    public void initView() {
        setToolbarTitle("服务中心");
    }

    @Override
    public void initPresenter() {
        flowTv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        flowTv.getPaint().setAntiAlias(true);//抗锯齿

    }


    @OnClick({R.id.flowTv, R.id.btn_update})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.flowTv:
                Intent intent = new Intent(this, H5WebActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_update:
                MyUtils.checkVersion(this, 1);

                break;
        }
    }
}
