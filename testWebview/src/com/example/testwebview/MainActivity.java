package com.example.testwebview;

import com.example.testwebview.R;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		WebView webview = (WebView) findViewById(R.id.webView1);
		webview.setWebViewClient(new MyBrowser());
		webview.getSettings().setJavaScriptEnabled(true);
		/*webview.loadUrl("http://truyencv.com/nho-thuat/chuong-435/");
		System.out.println ("test");
		//webview.addJavascriptInterface(new JsInterFace(), "AndroidApp");
		
		String myJsString = "chuong-1-790454";
		webview..evaluateJavascript("(function() { return FetchChapter(\"" + myJsString + "\"); })();", new ValueCallback<String>() {
		    @Override
		    public void onReceiveValue(String s) {
		        System.out.println(s); // Returns the value from the function
		    }
		});*/
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private class MyBrowser extends WebViewClient {
		   @Override
		   public boolean shouldOverrideUrlLoading(WebView view, String url) {
		      view.loadUrl(url);
		      return true;
		   }
	}
	
	private class JsInterFace{
		
		void receiveString(final String value) {
	        // String received from WebView
	        Log.d("MyApp", value);
	    }
	}
}
