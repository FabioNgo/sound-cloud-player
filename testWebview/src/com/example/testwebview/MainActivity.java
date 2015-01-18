package com.example.testwebview;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.annotation.SuppressLint;
import android.app.Activity;

public class MainActivity extends Activity implements OnClickListener {
	
	private static final String URL = "http://www.vnntu.com/web2015/soundcloud/login.html";
	private WebView mWebView;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mWebView = (WebView) findViewById(R.id.webview); 
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.setWebChromeClient(new WebChromeClient());
		mWebView.setWebViewClient(new WebViewClient(){
			@Override
	        public void onPageFinished(WebView view, String url) {
				String user = ((EditText) findViewById(R.id.edit_text)).getText().toString();
				if (user.isEmpty()) {
					user = "World";
				}
	            String javascript="javascript: document.getElementById('msg').innerHTML='Hello "+user+"!';";
	            view.loadUrl(javascript);
	        }
		});
		refreshWebView();
		findViewById(R.id.button).setOnClickListener(this);
	}
	
	private void refreshWebView() {
		mWebView.loadUrl(URL);
	}

	@Override
	public void onClick(View v) {
		refreshWebView();
	}

}
