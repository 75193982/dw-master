package com.xgx.dw.ui.activity;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.xgx.dw.R;
import com.xgx.dw.app.BaseApplication;
import com.xgx.dw.app.G;
import com.xgx.dw.base.BaseAppCompatActivity;
import com.xgx.dw.ble.BlueOperationContact;
import com.xgx.dw.utils.CommonUtils;
import com.xgx.dw.utils.MyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
    RippleView actionSave;
    @Bind(R.id.btnTv)
    TextView btnTv;
    @Bind(R.id.dyblEt)
    MaterialEditText dyblEt;
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
        BlueOperationContact.reset();
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
        dyblEt.addTextChangedListener(new MyTextWather());
        dlblEt.addTextChangedListener(new MyTextWather());
        jdjEt.addTextChangedListener(new MyDjTextWather());
        fdjEt.addTextChangedListener(new MyDjTextWather());
        pdjEt.addTextChangedListener(new MyDjTextWather());
        gdjEt.addTextChangedListener(new MyDjTextWather());
        gdlEt.addTextChangedListener(new MyDlTextWather());
        bjEt.addTextChangedListener(new MyDlTextWather());
        tzEt.addTextChangedListener(new MyDlTextWather());
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

    @Override
    public void initPresenter() {
        //查询是否有配对成功的设备，如果配对成功则自动连接

        title = getIntent().getIntExtra("type", -1);
        String currentTime = CommonUtils.formatDateTime1(new Date());
        String temp = "";
        switch (title) {
            case 0:
                setToolbarTitle("合闸");

                temp = String.format(BlueOperationContact.HeZaSendTemp, currentTime);
                OperationStr = String.format(BlueOperationContact.HeZaSend, currentTime, MyUtils.getJyCode(temp));

                break;
            case 1:
                setToolbarTitle("分闸");
                temp = String.format(BlueOperationContact.TiaoZaSendTemp, currentTime);
                OperationStr = String.format(BlueOperationContact.TiaoZaSend, currentTime, MyUtils.getJyCode(temp));

                break;
            case 2:
                setToolbarTitle("保电投入");
                temp = String.format(BlueOperationContact.BaoDianTrSendTemp, currentTime);
                OperationStr = String.format(BlueOperationContact.BaoDianTrSend, currentTime, MyUtils.getJyCode(temp));

                break;
            case 3:
                setToolbarTitle("保电解除");
                temp = String.format(BlueOperationContact.BaoDianJcSendTemp, currentTime);
                OperationStr = String.format(BlueOperationContact.BaoDianJcSend, currentTime, MyUtils.getJyCode(temp));

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
                OperationStr = BlueOperationContact.DianfeiLuruSend;
                inputDianfeiLayout.setVisibility(View.VISIBLE);
                changDlStr();
                break;
            case 41:
                setToolbarTitle("电费查询");
                temp = String.format(BlueOperationContact.DianFeiCxSendTemp, currentTime);
                OperationStr = String.format(BlueOperationContact.DianFeiCxSend, currentTime, MyUtils.getJyCode(temp));
                break;
            case 42:
                setToolbarTitle("电量查询");
                temp = String.format(BlueOperationContact.DianLiangCxSendTemp, currentTime);
                OperationStr = String.format(BlueOperationContact.DianFeiCxSend, currentTime, MyUtils.getJyCode(temp));

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
        }
        btnTv.setText(getToolbarTitle());
        resultTitle.setText(getToolbarTitle() + "结果");
        sendTitle.setText(getToolbarTitle() + "发送参数");
        sendTv.setText(OperationStr);
        if (!TextUtils.isEmpty(BaseApplication.getSetting().loadString(BaseApplication.getSetting().loadString(G.currentUsername) + "_bleAddress"))) {
            showProgress("连接设备中");
            connect(BaseApplication.getSetting().loadString(BaseApplication.getSetting().loadString(G.currentUsername) + "_bleAddress"));
            hideProgress();
        }
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

    private void changDlStr() {
        String gdl = "";
        String bjdl = "";
        String tzdl = "";

        gdl = MyUtils.changeDlStr(gdlEt.getText().toString());

        bjdl = MyUtils.changeDlStr(bjEt.getText().toString());
        tzdl = MyUtils.changeDlStr(tzEt.getText().toString());
        String currentTime = CommonUtils.formatDateTime1(new Date());
        //获取当前单号+1
        String newId = MyUtils.getNewOrderId();
        String temp = String.format(BlueOperationContact.DianfeiLuruSendTemp, newId, "55", gdl, bjdl, tzdl, currentTime);
        OperationStr = String.format(BlueOperationContact.DianfeiLuruSend, newId, "55", gdl, bjdl, tzdl, currentTime, MyUtils.getJyCode(temp));
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
            try {
                int count = 0;
                while (count == 0) {
                    count = is.available();
                }
                byte[] bytes = new byte[count];
                int readCount = 0; // 已经成功读取的字节的个数
                while (readCount < count) {
                    readCount += is.read(bytes, readCount, count - readCount);
                    synchronized (mBuffer) {
                        for (int i = 0; i < readCount; i++) {
                            mBuffer.add(bytes[i] & 0xFF);
                        }
                    }
                }
                //发送显示消息，进行显示刷新
                handler.sendMessage(handler.obtainMessage());
            } catch (IOException e) {
            }
//            byte[] buffer = new byte[1024];
//            int bytes;
//            bRun = true;
//            //接收线程
//            while (true) {
//                try {
//                    while (is.available() == 0) {
//                        while (bRun == false) {
//                        }
//                    }
//                    while (true) {
//
//                        bytes = is.read(buffer);
//                        synchronized (mBuffer) {
//                            for (int i = 0; i < bytes; i++) {
//                                mBuffer.add(buffer[i] & 0xFF);
//                            }
//                        }
//
//                        if (is.available() == 0) break;  //短时间没有数据才跳出进行显示
//                    }
//
//                    //发送显示消息，进行显示刷新
//                    handler.sendMessage(handler.obtainMessage());
//                } catch (IOException e) {
//                }
//            }
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
            //这里对接受到的数据进行解析
            //首先解析
            resultTv.setText(Html.fromHtml("报文返回数据为：" + buf.toString() + "<br/>" + MyUtils.decodeHex367(title, buf.toString())));   //显示数据
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
            if (title == 5) {

            }
            String[] data = input.split(" ");
            byte[] tmp = new byte[data.length];
            for (int i = 0; i < data.length; i++) {
                tmp[i] = (byte) Integer.parseInt(data[i], 16);
            }

            os.write(tmp);
        } catch (IOException e) {
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
            changDlStr();
        }

    }
}