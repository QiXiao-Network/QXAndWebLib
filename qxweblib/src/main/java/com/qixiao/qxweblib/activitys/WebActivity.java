package com.qixiao.qxweblib.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.RelativeLayout;

import com.qixiao.qxweblib.R;
import com.qixiao.qxweblib.actionbar.ActionbarManager;
import com.qixiao.qxweblib.views.webview.QXDownloadListener;
import com.qixiao.qxweblib.views.webview.QXWebChromeClient;
import com.qixiao.qxweblib.views.webview.QXWebView;
import com.qixiao.qxweblib.views.webview.QXWebViewClient;

public class WebActivity extends AppCompatActivity {

    public static final String ARG_URL = "url";
    public static final String ARG_TITLE = "title";
    private String url;
    private String title;
    private QXWebView webView;
    private SwipeRefreshLayout swipeRefresh;

    public static void start(Context context, String url, String title) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(ARG_URL, url);
        intent.putExtra(ARG_TITLE, title);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        url = this.getIntent().getStringExtra(ARG_URL);
        title = this.getIntent().getStringExtra(ARG_TITLE);
        initActionBar();
        initWebView();
    }

    private void initWebView() {

        swipeRefresh = (SwipeRefreshLayout) this.findViewById(R.id.swipe_refresh);
        swipeRefresh.setEnabled(false);  // TODO: 2017/5/12  
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.reload();
            }
        });
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        
        webView = (QXWebView) this.findViewById(R.id.webview);
        webView.setWebChromeClient(new QXWebChromeClient());
        webView.setWebViewClient(new QXWebViewClient(this));
        webView.setDownloadListener(new QXDownloadListener(this));

        initSettings();
        webView.loadUrl(url);
    }

    private void initSettings() {

        WebSettings webSetting = webView.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setUseWideViewPort(true);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setPluginState(WebSettings.PluginState.ON);
        webSetting.setAllowFileAccess(true);

        // 设置可以使用localStrorage
        webSetting.setDomStorageEnabled(true);
    }

    private void initActionBar() {

        RelativeLayout toolbar = (RelativeLayout) findViewById(R.id.toolbar);
        ActionbarManager.getInstance().init(this, toolbar, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.toolbar_back){
                    goBack();
                } else if(v.getId() == R.id.toolbar_refresh){
                    webView.reload();
                }
            }
        });

        // 设置标题
        ActionbarManager.getInstance().setTitle(title);
    }

    /**
     * 顶部栏back键按下
     */
    private void goBack(){
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.stopView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
