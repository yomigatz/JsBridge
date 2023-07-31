package com.github.lzyzsd.jsbridge.example;

import android.app.Application;
import android.os.Build;
import android.webkit.WebView;

/**
 * Time: 2023/7/29
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WebView.enableSlowWholeDocumentDraw();
        }
    }
}
