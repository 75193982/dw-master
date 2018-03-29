package com.xgx.dw.ui.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.xgx.dw.R;
import com.xgx.dw.TransformerBean;
import com.xgx.dw.adapter.TransformerAdapter;
import com.xgx.dw.base.BaseAppCompatActivity;
import com.xgx.dw.base.BaseEventBusActivity;
import com.xgx.dw.base.EventCenter;
import com.xgx.dw.bean.Taiqu;
import com.xgx.dw.presenter.impl.TransformerPresenterImpl;
import com.xgx.dw.presenter.interfaces.ITransformerPresenter;
import com.xgx.dw.ui.view.interfaces.ITransformerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TransformerActivity extends BaseEventBusActivity implements ITransformerView, Toolbar.OnMenuItemClickListener {
    private static int REFRESH_RECYCLERVIEW = 0;
    private TransformerAdapter adapter;
    private List<Taiqu> beans;
    private ITransformerPresenter presenter;
    @BindView(R.id.list)
    RecyclerView recyclerView;
    @BindView(R.id.numTv)
    TextView numTv;
    @BindView(R.id.query)
    EditText queryEt;

    @Override
    public void initContentView() {
        baseSetContentView(R.layout.activity_transformer);
    }

    @Override
    public void initPresenter() {
        this.presenter = new TransformerPresenterImpl();
        setDatas();
    }

    @Override
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
        queryEt.setHint("根据台区名称搜索");
        queryEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                setDatas();
            }
        });
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent localIntent = new Intent(getContext(), CreateTransformerAcvitity.class);
                localIntent.putExtra("bean", (Serializable) adapter.getItem(i));
                startActivity(localIntent);
            }
        });
    }

    @Override
    protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
        super.onActivityResult(paramInt1, paramInt2, paramIntent);
    }

    private void setDatas() {
        Taiqu taiqu = new Taiqu();
        taiqu.setName(queryEt.getText().toString());
        this.presenter.searchTransformer(this, taiqu);
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
                startActivityForResult(new Intent(this, CreateTransformerAcvitity.class), REFRESH_RECYCLERVIEW);
                break;
        }
        return true;
    }

    @Override
    public void searchTransformer(List<Taiqu> paramList) {
        this.beans = paramList;
        numTv.setText("共搜索到" + paramList.size() + "个台区");
        this.adapter.setNewData(this.beans);
    }

    @Override
    protected void onEventComming(EventCenter eventCenter) {
        if (EventCenter.TAIQU_SAVE == eventCenter.getEventCode()) {
            setDatas();
        }
    }

    @Override
    public boolean isBindEventBusHere() {
        return true;
    }
}
