package com.xgx.dw.utils;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

public class OpenFileWebChromeClient extends WebChromeClient {
    public static final int REQUEST_FILE_PICKER = 1;
    public ValueCallback<Uri> mFilePathCallback;
    public ValueCallback<Uri[]> mFilePathCallbacks;
    Fragment mContext;

    public OpenFileWebChromeClient(Fragment mContext) {
        super();
        this.mContext = mContext;
    }


    // Android < 3.0 调用这个方法
    public void openFileChooser(ValueCallback<Uri> filePathCallback) {
        mFilePathCallback = filePathCallback;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        mContext.startActivityForResult(Intent.createChooser(intent, "File Chooser"), REQUEST_FILE_PICKER);
    }

    // 3.0 + 调用这个方法
    public void openFileChooser(ValueCallback filePathCallback, String acceptType) {
        mFilePathCallback = filePathCallback;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        mContext.startActivityForResult(Intent.createChooser(intent, "File Chooser"), REQUEST_FILE_PICKER);
    }

    // / js上传文件的<input type="file" name="fileField" id="fileField" />事件捕获
    // Android > 4.1.1 调用这个方法
    public void openFileChooser(ValueCallback<Uri> filePathCallback, String acceptType, String capture) {
        mFilePathCallback = filePathCallback;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        mContext.startActivityForResult(Intent.createChooser(intent, "File Chooser"), REQUEST_FILE_PICKER);
    }

    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        mFilePathCallbacks = filePathCallback;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        mContext.startActivityForResult(Intent.createChooser(intent, "File Chooser"), REQUEST_FILE_PICKER);
        return true;
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {

        Toast.makeText(mContext.getActivity(), message, Toast.LENGTH_SHORT).show();

        result.confirm();

        return true;
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {

        return super.onJsConfirm(view, url, message, result);
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {

        return super.onJsPrompt(view, url, message, defaultValue, result);
    }
}
