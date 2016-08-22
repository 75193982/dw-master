package com.xgx.dw.ble;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.xgx.dw.app.BaseApplication;
import com.xgx.dw.ui.activity.DeviceListActivity;
import com.xgx.dw.utils.MyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Administrator on 2016/8/20.
 */
public class BlueToothManager {
    private static final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
    private static final String TAG = "BlueToothManager";
    private static final int MSG_NEW_DATA = 3;
    private final Activity context;
    private BluetoothAdapter mBluetoothAdapter;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;
    private List<Integer> mBuffer;
    private boolean isPause = false;
    private final int HEX = 0;
    private int mCodeType = HEX;
    private TextView mTextView;
    private String address = "";

    public BlueToothManager(BluetoothAdapter mBluetoothAdapter, Activity context) {
        this.mBluetoothAdapter = mBluetoothAdapter;
        mBuffer = new ArrayList<Integer>();
        this.context = context;
    }

    public void connect(String address, TextView mTextView) {
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        this.address = address;
        Log.d(TAG, "connect to: " + device);
        this.mTextView = mTextView;
        if (mConnectThread == null) {
            mConnectThread = new ConnectThread(device);
        }
        mConnectThread.start();


    }

    public ConnectedThread getmConnectedThread() {
        return mConnectedThread;
    }

    public void setmConnectedThread(ConnectedThread mConnectedThread) {
        this.mConnectedThread = mConnectedThread;
    }

    public ConnectThread getmConnectThread() {
        return mConnectThread;
    }

    public void setmConnectThread(ConnectThread mConnectThread) {
        this.mConnectThread = mConnectThread;
    }

    public void write(byte[] tmp) {
        if (mConnectedThread != null) {
            mConnectedThread.write(tmp);
        } else {
            Toast.makeText(context, "未连接设备", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, DeviceListActivity.class);
            context.startActivityForResult(intent, 1);
        }
    }

    /**
     * This thread runs while attempting to make an outgoing connection with a
     * device. It runs straight through; the connection either succeeds or
     * fails.
     */
    private class ConnectThread extends Thread {
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            mmDevice = device;
            BluetoothSocket tmp = null;

            // Get a BluetoothSocket for a connection with the
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectThread");
            setName("ConnectThread");

            // Always cancel discovery because it will slow down a connection
            mBluetoothAdapter.cancelDiscovery();
            new Thread(new Runnable() {

                @Override
                public void run() {
                    BluetoothSocket tmp = null;
                    // Make a connection to the BluetoothSocket
                    try {
                        // This is a blocking call and will only return on a
                        // successful connection or an exception


                        tmp = mmDevice.createRfcommSocketToServiceRecord(UUID.fromString(SPP_UUID));
                        tmp.connect();
                        BaseApplication.getSetting().saveString("bleAddress", address);
                    } catch (IOException e) {
                        Log.e(TAG, "unable to connect() socket", e);
                        // Close the socket
                        try {
                            tmp.close();
                        } catch (IOException e2) {
                            Log.e(TAG, "unable to close() socket during connection failure", e2);
                        }
                        return;
                    }

                    mConnectThread = null;

                    // Start the connected thread
                    // Start the thread to manage the connection and perform
                    // transmissions
                    mConnectedThread = new ConnectedThread(tmp);
                    mConnectedThread.start();
                }
            }).run();
        }

    }

    /**
     * This thread runs during a connection with a remote device. It handles all
     * incoming and outgoing transmissions.
     */
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            Log.d(TAG, "create ConnectedThread");
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "temp sockets not created", e);
            }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectedThread");
            byte[] buffer = new byte[256];
            int bytes;

            // Keep listening to the InputStream while connected
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    synchronized (mBuffer) {
                        for (int i = 0; i < bytes; i++) {
                            mBuffer.add(buffer[i] & 0xFF);
                        }
                    }
                    mHandler.sendEmptyMessage(MSG_NEW_DATA);
                } catch (IOException e) {
                    Log.e(TAG, "disconnected", e);
                    break;
                }
            }
        }

        public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);
            } catch (IOException e) {
                Log.e(TAG, "Exception during write", e);
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case MSG_NEW_DATA:
                    if (isPause) {
                        break;
                    } else {
                        StringBuffer buf = new StringBuffer();
                        synchronized (mBuffer) {
                            if (mCodeType == HEX) {
                                for (int i : mBuffer) {
                                    buf.append(Integer.toHexString(i));
                                    buf.append(' ');
                                }
                            }
                        }
                        if (mTextView != null) {
                            mTextView.setText(buf.toString());
                        }

                    }

                    break;

                default:
                    break;
            }
        }

    };
}
