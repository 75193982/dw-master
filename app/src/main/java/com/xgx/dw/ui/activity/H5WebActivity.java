package com.xgx.dw.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebBackForwardList;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsoluteLayout;
import android.widget.ProgressBar;

import com.xgx.dw.R;
import com.xgx.dw.base.BaseAppCompatActivity;
import com.xgx.dw.bean.LoginInformation;
import com.xgx.dw.utils.MediaUtility;
import com.xgx.dw.utils.OpenFileWebChromeActivityClient;
import com.xgx.dw.utils.OpenFileWebChromeClient;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;

public class H5WebActivity extends BaseAppCompatActivity {
    @Bind(R.id.ccwebview)
    WebView webview;
    private static final String TAG = "H5WebActivity";
    private String url = "";
    private ProgressBar progressbar;
    private static boolean isNetWork = true;
    private Handler handler = new Handler();
    private long timeout = 15000;
    private Timer timer;
    private WebChromeClient mOpenFileWebChromeClient;
    private String title;

    @Override
    public void initContentView() {
        baseSetContentView(R.layout.ccweb);
    }


    @Override
    public void initView() {
        isNetWork = true;
        progressbar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.MATCH_PARENT, 20, 0, 0));
        initWebView();
        // 是否允许脚本支持
        webview.addView(progressbar);
        mOpenFileWebChromeClient = new WebChromeClient(this);
        webview.setWebChromeClient(mOpenFileWebChromeClient);
        webview.setWebViewClient(new mywebviewclient());
        webview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) { // 表示按返回键
                        if (isNetWork) {
                            if (webview.canGoBack()) {
                                WebBackForwardList mWebBackForwardList = webview.copyBackForwardList();
                                int i = 0 - mWebBackForwardList.getCurrentIndex();
                                webview.goBackOrForward(i);
                            } else {
                                return true; // 已处理
                            }
                        } else {
                            return true; // 已处理
                        }
                        return true; // 已处理
                    }
                }
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case android.R.id.home:
                // 处理返回逻辑
                if (isNetWork) {
                    if (webview.canGoBack()) {
                        WebBackForwardList mWebBackForwardList = webview.copyBackForwardList();
                        int i = 0 - mWebBackForwardList.getCurrentIndex();
                        webview.goBackOrForward(i);

                    } else {
                        finish();
                    }
                } else {
                    finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void initPresenter() {
        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
        setToolbarTitle(checkText(title));
        url = "file:///android_asset/detail_pic.html";
        if (isNetworkConnected(getContext())) {
            webview.loadUrl(url);
        } else {
            webview.loadUrl("file:///android_asset/error.html");
        }
    }


    public void setUrl(String url) {
        this.url = url;
    }


    @Override
    public void onResume() {
        super.onResume();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                /*
                 * 超时后,首先判断页面加载进度,超时并且进度小于100,就执行超时后的动作
				 */
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        if (webview.getProgress() < 100) {
                            Log.d("testTimeout", "timeout...........");
                            timer.cancel();
                            timer.purge();
                        }
                    }
                });
            }
        }, timeout, 1);
    }


    public class WebChromeClient extends OpenFileWebChromeActivityClient {
        public WebChromeClient(Activity mContext) {
            super(mContext);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressbar.setVisibility(View.GONE);
            } else {
                if (progressbar.getVisibility() == View.GONE)
                    progressbar.setVisibility(View.VISIBLE);
                progressbar.setProgress(newProgress);
            }

            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }

    }

    public class mywebviewclient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        ;

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            setToolbarTitle(checkText(view.getTitle()));
            try {
                if (!view.getSettings().getLoadsImagesAutomatically()) {
                    view.getSettings().setLoadsImagesAutomatically(true);
                }
                timer.cancel();
                timer.purge();
            } catch (Exception e) {
            }
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);

        }

        @Override
        // 加载错误时，可以直接finish掉当前webview的activity。跳到列表页，提示加载出错。
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            isNetWork = false;
            view.loadUrl("file:///android_asset/error.html");
        }

    }

    // 判断网路是否连接
    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }


    private static final String APP_CACAHE_DIRNAME = "/webcache";

    private void initWebView() {
        clearWebViewCache();
        WebSettings webSettings = webview.getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 设置 缓存模式
        // 开启 DOM storage API 功能
        webSettings.setDomStorageEnabled(true);
        // 开启 database storage API 功能
        webSettings.setDatabaseEnabled(true);
        String cacheDirPath = getContext().getFilesDir().getAbsolutePath() + APP_CACAHE_DIRNAME;
        // String cacheDirPath = getCacheDir().getAbsolutePath()+Constant.APP_DB_DIRNAME;
        Log.i(TAG, "cacheDirPath=" + cacheDirPath);
        // 设置数据库缓存路径
        webSettings.setDatabasePath(cacheDirPath);
        // 设置 Application Caches 缓存目录
        webSettings.setAppCachePath(cacheDirPath);
        // 开启 Application Caches 功能
        webSettings.setAppCacheEnabled(true);

        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);//关键点
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setDisplayZoomControls(false);
        webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
        webSettings.setAllowFileAccess(true); // 允许访问文件
        webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        webSettings.setSupportZoom(true); // 支持缩放


        webSettings.setLoadWithOverviewMode(true);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;
        Log.d("maomao", "densityDpi = " + mDensity);
        if (mDensity == 240) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == 160) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        } else if (mDensity == 120) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        } else if (mDensity == DisplayMetrics.DENSITY_XHIGH) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == DisplayMetrics.DENSITY_TV) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        }


