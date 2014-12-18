package ngo.music.soundcloudplayer.boundary;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;




import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.controller.UserControllerFactory;
import ngo.music.soundcloudplayer.general.Contants;
import android.app.Activity;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class LoginUI extends android.support.v4.app.Fragment {

	public LoginUI() {
		// TODO Auto-generated constructor stub
	}
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		final View rootView = inflater.inflate(R.layout.login_layout, container, false);
		
		Button loginFacebook =  (Button)rootView.findViewById(R.id.login_facebook_button);
		loginFacebook.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				UserControllerFactory.createUserController(Contants.FACEBOOK_USER).login();
				// TODO Auto-generated method stub
				
				
			}
		});
		
		Button loginGooglePlus = (Button) rootView.findViewById(R.id.login_google_plus_button);
		loginGooglePlus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UserControllerFactory.createUserController(Contants.GOOGLE_PLUS_USER).login();
			}
		});
		
		Button loginSoundCloud = (Button) rootView.findViewById(R.id.login_soundcloud_button);
		loginSoundCloud.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SoundCloudLoginUI soundCloudLoginUI =  new SoundCloudLoginUI();
				FragmentManager fragmentManager = getActivity().getFragmentManager();
				int fragmentTransaction = fragmentManager.beginTransaction()
														.replace(R.id.login_layout, soundCloudLoginUI)
														.commit();
				// TODO Auto-generated method stub
//				//URI url = UserControllerFactory.createUserController(Contants.SOUNDCLOUD_USER).login();
//				final ApiWrapper wrapper = new ApiWrapper(
//		                Contants.CLIENT_ID,
//		                Contants.CLIENT_SECRET,
//		                Contants.REDIRECT_URI,
//		                null    /* token */);
//
//
//		        // generate the URL the user needs to open in the browser
//		        URI url = wrapper.authorizationCodeUrl(Endpoints.FACEBOOK_CONNECT, Token.SCOPE_NON_EXPIRING);
//				WebView webView = (WebView) rootView.findViewById(R.id.webview);
//	            webView.setWebViewClient(new WebViewClient() {
//	                @Override
//	                public boolean shouldOverrideUrlLoading(final WebView view, String url) {
//	                    try {
//							if (url.startsWith(Contants.REDIRECT_URI.toURL().toURI().toString())) {
//							    Uri result = Uri.parse(url);
//							    String error = result.getQueryParameter("error");
//							    String code = result.getQueryParameter("code");
//							}
//						} catch (MalformedURLException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						} catch (URISyntaxException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//	                    return true;
//	                }
//	            });
//
//	            try {
//					webView.loadUrl(wrapper.authorizationCodeUrl(Endpoints.FACEBOOK_CONNECT).toURL().toString());
//				} catch (MalformedURLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			}
		});
		return rootView;
	}

}
