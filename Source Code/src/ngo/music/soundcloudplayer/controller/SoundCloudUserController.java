/**
 * 
 */
package ngo.music.soundcloudplayer.controller;

import java.io.IOException;
import java.net.URI;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.os.Debug;
import android.util.Log;
import ngo.music.soundcloudplayer.api.ApiWrapper;
import ngo.music.soundcloudplayer.api.Endpoints;
import ngo.music.soundcloudplayer.api.Env;
import ngo.music.soundcloudplayer.api.Http;
import ngo.music.soundcloudplayer.api.Request;
import ngo.music.soundcloudplayer.api.Token;
import ngo.music.soundcloudplayer.entity.SoundCloudAccount;
import ngo.music.soundcloudplayer.general.Contants;


/**
 * @author LEBAO_000
 *
 */
public class SoundCloudUserController extends UserController implements Contants.UserContant {

	private static final String URL_LOGIN = "http://soundcloud.com/login";
	/**
	 * 
	 */
	public SoundCloudUserController() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public URI login() {
		return null;
		// TODO Auto-generated method stub
		
	}
	
	public String validateLogin (String username , String password){
		//CloudAPI api = new CloudAPI(Contants.CLIENT_ID,Contants.CLIENT_SECRET);
		ApiWrapper wrapper = new ApiWrapper(Contants.CLIENT_ID, Contants.CLIENT_SECRET, null,null);
		//System.out.println (wrapper);
		try {
			//wrapper.
			Token t = wrapper.login(username,password);
			
	        HttpResponse resp = wrapper.get(Request.to(Endpoints.MY_DETAILS));
	        JSONObject me = Http.getJSON(resp);
	        //System.out.println ("USERNAME  = " + me.getString("username"));
	        addInformation(me);

	        
//			System.out.println(wrapper.getURI(Request.to("/me"), true, true));
		//	t.
//			HttpResponse response = wrapper.put(Request.to("/me"));
//			//System.out.println(wrapper.getHttpClient().execute("/me"));
//			
//			
//			
//			for (Header header : resp.getAllHeaders()){
//				System.out.println ("head =  " + header.getName() + " " + header.getValue() );
//			}
			
			System.out.println(t.access);
			//wrapper.getURI(request, api, secure)
			//System.out.t.access
			/*
			 * Get id of user
			 * 
			 */
			//long id = wrapper.clientCredentials())("https://api.soundcloud.com/users/3207");
			//System.out.println(id);
		//	t.
//			Log.d("ID OF USER: ", String.(id));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return  "Invalid Username or Password";
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		return "OK";
	}
	
	public void addInformation(JSONObject me) throws JSONException{
		SoundCloudAccount soundcloudAccount = new SoundCloudAccount();
		soundcloudAccount.setAvatarUrl(me.getString(AVATAR_URL));
		soundcloudAccount.setCity(me.getString(CITY));
		soundcloudAccount.setCountry(me.getString(COUNTRY));
		soundcloudAccount.setDescription(me.getString(DESCRIPTION));
		soundcloudAccount.setDiscogsName(me.getString(DISCOGS_NAME));
		soundcloudAccount.setFollowersCount(me.getInt(FOLLOWERS_COUNT));
		soundcloudAccount.setFollowingCount(me.getInt(FOLLOWINGS_COUNT));
		soundcloudAccount.setFullName(me.getString(FULLNAME));
		soundcloudAccount.setId(me.getInt(ID));
		soundcloudAccount.setMySpaceName(me.getString(MYSPACE_NAME));
		soundcloudAccount.setOnline(me.getBoolean(ONLINE));
		soundcloudAccount.setPermalink(me.getString(PERMALINK));
		soundcloudAccount.setPermalinkUrl(me.getString(PERMALINK_URL));
		soundcloudAccount.setPlan(me.getString(PLAN));
		soundcloudAccount.setPlaylistCount(me.getInt(PLAYLIST_COUNT));
		soundcloudAccount.setPrimaryEmailConfirmed(me.getBoolean(PRIMARY_EMAIL_CONFIRMED));
		soundcloudAccount.setPrivatePlaylistsCount(me.getInt(PRIVATE_PLAYLISTS_COUNT));
		soundcloudAccount.setPrivateTracksCount(me.getInt(PRIVATE_TRACK_COUNT));
		soundcloudAccount.setPublicFavoriteCount(me.getInt(PUBLIC_FAVORITES_COUNT));
		soundcloudAccount.setTrackCount(me.getInt(TRACK_COUNT));
		soundcloudAccount.setUri(me.getString(URI));
		soundcloudAccount.setUsername(me.getString(USERNAME));
		soundcloudAccount.setWebsite(me.getString(WEBSITE));
		soundcloudAccount.setWebsiteTitle(me.getString(WEBSITE_TITLE));
		
		
		
	}

}
