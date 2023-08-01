
package com.github.lzyzsd.jsbridge.example;

import android.util.Log;
import android.webkit.JavascriptInterface;

import com.common.anni.jsbridge.BridgeWebView;
import com.common.anni.jsbridge.OnBridgeCallback;

import java.util.Map;

/**
 * Created on 2019/7/10.
 * Author: bigwang
 * Description:
 */
public class MainJavascriptInterface2 extends BridgeWebView.BaseJavascriptInterface {

    private BridgeWebView mWebView;

    public MainJavascriptInterface2(Map<String, OnBridgeCallback> callbacks, BridgeWebView webView) {
        super(callbacks);
        mWebView = webView;
    }

    public MainJavascriptInterface2(Map<String, OnBridgeCallback> callbacks) {
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
