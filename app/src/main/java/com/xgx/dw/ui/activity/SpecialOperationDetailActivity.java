package com.xgx.dw.ui.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.xgx.dw.R;
import com.xgx.dw.app.BaseApplication;
import com.xgx.dw.base.BaseAppCompatActivity;
import com.xgx.dw.ble.BlueOperationContact;
import com.xgx.dw.ui.activity.DeviceListActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//import android.view.Menu;            //如使用菜单加入此三包
//import android.view.MenuInflater;
//import android.view.MenuItem;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SpecialOperationDetailActivity extends BaseAppCompatActivity {

    private final static int REQUEST_CONNECT_DEVICE = 1;    //宏定义查询设备句柄

    private final static String MY_UUID = "00001101-0000-1000-8000-00805F9B34FB";   //SPP服务UUID号
    @Bind(R.id.sendTitle)
    TextView sendTitle;
    @Bind(R.id.sendTv)
    TextView sendTv;
    @Bind(R.id.resultTitle)
    TextView resultTitle;
    @Bind(R.id.resultTv)
    TextView resultTv;
    @Bind(R.id.action_save)
    RippleView actionSave;
    @Bind(R.id.btnTv)
    TextView btnTv;

    private InputStream is;    //输入流，用来接收蓝牙数据


    public String filename = ""; //用来保存存储的文件名
    BluetoothDevice _device = null;     //蓝牙设备
    BluetoothSocket _socket = null;      //蓝牙通信socket
    boolean _discoveryFinished = false;
    boolean bRun = true;
    boolean bThread = false;

    private BluetoothAdapter _bluetooth = BluetoothAdapter.getDefaultAdapter();    //获取本地蓝牙适配器，即蓝牙设备
    private int title;
    private String OperationStr = "";

    @Override
    public void initContentView() {
        baseSetContentView(R.layout.activity_special_operation_detail);
    }

    @Override
    public void initView() {
        if (_bluetooth == null) {
            Toast.makeText(this, "无法打开手机蓝牙，请确认手机是否有蓝牙功能！", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // 设置设备可以被搜索
        new Thread() {
            public void run() {
                if (_bluetooth.isEnabled() == false) {
                    _bluetooth.enable();
                }
            }
        }.start();
        getFab().setVisibility(View.VISIBLE);
        getFab().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onConnectButtonClicked(v);
            }
        });
        mBuffer = new ArrayList<Integer>();
    }

    @Override
    public void initPresenter() {
        //查询是否有配对成功的设备，如果配对成功则自动连接
        if (!TextUtils.isEmpty(BaseApplication.getSetting().loadString("bleAddress"))) {
            connect(BaseApplication.getSetting().loadString("bleAddress"));
        }
        title = getIntent().getIntExtra("type", -1);
        switch (title) {
            case 0:
                setToolbarTitle("合闸");
                OperationStr = BlueOperationContact.HeZaSend;

                break;
            case 1:
                setToolbarTitle("分闸");
                OperationStr = BlueOperationContact.TiaoZaSend;
                break;
            case 2:
                setToolbarTitle("保电投入");
                OperationStr = BlueOperationContact.BaoDianTrSend;

                break;
            case 3:
                setToolbarTitle("保电解除");
                OperationStr = BlueOperationContact.BaoDianJcSend;

                break;
            case 4:
                setToolbarTitle("报警解除");
                OperationStr = BlueOperationContact.HeZaSend;

                break;
            case 5:
                setToolbarTitle("倍率录入");
                OperationStr = BlueOperationContact.HeZaSend;

                break;
            case 6:
                setToolbarTitle("电表地址");
                OperationStr = BlueOperationContact.HeZaSend;

                break;
            case 7:
                setToolbarTitle("电价录入");
                OperationStr = BlueOperationContact.HeZaSend;
                break;
            case 42:
                setToolbarTitle("电量查询");
                OperationStr = BlueOperationContact.DianLiangCxSend;
                break;
            case 43:
                setToolbarTitle("功率查询");
                OperationStr = BlueOperationContact.DianLvCxSend;
                break;
        }
        btnTv.setText(getToolbarTitle());
        resultTitle.setText(getToolbarTitle() + "结果");
        sendTitle.setText(getToolbarTitle() + "发送参数");
        sendTv.setText(OperationStr);
    }


    //接收活动结果，响应startActivityForResult()
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:     //连接结果，由DeviceListActivity设置返回
                // 响应返回结果
                if (resultCode == Activity.RESULT_OK) {   //连接成功，由DeviceListActivity设置返回
                    // MAC地址，由DeviceListActivity设置返回
                    String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    // 得到蓝牙设备句柄
                    connect(address);
                }
                break;
            default:
                break;
        }
    }

    private void connect(String address) {
        _device = _bluetooth.getRemoteDevice(address);

        // 用服务号得到socket
        try {
            _socket = _device.createRfcommSocketToServiceRecord(UUID.fromString(MY_UUID));
        } catch (IOException e) {
            Toast.makeText(this, "连接失败！", Toast.LENGTH_SHORT).show();
        }
        //连接socket
        try {
            _socket.connect();
            BaseApplication.getSetting().saveString("bleAddress", address);
            Toast.makeText(this, "连接" + _device.getName() + "成功！", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            try {
                Toast.makeText(this, "连接失败！", Toast.LENGTH_SHORT).show();
                _socket.close();
                _socket = null;
            } catch (IOException ee) {
                Toast.makeText(this, "连接失败！", Toast.LENGTH_SHORT).show();
            }

            return;
        }

        //打开接收线程
        try {
            is = _socket.getInputStream();   //得到蓝牙数据输入流
        } catch (IOException e) {
            Toast.makeText(this, "接收数据失败！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (bThread == false) {
            ReadThread.start();
            bThread = true;
        } else {
            bRun = true;
        }
    }

    //接收数据线程
    Thread ReadThread = new Thread() {

        public void run() {
            byte[] buffer = new byte[256];
            int bytes;
            bRun = true;
            //接收线程
            while (true) {
                try {
                    while (is.available() == 0) {
                        while (bRun == false) {
                        }
                    }
                    while (true) {

                        bytes = is.read(buffer);
                        synchronized (mBuffer) {
                            for (int i = 0; i < bytes; i++) {
                                mBuffer.add(buffer[i] & 0xFF);
                            }
                        }

                        if (is.available() == 0) break;  //短时间没有数据才跳出进行显示
                    }

                    //发送显示消息，进行显示刷新
                    handler.sendMessage(handler.obtainMessage());
                } catch (IOException e) {
                }
            }
        }
    };
    List<Integer> mBuffer;
    //消息处理队列
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            StringBuffer buf = new StringBuffer();
            synchronized (mBuffer) {
                for (int i : mBuffer) {
                    String hexStr = Integer.toHexString(i);
                    if (hexStr.length() == 1) {
                        hexStr = "0" + hexStr;
                    }
                    buf.append(hexStr);
                    buf.append(' ');
                }
            }
            resultTv.setText(buf.toString());   //显示数据
            mBuffer.clear();
        }
    };

    //关闭程序掉用处理部分
    public void onDestroy() {
        super.onDestroy();
        if (_socket != null)  //关闭连接socket
            try {
                _socket.close();
            } catch (IOException e) {
            }
        //	_bluetooth.disable();  //关闭蓝牙服务
    }


    //连接按键响应函数
    public void onConnectButtonClicked(View v) {
        if (_bluetooth.isEnabled() == false) {  //如果蓝牙服务不可用则提示
            Toast.makeText(this, " 打开蓝牙中...", Toast.LENGTH_LONG).show();
            return;
        }


        //如未连接设备则打开DeviceListActivity进行设备搜索
        if (_socket == null) {
            Intent serverIntent = new Intent(this, DeviceListActivity.class); //跳转程序设置
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);  //设置返回宏定义
        } else {
            //关闭连接socket
            try {
                is.close();
                _socket.close();
                _socket = null;
                bRun = false;
            } catch (IOException e) {
            }
        }
        return;
    }


    @OnClick(R.id.action_save)
    public void onClick() {
        try {
            if (_socket == null) {
                onConnectButtonClicked(null);
                return;
            }
            OutputStream os = _socket.getOutputStream();   //蓝牙连接输出流
            String input = OperationStr;
            String[] data = input.split(" ");
            byte[] tmp = new byte[data.length];
            for (int i = 0; i < data.length; i++) {
                tmp[i] = (byte) Integer.parseInt(data[i], 16);
            }

            os.write(tmp);
        } catch (IOException e) {
        }
    }
}