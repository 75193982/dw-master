package com.xgx.dw.ui.activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.xgx.dw.R;
import com.xgx.dw.app.BaseApplication;
import com.xgx.dw.app.G;
import com.xgx.dw.app.Setting;
import com.xgx.dw.base.BaseEventBusActivity;
import com.xgx.dw.base.EventCenter;
import com.xgx.dw.ble.BlueOperationContact;
import com.xgx.dw.utils.FullGridViewLayout;
import com.xgx.dw.utils.Logger;
import com.xgx.dw.utils.MyUtils;
import com.xgx.dw.wifi.WifiFunction;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SpecialAdminToolsDzActivity extends BaseEventBusActivity {

    private final static int REQUEST_CONNECT_DEVICE = 1;    //宏定义查询设备句柄
    private final static String MY_UUID = "00001101-0000-1000-8000-00805F9B34FB";   //SPP服务UUID号
    @BindView(R.id.sendTitle)
    TextView sendTitle;
    @BindView(R.id.sendTv)
    TextView sendTv;
    @BindView(R.id.sdEt)
    MaterialEditText sdEt;
    @BindView(R.id.sdTimeEt)
    MaterialEditText sdTimeEt;
    @BindView(R.id.sdTzSw)
    SwitchCompat sdTzSw;
    @BindView(R.id.lxEt)
    MaterialEditText lxEt;
    @BindView(R.id.lxTimeEt)
    MaterialEditText lxTimeEt;
    @BindView(R.id.lxTzSw)
    SwitchCompat lxTzSw;
    @BindView(R.id.dzA1Et)
    MaterialEditText dzA1Et;
    @BindView(R.id.dzA1TEt)
    MaterialEditText dzA1TEt;
    @BindView(R.id.dzA2Et)
    MaterialEditText dzA2Et;
    @BindView(R.id.dzA2TEt)
    MaterialEditText dzA2TEt;
    @BindView(R.id.dzA3Et)
    MaterialEditText dzA3Et;
    @BindView(R.id.dzA3TEt)
    MaterialEditText dzA3TEt;
    @BindView(R.id.glTzSw)
    SwitchCompat glTzSw;
    @BindView(R.id.dzV1Et)
    MaterialEditText dzV1Et;
    @BindView(R.id.dzV1TEt)
    MaterialEditText dzV1TEt;
    @BindView(R.id.dzS1Et)
    SwitchCompat dzS1Et;
    @BindView(R.id.gyV1Et)
    MaterialEditText gyV1Et;
    @BindView(R.id.gyV1TEt)
    MaterialEditText gyV1TEt;
    @BindView(R.id.gyS1Et)
    SwitchCompat gyS1Et;
    @BindView(R.id.dzS2T1Et)
    MaterialEditText dzS2T1Et;
    @BindView(R.id.dzS2T2Et)
    MaterialEditText dzS2T2Et;
    @BindView(R.id.dzS2T3Et)
    MaterialEditText dzS2T3Et;
    @BindView(R.id.dzS2Et)
    SwitchCompat dzS2Et;
    @BindView(R.id.djt1TEt)
    MaterialEditText djt1TEt;
    @BindView(R.id.saveTvTv)
    TextView saveTvTv;
    @BindView(R.id.action_saveecode)
    LinearLayout actionSaveecode;
    @BindView(R.id.dingzhi)
    LinearLayout dingzhi;
    @BindView(R.id.btnTv)
    TextView btnTv;
    @BindView(R.id.action_save)
    LinearLayout actionSave;
    @BindView(R.id.deviceTv)
    TextView deviceTv;
    @BindView(R.id.resultTitle)
    TextView resultTitle;
    @BindView(R.id.resultTv)
    TextView resultTv;
    private InputStream is;    //输入流，用来接收蓝牙数据
    BluetoothDevice _device = null;     //蓝牙设备
    BluetoothSocket _socket = null;      //蓝牙通信socket
    Socket mSocketClient = null;
    boolean bRun = true;
    boolean bThread = false;
    private BluetoothAdapter _bluetooth = BluetoothAdapter.getDefaultAdapter();    //获取本地蓝牙适配器，即蓝牙设备
    private String OperationStr = "";
    private boolean isWifi = false;
    private ArrayList<String> datas;


    @Override
    public void initContentView() {
        baseSetContentView(R.layout.activity_special_admin_tools_dz);
    }

    @Override
    public void initView() {
//        getFab().setVisibility(View.VISIBLE);
//        getFab().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Setting setting = new Setting(getContext());
//                boolean isWifi = setting.loadBoolean("isWifi");
//                //根据偏好设置确定是走wifi模块还是蓝牙模块
//                if (isWifi) {
//                    //wifi模块
//                } else {
//                    //蓝牙连接
//                    onConnectButtonClicked(v);
//                }
//            }
//        });

        mBuffer = new ArrayList<Integer>();
        //查询是否有配对成功的设备，如果配对成功则自动连接
        FullGridViewLayout gridLayoutManager = new FullGridViewLayout(this, 6);
        datas = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            datas.add("00");
        }
        btnTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData();
            }
        });
        setToolbarTitle("新定值查询");
        setDatas();
    }


    @Override
    public void initPresenter() {
        Setting setting = new Setting(getContext());
        isWifi = setting.loadBoolean("isWifi");
        //根据偏好设置确定是走wifi模块还是蓝牙模块
        if (isWifi) {
            //wifi模块
            changeToWifi();
        } else {
            //蓝牙连接
            changeToBle();
        }
        lxTimeEt.addTextChangedListener(new MyDzWather());
        lxEt.addTextChangedListener(new MyDzWather());
        lxTzSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                changDzStr();
            }
        });

        sdEt.addTextChangedListener(new MyDzWather());
        sdTimeEt.addTextChangedListener(new MyDzWather());
        sdTzSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                changDzStr();
            }
        });

        dzA1Et.addTextChangedListener(new MyDzWather());
        dzA1TEt.addTextChangedListener(new MyDzWather());
        dzA2Et.addTextChangedListener(new MyDzWather());
        dzA2TEt.addTextChangedListener(new MyDzWather());
        dzA3Et.addTextChangedListener(new MyDzWather());
        dzA3TEt.addTextChangedListener(new MyDzWather());
        glTzSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                changDzStr();
            }
        });

        dzV1Et.addTextChangedListener(new MyDzWather());
        dzV1TEt.addTextChangedListener(new MyDzWather());
        dzS1Et.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                changDzStr();
            }
        });

        gyV1Et.addTextChangedListener(new MyDzWather());
        gyV1TEt.addTextChangedListener(new MyDzWather());
        gyS1Et.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                changDzStr();
            }
        });

        dzS2T1Et.addTextChangedListener(new MyDzWather());
        dzS2T2Et.addTextChangedListener(new MyDzWather());
        dzS2T3Et.addTextChangedListener(new MyDzWather());
        dzS2Et.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                changDzStr();
            }
        });

        djt1TEt.addTextChangedListener(new MyDzWather());
    }

    private void setDatas() {
        BlueOperationContact.reset();
        changDzStr();
        resultTitle.setText(getToolbarTitle() + "结果");
        sendTitle.setText(getToolbarTitle() + "发送参数");
        sendTv.setVisibility(View.VISIBLE);
    }

    private WifiFunction mwififunction;

    private void changeToWifi() {

        mwififunction = new WifiFunction(this);
        String apstr = "HC-22-5ecf7f89c1e9";
        String passwdstr = "";
        mwififunction.addNetWork(mwififunction.CreateWifiInfo(apstr, "", 1));
        connectToWifi();
    }

    private void changeToBle() {
        BlueOperationContact.reset();
        if (_bluetooth == null) {
            Toast.makeText(this, "无法打开手机蓝牙，请确认手机是否有蓝牙功能！", Toast.LENGTH_LONG).show();
            finish();
        }
        // 设置设备可以被搜索
        new Thread() {
            public void run() {
                if (_bluetooth.isEnabled() == false) {
                    _bluetooth.enable();

                }
            }
        }.start();

        if (!TextUtils.isEmpty(BaseApplication.getSetting().loadString(BaseApplication.getSetting().loadString(G.currentUsername) + "_bleAddress"))) {
            showProgress("连接设备中");
            connect(BaseApplication.getSetting().loadString(BaseApplication.getSetting().loadString(G.currentUsername) + "_bleAddress"));
            hideProgress();
        }

    }


    //接收活动结果，响应startActivityForResult()
    @Override
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

        // 用服务号得到socket
        try {
            _device = _bluetooth.getRemoteDevice(address);
            _socket = _device.createRfcommSocketToServiceRecord(UUID.fromString(MY_UUID));
        } catch (IOException e) {
            Toast.makeText(this, "连接失败！", Toast.LENGTH_SHORT).show();
        }
        //连接socket
        try {
            if (!_socket.isConnected()) {
                _socket.connect();
            }

            BaseApplication.getSetting().saveString(BaseApplication.getSetting().loadString(G.currentUsername) + "_bleAddress", address);
            deviceTv.setText("当前连接设备：\n" + _device.getName() + "\n" + _device.getAddress());
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

    private void connectToWifi() {

        // 用服务号得到socket
        Thread mThreadClient = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if ((mSocketClient != null) && (mSocketClient.isConnected())) {
                        try {
                            mSocketClient.close();
                            mSocketClient = null;
                        } catch (IOException e) {
                            //e.printStackTrace();
                        }
                    }
                    int timeout = 2000;
                    String sIP = "192.168.4.1";
                    String sPort = "80";
                    int port = Integer.parseInt(sPort);
                    mSocketClient = new Socket();    //portnum
                    SocketAddress isa = new InetSocketAddress(sIP, port);
                    mSocketClient.connect(isa, timeout);
                    Message msg = new Message();
                    msg.what = 1;
                    mHandler.sendMessage(msg);
                } catch (Exception e) {
                    Message msg = new Message();
                    msg.what = -1;
                    mHandler.sendMessage(msg);
                }

                //打开接收线程
                try {
                    is = mSocketClient.getInputStream();   //得到WIFI数据输入流
                } catch (IOException e) {
                    Message msg = new Message();
                    msg.what = -2;
                    mHandler.sendMessage(msg);
                    return;
                }
                if (bThread == false) {
                    ReadThread.start();
                    bThread = true;
                } else {
                    bRun = true;
                }
            }
        });
        mThreadClient.start();

    }

    //接收数据线程
    Thread ReadThread = new Thread() {

        public void run() {
            byte[] buffer = new byte[256];
            int bytes;

            // Keep listening to the InputStream while connected
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = is.read(buffer);
                    synchronized (mBuffer) {
                        for (int i = 0; i < bytes; i++) {
                            mBuffer.add(buffer[i] & 0xFF);
                        }
                    }
                    handler.sendEmptyMessage(MSG_NEW_DATA);
                } catch (IOException e) {
                    break;
                }
            }
        }
    };
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                Toast.makeText(SpecialAdminToolsDzActivity.this, "连接服务器成功", Toast.LENGTH_SHORT).show();
                return;
            }
            if (msg.what == -1) {
                Toast.makeText(SpecialAdminToolsDzActivity.this, "连接服务器失败,请检测网络连接！", Toast.LENGTH_SHORT).show();
                return;
            }
            if (msg.what == -2) {
                Toast.makeText(SpecialAdminToolsDzActivity.this, "断开服务器连接！", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    };
    List<Integer> mBuffer;
    private boolean isPause = false;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_NEW_DATA:
                    if (isPause) {
                        break;
                    } else {
                        try {
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
                                if (!buf.toString().toUpperCase().startsWith("68")) {
                                    btnTv.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.White));
                                    btnTv.setTextColor(ContextCompat.getColor(getContext(), R.color.Orange));
                                    btnTv.setText("操作失败，设备没有成功恢复数据");
                                    isPause = true;
                                } else {
                                    resultTv.setText(Html.fromHtml("报文返回数据为：" + buf.toString()));

                                }
                            }
                        } catch (Exception e) {

                        }

                    }
                    break;
                default:
                    break;
            }
        }

    };
    private static final int MSG_NEW_DATA = 3;

    @Override
    protected void onEventComming(EventCenter eventCenter) {
        if (EventCenter.ADMIN_TOOLS == eventCenter.getEventCode()) {
            changDzStr();
        }
    }

    private String getDlEt(String data) {
        String dl = "00";

        try {
            dl = Integer.toHexString(Integer.parseInt(data));
            if (dl.length() == 1) {
                dl = "0" + dl;
            }
        } catch (Exception e) {
        }

        return dl;
    }


    @Override
    public boolean isBindEventBusHere() {
        return true;
    }

    //关闭程序掉用处理部分
    public void onDestroy() {
        super.onDestroy();
        if (_socket != null)  //关闭连接socket
            try {
                _socket.close();
            } catch (IOException e) {
            }
        //	_bluetooth.disable();  //关闭蓝牙服务
        if ((mSocketClient != null) && (mSocketClient.isConnected())) {
            try {
                mSocketClient.close();
                mSocketClient = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //连接按键响应函数
    public void onConnectButtonClicked(View v) {
        if (_bluetooth.isEnabled() == false) {  //如果蓝牙服务不可用则提示
            Toast.makeText(this, " 打开蓝牙中...", Toast.LENGTH_LONG).show();
            return;
        }
        //如未连接设备则打开DeviceListActivity进行设备搜索
        if (_socket == null) {
            Intent serverIntent = new Intent(this, DeviceListNewActivity.class); //跳转程序设置
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


    private void sendData() {
        try {
            OutputStream os = null;
            if (isWifi) {
                os = mSocketClient.getOutputStream();
            } else {
                if (_socket == null) {
                    onConnectButtonClicked(null);
                    return;
                }
                os = _socket.getOutputStream();
            }
            String input = OperationStr;
            String[] data = input.split(" ");
            byte[] tmp = new byte[data.length];
            for (int i = 0; i < data.length; i++) {
                Logger.e("" + data[i]);
                tmp[i] = (byte) Integer.parseInt(data[i], 16);
            }
            Logger.e(tmp.toString());
            os.write(tmp);
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }
    }


    private void changDzStr() {
        OperationStr = BlueOperationContact.adminDzSend;
        //零序跳闸时间
        String sdTimeStr = "";
        String sdStr = "";
        String sdS1Et = "";

        String lxTimeStr = "";
        String lxStr = "";
        String lxS1Et = "";

        String dzA1EtStr = "";
        String dzA1TEtStr = "";
        String dzA2EtStr = "";
        String dzA3EtStr = "";
        String dzA2TEtStr = "";
        String dzA3TEtStr = "";
        String glTzSwStr = "";

        String dzV1EtStr = "";
        String dzV1TEtStr = "";
        String dzS1EtStr = "";

        String gyV1EtStr = "";
        String gyV1TEtStr = "";
        String gySwStr = "";

        String dzS2T1TEtStr = "";
        String dzS2T2TEtStr = "";
        String dzS2T3TEtStr = "";
        String dzS2EtStr = "";

        String djTime = "";
        sdStr = getDlEt(sdEt);
        sdTimeStr = getTimeEt(sdTimeEt);
        sdS1Et = sdTzSw.isChecked() ? "01" : "00";
        //零序
        lxStr = getDlEt(lxEt);
        lxTimeStr = getTimeEt(lxTimeEt);//零序跳闸时间3s
        lxS1Et = lxTzSw.isChecked() ? "01" : "00";
        //过流1段
        dzA1EtStr = getDlEt(dzA1Et);
        dzA1TEtStr = getTimeEt(dzA1TEt);
        //过流2段
        dzA2EtStr = getDlEt(dzA2Et);
        dzA2TEtStr = getTimeEt(dzA2TEt);
        //过流3段
        dzA3EtStr = getDlEt(dzA3Et);
        dzA3TEtStr = getTimeEt(dzA3TEt);
        glTzSwStr = glTzSw.isChecked() ? "01" : "00";

        //失压跳闸
        dzV1EtStr = getDyEt(gyV1Et);
        dzV1TEtStr = getTimeEt(gyV1TEt);
        dzS1EtStr = dzS1Et.isChecked() ? "01" : "00";

        //过压跳闸
        gyV1EtStr = getDyEt(dzV1Et);
        gyV1TEtStr = getTimeEt(dzV1TEt);
        gySwStr = gyS1Et.isChecked() ? "01" : "00";

        //重合闸
        dzS2T1TEtStr = getTimeEt1(dzS2T1Et);
        dzS2T2TEtStr = getTimeEt1(dzS2T2Et);
        dzS2T3TEtStr = getTimeEt1(dzS2T3Et);
        dzS2EtStr = dzS2Et.isChecked() ? "01" : "00";
        //待机时间
        djTime = getTimeEt1(djt1TEt);
        OperationStr = OperationStr
                + " " + sdStr + " " + sdTimeStr + " " + sdS1Et
                + " " + lxStr + " " + lxTimeStr + " " + lxS1Et
                + " " + dzA1EtStr + " " + dzA1TEtStr
                + " " + dzA2EtStr + " " + dzA2TEtStr
                + " " + dzA3EtStr + " " + dzA3TEtStr + " " + glTzSwStr
                + " " + gyV1EtStr + " " + gyV1TEtStr + " " + gySwStr
                + " " + dzV1EtStr + " " + dzV1TEtStr + " " + dzS1EtStr
                + " " + dzS2T1TEtStr + " " + dzS2T2TEtStr + " " + dzS2T3TEtStr + " " + dzS2EtStr
                + " " + djTime;
        String OperationStrTemp = "4A 00 00 FF FF 20 04 63 01 01 02 06"
                + " " + sdStr + " " + sdTimeStr + " " + sdS1Et
                + " " + lxStr + " " + lxTimeStr + " " + lxS1Et
                + " " + dzA1EtStr + " " + dzA1TEtStr
                + " " + dzA2EtStr + " " + dzA2TEtStr
                + " " + dzA3EtStr + " " + dzA3TEtStr + " " + glTzSwStr
                + " " + gyV1EtStr + " " + gyV1TEtStr + " " + gySwStr
                + " " + dzV1EtStr + " " + dzV1TEtStr + " " + dzS1EtStr
                + " " + dzS2T1TEtStr + " " + dzS2T2TEtStr + " " + dzS2T3TEtStr + " " + dzS2EtStr
                + " " + djTime;
        OperationStr = OperationStr + " " + MyUtils.getJyCode(OperationStrTemp) + " 16";
        sendTv.setText(OperationStr);
    }

    private String getDyEt(MaterialEditText et) {
        String dl = "00";
        try {
            if (Integer.valueOf(et.getText().toString()) < 250 && Integer.valueOf(et.getText().toString()) > 0) {
                dl = Integer.toHexString(Integer.valueOf(et.getText().toString()));
                if (dl.length() == 1) {
                    dl = "0" + dl;
                }

            } else {
                showToast("无法设置超过0-250的电压");
                et.setText("");
            }
        } catch (Exception e) {

        }

        return dl;
    }

    private String getDlEt(MaterialEditText et) {
        String dl = "00";

        try {
            if (Double.valueOf(et.getText().toString()) < 26) {
                dl = Integer.toHexString((int) (Double.valueOf(et.getText().toString()) * 10));
                if (dl.length() == 1) {
                    dl = "0" + dl;
                }

            } else {
                showToast("无法设置超过25的电流");
                et.setText("");
            }
        } catch (Exception e) {
            Log.i("xgx", e.getMessage());
        }

        return dl;
    }

    private String getTimeEt(MaterialEditText et) {
        String tzTime = "00";
        try {
            if (Float.valueOf(et.getText().toString()) * 10 > 255) {
                showToast("无法设置超过25.5秒");
                et.setText("");
            } else {
                tzTime = Integer.toHexString((int) (Float.valueOf(et.getText().toString()) * 10));
                if (tzTime.length() == 1) {
                    tzTime = "0" + tzTime;
                }
            }
        } catch (Exception e) {
            Log.i("xgx", e.getMessage());
        }

        return tzTime;
    }

    private String getTimeEt1(MaterialEditText et) {
        String tzTime = "00";
        try {
            if (Float.valueOf(et.getText().toString()) > 255) {
                showToast("无法设置超过255秒");
                et.setText("");
            } else {
                tzTime = Integer.toHexString(Integer.valueOf(et.getText().toString()));
                if (tzTime.length() == 1) {
                    tzTime = "0" + tzTime;
                }
            }
        } catch (Exception e) {
            Log.i("xgx", e.getMessage());
        }

        return tzTime;
    }

    class MyDzWather implements TextWatcher

    {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!TextUtils.isEmpty(s)) {
                changDzStr();
            }
        }

    }
}