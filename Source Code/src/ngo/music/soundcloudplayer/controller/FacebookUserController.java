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

import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.soundcloud.api.ApiWrapper;
import com.soundcloud.api.Endpoints;
import com.soundcloud.api.Token;
import com.soundcloud.api.CloudAPI;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.general.Contants;

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
                Contants.CLIENT_ID,
                Contants.CLIENT_SECRET,
                Contants.REDIRECT_URI,
                null    /* token */);


        // generate the URL the user needs to open in the browser
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

    static void startServer(ApiWrapper wrapper) throws IOException {
        ServerSocket socket = new ServerSocket(8000);
        for (;;) {
            final Socket client = socket.accept();
            try {
                InputStream is = client.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is), 8192);
                PrintStream out = new PrintStream(client.getOutputStream());
                String line = reader.readLine();
                if (line == null) throw new IOException("client closed connection without a request.");

                final String[] request = line.split(" ", 3);
                if (request.length != 3) throw new IOException("invalid request:" + line);
                if (!"GET".equals(request[0])) throw new IOException("invalid method:" + line);

                Map<String, String> params = parseParameters(request[1]);

                if (params.containsKey("error")) {
                    // error logging in, redirect mismatch etc.

                    reply(out, "Error: " + params.get("error_description"));
                } else if (params.containsKey("code")) {
                    // we got a code back, try to exchange it for a token
                    try {
                        Token token = wrapper.authorizationCode(params.get("code"));
                        reply(out, "Got token: " + token);
                    } catch (CloudAPI.InvalidTokenException e) {
                        reply(out, e.getMessage());
                    }
                } else {
                    // unexpected redirect
                    reply(out, "invalid request:"+request[1]);
                }
               break;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    client.close();
                } catch (IOException ignored) {
                }
            }
        }
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
		
	

}
