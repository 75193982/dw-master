package com.xgx.dw.ui.activity;

import android.content.Intent;
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
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.xgx.dw.R;
import com.xgx.dw.adapter.StoresAdapter;
import com.xgx.dw.base.BaseEventBusActivity;
import com.xgx.dw.base.EventCenter;
import com.xgx.dw.bean.SysDept;
import com.xgx.dw.presenter.impl.StorePresenterImpl;
import com.xgx.dw.presenter.interfaces.IStoresPresenter;
import com.xgx.dw.ui.view.interfaces.IStoresView;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class StoresMgrActivity extends BaseEventBusActivity implements IStoresView, Toolbar.OnMenuItemClickListener {
    private static int REFRESH_RECYCLERVIEW = 0;
    @BindView(R.id.query)
    EditText queryEt;
    private StoresAdapter adapter;
    private List<SysDept> beans;
    private IStoresPresenter presenter;
    @BindView(R.id.list)
    RecyclerView recyclerView;
    @BindView(R.id.numTv)
    TextView numTv;

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
                if (getIntent().getBooleanExtra("isSelect", false)) {
                    EventBus.getDefault().post(new EventCenter<SysDept>(EventCenter.COUNTY_SELECT, adapter.getItem(i)));
                    finish();
                } else {
                    Intent localIntent = new Intent(getContext(), CreateStoreActivity.class);
                    localIntent.putExtra("bean", (Serializable) adapter.getItem(i));
                    startActivityForResult(localIntent, REFRESH_RECYCLERVIEW);
                }

            }
        });
        queryEt.setHint("根据营业厅名称查询");
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
    }

    private void getDatas() {
        SysDept county = new SysDept();
        county.setSimplename(queryEt.getText().toString());
        this.presenter.searchStores(this, county);
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
    public void searchStores(List<SysDept> paramList) {
        this.beans = paramList;
        numTv.setText("共搜索到" + paramList.size() + "个营业厅");
        this.adapter.setNewData(this.beans);
    }

    @Override
    protected void onEventComming(EventCenter eventCenter) {
        if (EventCenter.COUNTY_SAVE == eventCenter.getEventCode()) {
            getDatas();
        }
    }

    @Override
    public boolean isBindEventBusHere() {
        return true;
    }
}
