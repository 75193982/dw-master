package com.xgx.dw.ui.activity;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.xgx.dw.PricingBean;
import com.xgx.dw.R;
import com.xgx.dw.SpotPricingBean;
import com.xgx.dw.UserBean;
import com.xgx.dw.app.BaseApplication;
import com.xgx.dw.app.G;
import com.xgx.dw.app.Setting;
import com.xgx.dw.base.BaseAppCompatActivity;
import com.xgx.dw.bean.LoginInformation;
import com.xgx.dw.ble.BlueOperationContact;
import com.xgx.dw.dao.PricingDaoHelper;
import com.xgx.dw.dao.SpotPricingBeanDaoHelper;
import com.xgx.dw.dao.UserBeanDaoHelper;
import com.xgx.dw.utils.AES;
import com.xgx.dw.utils.CommonUtils;
import com.xgx.dw.utils.Logger;
import com.xgx.dw.utils.MyUtils;
import com.xgx.dw.wifi.WifiActivity;
import com.xgx.dw.wifi.WifiFunction;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

//import android.view.Menu;            //如使用菜单加入此三包
//import android.view.MenuInflater;
//import android.view.MenuItem;

public class SpecialOperationDetailActivity extends BaseAppCompatActivity {

    private final static int REQUEST_CONNECT_DEVICE = 1;    //宏定义查询设备句柄
    @Bind(R.id.jdjEt)
    MaterialEditText jdjEt;
    @Bind(R.id.fdjEt)
    MaterialEditText fdjEt;
    @Bind(R.id.pdjEt)
    MaterialEditText pdjEt;
    @Bind(R.id.gdjEt)
    MaterialEditText gdjEt;
    @Bind(R.id.inputDianjiaLayout)
    LinearLayout inputDianjiaLayout;
    @Bind(R.id.gdlEt)
    MaterialEditText gdlEt;
    @Bind(R.id.bjEt)
    MaterialEditText bjEt;
    @Bind(R.id.tzEt)
    MaterialEditText tzEt;
    @Bind(R.id.inputDianfeiLayout)
    LinearLayout inputDianfeiLayout;
    private String beilvType = "4A";
    private int beilvNum = 0;
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
    LinearLayout actionSave;
    @Bind(R.id.btnTv)
    TextView btnTv;
    @Bind(R.id.deviceTv)
    TextView deviceTv;
    @Bind(R.id.dyblEt)
    MaterialEditText dyblEt;
    @Bind(R.id.input_editText)
    MaterialEditText inputEditText;
    @Bind(R.id.dlblEt)
    MaterialEditText dlblEt;
    @Bind(R.id.eddyEt)
    MaterialEditText eddyEt;
    @Bind(R.id.eddlEt)
    MaterialEditText eddlEt;
    @Bind(R.id.edfhEt)
    MaterialEditText edfhEt;
    @Bind(R.id.inputBeilvLayout)
    LinearLayout inputBeilvLayout;

    private InputStream is;    //输入流，用来接收蓝牙数据


    public String filename = ""; //用来保存存储的文件名
    BluetoothDevice _device = null;     //蓝牙设备
    BluetoothSocket _socket = null;      //蓝牙通信socket
    Socket mSocketClient = null;
    boolean _discoveryFinished = false;
    boolean bRun = true;
    boolean bThread = false;

    private BluetoothAdapter _bluetooth = BluetoothAdapter.getDefaultAdapter();    //获取本地蓝牙适配器，即蓝牙设备
    private int title;
    private String OperationStr = "";
    private int setp = 0;
    private PricingBean dlbean;
    private boolean isLuru = false;
    private boolean isWifi = false;


    @Override
    public void initContentView() {
        baseSetContentView(R.layout.activity_special_operation_detail);
    }

