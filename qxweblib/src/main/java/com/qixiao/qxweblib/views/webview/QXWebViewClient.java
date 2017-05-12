package com.qixiao.qxweblib.views.webview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.MailTo;
import android.net.Uri;
import android.net.http.SslError;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.qixiao.qxweblib.actionbar.ActionbarManager;
import com.qixiao.qxweblib.util.Utils;


/**
 * Created by liu jun on 2017/5/11.
 */

public class QXWebViewClient extends WebViewClient{


    private static final String TAG = QXWebViewClient.class.getSimpleName();
    Context context;

    public QXWebViewClient(Context context) {
        this.context = context;
    }


    public boolean shouldOverrideUrlLoading(WebView view, String url) {

        // Log.i(TAG, "shouldOverrideUrl  " + url);

        // handle mailto: and tel: links with native apps
        if (url.startsWith("mailto:")) {
            MailTo mt = MailTo.parse(url);
            Intent i = Utils.newEmailIntent(mt.getTo(), mt.getSubject(), mt.getBody(), mt.getCc());
            context.startActivity(i);
            return true;
        } else if (url.startsWith("tel:")) {
            Intent i = Utils.newTelIntent(url);
            context.startActivity(i);
            return true;
        } else if (url.startsWith("file:///android_asset/webkit/")) {
            return false;
        } else if (!(url.startsWith("http:") || url.startsWith("https:"))) {
            // custom handling, there can be a related app
            Intent customIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            Utils.execIntentIfSafe(context, customIntent);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        ActionbarManager.getInstance().setProgressBarVisible(true);

        Log.i(TAG, "onPageStarted  " + url);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        ActionbarManager.getInstance().setProgressBarVisible(false);

        Log.i(TAG, "onPageFinished  " + url);
    }

    @Override
    public void onReceivedSslError (WebView view, SslErrorHandler handler, SslError error) {

        super.onReceivedSslError(view, handler, error);
    }

}
