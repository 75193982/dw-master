package com.xgx.dw.ui.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.xgx.dw.R;
import com.xgx.dw.adapter.DeviceListAdapter;
import com.xgx.dw.base.BaseAppCompatActivity;
import com.xgx.dw.bean.DeviceInfo;
import com.xgx.dw.bean.MySection;

import java.util.ArrayList;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DeviceListNewActivity extends BaseAppCompatActivity {


    @BindView(R.id.deviceRecyclerView)
    RecyclerView deviceRecyclerView;
    @BindView(R.id.button_scan)
    LinearLayout buttonScan;
    @BindView(R.id.scanTv)
    TextView scanTv;
    private DeviceListAdapter deviceListAdapter;
    ArrayList<MySection> mySectionList;
    private ArrayList<MySection> myBondedList;
    private ArrayList<MySection> myFoundList;

    public void initContentView() {
        baseSetContentView(R.layout.activity_device_list);
    }

    // 返回时数据标签
    public static String EXTRA_DEVICE_ADDRESS = "设备地址";
    private BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解除注册
        unregisterReceiver(mReceiver);
    }

    public void initPresenter() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // 获取所有已经绑定的蓝牙设备
        Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
        mySectionList = new ArrayList<>();
        myBondedList = new ArrayList<>();
        myFoundList = new ArrayList<>();
        deviceListAdapter = new DeviceListAdapter(R.layout.item_content, R.layout.group_title, mySectionList);
        deviceRecyclerView.setHasFixedSize(true);
        deviceRecyclerView.setLayoutManager(new LinearLayoutManager(this, 1, false));
        deviceRecyclerView.setAdapter(deviceListAdapter);
        if (devices.size() > 0) {
            for (BluetoothDevice bluetoothDevice : devices) {
                DeviceInfo info = new DeviceInfo();
                info.setName(bluetoothDevice.getName());
                info.setAddress(bluetoothDevice.getAddress());
                myBondedList.add(new MySection(info));
            }
        }
        mySectionList.clear();
        mySectionList.add(new MySection(true, "已配对设备", false));
        mySectionList.addAll(myBondedList);
        mySectionList.add(new MySection(true, "可用设备", false));
        mySectionList.addAll(myFoundList);
        deviceListAdapter.setNewData(mySectionList);
        // 注册用以接收到已搜索到的蓝牙设备的receiver
        IntentFilter mFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, mFilter);
        // 注册搜索完时的receiver
        mFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, mFilter);
        deviceRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                mBluetoothAdapter.cancelDiscovery();
                try {
                    // 得到mac地址
                    DeviceInfo info = deviceListAdapter.getItem(i).t;
                    // 设置返回数据
                    Intent intent = new Intent();
                    intent.putExtra(EXTRA_DEVICE_ADDRESS, info.getAddress());

                    // 设置返回值并结束程序
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } catch (Exception e) {

                }
            }
        });
    }

    public void initView() {
        getSupportActionBar().setTitle("搜索设备");
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            // 获得已经搜索到的蓝牙设备
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // 搜索到的不是已经绑定的蓝牙设备
                // if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                try {
                    DeviceInfo info = new DeviceInfo();
                    info.setName(device.getName());
                    info.setAddress(device.getAddress());
                    myFoundList.add(new MySection(info));
                } catch (Exception e) {

                }

                //}
                // 搜索完成
            } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                setProgressBarIndeterminateVisibility(false);
                getSupportActionBar().setTitle("完成搜索");
                mySectionList.clear();
                mySectionList.add(new MySection(true, "已配对设备", false));
                mySectionList.addAll(myBondedList);
                mySectionList.add(new MySection(true, "可用设备", false));
                mySectionList.addAll(myFoundList);
                deviceListAdapter.setNewData(mySectionList);
                buttonScan.setEnabled(true);

            }
        }
    };


    @OnClick(R.id.button_scan)
    public void onClick() {
        setProgressBarIndeterminateVisibility(true);
        getSupportActionBar().setTitle("正在扫描....");
        buttonScan.setEnabled(false);
        // 如果正在搜索，就先取消搜索
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        // 开始搜索蓝牙设备,搜索到的蓝牙设备通过广播返回
        mBluetoothAdapter.startDiscovery();
    }
}
