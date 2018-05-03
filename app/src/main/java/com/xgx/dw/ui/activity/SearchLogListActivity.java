package com.xgx.dw.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.xgx.dw.R;
import com.xgx.dw.adapter.SearchLogListAdapter;
import com.xgx.dw.base.BaseAppCompatActivity;
import com.xgx.dw.bean.LoginInformation;
import com.xgx.dw.bean.MtzRecordFront;
import com.xgx.dw.net.DialogCallback;
import com.xgx.dw.net.JsonCallback;
import com.xgx.dw.net.LzyResponse;
import com.xgx.dw.net.URLs;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SearchLogListActivity extends BaseAppCompatActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.numTv)
    TextView numTv;
    SearchLogListAdapter adapter;
    private List<MtzRecordFront> beans;
    private String type;

    @Override
    public void initContentView() {
        baseSetContentView(R.layout.activity_search_log_list);
    }

    @Override
    public void initView() {
        type = checkText(getIntent().getStringExtra("type"));
        if (type.equals("1")) {
            setToolbarTitle("查表记录");
        } else if (type.equals("2")) {
            setToolbarTitle("功率记录");
        }
        beans = new ArrayList();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, 1, false));
        adapter = new SearchLogListAdapter(beans);
        this.adapter.openLoadAnimation();
        View localView = LayoutInflater.from(this).inflate(R.layout.base_empty_view, (ViewGroup) this.recyclerView.getParent(), false);
        adapter.setEmptyView(localView);
        recyclerView.setAdapter(this.adapter);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
            }
        });
    }

    @Override
    public void initPresenter() {
        //查询电价
        //查表记录
        OkGo.<LzyResponse<MtzRecordFront>>post(URLs.getURL(URLs.RECORD_LIST))
                .params("type", type)
                .params("userId", LoginInformation.getInstance().getUser().getUserId())
                .execute(new DialogCallback<LzyResponse<MtzRecordFront>>(this) {
                    @Override
                    public void onSuccess(Response<LzyResponse<MtzRecordFront>> response) {
                        beans = ((JSONArray) response.body().model).toJavaList(MtzRecordFront.class);
                        adapter.setNewData(beans);
                        numTv.setText("共查出(" + beans.size() + ")条记录");
                    }
                });

    }

}
