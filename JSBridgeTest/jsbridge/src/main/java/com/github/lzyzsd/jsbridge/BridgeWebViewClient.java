package com.github.lzyzsd.jsbridge;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.view.KeyEvent;
import android.webkit.ClientCertRequest;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by bruce on 10/28/15.
 */
public class BridgeWebViewClient extends WebViewClient {
    private BridgeWebView webView;
    private WebViewClient targetWebViewClient;

    public BridgeWebViewClient(BridgeWebView webView) {
        this.webView = webView;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        try {
            url = URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (url.startsWith(BridgeUtil.YY_RETURN_DATA)) { // 如果是返回数据
            webView.handlerReturnData(url);
            return true;
        } else if (url.startsWith(BridgeUtil.YY_OVERRIDE_SCHEMA)) { //
            webView.flushMessageQueue();
            return true;
        } else if (targetWebViewClient != null){
            return targetWebViewClient.shouldOverrideUrlLoading(view, url);
        } else {
            return super.shouldOverrideUrlLoading(view, url);
        }
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        if (targetWebViewClient != null) {
            targetWebViewClient.onPageStarted(view, url, favicon);
        }
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);

        if (BridgeWebView.toLoadJs != null) {
            BridgeUtil.webViewLoadLocalJs(view, BridgeWebView.toLoadJs);
        }

        //
        if (webView.getStartupMessage() != null) {
            for (Message m : webView.getStartupMessage()) {
                webView.dispatchMessage(m);
            }
            webView.setStartupMessage(null);
        }
        if (targetWebViewClient != null) {
            targetWebViewClient.onPageFinished(view, url);
        }
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        if (targetWebViewClient != null) {
            targetWebViewClient.onReceivedError(view, errorCode, description, failingUrl);
        }
    }

    @Override
    public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
        super.doUpdateVisitedHistory(view, url, isReload);
        if (targetWebViewClient != null) {
            targetWebViewClient.doUpdateVisitedHistory(view, url, isReload);
        }
    }

    @Override
    public void onFormResubmission(WebView view, android.os.Message dontResend, android.os.Message resend) {
        super.onFormResubmission(view, dontResend, resend);
        if (targetWebViewClient != null) {
            targetWebViewClient.onFormResubmission(view, dontResend, resend);
        }
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        super.onLoadResource(view, url);
        if (targetWebViewClient != null) {
            targetWebViewClient.onLoadResource(view, url);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onPageCommitVisible(WebView view, String url) {
        super.onPageCommitVisible(view, url);
        if (targetWebViewClient != null) {
            targetWebViewClient.onPageCommitVisible(view, url);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
        super.onReceivedClientCertRequest(view, request);
        if (targetWebViewClient != null) {
            targetWebViewClient.onReceivedClientCertRequest(view, request);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        if (targetWebViewClient != null) {
            targetWebViewClient.onReceivedError(view, request, error);
        }
    }

    @Override
    public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
        super.onReceivedHttpAuthRequest(view, handler, host, realm);
        if (targetWebViewClient != null) {
            targetWebViewClient.onReceivedHttpAuthRequest(view, handler, host, realm);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
        super.onReceivedHttpError(view, request, errorResponse);
        if (targetWebViewClient != null) {
            targetWebViewClient.onReceivedHttpError(view, request, errorResponse);
        }
    }

    @Override
    public void onReceivedLoginRequest(WebView view, String realm, String account, String args) {
        super.onReceivedLoginRequest(view, realm, account, args);
        if (targetWebViewClient != null) {
            targetWebViewClient.onReceivedLoginRequest(view ,realm, account, args);
        }
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        super.onReceivedSslError(view, handler, error);
        if (targetWebViewClient != null) {
            targetWebViewClient.onReceivedSslError(view, handler, error);
        }
    }

    @Override
    public void onScaleChanged(WebView view, float oldScale, float newScale) {
        super.onScaleChanged(view, oldScale, newScale);
        if (targetWebViewClient != null) {
            targetWebViewClient.onScaleChanged(view, oldScale, newScale);
        }
    }

    @Override
    public void onTooManyRedirects(WebView view, android.os.Message cancelMsg, android.os.Message continueMsg) {
        super.onTooManyRedirects(view, cancelMsg, continueMsg);
        if (targetWebViewClient != null) {
            targetWebViewClient.onTooManyRedirects(view, cancelMsg, continueMsg);
        }
    }

    @Override
    public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
        super.onUnhandledKeyEvent(view, event);
        if (targetWebViewClient != null) {
            targetWebViewClient.onUnhandledKeyEvent(view, event);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        if (targetWebViewClient != null) {
            return targetWebViewClient.shouldInterceptRequest(view, request);
        }
        return super.shouldInterceptRequest(view, request);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        if (targetWebViewClient != null) {
            return targetWebViewClient.shouldInterceptRequest(view, url);
        }
        return super.shouldInterceptRequest(view, url);
    }

    @Override
    public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
        if (targetWebViewClient != null) {
            return targetWebViewClient.shouldOverrideKeyEvent(view, event);
        }
        return super.shouldOverrideKeyEvent(view, event);
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        if (targetWebViewClient != null) {
            targetWebViewClient.shouldOverrideUrlLoading(view, request);
        }
        return super.shouldOverrideUrlLoading(view, request);
    }

    public void setTargetWebViewClient(WebViewClient targetWebViewClient) {
        this.targetWebViewClient = targetWebViewClient;
    }
}