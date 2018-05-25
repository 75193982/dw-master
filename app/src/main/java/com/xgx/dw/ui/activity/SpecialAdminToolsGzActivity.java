package com.xgx.dw.ui.activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.xgx.dw.R;
import com.xgx.dw.app.BaseApplication;
import com.xgx.dw.app.G;
import com.xgx.dw.app.Setting;
import com.xgx.dw.base.BaseEventBusActivity;
import com.xgx.dw.base.EventCenter;
import com.xgx.dw.ble.BlueOperationContact;
import com.xgx.dw.utils.CommonUtils;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;

public class SpecialAdminToolsGzActivity extends BaseEventBusActivity {

    private final static int REQUEST_CONNECT_DEVICE = 1;    //宏定义查询设备句柄
    private final static String MY_UUID = "00001101-0000-1000-8000-00805F9B34FB";   //SPP服务UUID号
    @BindView(R.id.sendTitle)
    TextView sendTitle;
    @BindView(R.id.sendTv)
    TextView sendTv;
    @BindView(R.id.codeEt)
    MaterialEditText codeEt;
    @BindView(R.id.addressEt)
    MaterialEditText addressEt;
    @BindView(R.id.eventEt)
    MaterialEditText eventEt;
    @BindView(R.id.lenthEt)
    MaterialEditText lenthEt;
    @BindView(R.id.timeTv)
    TextView timeTv;
    @BindView(R.id.flagTv)
    TextView flagTv;
    @BindView(R.id.typeTv)
    TextView typeTv;
    @BindView(R.id.type2Tv)
    TextView type2Tv;
    @BindView(R.id.contentEt)
    MaterialEditText contentEt;
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
        baseSetContentView(R.layout.activity_special_admin_tools_gz);
    }

    @Override
    public void initView() {

        mBuffer = new ArrayList<Integer>();
        //查询是否有配对成功的设备，如果配对成功则自动连接
        setToolbarTitle("新故障查询");
        codeEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String code = String.format("%04d", Integer.valueOf(s.toString()));
                code = code.toString().substring(2, 4) + " " + code.toString().substring(0, 2);

                codeEt.setContentDescription(code);
                setDatas();
            }
        });
        addressEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    int device = Integer.valueOf(s.toString());
                    addressEt.setContentDescription(changeDlStr(Integer.toHexString(device)));
                } catch (Exception e) {

                }
                setDatas();
            }
        });
        contentEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    contentEt.setContentDescription(getDlEt(contentEt));
                } catch (Exception e) {

                }
                setDatas();
            }
        });
        eventEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setDatas();
            }
        });
        contentEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setDatas();
            }
        });
        lenthEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setDatas();
            }
        });
        setDatas();
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
                showToast("无法设置超过25A的电流");
                et.setText("");
            }
        } catch (Exception e) {
            Log.i("xgx", e.getMessage());
        }

        return dl;
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
    }

    private void setDatas() {
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
                Toast.makeText(SpecialAdminToolsGzActivity.this, "连接服务器成功", Toast.LENGTH_SHORT).show();
                return;
            }
            if (msg.what == -1) {
                Toast.makeText(SpecialAdminToolsGzActivity.this, "连接服务器失败,请检测网络连接！", Toast.LENGTH_SHORT).show();
                return;
            }
            if (msg.what == -2) {
                Toast.makeText(SpecialAdminToolsGzActivity.this, "断开服务器连接！", Toast.LENGTH_SHORT).show();
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
        String codeStr = codeEt.getContentDescription() != null ? codeEt.getContentDescription().toString() : "00 00";//
        String addressStr = addressEt.getContentDescription() != null ? addressEt.getContentDescription().toString() : "00 00 00";
        String eventStr = getDlEt(eventEt.getText().toString());
        String lengthStr = getDlEt(lenthEt.getText().toString());
        String timeStr = timeTv.getContentDescription() == null ? TimeUtils.date2String(new Date(), new SimpleDateFormat("mm HH dd MM yy", Locale.getDefault())) : timeTv.getContentDescription().toString();
        String flagStr = flagTv.getContentDescription().toString();
        String type1Str = typeTv.getContentDescription().toString();
        String type2Str = type2Tv.getContentDescription().toString();
        String contentStr = contentEt.getContentDescription().toString();

        OperationStr = "68 8A 00 8A 00 68 E8"
                + " " + codeStr + " " + addressStr + " 00 0E F3 00 00 01 00 0B 05 3E 3F"
                + " " + eventStr + " " + lengthStr + " " + timeStr + " " + flagStr
                + " " + type1Str + type2Str + " "
                + contentStr + " 0B 05 03 04 01 12 25 00";
        String OperationStrTemp = "E8"
                + " " + codeStr + " " + addressStr + " 00 0E F3 00 00 01 00 0B 05 3E 3F"
                + " " + eventStr + " " + lengthStr + " " + timeStr + " " + flagStr
                + " " + type1Str + type2Str + " "
                + contentStr + " 0B 05 03 04 01 12 25 00";
        OperationStr = OperationStr + " " + MyUtils.getJyCode(OperationStrTemp) + " 16";
        sendTv.setText(OperationStr);
    }


    @OnClick({R.id.timeTv, R.id.type2Tv, R.id.flagTv, R.id.typeTv, R.id.btnTv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.timeTv:
                KeyboardUtils.hideSoftInput(this);
                //时间选择器
                //时间选择器
                TimePickerView pvTime = new TimePickerBuilder(getContext(), new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        timeTv.setText(TimeUtils.date2String(date, new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())));
                        timeTv.setContentDescription(TimeUtils.date2String(date, new SimpleDateFormat("mm HH dd MM yy", Locale.getDefault())));
                        setDatas();
                    }
                }).setType(new boolean[]{true, true, true, true, true, false}).build();
                pvTime.setDate(Calendar.getInstance());
                pvTime.show();
                break;
            case R.id.flagTv:
                final ArrayList<String> strList = new ArrayList<>();
                strList.add("分闸");
                strList.add("合闸");
                //条件选择器
                OptionsPickerView pvOptions = new OptionsPickerBuilder(SpecialAdminToolsGzActivity.this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        String tx = strList.get(options1);
                        flagTv.setText(tx);
                        if (tx.equals("分闸")) {
                            flagTv.setContentDescription("5A");
                        } else {
                            flagTv.setContentDescription("A5");

                        }
                        setDatas();

                    }
                }).build();
                pvOptions.setPicker(strList);
                pvOptions.show();
                break;
            case R.id.typeTv://0-速断，1-限流，2-零序，3-费控，4-手动，5-远程，6-失压，7-过压
                final ArrayList<String> typeList = new ArrayList<>();
                typeList.add("速断");
                typeList.add("限流");
                typeList.add("零序");
                typeList.add("费控");
                typeList.add("手动");
                typeList.add("远程");
                typeList.add("失压");
                typeList.add("过压");
                //条件选择器
                OptionsPickerView typeOptions = new OptionsPickerBuilder(SpecialAdminToolsGzActivity.this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        String tx = typeList.get(options1);
                        typeTv.setText(tx);
                        typeTv.setContentDescription(options1 + "");
                        setDatas();
                    }
                }).build();
                typeOptions.setPicker(typeList);
                typeOptions.show();
                break;
            case R.id.type2Tv://0-零线，1-A相，2-B相，3-C相
                final ArrayList<String> type2List = new ArrayList<>();
                type2List.add("零线");
                type2List.add("A相");
                type2List.add("B相");
                type2List.add("C相");
                //条件选择器
                OptionsPickerView type2Options = new OptionsPickerBuilder(SpecialAdminToolsGzActivity.this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        String tx = type2List.get(options1);
                        type2Tv.setText(tx);
                        type2Tv.setContentDescription(options1 + "");
                        setDatas();

                    }
                }).build();
                type2Options.setPicker(type2List);
                type2Options.show();
                break;
            case R.id.btnTv:
                sendData();
                break;
        }
    }


    public static String changeDlStr(String dl) {
        String str = "";
        try {
            str = dl;
            if (str.length() == 0) {
                str = "00 00";
            } else if (str.length() == 1) {
                str = "0" + str + " 00";
            } else if (str.length() == 2) {
                str = str + " 00";
            } else if (str.length() == 3) {
                str = str.substring(1, 3) + " 0" + str.substring(0, 1);
            } else if (str.length() == 4) {
                str = str.substring(2, 4) + " " + str.substring(0, 2);
            }
        } catch (Exception e) {
            str = "00 00";
        }

        return str;
    }
}