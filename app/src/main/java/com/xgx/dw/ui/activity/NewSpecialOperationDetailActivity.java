package com.xgx.dw.ui.activity;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.clj.fastble.BleManager;
import com.clj.fastble.conn.BleCharacterCallback;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.ListScanCallback;
import com.clj.fastble.utils.HexUtil;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import butterknife.Bind;
import butterknife.OnClick;

//import android.view.Menu;            //如使用菜单加入此三包
//import android.view.MenuInflater;
//import android.view.MenuItem;

public class NewSpecialOperationDetailActivity extends BaseAppCompatActivity {
    // 下面的所有UUID及指令请根据实际设备替换0000fff0-0000-1000-8000-00805f9b34fb
    private static final String UUID_SERVICE = "0000fff0-0000-1000-8000-00805f9b34fb";
    private static final String UUID_INDICATE = "0000000-0000-1000-8000-00805f9b34fb";
    private static final String UUID_NOTIFY_1 = "00000000-0000-1000-8000-00805f9b34fb";
    private static final String UUID_NOTIFY_2 = "0000fff4-0000-1000-8000-00805f9b34fb";
    private static final String UUID_WRITE = "0000fff3-0000-1000-8000-00805f9b34fb";
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
    @Bind(R.id.sendTitle)
    TextView sendTitle;
    @Bind(R.id.deviceTv)
    TextView deviceTv;
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


    public String filename = ""; //用来保存存储的文件名

    private int title;
    private String OperationStr = "";
    private BleManager bleManager;

    @Override
    public void initContentView() {
        baseSetContentView(R.layout.activity_special_operation_detail);
    }

    @Override
    public void initView() {
        bleManager = BleManager.getInstance();
        bleManager.init(getContext());
        if (!TextUtils.isEmpty(BaseApplication.getSetting().loadString(BaseApplication.getSetting().loadString(G.currentUsername) + "_bleAddress"))) {
            if (bleManager.isConnected()) {
                deviceTv.setText("当前连接设备：" + BaseApplication.getSetting().loadString(BaseApplication.getSetting().loadString(G.currentUsername) + "_bleAddress"));
                bleManager.notifyDevice(UUID_SERVICE,
                        UUID_NOTIFY_2,
                        new BleCharacterCallback() {
                            @Override
                            public void onSuccess(BluetoothGattCharacteristic characteristic) {
                                showToast("notify success: " + '\n' + Arrays.toString(characteristic.getValue()));

                            }

                            @Override
                            public void onFailure(BleException exception) {
                                bleManager.handleException(exception);
                            }
                        });
            }
        }

//        getFab().setVisibility(View.VISIBLE);
//        getFab().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onConnectButtonClicked(v);
//            }
//        });
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
                setToolbarTitle("倍率录入");
                OperationStr = BlueOperationContact.BeiLvLuruSend;
                inputBeilvLayout.setVisibility(View.VISIBLE);
                changStr();
                break;
            case 5:
                setToolbarTitle("电价录入");
                OperationStr = BlueOperationContact.DianjiaLuruSend;
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
                OperationStr = BlueOperationContact.DianFeiCxSend;
                break;
            case 42:
                setToolbarTitle("电量查询");
                OperationStr = BlueOperationContact.DianLiangCxSend;
                break;
            case 43:
                setToolbarTitle("功率查询");
                OperationStr = BlueOperationContact.DianLvCxSend;
                break;
            case 47:
                setToolbarTitle("倍率查询");
                OperationStr = BlueOperationContact.beiLvCxSend;
                break;
            case 48:
                setToolbarTitle("电价查询");
                OperationStr = BlueOperationContact.DianjiaCxSend;
                break;
        }
        btnTv.setText(getToolbarTitle());
        resultTitle.setText(getToolbarTitle() + "结果");
        sendTitle.setText(getToolbarTitle() + "发送参数");
        sendTv.setText(OperationStr);

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


    @OnClick(R.id.action_save)
    public void onClick() {
        if (!bleManager.isConnected()) {
            if (bleManager.isInScanning())
                return;

            showProgress();

            bleManager.scanDevice(new ListScanCallback(10000) {
                @Override
                public void onDeviceFound(final BluetoothDevice[] devices) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                                Log.i(TAG, "共发现" + devices.length + "台设备");
//                                for (int i = 0; i < devices.length; i++) {
//                                    Log.i(TAG, "name:" + devices[i].getName() + "------mac:" + devices[i].getAddress());
//                                    mNewDevicesArrayAdapter.add(devices[i].getName());
//
//                                }
                        }
                    });
                }

                @Override
                public void onScanTimeout() {
                    super.onScanTimeout();
                    hideProgress();
                }
            });
            return;
        }
        String input = OperationStr;
        boolean b = bleManager.writeDevice(
                UUID_SERVICE,
                UUID_WRITE,
                HexUtil.hexStringToBytes(input.replace(" ", "")),
                new BleCharacterCallback() {
                    @Override
                    public void onSuccess(BluetoothGattCharacteristic characteristic) {
                        showToast("write success: " + '\n' + Arrays.toString(characteristic.getValue()));

                    }

                    @Override
                    public void onFailure(BleException exception) {
                        bleManager.handleException(exception);

                    }
                });
        if (b) {
            showToast("发送成功");
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