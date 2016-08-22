package com.xgx.dw.ui.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter.OnRecyclerViewItemClickListener;
import com.xgx.dw.R;
import com.xgx.dw.SpotPricingBean;
import com.xgx.dw.adapter.SpotPricingAdapter;
import com.xgx.dw.base.BaseAppCompatActivity;
import com.xgx.dw.presenter.impl.SpotPricingPresenterImpl;
import com.xgx.dw.presenter.interfaces.ISpotPricingPresenter;
import com.xgx.dw.ui.view.interfaces.ISpotPricingView;

import java.util.ArrayList;
import java.util.List;

public class SpotPricingActivity extends BaseAppCompatActivity implements ISpotPricingView, Toolbar.OnMenuItemClickListener, BaseQuickAdapter.OnRecyclerViewItemClickListener {
    private static int REFRESH_RECYCLERVIEW = 0;
    private SpotPricingAdapter adapter;
    private List<SpotPricingBean> beans;
    private ISpotPricingPresenter presenter;
    @Bind({R.id.list})
    RecyclerView recyclerView;

    public void initContentView() {
        baseSetContentView(R.layout.activity_spot_pricing);
    }

    public void initPresenter() {
        this.presenter = new SpotPricingPresenterImpl();
        SpotPricingBean localSpotPricingBean = new SpotPricingBean();
        this.presenter.searchSpotPricing(this, localSpotPricingBean);
    }

    public void initView() {
        getSupportActionBar().setTitle(R.string.spotpricing);
        getToolbar().setOnMenuItemClickListener(this);
        this.beans = new ArrayList();
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this, 1, false));
        this.adapter = new SpotPricingAdapter(this.beans);
        this.adapter.openLoadAnimation();
        View localView = LayoutInflater.from(this).inflate(R.layout.base_empty_view, (ViewGroup) this.recyclerView.getParent(), false);
        this.adapter.setEmptyView(localView);
        this.adapter.setOnRecyclerViewItemClickListener(this);
        this.recyclerView.setAdapter(this.adapter);
    }

    protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
        super.onActivityResult(paramInt1, paramInt2, paramIntent);
        if (paramInt1 == REFRESH_RECYCLERVIEW) {
            SpotPricingBean localSpotPricingBean = new SpotPricingBean();
            this.presenter.searchSpotPricing(this, localSpotPricingBean);
        }
    }

    public boolean onCreateOptionsMenu(Menu paramMenu) {
        getMenuInflater().inflate(R.menu.menu_main, paramMenu);
        return true;
    }

    public void onItemClick(View paramView, int paramInt) {
        SpotPricingBean localSpotPricingBean = (SpotPricingBean) this.adapter.getItem(paramInt);
        Intent localIntent = new Intent(this, CreateSpotPricingAcvitity.class);
        localIntent.putExtra("bean", localSpotPricingBean);
        startActivityForResult(localIntent, REFRESH_RECYCLERVIEW);
    }

    public boolean onMenuItemClick(MenuItem paramMenuItem) {
        switch (paramMenuItem.getItemId()) {
            case R.id.action_save:
                startActivityForResult(new Intent(this, CreateSpotPricingAcvitity.class), REFRESH_RECYCLERVIEW);
                break;
        }
        return true;
    }

    public void searchSpotPricing(List<SpotPricingBean> paramList) {
        this.beans = paramList;
        this.adapter.setNewData(this.beans);
    }
}
