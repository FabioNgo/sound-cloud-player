package ngo.music.soundcloudplayer.boundary;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.api.ApiWrapper;
import ngo.music.soundcloudplayer.api.CloudAPI;
import ngo.music.soundcloudplayer.api.Endpoints;
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
public class FacebookLoginUI extends Fragment implements Constants {

	private View v;
	public FacebookLoginUI() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		
		v = inflater.inflate(R.layout.facebook_login_ui, null);
		new startServerBackground().execute();
//		https://soundcloud.com/connect?
//		client_id=b45b1aa10f1ac2941910a7f0d10f8e28
//		&response_type=token
//		&scope=non-expiring%20fast-connect%20purchase%20upload
//		&display=next
//		&redirect_uri=https%3A//soundcloud.com/soundcloud-callback.html
//		ApiWrapper wrapper = null;
//		wrapper = new ApiWrapper(CLIENT_ID, CLIENT_SECRET, REDIRECT_URI, null);
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
       // WebView webView = (WebView) v.findViewById(R.id.facebook_login_ui);
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
//        System.out.println ("AAAA");
//        String link = wrapper.authorizationCodeUrl(Endpoints.FACEBOOK_CONNECT).toString();
//        webView.loadUrl(link);
       // Intent i = new Intent(getActivity(), MainActivity.class);
        //startActivity(i);
		return v;
	}		
	
	private class startServerBackground extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			ApiWrapper wrapper = null;
			wrapper = new ApiWrapper(CLIENT_ID, CLIENT_SECRET, REDIRECT_URI, null);
			
			try {
				startServer(wrapper);
				 WebView webView = (WebView) v.findViewById(R.id.facebook_login_ui);
				webView.setWebViewClient(new WebViewClient() {
		            @Override
		            public boolean shouldOverrideUrlLoading(final WebView view, String url) {
		                //if (url.startsWith(REDIRECT_URI.toURL().toString())) {
						    Uri result;
							try {
								result = Uri.parse(REDIRECT_URI.toURL().getPath());
								String error = result.getQueryParameter("error");
		                        String code = result.getQueryParameter("code");
							} catch (MalformedURLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						    //tring error = result.getQueryParameter("error");
						   // String code = result.getQueryParameter("code");
						//}
		                return true;
		            }
		        });
				
				
			        String link = wrapper.authorizationCodeUrl(Endpoints.FACEBOOK_CONNECT).toString();
			        
			        webView.loadUrl(link);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println ("EXCEPTION");
				e.printStackTrace();
			}
			return null;
		}
		
	}
	static void startServer(ApiWrapper wrapper) throws IOException {
        ServerSocket socket = new ServerSocket(8000);
        
        for (;;) {
        	
            final Socket client = socket.accept();
            System.out.println ("DEBUG " );
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
