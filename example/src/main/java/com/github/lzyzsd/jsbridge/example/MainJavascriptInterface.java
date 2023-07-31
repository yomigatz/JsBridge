package com.github.lzyzsd.jsbridge.example;

import android.util.Log;
import android.webkit.JavascriptInterface;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.OnBridgeCallback;

import java.util.Map;

/**
 * Created on 2019/7/10.
 * Author: bigwang
 * Description:
 */
public class MainJavascriptInterface extends BridgeWebView.BaseJavascriptInterface {

    private BridgeWebView mWebView;

    public MainJavascriptInterface(Map<String, OnBridgeCallback> callbacks, BridgeWebView webView) {
        super(callbacks);
        mWebView = webView;
    }

    public MainJavascriptInterface(Map<String, OnBridgeCallback> callbacks) {
        super(callbacks);
    }

    @Override
    public String send(String data) {
        return "it is default response";
    }


    @JavascriptInterface
    public void submitFromWeb(String data, String callbackId) {
        Log.d("MainJavascriptInterface", data + ", callbackId: " + callbackId + " " + Thread.currentThread().getName());
        mWebView.sendResponse("submitFromWeb response", callbackId);
    }

    @JavascriptInterface
    public void test1(String data, String callbackId) {
        Log.d("MainJavascriptInterface","test1 >>>>"+ data);
//        mWebView.sendResponse("submitFromWeb response", callbackId);
    }



    @JavascriptInterface
    public void js_bridge_channel(String data, String callbackId) {
        Log.d("MainJavascriptInterface","js_bridge_channel"+ data);
        Log.d("MainJavascriptInterface", data + ", callbackId: " + callbackId + " " + Thread.currentThread().getName());
        mWebView.sendResponse("js_bridge_channel response", callbackId);
    }
}
