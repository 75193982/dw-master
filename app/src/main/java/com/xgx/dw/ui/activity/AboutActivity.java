package com.xgx.dw.ui.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.TextView;

import com.xgx.dw.R;
import com.xgx.dw.base.BaseAppCompatActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by xgx on 2016/10/29 0029 for dw-master
 */
public class AboutActivity extends BaseAppCompatActivity {
    @Bind(R.id.flowTv)
    TextView flowTv;

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


    @OnClick(R.id.flowTv)
    public void onClick() {
        Intent intent = new Intent(this, H5WebActivity.class);
        startActivity(intent);
    }
}
