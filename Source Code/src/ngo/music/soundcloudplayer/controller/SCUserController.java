/**
 * 
 */
package ngo.music.soundcloudplayer.controller;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import ngo.music.soundcloudplayer.api.ApiWrapper;
import ngo.music.soundcloudplayer.api.Endpoints;
import ngo.music.soundcloudplayer.api.Env;
import ngo.music.soundcloudplayer.api.Http;
import ngo.music.soundcloudplayer.api.Params;
import ngo.music.soundcloudplayer.api.Request;
import ngo.music.soundcloudplayer.api.Token;
import ngo.music.soundcloudplayer.database.DatabaseHandler;
import ngo.music.soundcloudplayer.entity.OnlineSong;
import ngo.music.soundcloudplayer.entity.SCPlaylist;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.entity.SCAccount;
import ngo.music.soundcloudplayer.entity.User;
import ngo.music.soundcloudplayer.general.BasicFunctions;
import ngo.music.soundcloudplayer.general.Constants;


/**
 * @author LEBAO_000
 *
 */
public class SCUserController extends UserController implements Constants.UserContant, Constants {
	
	private Token t = null;
	private User currentUser = null;
	private User guest = null;
	private ApiWrapper wrapper = null;
	
	
	/*
	 * Response String when resolve me/favorite
	 */
	private String responseString = null;
	
	private ArrayList<User> followings = new ArrayList<User>();
	private ArrayList<User> followers = new ArrayList<User>();
	private ArrayList<User> scUserSearchLists = new ArrayList<User>();
	
	private ArrayList<Integer> followingIdList = new ArrayList<Integer>();
	private ArrayList<Integer> followerIdList = new ArrayList<Integer>();

	private static final String ME_FAVORITE = "https://api.soundcloud.com/me/favorites/";
	private static final int OFFSET = 5;
	/*
	 *	Singleton Pattern
	 *	Allow only 1 object is created 
	 */
	private static SCUserController instance = null;
	   
	public static SCUserController getInstance() {
	      if(instance == null) {
	         instance = new SCUserController();
	      }
	      return instance;
	}
	
