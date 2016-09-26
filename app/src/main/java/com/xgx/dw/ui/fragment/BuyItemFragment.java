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

import butterknife.Bind;

import com.xgx.dw.R;
import com.xgx.dw.adapter.DataSearchItemAdapter;
import com.xgx.dw.base.BaseFragment;
import com.xgx.dw.ui.activity.SpecialOperationDetailActivity;
import com.xgx.dw.ui.custom.TitleBar;
import com.xgx.dw.ui.fragment.dummy.DummyContent;

import java.util.ArrayList;
import java.util.List;

public class BuyItemFragment extends BaseFragment implements DataSearchItemAdapter.MyOnItemClickListner {
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
        this.titleBar.setTitle(getResources().getString(R.string.tab_buy));
        final GridLayoutManager localGridLayoutManager = new GridLayoutManager(getActivity(), 3);
        this.recyclerView.setLayoutManager(localGridLayoutManager);
        View localView = LayoutInflater.from(getActivity()).inflate(R.layout.header, this.recyclerView, false);
        this.headLogo = ((ImageView) localView.findViewById(R.id.head_logo));
        this.headLogo.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.main_tab_home_banner_1));
        localView.setOnClickListener(new OnClickListener() {
            public void onClick(View paramAnonymousView) {
                Toast.makeText(paramAnonymousView.getContext(), "测试", Toast.LENGTH_SHORT).show();
            }
        });
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new DummyContent(0, "用户购电", "用户购电", this.drawableInt[0]));
        localArrayList.add(new DummyContent(1, "自助购电", "自助购电", this.drawableInt[1]));
        localArrayList.add(new DummyContent(2, "购电录入", "购电录入", this.drawableInt[2]));
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

    @Override
    public void onrRecyclerViewItemClick(int paramInt) {
        switch (paramInt) {
            case 2:
                startActivity(new Intent(getActivity(), SpecialOperationDetailActivity.class).putExtra("type", 6));
                break;
        }
    }
}

