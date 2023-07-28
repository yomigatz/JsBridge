package com.github.lzyzsd.jsbridge.example;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import com.common.anni.lib.brh5.H5SourceManager;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.OnBridgeCallback;
import com.google.gson.Gson;

public class MainActivity extends Activity implements OnClickListener {

	private final String TAG = "MainActivity";

	BridgeWebView webView;

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
		setContentView(R.layout.activity_main);

        webView = (BridgeWebView) findViewById(R.id.webView);

		button = (Button) findViewById(R.id.button);

		button.setOnClickListener(this);


		webView.setWebChromeClient(new WebChromeClient() {

			@SuppressWarnings("unused")
			public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType, String capture) {
				this.openFileChooser(uploadMsg);
			}

			@SuppressWarnings("unused")
			public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType) {
				this.openFileChooser(uploadMsg);
			}

			public void openFileChooser(ValueCallback<Uri> uploadMsg) {
				mUploadMessage = uploadMsg;
				pickFile();
			}

			@Override
			public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
				mUploadMessageArray = filePathCallback;
				pickFile();
				return true;
			}
		});

		initSetting();

		webView.addJavascriptInterface(new MainJavascriptInterface(webView.getCallbacks(), webView), "WebViewJavascriptBridge");
		webView.setGson(new Gson());
//		webView.loadUrl("file:///android_asset/test/demo.html");
//		webView.loadUrl("file:///android_asset/"+ H5SourceManager.ROOT_PATH +"/index.html#/analysis?");
		String url = "file:///android_asset/brH5/index.html#/analysis?token=eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAEWMwQ6DIBBE_2XPbsOCoHjyVxYK1cZWA5g0afrvhfTQ25vMzHvDvawwQc_RaWElylE57EMkZK8jxpGMYGUGrQR0sHKBiYwVauhJUgf5dPXdmpwruLTuoVIHfF5r9gunW0A-DvxX4XX8JHYYbZPwWZb9Wed5CdvGKfBMRl38_mji6iEhtPx8Acf4DQesAAAA.1Gs0t2EZlD3PdMGAdssjcEV5E0O9-HYP06LM3tkh4YMmxzm9d67MxbN4iqZtISKiFU0-_-SUk4F8f2x8rEjcyg&version=V1.1.0.20230728&apptype=Android&deviceNumber=EA09003A&env=dev&language=zh_CN";
		webView.loadUrl(url);
        User user = new User();
        Location location = new Location();
        location.address = "SDU";
        user.location = location;
        user.name = "大头鬼";

        webView.callHandler("functionInJs", new Gson().toJson(user), new OnBridgeCallback() {
            @Override
            public void onCallBack(String data) {
				Log.d(TAG, "onCallBack: " + data);
            }
        });

        webView.sendToWeb("hello");

	}

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

		settings.setUserAgentString("${settings.userAgentString} ");
		settings.setMediaPlaybackRequiresUserGesture(true);//媒体自动播放
		settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
		settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
	}

	public void pickFile() {
		Intent chooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
		chooserIntent.setType("image/*");
		startActivityForResult(chooserIntent, RESULT_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == RESULT_CODE) {
			if (null == mUploadMessage && null == mUploadMessageArray){
				return;
			}
			if(null!= mUploadMessage && null == mUploadMessageArray){
				Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
				mUploadMessage.onReceiveValue(result);
				mUploadMessage = null;
			}

			if(null == mUploadMessage && null != mUploadMessageArray){
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
		if (button.equals(v)) {
            webView.callHandler("functionInJs", "data from Java", new OnBridgeCallback() {

				@Override
				public void onCallBack(String data) {
					// TODO Auto-generated method stub
					Log.i(TAG, "reponse data from js " + data);
				}

			});
		}

	}

}
