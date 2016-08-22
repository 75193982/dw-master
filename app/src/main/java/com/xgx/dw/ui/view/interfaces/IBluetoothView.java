package com.xgx.dw.ui.view.interfaces;

import android.bluetooth.BluetoothAdapter;

import com.xgx.dw.base.IBaseView;

public abstract interface IBluetoothView
        extends IBaseView {
    public abstract void bluetoothResult(BluetoothAdapter paramBluetoothAdapter);
}