	private SCUserController() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void login() {
		try {
			retrevieUserInfoOnline(getApiWrapper());
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Validate the login.
	 * If username and password is correct.
	 * 	- Login to Soundcloud
	 *  - Get user from soundcloud
	 *  
	 * @param username
	 * @param password
	 * @return null if cannot login. User object if can login
	 */
	@Override
	public User validateLogin (String username , String password){

		
		wrapper = new ApiWrapper(Constants.CLIENT_ID, Constants.CLIENT_SECRET, null,null);
		try {
			
			//login
			t = wrapper.login(username,password);
			//Get user information from soundcloud 
	        retrevieUserInfoOnline(wrapper);
	        
	        

		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return  null;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return null;
		}
		
		return currentUser;
	}

	/**
	 * @throws IOException
	 * @throws JSONException
	 */
	public void retrevieUserInfoOnline(ApiWrapper wrapper) throws IOException, JSONException {
		HttpResponse resp = wrapper.get(Request.to(Endpoints.MY_DETAILS));
		//t = wrapper.clientCredentials();
		System.out.println (resp.getStatusLine());
		JSONObject me = Http.getJSON(resp);
		//set information of logged user
		
		currentUser  = addAllInformation(me);
		
	}
	
	private User addAllInformation(JSONObject me) throws JSONException{
		SCAccount soundcloudAccount = new SCAccount();
		soundcloudAccount.setAvatarUrl(me.getString(AVATAR_URL));
		soundcloudAccount.setCity(me.getString(CITY));
		soundcloudAccount.setCountry(me.getString(COUNTRY));
		soundcloudAccount.setDescription(me.getString(DESCRIPTION));
		//soundcloudAccount.setDiscogsName(me.getString(DISCOGS_NAME));
		soundcloudAccount.setFollowersCount(me.getInt(FOLLOWERS_COUNT));
		soundcloudAccount.setFollowingCount(me.getInt(FOLLOWINGS_COUNT));
		soundcloudAccount.setFullName(me.getString(FULLNAME));
		soundcloudAccount.setId(me.getInt(ID));
		//soundcloudAccount.setMySpaceName(me.getString(MYSPACE_NAME));
		//soundcloudAccount.setOnline(me.getBoolean(ONLINE));
		soundcloudAccount.setPermalink(me.getString(PERMALINK));
		soundcloudAccount.setPermalinkUrl(me.getString(PERMALINK_URL));
		//soundcloudAccount.setPlan(me.getString(PLAN));
		soundcloudAccount.setPlaylistCount(me.getInt(PLAYLIST_COUNT));
		soundcloudAccount.setPrimaryEmailConfirmed(me.getBoolean(PRIMARY_EMAIL_CONFIRMED));
		soundcloudAccount.setPrivatePlaylistsCount(me.getInt(PRIVATE_PLAYLISTS_COUNT));
		soundcloudAccount.setPrivateTracksCount(me.getInt(PRIVATE_TRACK_COUNT));
		soundcloudAccount.setPublicFavoriteCount(me.getInt(PUBLIC_FAVORITES_COUNT));
		soundcloudAccount.setTrackCount(me.getInt(TRACK_COUNT));
		soundcloudAccount.setUri(me.getString(URI_LINK));
		soundcloudAccount.setUsername(me.getString(USERNAME));
		soundcloudAccount.setWebsite(me.getString(WEBSITE));
		soundcloudAccount.setWebsiteTitle(me.getString(WEBSITE_TITLE));
		
		
		
		return soundcloudAccount;
		
	}
	
	private User addSimpleUserInfo (JSONObject me) throws JSONException{
		SCAccount soundcloudAccount = new SCAccount();
		soundcloudAccount.setAvatarUrl(me.getString(AVATAR_URL));
		soundcloudAccount.setCountry(me.getString(COUNTRY));
		soundcloudAccount.setFollowersCount(me.getInt(FOLLOWERS_COUNT));
		soundcloudAccount.setFollowingCount(me.getInt(FOLLOWINGS_COUNT));
		soundcloudAccount.setFullName(me.getString(FULLNAME));
		soundcloudAccount.setId(me.getInt(ID));
		soundcloudAccount.setPlaylistCount(me.getInt(PLAYLIST_COUNT));
		soundcloudAccount.setTrackCount(me.getInt(TRACK_COUNT));
		soundcloudAccount.setUri(me.getString(URI_LINK));
		soundcloudAccount.setUsername(me.getString(USERNAME));
		soundcloudAccount.setCity(me.getString(CITY));
		
		return soundcloudAccount;
	}

	public void logout() {
		// TODO Auto-generated method stub
		t  = null;
		guest = null;
		currentUser = null;
		SongController.getInstance().clear();
		
		
	}

	public boolean isLogin(){
		if (t == null) return false;
		else return true;
	}
	
	/**
	 * Get infomation of user to a bundle
	 * @param user current user
	 * @return a bundle
	 */
	public Bundle getBundle (User user){
		
		Bundle bundle = new Bundle();
		
		if (user == null) return bundle;
		bundle.putInt(ID, user.getId());
		bundle.putString(USERNAME, user.getUsername());
		bundle.putString(AVATAR_URL, user.getAvatarUrl());
		bundle.putString(CITY, user.getCity());
		bundle.putString(COUNTRY, user.getCountry());
		bundle.putString(DESCRIPTION, user.getDescription());
		bundle.putInt(FOLLOWERS_COUNT, user.getFollowersCount());
		bundle.putInt(FOLLOWINGS_COUNT, user.getFollowingCount());
		bundle.putString(FULLNAME, user.getFullName());
		bundle.putBoolean(ONLINE, user.isOnline());
		bundle.putInt(PLAYLIST_COUNT, user.getPlaylistCount());
		bundle.putString(PERMALINK, user.getPermalink());
		bundle.putString(PERMALINK_URL, user.getPermalinkUrl());
		bundle.putBoolean(PRIMARY_EMAIL_CONFIRMED, user.isPrimaryEmailConfirmed());
		bundle.putInt(PRIVATE_PLAYLISTS_COUNT, user.getPlaylistCount());
		bundle.putInt(PRIVATE_TRACK_COUNT, user.getPrivateTracksCount());
		bundle.putInt(PUBLIC_FAVORITES_COUNT,user.getPublicFavoriteCount());
		bundle.putString(URI_LINK, user.getUri());
		bundle.putInt(TRACK_COUNT, user.getTrackCount());
		
		
		return bundle;
	}
	
	/**
	 * Get current viewing user
	 * @return
	 * @throws Exception 
	 */
	public User getUser() throws Exception{
		
		if (guest != null){
			return guest;
		}else if (currentUser != null){
			return currentUser;
		}else{
			throw new Exception();
		}
		
	}
	
	/**
	 * Get current login user
	 * @return
	 * @throws Exception 
	 */
	public User getCurrentUser() throws Exception{
		if (currentUser == null){
			throw new Exception();
		}
		return currentUser;
	}
	
	public Token getToken(){
		return t;
	}
	
	public void setToken(Token t){
		this.t = t;
		currentUser = new SCAccount();
	}

	/**
	 * like a song 
	 * @param song song want to be like
	 * @return false if not login, true if sucess
	 */
	public boolean likeSong(Song song){
		/*
		 * Havent login
		 */
		Token t = this.t;
		if (t == null) return false;
		
		try {
			return new likeSongBackground().execute(song).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			return false;
			
		}
//		ApiWrapper wrapper = new ApiWrapper(Constants.CLIENT_ID, Constants.CLIENT_SECRET, null, t);
//		String request =  "https://api.soundcloud.com/me/favorites/" + song.getSoundcloudId();
//		System.out.println(request);
//		try {
//			wrapper.put(Request.to(request));
//			HttpResponse resp = wrapper.put(Request.to(request));
//			System.out.println (resp.getStatusLine());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return false;
//		}
//		return true;
	}
	
	private class likeSongBackground extends AsyncTask<Song, String,Boolean>{

		//private Song song;
		
		
		@Override
		protected Boolean doInBackground(Song...song) {
			// TODO Auto-generated method stub
			ApiWrapper wrapper = getApiWrapper();
			String request =  ME_FAVORITE + song[0].getId();
			
			try {
				wrapper.put(Request.to(request));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			return true;
		}
		
	}
	

	/**
	 * check if this user like the given song or not
	 * @param songID id of Song want to be check
	 * @param responseString json file of me/favorites
	 * @return true if liked before, false if not
	 */
	public boolean isLiked(int songID){
		
		
		try {
			return new checkLikedBackground().execute(songID).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
//		ApiWrapper wrapper = new ApiWrapper(Constants.CLIENT_ID, Constants.CLIENT_SECRET, null, t);
//		String request =  "https://api.soundcloud.com/me/favorites/";
//		HttpResponse resp;
//		try {
//			resp = wrapper.get(Request.to(request));
//			String me =  Http.getString(resp);
//			JSONArray array =  new JSONArray(me);
//			for (int i = 0; i < array.length(); i++) {
//				JSONObject object  = array.getJSONObject(i);
//				if (object.getInt(ID) == songID){
//					return true;
//				}
//			}
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//			return true;
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return true;
//		}
		
		//return false;
		//JSONObject me = Http.getJSON(resp);
		
		
		
		
	}
	private class checkLikedBackground extends AsyncTask<Integer, String, Boolean>{

		
		
		
		@Override
		protected Boolean doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			//ApiWrapper wrapper = new ApiWrapper(Constants.CLIENT_ID, Constants.CLIENT_SECRET, null, t);
//			String request =  ME_FAVORITE;
//			HttpResponse resp;
			try {
				
				JSONArray array =  new JSONArray(responseString);
				for (int i = 0; i < array.length(); i++) {
					JSONObject object  = array.getJSONObject(i);
					if (object.getInt(ID) == params[0]){
						return true;
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			return false;
			
		}
		
	}
	
	public String getResponseString (){
		return responseString;
	}
	
	public void setResponseString(String respStr){
		responseString = respStr;
	}
	
	public ApiWrapper getApiWrapper(){
		return new ApiWrapper(Constants.CLIENT_ID, Constants.CLIENT_SECRET, null, t);
	}

	
	/**
	 * Get the Following of current user
	 * @return list of following user
	 * @throws IOException
	 * @throws JSONException
	 */
	public ArrayList<User> getFollowingUsers(int offset) throws IOException, JSONException {
		
		// TODO Auto-generated method stub
		ApiWrapper wrapper = getApiWrapper();
		HttpResponse response;	
//		
//		if (offset > getUser().getFollowingCount()){
//			offset = offset-getUser().getFollowingCount();
//		}
		
		
		if (guest == null){
			response = wrapper.get(Request.to(ME_FOLLOWINGS + "/?offset=" + String.valueOf(offset)));
			
		}else {
			
			String request = Constants.USER_LINK + "/"+ String.valueOf(guest.getId()) +"/followings/?offset=" + String.valueOf(offset);

			response = wrapper.get(Request.to(request));
		}
		String respString = Http.getString(response);
		
		return getFollowingsJSON(respString);
	}
	
	public ArrayList<User> getSCUserSearch(String query ,int page) throws IOException, JSONException {
		
		// TODO Auto-generated method stub
		ApiWrapper wrapper = getApiWrapper();
			
//		
//		if (offset > getUser().getFollowingCount()){
//			offset = offset-getUser().getFollowingCount();
//		}
			
		
		int offset = page * 10;
		String request = Constants.USER_LINK + "/" + "?q=" + query + "&limit=10&offset=" + offset;
		System.out.println ("REQUEST USER = " + request);
		HttpResponse response = wrapper.get(Request.to(request));
		
		String respString = Http.getString(response);
		JSONArray userArray = new JSONArray(respString);
		for (int i = 0; i < userArray.length(); i++){
			JSONObject object = userArray.getJSONObject(i);	
			scUserSearchLists.add(addSimpleUserInfo(object));
		}
		return scUserSearchLists;
	}
	
	/**
	 * Get the Followers of current user
	 * @return List of followers
	 * @throws IOException
	 * @throws JSONException
	 */
	public ArrayList<User> getFollowerUsers(int offset) throws IOException, JSONException {
		// TODO Auto-generated method stub
	
		
		ApiWrapper wrapper = getApiWrapper();
		
		HttpResponse response;
		if (guest == null){
			
			response = wrapper.get(Request.to(ME_FOLLOWERS + "/?offset=" + String.valueOf(offset)));
		}else{
		
			String request = Constants.USER_LINK + "/"+ String.valueOf(guest.getId()) +"/followers/?offset=" + String.valueOf(offset);
			response = wrapper.get(Request.to(request));
		}
		String respString = Http.getString(response);
		
		return getFollowersJSON(respString);
	}

	
	private ArrayList<User> getFollowingsJSON(String responseString) throws JSONException{
		//ArrayList<User> users = new ArrayList<User>();
		JSONArray userArray = new JSONArray(responseString);
		for (int i = 0; i < userArray.length(); i++){
			JSONObject object = userArray.getJSONObject(i);
			int position = searchId(followingIdList, object.getInt(ID));
			
			if (position < 0){
				followings.add(addSimpleUserInfo(object));
				followingIdList.add(- (position + 1), object.getInt(ID));
			}
			
		}
		
		return followings;
	}
	
	
	private ArrayList<User> getFollowersJSON(String responseString) throws JSONException{
		//ArrayList<User> users = new ArrayList<User>();
		JSONArray userArray = new JSONArray(responseString);
		for (int i = 0; i < userArray.length(); i++){
			JSONObject object = userArray.getJSONObject(i);
			int position = searchId(followerIdList, object.getInt(ID));
			
			if (position < 0){
				followers.add(addSimpleUserInfo(object));
				followerIdList.add(- (position + 1), object.getInt(ID));
			}
				
		}
		
		return followers;
	}
	
	
	/**
	 * Check a user if current user is follow or not
	 * @param user
	 * @return
	 */
	public boolean isFollowing (SCAccount user){
		ApiWrapper wrapper = getApiWrapper();
		
		String request = Constants.ME_FOLLOWINGS + "/"+String.valueOf(user.getId());
	
		
		//System.out.println (resp.getStatusLine());
		//String me =  Http.getString(resp);
		//System.out.println (me);
		JSONObject obj;
		try {
			HttpResponse response = wrapper.get(Request.to(request));
			obj = Http.getJSON(response);
			JSONArray a = obj.getJSONArray("errors");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return false;
			
		}
		
		//System.out.println (obj);
		return true;
		
	}
	/**
	 * @return the guest
	 */
	public User getGuest() {
		return guest;
	}

	/**
	 * @param guest the guest to set
	 */
	public void setGuest(User guest) {
		this.guest = guest;
	}

	/**
	 * Follow an user in soundcloud
	 * @param soundCloudAccount
	 */
	public void follow(SCAccount soundCloudAccount) {
		// TODO Auto-generated method stub
		ApiWrapper wrapper = getApiWrapper();
		
		String request = Constants.ME_FOLLOWINGS + "/"+String.valueOf(soundCloudAccount.getId());
	
		try {
			 wrapper.put(Request.to(request));
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			
		}
		
		
	}

	/**
	 * Unfollow an user on soundcloud
	 * @param soundCloudAccount
	 */
	public void unFollow(SCAccount soundCloudAccount) {
		// TODO Auto-generated method stub
		ApiWrapper wrapper = getApiWrapper();
		
		String request = Constants.ME_FOLLOWINGS + "/"+String.valueOf(soundCloudAccount.getId());
	
		try {
			 wrapper.delete(Request.to(request));
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			
		}
		
	}
	
	/**
	 * Get info of soundclouduser by id
	 * @param params
	 * @return
	 * @throws IOException 
	 * @throws JSONException 
	 */
	public SCAccount getUserbyId(String params) throws IOException, JSONException{
		SCAccount soundCloudAccount = new SCAccount();
		wrapper = getApiWrapper();
		
		System.out.println ("INSIDE GET USER BY ID");
			HttpResponse resp  = wrapper.get(Request.to(Constants.USER_LINK + "/" + String.valueOf(params)));
			
			String respString = Http.getString(resp);
			JSONObject me = new JSONObject(respString);
			soundCloudAccount = (SCAccount) addSimpleUserInfo(me);
			return soundCloudAccount;

	}

	
	/**
	 * Reset data of controller
	 */
	public void clearUserData(){
		followers.clear();
		followings.clear();
		followerIdList.clear();
		followingIdList.clear();
		
	}
	
	
	public ArrayList<User> getFollowings(){
		return followings;
	}
	
	public ArrayList<User> getFollowers(){
		return followers;
		
	}
	
	public ArrayList<User> getSCUserSearchs(){
		return scUserSearchLists;
	}
	
	/**
	 * search the Id of user in a list
	 * 
	 */
	private int searchId(ArrayList<Integer> songidList, int songID){
		
		return Collections.binarySearch(songidList,songID);
	

	}
	
//	/**
//	 * Search User from Soundcloud
//	 * @param query
//	 * @param page
//	 */
//	public ArrayList<SCAccount> searchUserSC(String query, int page) {
//		// TODO Auto-generated method stub
//		SCUserController soundCloudUserController = SCUserController.getInstance();
//		ApiWrapper wrapper = soundCloudUserController.getApiWrapper();
//		ArrayList<SCAccount> soundcloudUser = new ArrayList<SCAccount>(); 
//		int offset = page*OFFSET;
//		String request =  "http://api.soundcloud.com/users.json?q=" + query + "&limit=5&offset="+ String.valueOf(offset);
//		
//		//System.out.println (me);
//	//	JSONObject obj = Http.getJSON(resp);
//		//JSONArray a = obj.getJSONArray("errors");
//		//System.out.println (obj);
//		try {
//			HttpResponse resp = wrapper.get(Request.to(request));
//			//System.out.println (resp.getStatusLine());
//			String me =  Http.getString(resp);
//			JSONArray array =  new JSONArray(me);
//			//JSONObject object = array.getJSONObject(0);
//			//System.out.println (object.toString());
//			for (int i = 0; i < array.length(); i++) {
//				JSONObject object  = array.getJSONObject(i);
//				soundcloudUser.add((SCAccount) addSimpleUserInfo(object));
//			}
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return soundcloudUser;
//	}

	public void clearSearch() {
		// TODO Auto-generated method stub
		scUserSearchLists.clear();
	}


	

	
	
	
	
	
	
}
