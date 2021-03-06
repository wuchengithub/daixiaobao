package com.wookii;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

/**
 * 易宝支付
 * 
 * @author Administrator
 * 
 */
public class YeePayActivity extends Activity {

	private Intent intent;
	private ImageView back;
	private WebView mWebView;
	private String uri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(ResourcesIdFactoryUtils.getLayoutId(
				getApplicationContext(), "yeepay"));
		init();
		openWebView(uri);
	}

	/**
	 * 打开服务端返回的支付URL进行支付
	 * 
	 * @param url
	 */
	@SuppressLint("SetJavaScriptEnabled")
	protected void openWebView(String url) {
		final ProgressDialog pd = new ProgressDialog(YeePayActivity.this);
		// 设置滚动条不显示
		mWebView.setVerticalScrollBarEnabled(false);
		mWebView.setHorizontalScrollBarEnabled(false);
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setBuiltInZoomControls(true);
		webSettings.setJavaScriptEnabled(true);
		webSettings.setUseWideViewPort(true);
		// 加载URL
		mWebView.loadUrl(url);
		// 加载页面
		mWebView.setWebChromeClient(new WebChromeClient() {
			@SuppressLint("NewApi")
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress == 100) {
					int colorId = ResourcesIdFactoryUtils.getColorId(
							getApplicationContext(), "background_recharge");
					if (pd.isShowing()) {
						pd.dismiss();
					}
				} else {
					if (!pd.isShowing())
						pd.show();
				}
			}
		});

		// 这个是当网页上的连接被点击的时候
		mWebView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(final WebView view,
					final String url) {
				view.loadUrl(url);
				return true;
			}

			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				if (!pd.isShowing())
					pd.show();
			}
		});

	}

	/**
	 * goBack()表示返回webView的上一页面
	 */
	public boolean onKeyDown(int keyCoder, KeyEvent event) {
		if (mWebView.canGoBack() && keyCoder == KeyEvent.KEYCODE_BACK) {
			mWebView.goBack();
		} else if (keyCoder == KeyEvent.KEYCODE_MENU) {

		} else if (!mWebView.canGoBack() && keyCoder == KeyEvent.KEYCODE_BACK) {
			notifyClosed();
			finish();
		}
		return true;
	}

	/**
	 * 通知页面被关闭
	 */
	private void notifyClosed() {
		setResult(WookiiSDKManager.COMMON_SESULT_CODE);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	/**
	 * 初始化控件
	 */
	private void init() {
		intent = getIntent();
		uri = intent.getStringExtra("uri");
		mWebView = (WebView) findViewById(ResourcesIdFactoryUtils.getId(
				getApplicationContext(), "wv_recharge"));
		back = (ImageView) findViewById(ResourcesIdFactoryUtils.getId(
				getApplicationContext(), "back"));
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				notifyClosed();
				finish();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}
}