    @Override
    public void initView() {
        getFab().setVisibility(View.VISIBLE);
        getFab().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Setting setting = new Setting(getContext());
                boolean isWifi = setting.loadBoolean("isWifi");
                //根据偏好设置确定是走wifi模块还是蓝牙模块
                if (isWifi) {
                    //wifi模块
                } else {
                    //蓝牙连接
                    onConnectButtonClicked(v);
                }
            }
        });
        mBuffer = new ArrayList<Integer>();
        dyblEt.addTextChangedListener(new MyTextWather());
        dlblEt.addTextChangedListener(new MyTextWather());
        jdjEt.addTextChangedListener(new MyDjTextWather());
        fdjEt.addTextChangedListener(new MyDjTextWather());
        pdjEt.addTextChangedListener(new MyDjTextWather());
        gdjEt.addTextChangedListener(new MyDjTextWather());
        gdlEt.addTextChangedListener(new MyDlTextWather());
        bjEt.addTextChangedListener(new MyDlTextWather());
        tzEt.addTextChangedListener(new MyDlTextWather());
        //查询是否有配对成功的设备，如果配对成功则自动连接
        title = getIntent().getIntExtra("type", -1);
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

    }

    private void setDatas() {
        BlueOperationContact.reset();
        String currentTime = CommonUtils.formatDateTime1(new Date());
        String temp = "";
        switch (title) {
            case 0:
                setToolbarTitle("合闸");
                temp = String.format(BlueOperationContact.HeZaSendTemp, currentTime);
                OperationStr = String.format(BlueOperationContact.HeZaSend, currentTime, MyUtils.getJyCode(temp));
                // OperationStr = "68 8A 00 8A 00 68 6A 00 00 FF FF 21 05 E0 01 01 02 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 23 12 17 04 05 C8 16";
                break;
            case 1:
                setToolbarTitle("分闸");
                temp = String.format(BlueOperationContact.TiaoZaSendTemp, currentTime);
                OperationStr = String.format(BlueOperationContact.TiaoZaSend, currentTime, MyUtils.getJyCode(temp));

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
                setToolbarTitle("倍率录入");
                inputBeilvLayout.setVisibility(View.VISIBLE);
                changStr();
                break;
            case 5:
                setToolbarTitle("电价录入");
                inputDianjiaLayout.setVisibility(View.VISIBLE);
                changDjStr();
                break;
            case 6:
                setToolbarTitle("电费录入");
                inputDianfeiLayout.setVisibility(View.VISIBLE);
                changDlStr(gdlEt.getText().toString(), bjEt.getText().toString(), tzEt.getText().toString(), "1", "刷新");
                break;
            case 66:
                setToolbarTitle("电费录入");
                isLuru = true;
                dlbean = (PricingBean) getIntent().getSerializableExtra("dlbean");
                String price = "";
                try {
                    price = AES.decrypt(G.appsecret, dlbean.getPrice());
                } catch (Exception e) {
                    price = "";
                }
                changDlStr(price, dlbean.getBjprice(), "0", dlbean.getSpotpriceId(), dlbean.getType());
                break;
            case 41:
                setToolbarTitle("电费查询");
                temp = String.format(BlueOperationContact.DianFeiCxSendTemp, currentTime);
                OperationStr = String.format(BlueOperationContact.DianFeiCxSend, currentTime, MyUtils.getJyCode(temp));
                break;
            case 42:
                setToolbarTitle("电量查询");
                temp = String.format(BlueOperationContact.DianLiangCxSendTemp, currentTime);
                OperationStr = String.format(BlueOperationContact.DianLiangCxSend, currentTime, MyUtils.getJyCode(temp));

                break;
            case 43:
                setToolbarTitle("功率查询");
                temp = String.format(BlueOperationContact.DianLvCxSendTemp, currentTime);
                OperationStr = String.format(BlueOperationContact.DianLvCxSend, currentTime, MyUtils.getJyCode(temp));

                break;
            case 47:
                setToolbarTitle("倍率查询");
                temp = String.format(BlueOperationContact.beiLvCxSendTemp, currentTime);
                OperationStr = String.format(BlueOperationContact.beiLvCxSend, currentTime, MyUtils.getJyCode(temp));

                break;
            case 48:
                setToolbarTitle("电价查询");
                temp = String.format(BlueOperationContact.DianjiaCxSendTemp, currentTime);
                OperationStr = String.format(BlueOperationContact.DianjiaCxSend, currentTime, MyUtils.getJyCode(temp));
                break;
            case 8:
                setToolbarTitle("超管调试");
                break;
        }
        btnTv.setText(getToolbarTitle());
        resultTitle.setText(getToolbarTitle() + "结果");
        sendTitle.setText(getToolbarTitle() + "发送参数");

        UserBean bean = LoginInformation.getInstance().getUser();
        if (title != 66) {
            sendTv.setText(OperationStr);
        }
        if (!bean.getType().equals("20")) {
            if (title == 8) {
                inputEditText.setVisibility(View.VISIBLE);
            } else {
                sendTv.setVisibility(View.VISIBLE);
            }
        }
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

    private void changStr() {
        String dl = "";
        String dy = "";
        String eddy = "";
        if (TextUtils.isEmpty(dlblEt.getText().toString())) {
            dl = "00 00";
        } else {
            //先转换成int型 再转成16进制
            dl = toHexString(dlblEt.getText().toString()).toUpperCase();
        }
        if (TextUtils.isEmpty(dyblEt.getText().toString())) {
            dy = "00 00";
        } else {
            dy = toHexString(dyblEt.getText().toString()).toUpperCase();
        }
        eddy = "00 10";
        eddyEt.setText("100");
        String currentTime = CommonUtils.formatDateTime1(new Date());
        String temp = String.format(BlueOperationContact.BeiLvLuruSendTemp, dy, dl, eddy, currentTime);
        OperationStr = String.format(BlueOperationContact.BeiLvLuruSend, dy, dl, eddy, currentTime, MyUtils.getJyCode(temp));
        sendTv.setText(OperationStr);

    }

    private String toHexString(String tempStr) {
        String dlbl = tempStr;
        try {
            int bl = Integer.valueOf(dlbl);
            dlbl = Integer.toHexString(bl);
            //自动补全4位
            if (dlbl.length() == 0) {
                dlbl = "00 00";
            } else if (dlbl.length() == 1) {
                dlbl = "0" + dlbl + " 00";
            } else if (dlbl.length() == 2) {
                dlbl = dlbl + " 00";
            } else if (dlbl.length() == 3) {
                dlbl = dlbl.substring(1, 3) + " 0" + dlbl.substring(0, 1);
            } else if (dlbl.length() == 4) {
                dlbl = dlbl.substring(2, 4) + " " + dlbl.substring(0, 2);
            } else if (dlbl.length() > 4) {
                dlbl = "00 00";
            }
        } catch (Exception e) {

        }
        return dlbl;
    }


    private void changDjStr() {
        String fdj = "";
        String pdj = "";
        String gdj = "";
        String jdj = "";
        jdj = MyUtils.changeDjStr(jdjEt.getText().toString());
        pdj = MyUtils.changeDjStr(pdjEt.getText().toString());
        fdj = MyUtils.changeDjStr(fdjEt.getText().toString());
        gdj = MyUtils.changeDjStr(gdjEt.getText().toString());
        String currentTime = CommonUtils.formatDateTime1(new Date());
        String temp = String.format(BlueOperationContact.DianjiaLuruSendTemp, jdj, fdj, pdj, gdj, currentTime);
        OperationStr = String.format(BlueOperationContact.DianjiaLuruSend, jdj, fdj, pdj, gdj, currentTime, MyUtils.getJyCode(temp));
        sendTv.setText(OperationStr);
    }

    private void changDlStr(String gdl, String bjdl, String tzdl, String orderId, String type) {
        gdl = MyUtils.changeDlStr(gdl);
        bjdl = MyUtils.changeDlStr(bjdl);
        tzdl = MyUtils.changeDlStr(tzdl);
        String currentTime = CommonUtils.formatDateTime1(new Date());
        //获取当前单号+1
        String newId = MyUtils.getNewOrderId(orderId);
        if (type.equals("追加")) {
            type = "55";
        } else if (type.equals("刷新")) {
            type = "AA";
        } else {
            type = "55";
        }
        String temp = String.format(BlueOperationContact.DianfeiLuruSendTemp, newId, type, gdl, bjdl, tzdl, currentTime);
        OperationStr = String.format(BlueOperationContact.DianfeiLuruSend, newId, type, gdl, bjdl, tzdl, currentTime, MyUtils.getJyCode(temp));
        if (title == 66) {
            SpotPricingBean spotPricingBean = SpotPricingBeanDaoHelper.getInstance().getDataById(dlbean.getPid());
            UserBean userBean = LoginInformation.getInstance().getUser();
            String voltageRatio = userBean.getVoltageRatio();
            String currentRatio = userBean.getCurrentRatio();
            String price = spotPricingBean.getName();
            String priceNum = "";
            try {
                priceNum = AES.decrypt(G.appsecret, dlbean.getPrice());
            } catch (Exception e) {
                priceNum = "";
            }

            sendTv.setText("购电量：" + priceNum + "\n" + "电压倍率：" + voltageRatio +
                    "\n" + "电量倍率：" + currentRatio + "\n" + "电价：" + price);
            sendTv.setVisibility(View.VISIBLE);
        } else {
            sendTv.setText(OperationStr);
        }
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

    private String fmsg;
    private String smsg;
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
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                Toast.makeText(SpecialOperationDetailActivity.this, "连接服务器成功", Toast.LENGTH_SHORT).show();
                return;
            }
            if (msg.what == -1) {
                Toast.makeText(SpecialOperationDetailActivity.this, "连接服务器失败,请检测网络连接！", Toast.LENGTH_SHORT).show();
                return;
            }
            if (msg.what == -2) {
                Toast.makeText(SpecialOperationDetailActivity.this, "断开服务器连接！", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    };
    List<Integer> mBuffer;
    //消息处理队列
//    Handler handler = new Handler() {
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            StringBuffer buf = new StringBuffer();
//            synchronized (mBuffer) {
//                for (int i : mBuffer) {
//                    String hexStr = Integer.toHexString(i);
//                    if (hexStr.length() == 1) {
//                        hexStr = "0" + hexStr;
//                    }
//                    buf.append(hexStr);
//                    buf.append(' ');
//                }
//            }
//            //这里对接受到的数据进行解析
//            //首先解析
//            resultTv.setText(Html.fromHtml("报文返回数据为：" + buf.toString() + "<br/>" + MyUtils.decodeHex367(title, buf.toString())));   //显示数据
//            mBuffer.clear();
//        }
//    };
    private boolean isPause = false;

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
                                    UserBean bean = LoginInformation.getInstance().getUser();
                                    if (title == 8) {
                                        resultTv.setText(Html.fromHtml("报文返回数据为：" + buf.toString()));
                                        return;
                                    }
                                    if (!bean.getType().equals("20")) {
                                        resultTv.setText(Html.fromHtml("报文返回数据为：" + buf.toString() + "<br/>" + MyUtils.decodeHex367(title, buf.toString())));   //显示数据
                                    } else {
                                        resultTv.setText(Html.fromHtml(MyUtils.decodeHex367(title, buf.toString())));   //显示数据
                                    }
                                    actionSave.setEnabled(false);
                                    btnTv.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.White));
                                    btnTv.setText("操作成功");
                                    btnTv.setTextColor(ContextCompat.getColor(getContext(), R.color.Orange));

                                    if (title == 4 && isLuru == true) {//表示倍率录入成功
                                        String dj = bean.getPrice();
                                        title = 5;
                                        changDjStr(dj);
                                        sendData();
                                    } else if (title == 5 && isLuru == true) {
                                        if (getIntent().getIntExtra("type", -1) == 66) {
                                            title = 66;
                                            String priceNum = "";
                                            try {
                                                priceNum = AES.decrypt(G.appsecret, dlbean.getPrice());
                                            } catch (Exception e) {
                                                priceNum = "";
                                            }
                                            changDlStr(priceNum, dlbean.getBjprice(), "0", dlbean.getSpotpriceId(), dlbean.getType());
                                        } else {
                                            title = 6;
                                            changDlStr(gdlEt.getText().toString(), bjEt.getText().toString(), tzEt.getText().toString(), "1", "刷新");
                                        }
                                        sendData();
                                        Setting setting = new Setting(getContext());
                                        String userId = LoginInformation.getInstance().getUser().getUserId();
                                        setting.saveBoolean(userId + "_isFirstBuy", false);
                                        btnTv.setText("购电成功");
                                        btnTv.setTextColor(ContextCompat.getColor(getContext(), R.color.Orange));

                                    } else if (title == 66 || title == 6) {
                                        if (dlbean != null) {

                                            //购电成功后 检查是否要保电投入
                                            if (dlbean.getFinishtype().contains("3")) {
                                                title = 2;
                                                OperationStr = BlueOperationContact.BaoDianTrSend;
                                                sendData();
                                            }
                                            dlbean.setFinishtype("2");
                                            PricingDaoHelper.getInstance().addData(dlbean);
                                        }
                                        btnTv.setText("购电成功");
                                        btnTv.setTextColor(ContextCompat.getColor(getContext(), R.color.Orange));
                                        setResult(1001);
                                    }
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


    @OnClick(R.id.action_save)
    public void onClick() {
        if (title == 6) {
            Setting setting = new Setting(getContext());
            UserBean bean = LoginInformation.getInstance().getUser();
            if (bean.getType().equals("20")) {
                String userId = LoginInformation.getInstance().getUser().getUserId();
                boolean isFirstBuy = setting.loadBoolean(userId + "_isFirstBuy");
                if (isFirstBuy) {
                    //先录入倍率，在录入电价，最后再录入电费
                    //1检查倍率和电价是否为空
                    String dy = bean.getVoltageRatio();
                    String dl = bean.getCurrentRatio();
                    String dj = bean.getPrice();
                    if (TextUtils.isEmpty(bean.getVoltageRatio()) || TextUtils.isEmpty(bean.getCurrentRatio()) || TextUtils.isEmpty(bean.getPrice())) {
                        showToast("电压倍率/电流倍率/电价信息不完整");
                        return;
                    }
                    title = 4;
                    changStr(dy, dl);
                } else {
                    changDlStr(gdlEt.getText().toString(), bjEt.getText().toString(), tzEt.getText().toString(), "1", "刷新");
                }
            }
        } else if (title == 66) {
            Setting setting = new Setting(getContext());
            UserBean bean = UserBeanDaoHelper.getInstance().getDataById(dlbean.getUserPrimaryid());
            if (bean.getType().equals("20")) {
                String userId = bean.getUserId();
                boolean isFirstBuy = setting.loadBoolean(userId + "_isFirstBuy");
                if (!isFirstBuy) {
                    //先录入倍率，在录入电价，最后再录入电费
                    //1检查倍率和电价是否为空
                    String dy = bean.getVoltageRatio();
                    String dl = bean.getCurrentRatio();
                    String dj = bean.getPrice();
                    if (TextUtils.isEmpty(bean.getVoltageRatio()) || TextUtils.isEmpty(bean.getCurrentRatio()) || TextUtils.isEmpty(bean.getPrice())) {
                        showToast("电压倍率/电流倍率/电价信息不完整");
                        return;
                    }
                    title = 4;
                    changStr(dy, dl);
                } else {
                    if (dlbean.getFinishtype().contains("1")) {//表示需要重新上传电压倍率
                        String dy = bean.getVoltageRatio();
                        String dl = bean.getCurrentRatio();
                        if (TextUtils.isEmpty(bean.getVoltageRatio()) || TextUtils.isEmpty(bean.getCurrentRatio()) || TextUtils.isEmpty(bean.getPrice())) {
                            showToast("电压倍率/电流倍率/电价信息不完整");
                            return;
                        }
                        title = 4;
                        changStr(dy, dl);
                    } else {
                        String priceNum = "";
                        try {
                            priceNum = AES.decrypt(G.appsecret, dlbean.getPrice());
                        } catch (Exception e) {
                            priceNum = "";
                        }
                        changDlStr(priceNum, dlbean.getBjprice(), "0", dlbean.getSpotpriceId(), dlbean.getType());
                    }
                }
            }
        } else if (title == 8) {
            OperationStr = inputEditText.getText().toString();
            if (TextUtils.isEmpty(OperationStr)) {
                showToast("请输入指令");
                return;
            }
        }
        sendData();


    }

    private void changStr(String dy, String dl) {
        String eddy = "";
        if (TextUtils.isEmpty(dl)) {
            dl = "00 00";
        } else {
            //先转换成int型 再转成16进制
            dl = toHexString(dl).toUpperCase();
        }
        if (TextUtils.isEmpty(dy)) {
            dy = "00 00";
        } else {
            dy = toHexString(dy).toUpperCase();
        }
        eddy = "00 10";
        eddyEt.setText("100");
        String currentTime = CommonUtils.formatDateTime1(new Date());
        String temp = String.format(BlueOperationContact.BeiLvLuruSendTemp, dy, dl, eddy, currentTime);
        OperationStr = String.format(BlueOperationContact.BeiLvLuruSend, dy, dl, eddy, currentTime, MyUtils.getJyCode(temp));
    }

    private void changDjStr(String dj) {
        //根据ID获取电价
        SpotPricingBean pricingbean = SpotPricingBeanDaoHelper.getInstance().getDataById(dj);
        String jdj = "";

        String fdj = "";
        String pdj = "";
        String gdj = "";
        if (pricingbean.getType().equals("普通")) {
            jdj = pricingbean.getPrice_count();
            fdj = pricingbean.getPrice_count();
            pdj = pricingbean.getPrice_count();
            gdj = pricingbean.getPrice_count();
        } else if (pricingbean.getType().equals("分时")) {
            jdj = pricingbean.getPointed_price();
            fdj = pricingbean.getPeek_price();
            pdj = pricingbean.getFlat_price();
            gdj = pricingbean.getValley_price();
        }

        fdj = MyUtils.changeDjStr(fdj);
        pdj = MyUtils.changeDjStr(pdj);
        gdj = MyUtils.changeDjStr(gdj);
        jdj = MyUtils.changeDjStr(jdj);
        String currentTime = CommonUtils.formatDateTime1(new Date());
        String temp = String.format(BlueOperationContact.DianjiaLuruSendTemp, jdj, fdj, pdj, gdj, currentTime);
        OperationStr = String.format(BlueOperationContact.DianjiaLuruSend, jdj, fdj, pdj, gdj, currentTime, MyUtils.getJyCode(temp));
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


    class MyTextWather implements TextWatcher

    {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            changStr();
        }

    }

    class MyDjTextWather implements TextWatcher

    {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            changDjStr();
        }

    }

    class MyDlTextWather implements TextWatcher

    {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            changDlStr(gdlEt.getText().toString(), bjEt.getText().toString(), tzEt.getText().toString(), "1", "刷新");
        }

    }

}