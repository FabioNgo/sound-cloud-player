package ngo.music.soundcloudplayer.boundary;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;




import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolException;
import org.apache.http.client.CircularRedirectException;
import org.apache.http.client.RedirectHandler;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRedirectHandler;
import org.apache.http.impl.client.RedirectLocations;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;




















//import com.codebutler.android_websockets.WebSocketClient;












import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.api.ApiWrapper;
import ngo.music.soundcloudplayer.api.CloudAPI;
import ngo.music.soundcloudplayer.api.Endpoints;
import ngo.music.soundcloudplayer.api.Request;
import ngo.music.soundcloudplayer.api.Token;
import ngo.music.soundcloudplayer.controller.SoundCloudUserController;
import ngo.music.soundcloudplayer.entity.User;
import ngo.music.soundcloudplayer.general.Constants;
import android.support.v4.app.Fragment;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;




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
	
	private class loginFacebookBackground extends AsyncTask<String, String, String>{

		
		ProgressDialog pDialog;
		
		
		SoundCloudUserController soundCloudUserController = SoundCloudUserController.getInstance();
		
		@Override
		protected void onPreExecute() {
			
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Login......");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			
			pDialog.show();
			
	       
		}
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
	       // String link = null;
			//URI uri = wrapper.authorizationCodeUrl(Endpoints.FACEBOOK_CONNECT, Token.SCOPE_NON_EXPIRING, CloudAPI.POPUP);
			
		 
			// link = uri.toString();
			/// wrapper.requestToken(new Request(uri));
			// System.out.println ("LINK = " + link);
			return null;
		}
		
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			
			WebView webView = (WebView) v.findViewById(R.id.facebook_login_ui);
			//System.out.println (result);
			//webView.setWebChromeClient(new WebChromeClient());
			webView.setWebViewClient(new MyBrowser());
			//webView.setWebViewClient(new MyBrowser());
		//	webView = (WebView) rootView.findViewById(R.id.webView);
			//webView.
			webView.getSettings().setJavaScriptEnabled(true);
			webView.getSettings().setLoadsImagesAutomatically(true);
			webView.clearCache(true);
			webView.loadUrl(LINK);
			 //DefaultHttpClient httpClient = new DefaultHttpClient();
//			    RedirectHandler customRedirectHandler = new CustomRedirectHandler();
//			    //...
//			    ((AbstractHttpClient) wrapper.getHttpClient()).setRedirectHandler(customRedirectHandler);
//			    //webView.
				
				//soundCloudUserController.setResponseString(stringResponse);
				
				
			 
			pDialog.dismiss();
		}
	}
	
	private class retriveUserBackground extends AsyncTask<String, String, String>{
		SoundCloudUserController soundCloudUserController = SoundCloudUserController.getInstance();
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
			startActivity(i);
		}
		
	}
	
	private class MyBrowser extends WebViewClient {
		@Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
			
	        if (url.contains("access_token")){
	        	String result = url.substring(url.indexOf("access_token=") + 13, url.indexOf("&scope="));
	        	
	        	wrapper.setToken(new Token(result,"refresh_token"));
	        	new retriveUserBackground().execute();
	        }
			return false;
	      
	    }
	}
	
	public class MyWebChromeClient extends WebChromeClient {
		
	}
	



   

    
	
	
	
}
