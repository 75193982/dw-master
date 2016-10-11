package com.xgx.dw.ble;

/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.clj.fastble.BleManager;
import com.clj.fastble.bluetooth.BleGattCallback;
import com.clj.fastble.conn.BleCharacterCallback;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.ListScanCallback;
import com.clj.fastble.utils.HexUtil;
import com.xgx.dw.R;
import com.xgx.dw.app.BaseApplication;
import com.xgx.dw.app.G;
import com.xgx.dw.base.BaseAppCompatActivity;
import com.xgx.dw.utils.MyUtils;

import java.util.Arrays;


public class ScanDeveiceList extends BaseAppCompatActivity {
    // 调试用
    private static final String TAG = "DeviceListActivity";
    private static final boolean D = true;

    // 返回时数据标签
    public static String EXTRA_DEVICE_ADDRESS = "设备地址";

    // 成员域
    private ArrayAdapter<String> mNewDevicesArrayAdapter;
    private BleManager bleManager;


    @Override
    public void initContentView() {
        baseSetContentView(R.layout.device_list);
    }

    @Override
    public void initView() {
        // 设定默认返回值为取消
        bleManager = BleManager.getInstance();
        bleManager.init(getContext());
        setToolbarTitle("搜索设备");   // 设定扫描按键响应
        Button scanButton = (Button) findViewById(R.id.button_scan);
        scanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (bleManager.isInScanning())
                    return;

                showProgress();

                bleManager.scanDevice(new ListScanCallback(10000) {
                    @Override
                    public void onDeviceFound(final BluetoothDevice[] devices) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.i(TAG, "共发现" + devices.length + "台设备");
                                for (int i = 0; i < devices.length; i++) {
                                    Log.i(TAG, "name:" + devices[i].getName() + "------mac:" + devices[i].getAddress());
                                    mNewDevicesArrayAdapter.add(devices[i].getName());

                                }
                            }
                        });
                    }

                    @Override
                    public void onScanTimeout() {
                        super.onScanTimeout();
                        hideProgress();
                    }
                });

                v.setVisibility(View.GONE);
            }
        });

        // 初使化设备存储数组
        mNewDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);


        // 设置新查找设备列表
        ListView newDevicesListView = (ListView) findViewById(R.id.new_devices);
        newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
        newDevicesListView.setOnItemClickListener(mDeviceClickListener);

    }

    @Override
    public void initPresenter() {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void OnCancel(View v) {
        finish();
    }


    // 选择设备响应函数
    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            // 准备连接设备，关闭服务查找
            try {
                // 得到mac地址
                String info = ((TextView) v).getText().toString();
                //  String address = info.substring(info.length() - 17);
                connectNameDevice(info);
//                // 设置返回数据
//                Intent intent = new Intent();
//                intent.putExtra(EXTRA_DEVICE_ADDRESS, address);
//
//                // 设置返回值并结束程序
//                setResult(Activity.RESULT_OK, intent);
//                finish();
            } catch (Exception e) {

            }

        }
    };


    /**
     * 连接设备
     */
    private void connectSpecialDevice(final BluetoothDevice device) {
        showProgress();
        bleManager.connectDevice(device, new BleGattCallback() {
            @Override
            public void onConnectSuccess(BluetoothGatt gatt, int status) {
                gatt.discoverServices();
            }

            @Override
            public void onServicesDiscovered(final BluetoothGatt gatt, int status) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideProgress();
                        // showConnectState(device.getName(), gatt);
                    }
                });
            }

            @Override
            public void onConnectFailure(BleException exception) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideProgress();
                        showDisConnectState();
                    }
                });
                bleManager.handleException(exception);
            }
        });
    }

    /**
     * 直连某一蓝牙设备
     */
    private void connectNameDevice(final String deviceName) {
        showProgress();
        bleManager.connectDevice(deviceName, 10000, new BleGattCallback() {
            @Override
            public void onConnectSuccess(BluetoothGatt gatt, int status) {
                gatt.discoverServices();
            }

            @Override
            public void onServicesDiscovered(final BluetoothGatt gatt, int status) {
                bleManager.getBluetoothState();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideProgress();
                        showConnectState(deviceName, gatt);
                        BaseApplication.getSetting().saveString(BaseApplication.getSetting().loadString(G.currentUsername) + "_bleAddress", deviceName);
                        Toast.makeText(getContext(), "链接成功", Toast.LENGTH_SHORT).show();
                        //  showConnectState(deviceName, gatt);
                    }
                });
            }

            @Override
            public void onConnectFailure(BleException exception) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideProgress();
                        showDisConnectState();
                    }
                });
                bleManager.handleException(exception);
            }
        });
    }

    /**
     * 显示未连接状态
     */
    private void showDisConnectState() {
        bleManager.closeBluetoothGatt();
        Toast.makeText(getContext(), "链接失败", Toast.LENGTH_SHORT).show();

        //layout_item_connect.setVisibility(View.VISIBLE);
        //layout_item_state.setVisibility(View.GONE);

        //layout_device_list.removeAllViews();
    }

    /**
     * 显示连接状态
     */
    private void showConnectState(String deviceName, BluetoothGatt gatt) {
        bleManager.getBluetoothState();


        if (gatt != null) {
            for (final BluetoothGattService service : gatt.getServices()) {

                for (final BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
                    switch (characteristic.getProperties()) {
                        case 2:
                            break;

                        case 8:
                            String writeData = "68 8A 00 8A 00 68 6A 00 00 FF FF 21 05 EC 00 00 01 04 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 40 37 17 04 05 23 16";
                            startWrite(service.getUuid().toString(), characteristic.getUuid().toString(), writeData.replace(" ", ""));

                            break;

                        case 16:
                            startNotify(service.getUuid().toString(), characteristic.getUuid().toString());
                            break;

                        case 32:
                            break;
                    }

                }
            }


        }


    }

    private void startNotify(String serviceUUID, final String characterUUID) {
        Log.i(TAG, "startNotify");
        boolean suc = bleManager.notifyDevice(
                serviceUUID,
                characterUUID,
                new BleCharacterCallback() {
                    @Override
                    public void onSuccess(final BluetoothGattCharacteristic characteristic) {
                        Log.d(TAG, "notify success： " + '\n' + Arrays.toString(characteristic.getValue()));

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showToast(Arrays.toString(characteristic.getValue()));
                            }
                        });
                    }

                    @Override
                    public void onFailure(BleException exception) {
                        bleManager.handleException(exception);
                    }
                });

        if (suc) {
            showToast("监听通知成功");
        }
    }

    private void startWrite(String serviceUUID, final String characterUUID, String writeData) {
        Log.i(TAG, "startWrite");
        boolean suc = bleManager.writeDevice(
                serviceUUID,
                characterUUID,
                HexUtil.hexStringToBytes(writeData),
                new BleCharacterCallback() {
                    @Override
                    public void onSuccess(final BluetoothGattCharacteristic characteristic) {
                        Log.d(TAG, "write success: " + '\n' + Arrays.toString(characteristic.getValue()));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showToast(Arrays.toString(characteristic.getValue()));
                            }
                        });
                    }

                    @Override
                    public void onFailure(BleException exception) {
                        bleManager.handleException(exception);
                        showToast("错误：" + exception.getDescription());

                    }
                });

        if (suc) {
            showToast("监听写入成功");

        }
    }
}
