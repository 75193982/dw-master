package com.xgx.dw.base;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.xgx.dw.R;
import com.xgx.dw.net.AsyncHttpNetCenter;
import com.xgx.dw.ui.custom.CustomConfirmDialog;

import butterknife.Bind;
import butterknife.ButterKnife;

public abstract class BaseAppCompatActivity extends AppCompatActivity implements IBaseView {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    public static int REQUSET_CODE_WRITE_EXTERNAL_STORAGE = 1001;

    public FloatingActionButton getFab() {
        return fab;
    }

    private ProgressDialog mProgressDialog;
    FragmentManager fragmentManager;
    private int resId;

    /**
     * 初始化布局
     */
    public abstract void initContentView();


    /**
     * 初始化控件
     */
    public abstract void initView();

    /**
     * 初始化控制中心
     */
    public abstract void initPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView();
        ButterKnife.bind(this);
        // 隐藏标题栏
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
        initPresenter();
        //检查SD卡写权限授予情况
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUSET_CODE_WRITE_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1001:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //授权成功，直接操作
                } else {
                    //禁止授权
                }
                break;
        }
    }


    public void setToolbarTitle(String s) {
        toolbarTitle.setText(s);
    }

    public String getToolbarTitle() {
        return toolbarTitle.getText().toString();
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setLayoutResId(int resId) {
        this.resId = resId;
    }


    public void baseSetContentView(int layoutResId) {
        setContentView(R.layout.activity_base_compatactivity);
        LinearLayout contentLayout = (LinearLayout) findViewById(R.id.contentLayout);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(layoutResId, null);
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        contentLayout.addView(v);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case android.R.id.home:
                // 处理返回逻辑
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void finish() {
        // 清除网络请求队列
        AsyncHttpNetCenter.getInstance().clearRequestQueue(this);
        super.finish();
    }

    /**
     * 显示单选对话框
     *
     * @param title           标题
     * @param message         提示信息
     * @param strings         选项数组
     * @param checkedItem     默认选中
     * @param onClickListener 点击事件的监听
     */
    public void showRadioButtonDialog(String title, String message, String[] strings, int checkedItem, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        if (!TextUtils.isEmpty(message)) {
            builder.setMessage(message);
        }
        builder.setSingleChoiceItems(strings, checkedItem, onClickListener);
        builder.create();
        builder.show();
    }

    /**
     * 显示单选对话框
     *
     * @param title           标题
     * @param strings         选项数组
     * @param onClickListener 点击事件的监听
     */
    public void showRadioButtonDialog(String title, String[] strings, DialogInterface.OnClickListener onClickListener) {
        showRadioButtonDialog(title, null, strings, 0, onClickListener);
    }

    /**
     * 弹出自定义对话框
     */
    public void showConfirmDialog(String title, View.OnClickListener positiveListener) {
        CustomConfirmDialog confirmDialog = new CustomConfirmDialog(this, title, positiveListener);
        confirmDialog.show();
    }

    @Override
    public void showProgress(boolean flag, String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(flag);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setMessage(message);
        }

        mProgressDialog.show();
    }

    @Override
    public void showProgress(String message) {
        showProgress(true, message);
    }

    @Override
    public void showProgress() {
        showProgress(true);
    }

    @Override
    public void showProgress(boolean flag) {
        showProgress(flag, "");
    }

    @Override
    public void hideProgress() {
        if (mProgressDialog == null) return;

        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void showToast(int resId) {
        showToast(getString(resId));
    }

    @Override
    public void showToast(String msg) {
        if (!isFinishing()) {

            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void close() {
        finish();
    }

    /**
     * @param text
     */
    public String checkText(String text) {
        return TextUtils.isEmpty(text) ? "" : text;
    }

    //--------------------------Fragment相关--------------------------//

    /**
     * 获取Fragment管理器
     *
     * @return
     */
    public FragmentManager getBaseFragmentManager() {
        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
        }
        return fragmentManager;
    }

    /**
     * 获取Fragment事物管理
     *
     * @return
     */
    public FragmentTransaction getFragmentTransaction() {
        return getBaseFragmentManager().beginTransaction();
    }

    /**
     * 替换一个Fragment
     *
     * @param res
     * @param fragment
     */
    public void replaceFragment(int res, BaseFragment fragment) {
        replaceFragment(res, fragment, false);
    }

    /**
     * 替换一个Fragment并设置是否加入回退栈
     *
     * @param res
     * @param fragment
     * @param isAddToBackStack
     */
    public void replaceFragment(int res, BaseFragment fragment, boolean isAddToBackStack) {
        FragmentTransaction fragmentTransaction = getFragmentTransaction();
        fragmentTransaction.replace(res, fragment);
        if (isAddToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();

    }

    /**
     * 添加一个Fragment
     *
     * @param res
     * @param fragment
     */
    public void addFragment(int res, Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentTransaction();
        fragmentTransaction.add(res, fragment);
        fragmentTransaction.commit();
    }

    /**
     * 移除一个Fragment
     *
     * @param fragment
     */
    public void removeFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentTransaction();
        fragmentTransaction.remove(fragment);
        fragmentTransaction.commit();
    }

    /**
     * 显示一个Fragment
     *
     * @param fragment
     */
    public void showFragment(Fragment fragment) {
        if (fragment.isHidden()) {
            FragmentTransaction fragmentTransaction = getFragmentTransaction();
            fragmentTransaction.show(fragment);
            fragmentTransaction.commit();
        }
    }

    /**
     * 隐藏一个Fragment
     *
     * @param fragment
     */
    public void hideFragment(Fragment fragment) {
        if (!fragment.isHidden()) {
            FragmentTransaction fragmentTransaction = getFragmentTransaction();
            fragmentTransaction.hide(fragment);
            fragmentTransaction.commit();
        }
    }


    //--------------------------Fragment相关end--------------------------//
}
