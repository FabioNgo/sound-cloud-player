package ngo.music.soundcloudplayer.boundary;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.zip.Inflater;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.api.ApiWrapper;
import ngo.music.soundcloudplayer.api.CloudAPI;
import ngo.music.soundcloudplayer.api.Endpoints;
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
public class FacebookLoginUI extends Fragment implements Constants {

	public FacebookLoginUI() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		
		View v = inflater.inflate(R.layout.facebook_login_ui, null);
//		https://soundcloud.com/connect?
//		client_id=b45b1aa10f1ac2941910a7f0d10f8e28
//		&response_type=token
//		&scope=non-expiring%20fast-connect%20purchase%20upload
//		&display=next
//		&redirect_uri=https%3A//soundcloud.com/soundcloud-callback.html
		ApiWrapper wrapper = null;
		wrapper = new ApiWrapper(CLIENT_ID, CLIENT_SECRET, REDIRECT_URI, null);
		//System.out.println (getResources("callback.html"));
		//String authorizationUrl = wrapper.("callback.html");
		// TODO Auto-generated method stub
        WebView webView = (WebView) v.findViewById(R.id.facebook_login_ui);
//        webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(final WebView view, String url) {
//                //if (url.startsWith(REDIRECT_URI.toURL().toString())) {
//				    Uri result;
//					try {
//						result = Uri.parse(REDIRECT_URI.toURL().getPath());
//					} catch (MalformedURLException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				    //tring error = result.getQueryParameter("error");
//				   // String code = result.getQueryParameter("code");
//				//}
//                return true;
//            }
//        });
        System.out.println ("AAAA");
        String link = wrapper.authorizationCodeUrl(Endpoints.FACEBOOK_CONNECT).toString();
        webView.loadUrl(link);
       // Intent i = new Intent(getActivity(), MainActivity.class);
        //startActivity(i);
		return v;
	}		
	
}
