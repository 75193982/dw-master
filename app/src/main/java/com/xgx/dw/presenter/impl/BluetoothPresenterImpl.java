package com.xgx.dw.presenter.impl;

import android.bluetooth.BluetoothAdapter;
import com.xgx.dw.base.BasePresenter;
import com.xgx.dw.presenter.interfaces.IBluetoothPresenter;
import com.xgx.dw.ui.view.interfaces.IBluetoothView;

public class BluetoothPresenterImpl
  extends BasePresenter
  implements IBluetoothPresenter
{
  private BluetoothAdapter adapter;
  
  public void getBlueTooth(IBluetoothView paramIBluetoothView) {}
}


