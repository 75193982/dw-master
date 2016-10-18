package com.xgx.dw.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.xgx.dw.PricingBean;
import com.xgx.dw.R;
import com.xgx.dw.SearchDlLog;
import com.xgx.dw.SearchDlLogDao;
import com.xgx.dw.adapter.SearchLogListAdapter;
import com.xgx.dw.adapter.SpotListAdapter;
import com.xgx.dw.base.BaseAppCompatActivity;
import com.xgx.dw.bean.LoginInformation;
import com.xgx.dw.dao.PricingDaoHelper;
import com.xgx.dw.dao.SearchDlDaoHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/10/16 0016.
 */
public class SearchLogListActivity extends BaseAppCompatActivity {
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.numTv)
    TextView numTv;
    SearchLogListAdapter adapter;
    private List<SearchDlLog> beans;
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
            setToolbarTitle("倍率记录");
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
        String userid = LoginInformation.getInstance().getUser().getUserId();
        beans = SearchDlDaoHelper.getInstance().queryByUserId(type, userid);
        adapter.setNewData(beans);
        numTv.setText("共查出(" + beans.size() + ")条记录");

    }

}
