package com.xgx.dw.ui.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.xgx.dw.R;
import com.xgx.dw.UserBean;
import com.xgx.dw.app.G;
import com.xgx.dw.app.Setting;
import com.xgx.dw.base.BaseEventBusActivity;
import com.xgx.dw.base.EventCenter;
import com.xgx.dw.bean.LoginInformation;
import com.xgx.dw.net.DialogCallback;
import com.xgx.dw.net.LzyResponse;
import com.xgx.dw.net.URLs;
import com.xgx.dw.utils.MyUtils;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/7.
 */

public class PhoneCodeActivity extends BaseEventBusActivity {
    @BindView(R.id.contentTv)
    TextView contentTv;
    @BindView(R.id.phoneTv)
    EditText phoneTv;
    @BindView(R.id.phoneEt)
    EditText phoneEt;
    @BindView(R.id.resetClockTv)
    TextView resetClockTv;
    @BindView(R.id.commitBtn)
    Button commitBtn;
    private int length;
    private UserBean user;

    @Override
    protected void onEventComming(EventCenter eventCenter) {

    }

    @Override
    public boolean isBindEventBusHere() {
        return true;
    }

    @Override
    public void initContentView() {
        baseSetContentView(R.layout.activity_phone_code);
    }

    @Override
    public void initView() {
        setToolbarTitle("验证手机号");
        commitBtn.setEnabled(false);
        phoneTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                length = editable.toString().length();
                if (length >= 6) {
                    commitBtn.setEnabled(true);
                } else {
                    commitBtn.setEnabled(false);
                }

            }
        });
    }

    @Override
    public void initPresenter() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
            /*if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){

                return;
            }*/
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, REQUEST_CODE_ASK_CALL_PHONE);
        } else {
            intentFilter = new IntentFilter();
            intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
            intentFilter.setPriority(Integer.MAX_VALUE);
            registerReceiver(receiver, intentFilter);
        }
        user = (UserBean) getIntent().getSerializableExtra("user");
        //  contentTv.setText("验证码已发送到 +86 " + user.getPhone());

        //  sendCode(phone);
    }


    @OnClick({R.id.commitBtn, R.id.resetClockTv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.commitBtn:
                if (!TextUtils.isEmpty(smsContent) && phoneTv.getText().toString().equals(smsContent)) {
                    //验证成功
                    Setting setting = new Setting(this);
                    setting.saveString(G.currentUserLoginPhone, phoneEt.getText().toString());
                    setting.saveBoolean(user.getUserId() + user.getPhone() + phoneEt.getText().toString(), true);
                    setLoginInfomation(user);
                    startActivity(new Intent(PhoneCodeActivity.this, MainActivity.class));

                } else {
                    ToastUtils.showShort("请输入正确的验证码");
                }
                break;
            case R.id.resetClockTv:
                if (isSend) {
                    //验证手机号是否在用户方的手机号队列里
                    if (TextUtils.isEmpty(phoneEt.getText().toString()) || !RegexUtils.isMobileExact(phoneEt.getText().toString())) {
                        ToastUtils.showShort("请输入正确的手机号");
                        break;
                    }
                    if (!user.getPhone().contains(phoneEt.getText().toString())) {
                        ToastUtils.showShort("你输入的手机号不属于该设备用户方");
                        break;
                    }
                    sendCode(phoneEt.getText().toString());
                }
                break;
        }
    }

    private void setLoginInfomation(UserBean userBean) {
        Setting setting = new Setting(this);
        setting.saveString(G.currentUsername, userBean.getUserId());
        setting.saveString(G.currentUserType, userBean.getType());
        setting.saveString(G.currentStoreId, userBean.getStoreId());
        setting.saveString(G.currentStoreName, userBean.getStoreName());
        setting.saveString(G.currentTransformId, userBean.getTransformerId());
        setting.saveString(G.currentTransformName, userBean.getTransformerName());
        setting.saveString(G.currentPassword, userBean.getPassword());
        setting.saveString("user", new Gson().toJson(userBean));
        LoginInformation.getInstance().setUser(userBean);
    }

    private void sendCode(String phone) {
        OkGo.cancelTag(OkGo.getInstance().getOkHttpClient(), this);
        OkGo.<LzyResponse<String>>post(URLs.getURL(URLs.MTUSER_CODE)).params("mobileNo", phone).tag(this).execute(new DialogCallback<LzyResponse<String>>(this) {
            @Override
            public void onSuccess(Response<LzyResponse<String>> response) {
                smsContent = response.body().message;
                ToastUtils.showShort("验证码已发送");
                countDown();
            }

        });


    }


    private final Handler timerhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 100) {
                cleanSendTimerTask();
                resetClockTv.setText("获取验证码");
                i = 60;
                resetClockTv.setTextColor(getResources().getColor(R.color.Green));
            } else {
                resetClockTv.setText("剩余" + msg.what + "秒");
                resetClockTv.setTextColor(getResources().getColor(R.color.Gray));

            }
        }
    };

    private void cleanSendTimerTask() {
        if (task != null) {
            task.cancel();
            task = null;
        }
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    private Timer timer;
    private TimerTask task;
    private int i = 60;
    private boolean isSend = Boolean.TRUE;

    private void countDown() {
        task = new TimerTask() {
            @Override
            public void run() {
                i = i - 1;
                Log.i("timer", i + "");
                Message message = new Message();
                message.what = i;
                isSend = Boolean.FALSE;
                if (i == 0) {
                    message.what = 100;
                    isSend = Boolean.TRUE;
                }
                timerhandler.sendMessage(message);
            }
        };
        timer = new Timer();
        timer.schedule(task, 0, 1000);


    }

    private String smsContent;
    private IntentFilter intentFilter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            phoneTv.setText(smsContent);
        }
    };

    private String patternCoder = "(?<!\\d)\\d{6}(?!\\d)";
    final public static int REQUEST_CODE_ASK_CALL_PHONE = 123;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                Object[] objs = (Object[]) intent.getExtras().get("pdus");
                String format = intent.getStringExtra("format");//23以后需要的
                if (format != null) {
                    Log.e("format", format);
                }
                for (Object obj : objs) {
                    byte[] pdu = (byte[]) obj;
                    SmsMessage sms = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        sms = SmsMessage.createFromPdu(pdu, format);//API23以后修改成这个
                    } else {
                        sms = SmsMessage.createFromPdu(pdu);
                    }
                    // 短信的内容
                    String message = sms.getMessageBody();
                    Log.e("logo", "message     " + message);
                    // 短息的手机号。。+86开头？
                    String from = sms.getOriginatingAddress();
                    Log.e("logo", "from     " + from);
                    if (!TextUtils.isEmpty(from)) {
                        String code = patternCode(message);
                        if (!TextUtils.isEmpty(code)) {
                            smsContent = code;
                            handler.sendEmptyMessage(1);
                        }
                    }
                }
            } catch (Exception e) {

            }

        }
    };


    /***
     * 获取权限回调
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_CALL_PHONE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    intentFilter = new IntentFilter();
                    intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
                    intentFilter.setPriority(Integer.MAX_VALUE);
                    registerReceiver(receiver, intentFilter);
                } else {
                    // Permission Denied
                    intentFilter = new IntentFilter();
                    intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
                    intentFilter.setPriority(Integer.MAX_VALUE);
                    registerReceiver(receiver, intentFilter);
                    Toast.makeText(PhoneCodeActivity.this, "CALL_PHONE Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * 匹配短信中间的6个数字（验证码等）
     *
     * @param patternContent
     * @return
     */
    private String patternCode(String patternContent) {
        if (TextUtils.isEmpty(patternContent)) {
            return null;
        }
        Pattern p = Pattern.compile(patternCoder);
        Matcher matcher = p.matcher(patternContent);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != receiver) {
            unregisterReceiver(receiver);
        }

    }
}