/**
 * 用WebView显示图片，可使用这个参数 设置网页布局类型： 1、LayoutAlgorithm.NARROW_COLUMNS ：
 * 适应内容大小 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
 */
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);

    }

    /**
     * 清除WebView缓存
     */
    public void clearWebViewCache() {

        // 清理Webview缓存数据库
        try {
            getContext().deleteDatabase("webview.db");
            getContext().deleteDatabase("webviewCache.db");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // WebView 缓存文件
        File appCacheDir = new File(getContext().getFilesDir().getAbsolutePath() + APP_CACAHE_DIRNAME);
        Log.e(TAG, "appCacheDir path=" + appCacheDir.getAbsolutePath());

        File webviewCacheDir = new File(getContext().getCacheDir().getAbsolutePath() + "/webviewCache");
        Log.e(TAG, "webviewCacheDir path=" + webviewCacheDir.getAbsolutePath());

        // 删除webview 缓存目录
        if (webviewCacheDir.exists()) {
            deleteFile(webviewCacheDir);
        }
        // 删除webview 缓存 缓存目录
        if (appCacheDir.exists()) {
            deleteFile(appCacheDir);
        }
    }

    /**
     * 递归删除 文件/文件夹
     *
     * @param file
     */
    public void deleteFile(File file) {

        Log.i(TAG, "delete file path=" + file.getAbsolutePath());

        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
            file.delete();
        } else {
            Log.e(TAG, "delete file no exists " + file.getAbsolutePath());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == OpenFileWebChromeClient.REQUEST_FILE_PICKER) {
            if (mOpenFileWebChromeClient.mFilePathCallback != null) {
                Uri result = intent == null || resultCode != Activity.RESULT_OK ? null : intent.getData();
                if (result != null) {
                    String path = MediaUtility.getPath(getContext(), result);
                    Uri uri = Uri.fromFile(new File(path));
                    mOpenFileWebChromeClient.mFilePathCallback.onReceiveValue(uri);
                } else {
                    mOpenFileWebChromeClient.mFilePathCallback.onReceiveValue(null);
                }
            }
            if (mOpenFileWebChromeClient.mFilePathCallbacks != null) {
                Uri result = intent == null || resultCode != Activity.RESULT_OK ? null : intent.getData();
                if (result != null) {
                    String path = MediaUtility.getPath(getContext(), result);
                    Uri uri = Uri.fromFile(new File(path));
                    mOpenFileWebChromeClient.mFilePathCallbacks.onReceiveValue(new Uri[]{uri});
                } else {
                    mOpenFileWebChromeClient.mFilePathCallbacks.onReceiveValue(null);
                }
            }

            mOpenFileWebChromeClient.mFilePathCallback = null;
            mOpenFileWebChromeClient.mFilePathCallbacks = null;
        }
    }
}
