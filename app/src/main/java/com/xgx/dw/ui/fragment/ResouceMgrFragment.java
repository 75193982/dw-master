package com.xgx.dw.ui.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.GridLayoutManager.SpanSizeLookup;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.BindView;

import com.xgx.dw.R;
import com.xgx.dw.adapter.DataSearchItemAdapter;
import com.xgx.dw.adapter.DataSearchItemAdapter.MyOnItemClickListner;
import com.xgx.dw.app.G;
import com.xgx.dw.app.Setting;
import com.xgx.dw.base.BaseFragment;
import com.xgx.dw.ui.activity.CreateUserOneAcvitity;
import com.xgx.dw.ui.activity.SpotPricingActivity;
import com.xgx.dw.ui.activity.StoresMgrActivity;
import com.xgx.dw.ui.activity.TransformerActivity;
import com.xgx.dw.ui.activity.UserMgrActivity;
import com.xgx.dw.ui.custom.TitleBar;
import com.xgx.dw.ui.fragment.dummy.DummyContent;

import java.util.ArrayList;
import java.util.List;

public class ResouceMgrFragment extends BaseFragment implements MyOnItemClickListner {
    private int[] drawableInt = {R.drawable.home_paylists_big, R.drawable.home_useelesafe_unrule_big, R.drawable.home_set_serviceauthorize, R.drawable.home_elecri_big, R.drawable.home_paylists_big, R.drawable.home_elecri_big, R.drawable.home_analysis};
    private ImageView headLogo;
    @BindView({R.id.list})
    RecyclerView recyclerView;
    @BindView({R.id.title_bar})
    TitleBar titleBar;

    public int getLayoutRes() {
        return R.layout.fragment_recyclerview;
    }

    public void initView() {
        this.titleBar.setTitle(getResources().getString(R.string.tab_me));
        final GridLayoutManager localGridLayoutManager = new GridLayoutManager(getActivity(), 3);
        this.recyclerView.setLayoutManager(localGridLayoutManager);
        View localView = LayoutInflater.from(getActivity()).inflate(R.layout.header, this.recyclerView, false);
        this.headLogo = ((ImageView) localView.findViewById(R.id.head_logo));
        this.headLogo.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.main_tab_home_banner_2));
        localView.setOnClickListener(new OnClickListener() {
            public void onClick(View paramAnonymousView) {
                Toast.makeText(paramAnonymousView.getContext(), "测试", Toast.LENGTH_SHORT).show();
            }
        });
        ArrayList localArrayList = new ArrayList();
        Setting setting = new Setting(getContext());
        String currentUserType = setting.loadString(G.currentUserType);
        if (currentUserType.equals("10")) {
            localArrayList.add(new DummyContent(1, "台区管理", "台区管理", this.drawableInt[1]));
            localArrayList.add(new DummyContent(2, "电价管理", "电价管理", this.drawableInt[2]));
            localArrayList.add(new DummyContent(3, "用户资料", "用户资料", this.drawableInt[3]));
        } else if (currentUserType.equals("11")) {
            localArrayList.add(new DummyContent(2, "电价管理", "电价管理", this.drawableInt[2]));
            localArrayList.add(new DummyContent(3, "用户资料", "用户资料", this.drawableInt[3]));
        } else {
            localArrayList.add(new DummyContent(0, "营业厅管理", "营业厅管理", this.drawableInt[0]));
            localArrayList.add(new DummyContent(1, "台区管理", "台区管理", this.drawableInt[1]));
            localArrayList.add(new DummyContent(2, "电价管理", "电价管理", this.drawableInt[2]));
            localArrayList.add(new DummyContent(3, "用户资料", "用户资料", this.drawableInt[3]));
        }
        final DataSearchItemAdapter localDataSearchItemAdapter = new DataSearchItemAdapter(localView, localArrayList);
        this.recyclerView.setAdapter(localDataSearchItemAdapter);
        localDataSearchItemAdapter.setOnItemClickListner(this);
        localGridLayoutManager.setSpanSizeLookup(new SpanSizeLookup() {
            public int getSpanSize(int paramAnonymousInt) {
                if (localDataSearchItemAdapter.isHeader(paramAnonymousInt)) {
                    return localGridLayoutManager.getSpanCount();
                }
                return 1;
            }
        });
    }

    public void onrRecyclerViewItemClick(int paramInt) {
        switch (paramInt) {

            case 0:
                startActivity(new Intent(getActivity(), StoresMgrActivity.class));
                return;
            case 1:
                startActivity(new Intent(getActivity(), TransformerActivity.class));
                return;
            case 2:
                startActivity(new Intent(getActivity(), SpotPricingActivity.class));
                return;
            case 3:
                startActivity(new Intent(getActivity(), UserMgrActivity.class));
                return;
            default:
                return;
        }
    }
}

