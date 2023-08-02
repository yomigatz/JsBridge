
package com.github.lzyzsd.jsbridge.example.testJSLib;

import android.util.Log;
import android.webkit.JavascriptInterface;

import com.common.anni.jsbridge.BridgeWebView;
import com.common.anni.jsbridge.OnBridgeCallback;

import java.util.Map;

public class TestJSInterface extends BridgeWebView.BaseJavascriptInterface {

    private BridgeWebView mWebView;

    public TestJSInterface(Map<String, OnBridgeCallback> callbacks, BridgeWebView webView) {
        super(callbacks);
        mWebView = webView;
    }

    public TestJSInterface(Map<String, OnBridgeCallback> callbacks) {
        super(callbacks);
    }

    @Override
    public String send(String data) {
        return "it is default response";
    }


    @JavascriptInterface
    public void submitFromWeb(String data, String callbackId) {
        Log.d("TestJSInterface", data + ", callbackId: " + callbackId + " " + Thread.currentThread().getName());
        mWebView.sendResponse("submitFromWeb response", callbackId);
    }

    @JavascriptInterface
    public void js_bridge_channel(String data, String callbackId) {
        Log.d("TestJSInterface","js_bridge_channel"+ data);
        Log.d("TestJSInterface", data + ", callbackId: " + callbackId + " " + Thread.currentThread().getName());
        mWebView.sendResponse("js_bridge_channel response", callbackId);
    }
}
