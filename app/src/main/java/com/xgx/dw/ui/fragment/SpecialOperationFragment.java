package com.xgx.dw.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.GridLayoutManager.SpanSizeLookup;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.Bind;

import com.squareup.haha.perflib.Main;
import com.xgx.dw.R;
import com.xgx.dw.adapter.DataSearchItemAdapter;
import com.xgx.dw.adapter.DataSearchItemAdapter.MyOnItemClickListner;
import com.xgx.dw.app.G;
import com.xgx.dw.app.Setting;
import com.xgx.dw.base.BaseFragment;
import com.xgx.dw.bean.LoginInformation;
import com.xgx.dw.ui.activity.DeviceListActivity;
import com.xgx.dw.ui.activity.MainActivity;
import com.xgx.dw.ui.activity.SpecialOperationDetailActivity;
import com.xgx.dw.ui.custom.TitleBar;
import com.xgx.dw.ui.fragment.dummy.DummyContent;
import com.xgx.dw.utils.MyUtils;
import com.xgx.dw.wifi.WifiActivity;

import java.util.ArrayList;

public class SpecialOperationFragment extends BaseFragment implements MyOnItemClickListner, MainActivity.OnFABClickListener {
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
        ((MainActivity) getBaseActivity()).setOnItemClickListener(this);
        this.titleBar.setTitle(getResources().getString(R.string.tab_action));
        final GridLayoutManager localGridLayoutManager = new GridLayoutManager(getActivity(), 3);
        this.recyclerView.setLayoutManager(localGridLayoutManager);
        View localView = LayoutInflater.from(getActivity()).inflate(R.layout.header, this.recyclerView, false);
        this.headLogo = ((ImageView) localView.findViewById(R.id.head_logo));
        this.headLogo.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.main_tab_home_banner_3));
        localView.setOnClickListener(new OnClickListener() {
            public void onClick(View paramAnonymousView) {
                Toast.makeText(paramAnonymousView.getContext(), "测试", Toast.LENGTH_LONG).show();
            }
        });
        ArrayList localArrayList = new ArrayList();
        Setting setting = new Setting(getContext());
        String currentUserType = LoginInformation.getInstance().getUser().getType();
        if (currentUserType.equals("20")) {
            localArrayList.add(new DummyContent(0, "合闸", "合闸", this.drawableInt[0]));
            //  localArrayList.add(new DummyContent(1, "分闸", "分闸", this.drawableInt[1]));
            // localArrayList.add(new DummyContent(6, "电费录入", "电费录入", this.drawableInt[6]));
        } else if (currentUserType.equals("30")) {
            localArrayList.add(new DummyContent(0, "合闸", "合闸", this.drawableInt[0]));
            localArrayList.add(new DummyContent(1, "分闸", "分闸", this.drawableInt[1]));
            localArrayList.add(new DummyContent(3, "保电解除", "保电解除", this.drawableInt[3]));

        } else if (currentUserType.equals("31")) {
            localArrayList.add(new DummyContent(0, "合闸", "合闸", this.drawableInt[0]));
            localArrayList.add(new DummyContent(1, "分闸", "分闸", this.drawableInt[1]));
            localArrayList.add(new DummyContent(3, "保电解除", "保电解除", this.drawableInt[3]));

        } else if (currentUserType.equals("32")) {
            localArrayList.add(new DummyContent(0, "合闸", "合闸", this.drawableInt[0]));
            localArrayList.add(new DummyContent(1, "分闸", "分闸", this.drawableInt[1]));
            localArrayList.add(new DummyContent(2, "保电投入", "保电投入", this.drawableInt[2]));
            localArrayList.add(new DummyContent(3, "保电解除", "保电解除", this.drawableInt[3]));

        } else {
            if ("10,11".contains(currentUserType)) {
                if (!LoginInformation.getInstance().getUser().getIsTest().equals("1")) {
                    //你当前没有权限扫描购电信息
                    return;
                } else {
                    localArrayList.add(new DummyContent(0, "合闸", "合闸", this.drawableInt[0]));
                    localArrayList.add(new DummyContent(1, "分闸", "分闸", this.drawableInt[1]));
                    localArrayList.add(new DummyContent(2, "保电投入", "保电投入", this.drawableInt[2]));
                    localArrayList.add(new DummyContent(3, "保电解除", "保电解除", this.drawableInt[3]));
                    localArrayList.add(new DummyContent(4, "倍率录入", "倍率录入", this.drawableInt[6]));
                    // localArrayList.add(new DummyContent("6", "电表地址", "电表地址", this.drawableInt[6]));
                    localArrayList.add(new DummyContent(5, "电价录入", "电价录入", this.drawableInt[6]));
                    localArrayList.add(new DummyContent(6, "电费录入", "电费录入", this.drawableInt[6]));
                    localArrayList.add(new DummyContent(7, "定值设置", "定值设置", this.drawableInt[6]));
                }
            } else {
                localArrayList.add(new DummyContent(0, "合闸", "合闸", this.drawableInt[0]));
                localArrayList.add(new DummyContent(1, "分闸", "分闸", this.drawableInt[1]));
                localArrayList.add(new DummyContent(2, "保电投入", "保电投入", this.drawableInt[2]));
                localArrayList.add(new DummyContent(3, "保电解除", "保电解除", this.drawableInt[3]));
                localArrayList.add(new DummyContent(4, "倍率录入", "倍率录入", this.drawableInt[6]));
                // localArrayList.add(new DummyContent("6", "电表地址", "电表地址", this.drawableInt[6]));
                localArrayList.add(new DummyContent(5, "电价录入", "电价录入", this.drawableInt[6]));
                localArrayList.add(new DummyContent(6, "电费录入", "电费录入", this.drawableInt[6]));
                localArrayList.add(new DummyContent(7, "定值设置", "定值设置", this.drawableInt[6]));
            }


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
        if (paramInt != 7) {
            startActivity(new Intent(getActivity(), SpecialOperationDetailActivity.class).putExtra("type", paramInt));

        } else {
            showToast("敬请期待");
        }
        // startActivity(new Intent(getActivity(), .class));
    }

    @Override
    public void OnFABClickListener(View paramView) {
        final Setting setting = new Setting(getContext());
        boolean isWifi = setting.loadBoolean("isWifi");
        if (isWifi) {
            Intent intent = new Intent(getBaseActivity(), WifiActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getBaseActivity(), DeviceListActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void OnFABLongClickListener(View paramView) {
        final Setting setting = new Setting(getContext());
        boolean isWifi = setting.loadBoolean("isWifi");
        if (isWifi) {
            AlertDialog.Builder alertdialog = new AlertDialog.Builder(getContext());
            alertdialog.setTitle("请注意");
            alertdialog.setMessage("您是否要切换成蓝牙连接模式");
            alertdialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    ((MainActivity) getActivity()).getFab().setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_bluetooth_searching_black_24dp));
                    setting.saveBoolean("isWifi", false);
                }
            });
            alertdialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertdialog.show();
        } else {
            AlertDialog.Builder alertdialog = new AlertDialog.Builder(getContext());
            alertdialog.setTitle("请注意");
            alertdialog.setMessage("您是否要切换成wifi连接模式");
            alertdialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    ((MainActivity) getActivity()).getFab().setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_signal_wifi_4_bar_black_24dp));
                    setting.saveBoolean("isWifi", true);
                }
            });
            alertdialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertdialog.show();
        }

    }
}


