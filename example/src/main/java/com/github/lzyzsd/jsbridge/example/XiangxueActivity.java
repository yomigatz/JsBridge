package com.github.lzyzsd.jsbridge.example;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class XiangxueActivity extends Activity implements OnClickListener {

    private final String TAG = "MainActivity";

    WebView webView;

    Button button;

    int RESULT_CODE = 0;

    ValueCallback<Uri> mUploadMessage;

    ValueCallback<Uri[]> mUploadMessageArray;

    static class Location {
        String address;
    }

    static class User {
        String name;
        Location location;
        String testStr;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiangxue);

        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        //设置为ChromeClinet 才能执行js代码
        WebChromeClient webChromeClient = new WebChromeClient();
        webView.setWebChromeClient(webChromeClient);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        //加载本地html
        webView.loadUrl("file:///android_asset/xiangxue/my_index.html");

        String jsCommand = assetFile2Str(this, "xiangxue/xiangxuejs.js");
        webView.loadUrl("javascript:" + jsCommand);

        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(this);


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }
        });

        initSetting();
        webView.addJavascriptInterface(this, "main");

    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initSetting() {
        WebSettings settings = webView.getSettings();
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
        try {//本地HTML里面有跨域的请求 原生webview需要设置之后才能实现跨域请求
            if (Build.VERSION.SDK_INT >= 16) {
                Class<?> clazz = webView.getSettings().getClass();
                Method method = clazz.getMethod(
                        "setAllowUniversalAccessFromFileURLs", boolean.class);
                if (method != null) {
                    method.invoke(webView.getSettings(), true);
                }
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == RESULT_CODE) {
            if (null == mUploadMessage && null == mUploadMessageArray) {
                return;
            }
            if (null != mUploadMessage && null == mUploadMessageArray) {
                Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            }

            if (null == mUploadMessage && null != mUploadMessageArray) {
                Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
                if (result != null) {
                    mUploadMessageArray.onReceiveValue(new Uri[]{result});
                }
                mUploadMessageArray = null;
            }

        }
    }

    @Override
    public void onClick(View v) {
    }

    public String assetFile2Str(Context c, String urlStr) {
        InputStream in = null;
        try {
            in = c.getAssets().open(urlStr);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            String line = null;
            StringBuilder sb = new StringBuilder();
            do {
                line = bufferedReader.readLine();
                if (line != null && !line.matches("^\\s*\\/\\/.*")) {
                    sb.append(line);
                }
            } while (line != null);

            bufferedReader.close();
            in.close();

            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }

    @JavascriptInterface
    public void click1(String msg) {
        Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_SHORT).show();
        try {
            JSONObject jsonObject=new JSONObject(msg);
            JSONObject param = jsonObject.getJSONObject("param");
            String callbackname = param.getString("callbackname");
            handleCallback(callbackname,"333333");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleCallback(final String callbackname, final String response){
        if(!TextUtils.isEmpty(callbackname) && !TextUtils.isEmpty(response)){
            webView.post(new Runnable() {
                @Override
                public void run() {
                    String jscode = "javascript:xiangxuejs.callback('" + callbackname + "'," + response + ")";
                    Log.e("xxxxxx", jscode);
                    ValueCallback<String> resultCallback=new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            Log.e("2222222", value);
                        }
                    };
                    webView.evaluateJavascript(jscode, resultCallback);
                }
            });
        }
    }
}