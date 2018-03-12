package com.xgx.dw.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.xgx.dw.R;
import com.xgx.dw.SpotPricingBean;
import com.xgx.dw.adapter.SpotPricingAdapter;
import com.xgx.dw.base.BaseAppCompatActivity;
import com.xgx.dw.bean.LoginInformation;
import com.xgx.dw.dao.SpotPricingBeanDaoHelper;
import com.xgx.dw.presenter.impl.SpotPricingPresenterImpl;
import com.xgx.dw.presenter.interfaces.ISpotPricingPresenter;
import com.xgx.dw.ui.view.interfaces.ISpotPricingView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SearchSpotPricingListActivity extends BaseAppCompatActivity implements ISpotPricingView {
    private static int REFRESH_RECYCLERVIEW = 0;
    private SpotPricingAdapter adapter;
    private List<SpotPricingBean> beans;
    private ISpotPricingPresenter presenter;
    @BindView(R.id.list)
    RecyclerView recyclerView;

    @Override
    public void initContentView() {
        baseSetContentView(R.layout.activity_spot_pricing);
    }

    @Override
    public void initPresenter() {
        String storeId = LoginInformation.getInstance().getUser().getStoreId();
        if (TextUtils.isEmpty(storeId)) {
            searchSpotPricing(SpotPricingBeanDaoHelper.getInstance().getAllData());
        } else {
            searchSpotPricing(SpotPricingBeanDaoHelper.getInstance().testQueryBy(LoginInformation.getInstance().getUser().getStoreId()));
        }
    }

    @Override
    public void initView() {
        getSupportActionBar().setTitle(R.string.spotpricing);
        this.beans = new ArrayList();
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this, 1, false));
        this.adapter = new SpotPricingAdapter(this.beans);
        this.adapter.openLoadAnimation();
        View localView = LayoutInflater.from(this).inflate(R.layout.base_empty_view, (ViewGroup) this.recyclerView.getParent(), false);
        this.adapter.setEmptyView(localView);
        this.recyclerView.setAdapter(this.adapter);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent intent = new Intent();
                intent.putExtra("entity", adapter.getItem(i));
                setResult(1001, intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
        super.onActivityResult(paramInt1, paramInt2, paramIntent);
    }


    @Override
    public void searchSpotPricing(List<SpotPricingBean> paramList) {
        this.beans = paramList;
        this.adapter.setNewData(this.beans);
    }
}
