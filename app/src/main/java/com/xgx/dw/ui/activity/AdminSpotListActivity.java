package com.xgx.dw.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.xgx.dw.PricingBean;
import com.xgx.dw.R;
import com.xgx.dw.adapter.SpotListAdapter;
import com.xgx.dw.app.G;
import com.xgx.dw.base.BaseAppCompatActivity;
import com.xgx.dw.bean.LoginInformation;
import com.xgx.dw.dao.PricingDaoHelper;
import com.xgx.dw.utils.AES;
import com.xgx.dw.utils.MyStringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/10/16 0016.
 */
public class AdminSpotListActivity extends BaseAppCompatActivity {
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    SpotListAdapter adapter;
    private List<PricingBean> beans;
    @Bind(R.id.numTv)
    TextView numTv;

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
                startActivity(new Intent(getContext(), TestGeneratectivity.class).putExtra("type", 5).putExtra("id", adapter.getItem(i).getUserPrimaryid()));
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        String userid = getIntent().getStringExtra("id");
        beans = PricingDaoHelper.getInstance().queryByUserId(userid);
        if (beans != null && beans.size() > 0) {
            int num = 0;
            for (int i = 0; i < beans.size(); i++) {
                String price = "";
                try {
                    price = AES.decrypt(G.appsecret, beans.get(i).getPrice());
                } catch (Exception e) {
                    price = "";
                }
                num += MyStringUtils.toInt(price, 0);
            }
            numTv.setText(num + "元");
        }
        adapter.setNewData(beans);
    }

    @Override
    public void initPresenter() {
        //查询电价
    }

}
