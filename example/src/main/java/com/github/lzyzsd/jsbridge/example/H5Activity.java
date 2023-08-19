package com.github.lzyzsd.jsbridge.example;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.common.anni.jsbridge.BridgeWebView;
import com.common.anni.jsbridge.OnBridgeCallback;
import com.github.lzyzsd.jsbridge.example.testJSLib.TestJSInterface;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class H5Activity extends Activity implements OnClickListener {

    private final String TAG = "H5Activity";

    BridgeWebView webView;

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h5);

        webView = findViewById(R.id.webView);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);

//        webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                return super.shouldOverrideUrlLoading(view, request);
//            }
//        });
        initSetting();

        loadUrl();

    }

    private void loadUrl(){
        //加载本地html
//        String url = "file:///android_asset/brH5/index.html#/analysis?token=eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAEWMQQ7CIBRE7_LXYPhQELrqVT4FLKYKKW1iYry71I27N5mZ94b7nmEEcklbQsujCpYPyUjuMSFPQrqQlByuygCDTDuMaJxwg7FCMmiH7--zaa2D33KJnRjQEXqeF9pukVOt_F_FV_1JUGs0p4SOfSnPPm9LXFfaIk1o1GUuj1PcPSiElp8vglLWUKwAAAA.mnp1kOSZuhrGClr_YHZa_V1JHWg0sB-YPL5J8aR31zri9clEhDVIMSTIp9mETEYIYbZwXjSYdXHU7Se91i4Qhw&version=V1.1.0.20230802&apptype=Android&deviceNumber=EA09003A&env=dev&language=zh_CN";
        String url = "file:///android_asset/TestJSLib/jslib_index.html";
        webView.loadUrl(url);
        Log.d("NRRR","+++++++++1");
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initSetting() {
        //设置为ChromeClinet 才能执行js代码
        WebChromeClient webChromeClient = new WebChromeClient();
        webView.setWebChromeClient(webChromeClient);

        WebSettings settings = webView.getSettings();
        settings.setAllowFileAccess(true);
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setLoadsImagesAutomatically(true);
        settings.setBlockNetworkImage(false);
        // 禁用缓存
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setSupportMultipleWindows(false);
        settings.setAllowUniversalAccessFromFileURLs(true);

        settings.setUserAgentString(settings.getUserAgentString() + " ");
        settings.setMediaPlaybackRequiresUserGesture(true);//媒体自动播放
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        settings.setDefaultTextEncodingName("utf-8");

        webView.addJavascriptInterface(new TestJSInterface(webView.getCallbacks(),webView), "AndroidJavascriptBridge");
        webView.setGson(new Gson());

    }

    @Override
    public void onClick(View v) {
        if (button.equals(v)) {
            Log.d(TAG,"+++++++++++");
            webView.callHandler("functionInJs", "data from Java +++", new OnBridgeCallback() {
                @Override
                public void onCallBack(String data) {
                    Log.i(TAG, "reponse data from js " + data);
                }
            });
        }
    }
}