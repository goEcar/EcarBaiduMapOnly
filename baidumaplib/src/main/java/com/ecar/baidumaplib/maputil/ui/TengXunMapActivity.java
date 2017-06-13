package com.ecar.baidumaplib.maputil.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import com.ecar.baidumaplib.R;


/*************************************
 功能：
 创建者： kim_tony
 创建日期：2017/6/13
 版权所有：深圳市亿车科技有限公司
 *************************************/

public class TengXunMapActivity extends Activity {
    private WebView detailsWebView;
    private String mUrl;
    private Handler handler;
    private ProgressDialog pd;
    private String mTitle;

    public static void launch(Activity activity, String url, String title) {
        Intent intent = new Intent(activity, TengXunMapActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_txmap);
        mUrl = this.getIntent().getStringExtra("url");
        mTitle = this.getIntent().getStringExtra("title");
        detailsWebView = (WebView) findViewById(R.id.web_wv);

//        detailsWebView.requestFocus(View.FOCUS_DOWN);
//        detailsWebView.getSettings().setUserAgentString("User-Agent");

        WebSettings webSetting = detailsWebView.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(true);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);

        detailsWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });


        // 设置web视图客户端
        detailsWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {// 载入进度改变而触发
                if (progress == 100) {
                    handler.sendEmptyMessage(1);// 如果全部载入,隐藏进度对话框 }
                    detailsWebView.setVisibility(View.VISIBLE);
                }
                super.onProgressChanged(view, progress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });
        detailsWebView.setVisibility(View.GONE);
        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("数据载入中，请稍候！");
        handler = new Handler() {
            public void handleMessage(Message msg) {// 定义一个Handler，用于处理下载线程与UI间通讯
                super.handleMessage(msg);
                if (!Thread.currentThread().isInterrupted()) {
                    switch (msg.what) {
                        case 0:
                            pd.show();// 显示进度对话框
                            break;
                        case 1:
                            pd.hide();// 隐藏进度对话框，不可使用dismiss()、cancel(),否则再次调用show()时，显示的对话框小圆圈不会动。
                            break;
                    }
                }
            }
        };
        loadurl(detailsWebView, mUrl);
    }

    public void loadurl(final WebView view, final String url) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
                view.loadUrl(url);// 载入网页
            }
        });
    }
}