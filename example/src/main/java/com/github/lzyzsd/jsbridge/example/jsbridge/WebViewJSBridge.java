package com.github.lzyzsd.jsbridge.example.jsbridge;


/**
 * Time: 2023/8/1
 */
public interface WebViewJSBridge {

    void sendToWeb(String data);

    void sendToWeb(String data, OnBridgeCallback responseCallback);

    void sendToWeb(String function, Object... values);
}