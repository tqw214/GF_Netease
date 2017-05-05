package com.viger.test;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.viger.netease.R;

/**
 * Created by Administrator on 2017/4/21.
 */

public class WebViewTestActivity extends Activity {

    private WebView mWebView;
    private Button js;

    @JavascriptInterface
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_test);

        mWebView = (WebView) findViewById(R.id.webView_test);
        js = (Button) findViewById(R.id.js);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.loadUrl("file:///android_asset/my.html");

        mWebView.addJavascriptInterface(this, "demo");

        js.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWebView.loadUrl("javascript:changeImage01()");
            }
        });
    }

    public void inPut(String name) {
        Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
    }


//        mWebView.setWebViewClient(new WebViewClient(){
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                Log.i("tag","==>shouldOverrideUrlLoading" + url);
//                view.loadUrl(url);
//                return true;
//            }
//
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                super.onPageStarted(view, url, favicon);
//                Log.i("tag","onPageStarted==>" + url);
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//                Log.i("tag","onPageFinished==>" + url);
//            }
//
//            @Override
//            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                super.onReceivedError(view, errorCode, description, failingUrl);
//                Log.i("tag","onReceivedError_过时==>" + errorCode);
//            }
//
//            @Override
//            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//                super.onReceivedError(view, request, error);
//                Log.i("tag","onReceivedError==>" + error.toString());
//            }
//
//            @Override
//            public void onLoadResource(WebView view, String url) {
//                super.onLoadResource(view, url);
//                Log.i("tag","onLoadResource==>" + url);
//            }
//        });

//        mWebView.setWebChromeClient(new WebChromeClient(){
//
//            @Override
//            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
//                Log.i("tag", "onJsAlert==>"+message);
//                result.confirm();
//                click();
//                return true;
//            }
//        });




    public void click() {
        Toast.makeText(this, "js", Toast.LENGTH_SHORT).show();
    }
}
