package com.xgx.dw.wifi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.xgx.dw.R;

public class WifiActivity extends Activity{

	private  EditText output_edittext;
	private  EditText input_edittext;
	private  EditText AP;
	private  EditText passwd;
	private  EditText remoteIP;
	private  EditText remotePort;
	private  Button setting_button;
	private  ToggleButton connect_button;
	private  Button clear_button;
	private  Button exit_button;
	private  Button send_button;
	private  ProgressDialog pdialog;
	private CheckBox char_checkBox;

	final String FILE_NAME="sysset.init";

	//socket连接
	static PrintWriter out= null;
	static DataOutputStream data_out = null;
	static DataInputStream data_in = null;
	static String data_string = "";
	private Thread mThreadClient = null;
	private Socket mSocketClient = null;
	private boolean isConnecting = false;
	private boolean netconnect = false;
	private boolean flagFrame = false; //帧接收完成标志，即接收到一帧新数据
	private int cntRxd ; //接收计数器
	static int cntbkp = 0;
	static int idletmr = 0;

	//wifi设置
	private WifiFunction mwififunction;
	private List<ScanResult> list;
	private ScanResult mScanResult;
	private StringBuffer sb=new StringBuffer();
	private boolean scanfwifi = false;
	private String apstr;
	private String passwdstr;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wifi);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //防止手机休眠
		mwififunction = new WifiFunction(WifiActivity.this);
		flagFrame = false;
		cntRxd = 0;

		output_edittext = (EditText)findViewById(R.id.output_editText);
		input_edittext =  (EditText)findViewById(R.id.input_editText);
		setting_button = (Button)findViewById(R.id.setting_button);
		connect_button = (ToggleButton)findViewById(R.id.connect_button);
		clear_button = (Button)findViewById(R.id.clear_button);
		exit_button = (Button)findViewById(R.id.exit_button);
		send_button = (Button)findViewById(R.id.send_button);
		char_checkBox = (CheckBox)findViewById(R.id.char_checkBox);

		char_checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if(arg1){
					Toast.makeText(WifiActivity.this, "以字符方式发送和接收", Toast.LENGTH_SHORT).show();
				}
				else{
					Toast.makeText(WifiActivity.this, "以ASCII码方式发送和接收", Toast.LENGTH_SHORT).show();
				}
			}
		});

		setting_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(mwififunction.checkWifiWork() == 0)
				{
					Toast.makeText(WifiActivity.this, "请先打开WIFI！", Toast.LENGTH_SHORT).show();
					startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
					netconnect = false;
					return;
				}

				LayoutInflater factory=LayoutInflater.from(WifiActivity.this);
				final View setting_view=factory.inflate(R.layout.activity_setting,null);
				AlertDialog.Builder dialog=new AlertDialog.Builder(WifiActivity.this);
				dialog.setTitle("连接参数设置");
				dialog.setView(setting_view);
				AP = (EditText)setting_view.findViewById(R.id.AP);
				passwd = (EditText)setting_view.findViewById(R.id.passwd);
				remoteIP = (EditText)setting_view.findViewById(R.id.remoteIP);
				remotePort = (EditText)setting_view.findViewById(R.id.remotePort);
				initset();

				final Thread setthread = new Thread(new Runnable(){
					public void run() {
						try {
							Thread.sleep(5000);
						}catch(InterruptedException e){}
						while(mwififunction.checkWifiWork()==0){
							try {
								Thread.sleep(500);
							}catch(InterruptedException e){}
						}
						try {
							Thread.sleep(3000);
						}catch(InterruptedException e){}

						//WifiFunction.set_static(apstr,localipstr,"192.168.0.1","202.114.88.10");
						Message message = new Message();
						message.what = 1;
						sethandler.sendMessage(message);
					}
				});
				dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						apstr=AP.getText().toString();
						passwdstr=passwd.getText().toString();
						String remoteipstr=remoteIP.getText().toString();
						String remoteportstr=remotePort.getText().toString();
						saveset(apstr,passwdstr,remoteipstr,remoteportstr);

						if(mwififunction.getSSID().equals(apstr) || mwififunction.getSSID().equals("\""+ apstr+"\""))
						{
							output_edittext.append("已连接到热点： "+apstr+"\n");
							netconnect = true;
							return;
						}

						if(sb!=null){
							sb=new StringBuffer();
						}
						mwififunction.startScan();
						//output_edittext.append("已配置过的wifi网络:\n"+mwififunction.lookUpConfigure().toString()+"\n");
						list=mwififunction.getWifiList();
						if(list!=null){
							for(int i=0;i<list.size();i++){
								mScanResult=list.get(i);
								if(mScanResult.SSID.toString().equals(apstr)){
									scanfwifi=true;
									output_edittext.append("扫描到热点: "+apstr+"\n");
									int intNetworkID=mwififunction.IsConfiguration(apstr);
									if(intNetworkID == -1)
									{
										output_edittext.append("没连接过的网络，正在建立新的连接...\n");
										if(passwdstr.equals("")){
											mwififunction.addNetWork(mwififunction.CreateWifiInfo(apstr,"",1));
										}else{
											mwififunction.addNetWork(mwififunction.CreateWifiInfo(apstr,passwdstr,3));
										}
									}
									else
									{
										output_edittext.append("正在建立连接...\n");
										mwififunction.connetionConfiguration(intNetworkID);
									}

									pdialog = ProgressDialog.show(WifiActivity.this, "", "正在连接网络，请等待...", true, false);

									setthread.start();
									break;
								}
							}
							if(scanfwifi==false){
								Toast.makeText(WifiActivity.this, "未搜索到相关网络！", Toast.LENGTH_SHORT).show();
								netconnect = false;
								return;
							}
						}
					}
				});
				dialog.setNegativeButton("取消",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				});
				dialog.show();
			}
		});

		connect_button.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked){
				connect_button.setChecked(isChecked);
				if(isChecked){
					if(netconnect==false)
					{
						connect_button.setChecked(false);
						Toast.makeText(WifiActivity.this, "请先设置网络参数！", Toast.LENGTH_SHORT).show();
						return;
					}else{
						isConnecting=true;
						mThreadClient = new Thread(mRunnable);
						mThreadClient.start();
						timehandler.postDelayed(timerunnable, 10);
					}
				}else{
					isConnecting=false;
					if((mSocketClient!=null) && (mSocketClient.isConnected())){
						try {
							mSocketClient.close();
							mSocketClient = null;
						} catch (IOException e) {
							//e.printStackTrace();
						}
					}
					mThreadClient.interrupt();
					mThreadClient=null;
					timehandler.removeCallbacks(timerunnable);
				}
			}
		});

		clear_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				output_edittext.setText("");
				input_edittext.setText("");
			}
		});

		exit_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});

		send_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if((mSocketClient==null)||!(mSocketClient.isConnected())){
					new AlertDialog.Builder(WifiActivity.this).setTitle("网络连接错误").setMessage("网络未连接")
							.setPositiveButton("ok", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) { }
							}).show();
					return;
				}
				if(input_edittext.getText().toString().length()==0){
					Toast.makeText(WifiActivity.this, "发送内容不能为空！", Toast.LENGTH_SHORT).show();
					return;
				}

				String send_str = input_edittext.getText().toString();
				int send_len = send_str.length();
				if(char_checkBox.isChecked()){//字符发送
					out.print(send_str);
					out.flush();
					output_edittext.append("以字符方式发送数据：\n");
					output_edittext.append(input_edittext.getText().toString()+"\n");
				}else{//ASII码发送
					char[] temp = new char[1024];
					for(int i = 0; i < send_len; ++i) {
						temp[i] = (char) (send_str.charAt(i) - '0');
					}
					String strTemp = new String(temp,0,send_len);
					try {
						data_out.writeBytes(strTemp);
						output_edittext.append("以ASCII码方式发送数据：\n");
						output_edittext.append(send_str+"\n");
					} catch (IOException e) {
						output_edittext.append("发送失败，请输入正确的数据：\n");
					}
				}

				input_edittext.setText("");
				input_edittext.clearFocus();
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(input_edittext.getWindowToken(), 0);
			}
		});
	}

	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			if(msg.what == 1){
				Toast.makeText(WifiActivity.this, "连接服务器成功", Toast.LENGTH_SHORT).show();
				return;
			}
			if(msg.what == -1){
				Toast.makeText(WifiActivity.this, "连接服务器失败,请检测网络连接！", Toast.LENGTH_SHORT).show();
				connect_button.setChecked(false);
				return;
			}
			if(msg.what == -2){
				Toast.makeText(WifiActivity.this, "断开服务器连接！", Toast.LENGTH_SHORT).show();
				return;
			}
			if(msg.what == -1000){//接收到数据
				output_edittext.append("收到数据：\n");
				output_edittext.append(data_string+"\n");
				data_string="";
			}
		}
	};
	private Runnable mRunnable	= new Runnable(){
		public void run(){
			int timeout = 2000;
			String sIP="192.168.1.10";
			String sPort="8080";
			if(remoteIP!=null)
				sIP = remoteIP.getText().toString();
			if(remoteIP!=null)
				sPort = remotePort.getText().toString();
			int port = Integer.parseInt(sPort);
			try
			{
				mSocketClient = new Socket();	//portnum
				SocketAddress isa = new InetSocketAddress(sIP, port);
				mSocketClient.connect(isa, timeout);

				out = new PrintWriter(new BufferedWriter(new OutputStreamWriter (mSocketClient.getOutputStream())),true);
				data_out = new DataOutputStream(mSocketClient.getOutputStream());
				data_in = new DataInputStream(mSocketClient.getInputStream());

				Message msg = new Message();
				msg.what = 1;
				mHandler.sendMessage(msg);
			}
			catch (Exception e)
			{
				Message msg = new Message();
				msg.what = -1;
				mHandler.sendMessage(msg);
				return;
			}

			String once_str;

			while (isConnecting)
			{
				try
				{
					if(mSocketClient.isConnected())
					{
						if (data_in.available() > 0) {
							int backLength = data_in.available();
							if(char_checkBox.isChecked()){
								//以字符方式接收
								byte[] buffer = new byte[backLength];
								data_in.read(buffer);
								once_str =new String(buffer);
							}else{
								char[] buffer = new char[backLength];
								for(int j = 0 ; j < backLength ; ++j)
								{
									buffer[j] = (char) data_in.readByte();
									buffer[j] = (char) (buffer[j] + '0');
								}
								once_str= new String(buffer);
							}
							cntRxd++;
							data_string=data_string+once_str;
						}
					}
				}
				catch (Exception e)
				{
					Message msg = new Message();
					msg.what = -2;
					mHandler.sendMessage(msg);
					return;
				}
			}
		}
	};

	private Handler sethandler = new Handler(){
		public void handleMessage(Message msg){
			super.handleMessage(msg);
			if(msg.what == 1){
				netconnect=true;
				pdialog.dismiss();
				output_edittext.append("设置成功\n");
				Toast.makeText(WifiActivity.this, "设置成功！", Toast.LENGTH_SHORT).show();
			}
		}
	};

	private Handler timehandler = new Handler();
	Runnable timerunnable = new Runnable() {
		public void run() {
			if (cntRxd > 0){
				if (cntbkp != cntRxd){
					cntbkp = cntRxd;
					idletmr = 0;
				}else {
					if (idletmr < 5){
						idletmr += 1;
						if(idletmr >= 5){
							flagFrame = true;  //设置帧接收完成标志
						}
					}
				}
			}else {
				cntbkp = 0;
			}
			if(flagFrame){
				flagFrame = false;
				cntRxd = 0;  //接收计数器清零

				Message msg = new Message();
				msg.what = -1000;
				mHandler.sendMessage(msg);
			}

			timehandler.postDelayed(this,10);
		}
	};

	private void initset(){
		try{
			FileInputStream fis=openFileInput(FILE_NAME);
			InputStreamReader isr=new InputStreamReader(fis,"utf8");
			BufferedReader br=new BufferedReader(isr);

			if(br.ready())
			{
				AP.setText(br.readLine());
				passwd.setText(br.readLine());
				remoteIP.setText(br.readLine());
				remotePort.setText(br.readLine());
			}

			fis.close();
			br.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	private void saveset(String aptr,String ipstr,String controlportstr,String videoportstr){
		try{
			FileOutputStream fos=openFileOutput(FILE_NAME,MODE_PRIVATE);
			PrintStream ps=new PrintStream(fos);
			ps.println(aptr);
			ps.println(ipstr);
			ps.println(controlportstr);
			ps.println(videoportstr);
			ps.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	protected void onDestroy() {
		if(isConnecting)
		{
			isConnecting = false;
			if((mSocketClient!=null) && (mSocketClient.isConnected())){
				try {
					mSocketClient.close();
					mSocketClient = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			mThreadClient.interrupt();
			mThreadClient=null;
		}
		super.onDestroy();
	}
}
