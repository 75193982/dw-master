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
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.xgx.dw.R;
import com.xgx.dw.TransformerBean;
import com.xgx.dw.adapter.TransformerAdapter;
import com.xgx.dw.base.BaseAppCompatActivity;
import com.xgx.dw.presenter.impl.TransformerPresenterImpl;
import com.xgx.dw.presenter.interfaces.ITransformerPresenter;
import com.xgx.dw.ui.view.interfaces.ITransformerView;

import java.util.ArrayList;
import java.util.List;

public class TransformerActivity extends BaseAppCompatActivity implements ITransformerView, Toolbar.OnMenuItemClickListener {
    private static int REFRESH_RECYCLERVIEW = 0;
    private TransformerAdapter adapter;
    private List<TransformerBean> beans;
    private ITransformerPresenter presenter;
    @Bind({R.id.list})
    RecyclerView recyclerView;

    public void initContentView() {
        baseSetContentView(R.layout.activity_transformer);
    }

    public void initPresenter() {
        this.presenter = new TransformerPresenterImpl();
        TransformerBean localTransformerBean = new TransformerBean();
        this.presenter.searchTransformer(this, localTransformerBean);
    }

    public void initView() {
        getSupportActionBar().setTitle(R.string.transformer);
        getToolbar().setOnMenuItemClickListener(this);
        this.beans = new ArrayList();
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this, 1, false));
        this.adapter = new TransformerAdapter(this.beans);
        this.adapter.openLoadAnimation();
        View localView = LayoutInflater.from(this).inflate(R.layout.base_empty_view, (ViewGroup) this.recyclerView.getParent(), false);
        this.adapter.setEmptyView(localView);
        this.recyclerView.setAdapter(this.adapter);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                TransformerBean localTransformerBean = (TransformerBean) baseQuickAdapter.getItem(i);
                Intent localIntent = new Intent(getContext(), CreateTransformerAcvitity.class);
                localIntent.putExtra("bean", localTransformerBean);
                startActivityForResult(localIntent, REFRESH_RECYCLERVIEW);
            }
        });
    }

    protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
        super.onActivityResult(paramInt1, paramInt2, paramIntent);
        if (paramInt1 == REFRESH_RECYCLERVIEW) {
            TransformerBean localTransformerBean = new TransformerBean();
            this.presenter.searchTransformer(this, localTransformerBean);
        }
    }

    public boolean onCreateOptionsMenu(Menu paramMenu) {
        getMenuInflater().inflate(R.menu.menu_main, paramMenu);
        return true;
    }


    public boolean onMenuItemClick(MenuItem paramMenuItem) {
        switch (paramMenuItem.getItemId()) {
            case R.id.action_save:
                startActivityForResult(new Intent(this, CreateTransformerAcvitity.class), REFRESH_RECYCLERVIEW);
                break;
        }
        return true;
    }

    public void searchTransformer(List<TransformerBean> paramList) {
        this.beans = paramList;
        this.adapter.setNewData(this.beans);
    }
}
