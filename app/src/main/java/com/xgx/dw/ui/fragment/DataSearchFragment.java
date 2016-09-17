package com.xgx.dw.ui.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.GridLayoutManager.SpanSizeLookup;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import butterknife.Bind;

import com.xgx.dw.R;
import com.xgx.dw.adapter.DataSearchItemAdapter;
import com.xgx.dw.adapter.DataSearchItemAdapter.MyOnItemClickListner;
import com.xgx.dw.base.BaseFragment;
import com.xgx.dw.ui.activity.SpecialOperationDetailActivity;
import com.xgx.dw.ui.custom.TitleBar;
import com.xgx.dw.ui.fragment.dummy.DummyContent;

import java.util.ArrayList;
import java.util.List;

public class DataSearchFragment extends BaseFragment implements MyOnItemClickListner {
    private DataSearchItemAdapter adapter;
    private int[] drawableInt = {R.drawable.home_paylists_big, R.drawable.home_useelesafe_unrule_big, R.drawable.home_set_serviceauthorize, R.drawable.home_elecri_big, R.drawable.home_paylists_big, R.drawable.home_elecri_big, R.drawable.home_analysis};
    private ImageView headLogo;
    @Bind({R.id.list})
    RecyclerView recyclerView;
    @Bind({R.id.title_bar})
    TitleBar titleBar;

    public int getLayoutRes() {
        return R.layout.fragment_recyclerview;
    }

    public void initView() {
        this.titleBar.setTitle(getResources().getString(R.string.tab_search));
        ImageView localImageView = new ImageView(getActivity());
        localImageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.icon_setting));
        this.titleBar.setRightView(localImageView);
        final GridLayoutManager localGridLayoutManager = new GridLayoutManager(getActivity(), 3);
        this.recyclerView.setLayoutManager(localGridLayoutManager);
        View localView = LayoutInflater.from(getActivity()).inflate(R.layout.header, this.recyclerView, false);
        this.headLogo = ((ImageView) localView.findViewById(R.id.head_logo));
        this.headLogo.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.main_tab_home_banner_4));
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new DummyContent("0", "购电记录", "购电记录", this.drawableInt[0]));
        localArrayList.add(new DummyContent("1", "电费余额", "电费余额", this.drawableInt[1]));
        localArrayList.add(new DummyContent("2", "电量查询", "电量查询", this.drawableInt[2]));
        localArrayList.add(new DummyContent("3", "功率查询", "功率查询", this.drawableInt[3]));
        localArrayList.add(new DummyContent("4", "电量报表", "电量报表", this.drawableInt[4]));
        localArrayList.add(new DummyContent("5", "功率报表", "功率报表", this.drawableInt[5]));
        localArrayList.add(new DummyContent("6", "倍率查询", "倍率查询", this.drawableInt[5]));
        localArrayList.add(new DummyContent("7", "电价查询", "电价查询", this.drawableInt[5]));
        localArrayList.add(new DummyContent("8", "个人资料", "个人资料", this.drawableInt[5]));
        this.adapter = new DataSearchItemAdapter(localView, localArrayList);
        this.recyclerView.setAdapter(this.adapter);
        this.adapter.setOnItemClickListner(this);
        localGridLayoutManager.setSpanSizeLookup(new SpanSizeLookup() {
            public int getSpanSize(int paramAnonymousInt) {
                if (DataSearchFragment.this.adapter.isHeader(paramAnonymousInt)) {
                    return localGridLayoutManager.getSpanCount();
                }
                return 1;
            }
        });
    }

    public void onrRecyclerViewItemClick(int paramInt) {
        switch (paramInt) {
            case 1:
                startActivity(new Intent(getActivity(), SpecialOperationDetailActivity.class).putExtra("type", 41));
                break;
            case 2:
                startActivity(new Intent(getActivity(), SpecialOperationDetailActivity.class).putExtra("type", 42));
                break;
            case 3:
                startActivity(new Intent(getActivity(), SpecialOperationDetailActivity.class).putExtra("type", 43));
                break;
            case 6:
                startActivity(new Intent(getActivity(), SpecialOperationDetailActivity.class).putExtra("type", 47));
                break;
            case 7:
                startActivity(new Intent(getActivity(), SpecialOperationDetailActivity.class).putExtra("type", 48));
                break;
        }
    }
}
