package com.qixiao.qxweblib.views.webview;

import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.qixiao.qxweblib.actionbar.ActionbarManager;

/**
 * Created by liu jun on 2017/5/11.
 */

public class QXWebChromeClient extends WebChromeClient {

    private static final String TAG = QXWebChromeClient.class.getSimpleName();

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        ActionbarManager.getInstance().setTitle(title);
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        ActionbarManager.getInstance().setProgress(newProgress);

        Log.i(TAG, "onProgressChanged  " + newProgress);
    }
}
