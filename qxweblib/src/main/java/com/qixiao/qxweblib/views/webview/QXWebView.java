package com.qixiao.qxweblib.views.webview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;

/**
 * Created by liu jun on 2017/5/11.
 */

public class QXWebView extends WebView{

    public static final String ABOUT_BLANK = "about:blank";

    private QXWebChromeClient webChromeClient = null;
    private QXWebViewClient webViewClient = null;

    public boolean is_gone=true;

    public QXWebView(Context context) {
        super(context);
        init();
    }

    public QXWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init(){

    }

    public void setWebChromeClient(QXWebChromeClient webChromeClient) {
        this.webChromeClient = webChromeClient;
        super.setWebChromeClient(webChromeClient);
    }

    public void setWebViewClient(QXWebViewClient webViewClient) {
        this.webViewClient = webViewClient;
        super.setWebViewClient(webViewClient);
    }

    public void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == View.GONE) {
            try {
                WebView.class.getMethod("onPause").invoke(this);//stop flash
            } catch (Exception e) {
            }
            this.pauseTimers();
            this.is_gone = true;
        } else if (visibility == View.VISIBLE) {
            try {
                WebView.class.getMethod("onResume").invoke(this);//resume flash
            } catch (Exception e) {
            }
            this.resumeTimers();
            this.is_gone = false;
        }
    }

    /**
     * 在依附的 Activity/Fragment Destroy 时调用
     */
    public void stopView() {
        try {
            WebView.class.getMethod("onPause").invoke(this);//stop flash
        } catch (Exception e) {
        }
        this.pauseTimers();
    }

    public void resumeView() {
        try {
            WebView.class.getMethod("onResume").invoke(this);//resume flash
        } catch (Exception e) {
        }
        this.resumeTimers();
    }

    @Override
    public void loadUrl(String url) {
        if(url==null) {
            return;
        }
        if(!url.startsWith("http")){
            url = "http://" + url;
        }
        super.loadUrl(url);
    }

}
