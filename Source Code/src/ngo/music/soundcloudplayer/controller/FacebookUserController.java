/**
 * 
 */
package ngo.music.soundcloudplayer.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import android.app.DownloadManager.Request;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.api.ApiWrapper;
import ngo.music.soundcloudplayer.api.Endpoints;
import ngo.music.soundcloudplayer.api.Token;
import ngo.music.soundcloudplayer.api.CloudAPI;
import ngo.music.soundcloudplayer.entity.User;
import ngo.music.soundcloudplayer.general.Constants;

/**
 * @author LEBAO_000
 *
 */
public class FacebookUserController extends UserController {

	/**
	 * 
	 */
	public FacebookUserController() {
		// TODO Auto-generated constructor stub
	}

	//public void login();
	public URI login() {
		// TODO Auto-generated method stub
		final ApiWrapper wrapper = new ApiWrapper(
                Constants.CLIENT_ID,
                Constants.CLIENT_SECRET,
                Constants.REDIRECT_URI,
                null    /* token */);

		System.out.println ("LOGIN FACEBOOK");
        // generate the URL the user needs to open in the browser
		//wrapper.resolve(Request.)
        URI url = wrapper.authorizationCodeUrl(Endpoints.FACEBOOK_CONNECT, Token.SCOPE_NON_EXPIRING);
       
        return url;
        // start a web server to get the redirect information
//        try {
//			startServer(wrapper);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

        // note: on Android you would use a WebView instead and override 'shouldOverrideUrlLoading':

        	
//            WebView webView = (WebView) new WebView()
//            webView.setWebViewClient(new WebViewClient() {
//                @Override
//                public boolean shouldOverrideUrlLoading(final WebView view, String url) {
//                    if (url.startsWith(Contants.REDIRECT_URI.toURL().toString())) {
//                        Uri result = Uri.parse(url);
//                        String error = result.getQueryParameter("error");
//                        String code = result.getQueryParameter("code");
//                    }
//                    return true;
//                }
//            });

            
      //      webView.loadUrl(wrapper.authorizationCodeUrl(Endpoints.FACEBOOK_CONNECT).toURL().toString());
        
    }



    static void reply(PrintStream out, String text) {
        System.out.println(text);

        out.println("HTTP/1.1 200 OK");
        out.println("Content-Type: text/plain");
        out.println();
        out.println(text);
        out.flush();
    }

    static Map<String, String> parseParameters(String request) {
        Map<String, String> params = new HashMap<String, String>();
        if (request.contains("?")) {
            String query = request.substring(Math.min(request.length(), request.indexOf("?") + 1),
                    request.length());
            for (String s : query.split("&")) {
                String[] kv = s.split("=", 2);
                if (kv != null && kv.length == 2) {
                    try {
                        params.put(URLDecoder.decode(kv[0], "UTF-8"),
                                URLDecoder.decode(kv[1], "UTF-8"));
                    } catch (UnsupportedEncodingException ignored) {
                    }
                }
            }
        }
        return params;
    }

	@Override
	public User validateLogin(String username, String password) {
		// TODO Auto-generated method stub
		return null;
	}
		
	

}
