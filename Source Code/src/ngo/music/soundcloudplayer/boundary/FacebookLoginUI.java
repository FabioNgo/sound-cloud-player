package ngo.music.soundcloudplayer.boundary;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;

import org.json.JSONException;

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
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.YuvImage;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
public class FacebookLoginUI extends Fragment implements Constants, Constants.UserContant {

	private View v;
	ApiWrapper wrapper = null;
	public FacebookLoginUI() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		
		v = inflater.inflate(R.layout.facebook_login_ui, null);
		wrapper = new ApiWrapper(CLIENT_ID, CLIENT_SECRET, REDIRECT_URI, null);
//		new startServerBackground().execute();
//		https://soundcloud.com/connect?
//		client_id=b45b1aa10f1ac2941910a7f0d10f8e28
//		&response_type=token
//		&scope=non-expiring%20fast-connect%20purchase%20upload
//		&display=next
//		&redirect_uri=https%3A//soundcloud.com/soundcloud-callback.html
		
		//PrintWriter writer = null;
		//File file = new File("callback.html");
		//System.out.println (file.getAbsolutePath());
		//File callback = new File(writer.)
		
		new loginFacebookBackground().execute();
		
		//System.out.println (temp.getPath());
//	
//		try {
//			startServer(wrapper);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		//System.out.println (getResources("callback.html"));
		//String authorizationUrl = wrapper.("callback.html");
		// TODO Auto-generated method stub
        
        
       
       // Intent i = new Intent(getActivity(), MainActivity.class);
        //startActivity(i);
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
			System.out.println ("AAAA");
	        String link = null;
			try {
				
				 
				URI uri = wrapper.authorizationCodeUrl(Endpoints.FACEBOOK_CONNECT, Token.SCOPE_NON_EXPIRING, CloudAPI.POPUP);
				 link = uri.toURL().toString();
				 wrapper.requestToken(new Request(uri));
				 System.out.println (link);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return link;
		}
		
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			WebView webView = (WebView) v.findViewById(R.id.facebook_login_ui);
			webView.loadUrl(result);
			// MusicPlayerMainActivity.isExplore = true;
			
				new retriveUserBackground().execute(result);
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
				System.out.println (wrapper.resolve(params[0]));
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
			System.out.println (bundle.getString(USERNAME));
			i.putExtra(USER, bundle);
			//i.putExtra(ME_FAVORITES,stringResponse);
			//MusicPlayerMainActivity.getActivity().finish();
			startActivity(i);
		}
		
	}
	
}
