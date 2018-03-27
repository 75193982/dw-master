package com.xgx.dw.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.xgx.dw.R;
import com.xgx.dw.StoreBean;
import com.xgx.dw.adapter.StoresAdapter;
import com.xgx.dw.base.BaseAppCompatActivity;
import com.xgx.dw.presenter.impl.StorePresenterImpl;
import com.xgx.dw.presenter.interfaces.IStoresPresenter;
import com.xgx.dw.ui.view.interfaces.IStoresView;
import com.xgx.dw.vo.request.StoresRequest;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StoresMgrActivity extends BaseAppCompatActivity implements IStoresView, Toolbar.OnMenuItemClickListener {
    private static int REFRESH_RECYCLERVIEW = 0;
    @BindView(R.id.queryEt)
    EditText queryEt;
    private StoresAdapter adapter;
    private List<StoreBean> beans;
    private IStoresPresenter presenter;
    @BindView(R.id.list)
    RecyclerView recyclerView;

    @Override
    public void initContentView() {
        super.baseSetContentView(R.layout.activity_stores_mgr);
    }

    @Override
    public void initPresenter() {
        this.presenter = new StorePresenterImpl();
        getDatas();
    }

    @Override
    public void initView() {
        getSupportActionBar().setTitle(R.string.stores);
        getToolbar().setOnMenuItemClickListener(this);
        this.beans = new ArrayList();
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this, 1, false));
        this.adapter = new StoresAdapter(this.beans);
        this.adapter.openLoadAnimation();
        View localView = LayoutInflater.from(this).inflate(R.layout.base_empty_view, (ViewGroup) this.recyclerView.getParent(), false);
        this.adapter.setEmptyView(localView);
        this.recyclerView.setAdapter(this.adapter);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                StoreBean localStoreBean = (StoreBean) baseQuickAdapter.getItem(i);
                Intent localIntent = new Intent(getContext(), CreateStoreActivity.class);
                localIntent.putExtra("bean", localStoreBean);
                startActivityForResult(localIntent, REFRESH_RECYCLERVIEW);
            }
        });
        queryEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                getDatas();
            }
        });
    }

    @Override
    protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
        super.onActivityResult(paramInt1, paramInt2, paramIntent);
        if (paramInt1 == REFRESH_RECYCLERVIEW) {
            getDatas();
        }
    }

    private void getDatas() {
        StoresRequest localStoresRequest = new StoresRequest();
        localStoresRequest.storeName = queryEt.getText().toString();
        this.presenter.searchStores(this, localStoresRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu paramMenu) {
        getMenuInflater().inflate(R.menu.menu_main, paramMenu);
        return true;
    }


    @Override
    public boolean onMenuItemClick(MenuItem paramMenuItem) {
        switch (paramMenuItem.getItemId()) {
            case R.id.action_save:
                startActivityForResult(new Intent(this, CreateStoreActivity.class), REFRESH_RECYCLERVIEW);
                break;
        }
        return true;
    }

    @Override
    public void searchStores(List<StoreBean> paramList) {
        this.beans = paramList;
        this.adapter.setNewData(this.beans);
    }

}
