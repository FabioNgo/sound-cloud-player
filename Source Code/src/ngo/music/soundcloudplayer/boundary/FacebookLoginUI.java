package ngo.music.soundcloudplayer.boundary;


import java.io.IOException;

import org.json.JSONException;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.api.ApiWrapper;
import ngo.music.soundcloudplayer.api.Token;
import ngo.music.soundcloudplayer.controller.SCUserController;
import ngo.music.soundcloudplayer.general.Constants;
import android.support.v4.app.Fragment;
import android.annotation.SuppressLint;

import android.content.Intent;
import android.graphics.YuvImage;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;





/**
 * Login UI of SoundCloud
 * @author LEBAO_000
 *
 */
@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
public class FacebookLoginUI extends Fragment implements Constants, Constants.UserContant {

	private static final String LINK = "http://www.vnntu.com/web2015/soundcloud/login.html";
	private View v;
	ApiWrapper wrapper = null;

	//JavaScriptInterface JSInterface;
	//private WebSocketClient client;
	
	 // Client name
    private String name = null;
	public FacebookLoginUI() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		
		v = inflater.inflate(R.layout.facebook_login_ui, null);
		wrapper = new ApiWrapper(CLIENT_ID, CLIENT_SECRET, REDIRECT_URI, null);

		WebView webView = (WebView) v.findViewById(R.id.facebook_login_ui);
		
		webView.setWebChromeClient(new MyWebChromeClient());
		webView.setWebViewClient(new MyBrowser());
		//webView.
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().getJavaScriptEnabled();
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

		webView.clearCache(true);
        	
		webView.loadUrl(LINK);

		return v;
	}		
	
	
	
	private class retriveUserBackground extends AsyncTask<String, String, String>{
		SCUserController soundCloudUserController = SCUserController.getInstance();
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				//System.out.println (wrapper.resolve(params[0]));
				soundCloudUserController.retrevieUserInfoOnline(wrapper);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			Intent i = new Intent(getActivity(), MusicPlayerMainActivity.class);
			Bundle bundle = soundCloudUserController.getBundle(soundCloudUserController.getCurrentUser());
			//System.out.println (bundle.getString(USERNAME));
			i.putExtra(USER, bundle);
			//i.putExtra(ME_FAVORITES,stringResponse);
			//MusicPlayerMainActivity.getActivity().finish();
			getActivity().finish();
			startActivity(i);
		}
		
	}
	
	private class MyBrowser extends WebViewClient {
		@Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
			
	        if (url.contains("access_token")){
	        	String result = url.substring(url.indexOf("access_token=") + 13, url.indexOf("&scope="));
	        	System.out.println (result);
	        	Token t = new Token(result,"refresh_token");
	        	wrapper.setToken(t);
	        	SCUserController soundCloudUserController = SCUserController.getInstance();
	        	soundCloudUserController.setToken(t);
	        	new retriveUserBackground().execute();
	        }
			return false;
	      
	    }
	}
	
	public class MyWebChromeClient extends WebChromeClient {
		
	}
	



   

    
	
	
	
}
