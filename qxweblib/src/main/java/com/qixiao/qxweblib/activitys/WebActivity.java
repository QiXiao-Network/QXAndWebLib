package com.qixiao.qxweblib.activitys;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.qixiao.qxweblib.R;

public class WebActivity extends AppCompatActivity {

    public static final String ARG_URL = "url";
    public static final String ARG_TITLE = "title";
    private String url;
    private String title;
    private WebView webView;
    private ActionBar actionBar;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefresh;

    public static void start(Context context, String url, String title){
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
        swipeRefresh.setEnabled(true);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.reload();
            }
        });
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorAccent));

        progressBar = (ProgressBar) this.findViewById(R.id.web_progressbar);
        webView = (WebView) this.findViewById(R.id.webview);
        webView.setWebChromeClient(new WebChromeClient(){

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if(actionBar != null) {
                    actionBar.setTitle(title);
                }
            }

        });

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Animation showAction = new AlphaAnimation(0, 1);
                showAction.setDuration(200);
                progressBar.startAnimation(showAction);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Animation hiddenAction = new AlphaAnimation(1, 0);
                hiddenAction.setDuration(500);
                progressBar.startAnimation(hiddenAction);
                progressBar.setVisibility(View.GONE);

                if(swipeRefresh.isRefreshing()){
                    swipeRefresh.setRefreshing(false);
                }
            }
        });
        initSettings();

        webView.loadUrl(url);
    }

    private void initSettings(){

        WebSettings webSetting=webView.getSettings();
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

    private void initActionBar(){

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        actionBar = getSupportActionBar();
        if(actionBar == null)
            return ;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
