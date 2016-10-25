package com.xgx.dw.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.xgx.dw.PricingBean;
import com.xgx.dw.R;
import com.xgx.dw.SpotPricingBean;
import com.xgx.dw.adapter.SpotListAdapter;
import com.xgx.dw.adapter.SpotPricingAdapter;
import com.xgx.dw.base.BaseActivity;
import com.xgx.dw.base.BaseAppCompatActivity;
import com.xgx.dw.bean.LoginInformation;
import com.xgx.dw.dao.PricingDaoHelper;
import com.xgx.dw.dao.SpotPricingBeanDaoHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/16 0016.
 */
public class SpotListActivity extends BaseAppCompatActivity {
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    SpotListAdapter adapter;
    private List<PricingBean> beans;

    @Override
    public void initContentView() {
        baseSetContentView(R.layout.activity_spot_list);
    }

    @Override
    public void initView() {
        setToolbarTitle("购电记录");
        beans = new ArrayList();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, 1, false));
        adapter = new SpotListAdapter(beans);
        this.adapter.openLoadAnimation();
        View localView = LayoutInflater.from(this).inflate(R.layout.base_empty_view, (ViewGroup) this.recyclerView.getParent(), false);
        adapter.setEmptyView(localView);
        recyclerView.setAdapter(this.adapter);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                if (!adapter.getItem(i).getFinishtype().equals("2")) {
                    Intent intent = new Intent(SpotListActivity.this, SpecialOperationDetailActivity.class);
                    intent.putExtra("type", 66);
                    intent.putExtra("dlbean", adapter.getItem(i));
                    startActivity(intent);
                } else {
                    showToast("已完成购电");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        String userid = LoginInformation.getInstance().getUser().getUserId();
        beans = PricingDaoHelper.getInstance().queryByUserId(userid);
        adapter.setNewData(beans);
    }

    @Override
    public void initPresenter() {
        //查询电价
    }

}
