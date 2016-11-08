package com.qixiao.qxweblib.activitys;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

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
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (actionBar != null) {
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

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
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

                if (swipeRefresh.isRefreshing()) {
                    swipeRefresh.setRefreshing(false);
                }
            }
        });

        initSettings();
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                String suggestedFilename = URLUtil.guessFileName(url, contentDisposition, mimetype);
                if (suggestedFilename.endsWith("bin")) {
                    suggestedFilename = suggestedFilename.concat(".apk");
                }
                if (handleDownload(WebActivity.this, url, suggestedFilename)) {
                    Toast.makeText(WebActivity.this, "start download " + suggestedFilename, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(WebActivity.this, "download couldn't be handled because user has disabled download manager app on the device", Toast.LENGTH_SHORT).show();
                }
            }
        });
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

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        actionBar = getSupportActionBar();
        if (actionBar == null)
            return;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(title);
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
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean handleDownload(final Context context, final String fromUrl, final String toFilename) {
        if (Build.VERSION.SDK_INT < 9) {
            throw new RuntimeException("Method requires API level 9 or above");
        }

        final DownloadManager.Request request = new DownloadManager.Request(Uri.parse(fromUrl));
        if (Build.VERSION.SDK_INT >= 11) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, toFilename);

        final DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        try {
            try {
                dm.enqueue(request);
            } catch (SecurityException e) {
                if (Build.VERSION.SDK_INT >= 11) {
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                }
                dm.enqueue(request);
            }

            return true;
        }
        // if the download manager app has been disabled on the device
        catch (IllegalArgumentException e) {
            // show the settings screen where the user can enable the download manager app again
            openAppSettings(context, getPackageName());
            return false;
        }
    }

    @SuppressLint("NewApi")
    private boolean openAppSettings(final Context context, final String packageName) {
        if (Build.VERSION.SDK_INT < 9) {
            throw new RuntimeException("Method requires API level 9 or above");
        }

        try {
            final Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + packageName));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(intent);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
