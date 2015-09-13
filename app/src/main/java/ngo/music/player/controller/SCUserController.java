/**
 * 
 */
package ngo.music.player.controller;

import android.os.AsyncTask;
import android.os.Bundle;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import ngo.music.player.boundary.UserLoginActivity;
import ngo.music.player.database.ArtistSCDatabaseTable;
import ngo.music.player.database.SCLoginDatabaseTable;
import ngo.music.player.entity.SCAccount;
import ngo.music.player.entity.Song;
import ngo.music.player.entity.User;
import ngo.music.player.helper.Constants;
import ngo.music.player.helper.States;


/**
 * @author LEBAO_000
 *
 */
public class SCUserController extends UserController implements Constants.UserContant, Constants {
	

	private User currentUser = null;
	private User guest = null;
	
	
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
	public void login() throws IOException, JSONException {
			
			States.loginState = LOGGED_IN;
			SCLoginDatabaseTable db = SCLoginDatabaseTable.getInstance(UserLoginActivity.getActivity());
			

//			retrevieUserInfoOnline(getApiWrapper());
			
		
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

		

		
		return currentUser;
	}

	/**
	 * Get User Information from online database if logged in
	 * @throws IOException
	 * @throws JSONException
	 */
	public void retrevieUserInfoOnline() throws IOException, JSONException {


		currentUser  = addSimpleUserInfo(null);

	}
	

	private User addSimpleUserInfo (JSONObject me) throws JSONException{
		String userId = String.valueOf(me.getInt(ID));
		SCAccount soundcloudAccount = new SCAccount();
		soundcloudAccount = getSCArtistFromDatabase(userId);
		//System.out.println ("USER INFO " + soundcloudAccount);
		if (soundcloudAccount != null){
			
			return soundcloudAccount;
		}else{
			soundcloudAccount = new SCAccount();
		
			soundcloudAccount.setId(String.valueOf(me.getInt(ID)));
			
			
			soundcloudAccount.setAvatarUrl(me.getString(AVATAR_URL));
			soundcloudAccount.setCountry(me.getString(COUNTRY));
			//soundcloudAccount.setFollowersCount(me.getInt(FOLLOWERS_COUNT));
			//soundcloudAccount.setFollowingCount(me.getInt(FOLLOWINGS_COUNT));
			soundcloudAccount.setFullName(me.getString(FULLNAME));
			
			//soundcloudAccount.setPlaylistCount(me.getInt(PLAYLIST_COUNT));
			//soundcloudAccount.setTrackCount(me.getInt(TRACK_COUNT));
			//soundcloudAccount.setUri(me.getString(URI_LINK));
			soundcloudAccount.setUsername(me.getString(USERNAME));
			soundcloudAccount.setCity(me.getString(CITY));
			
			return soundcloudAccount;
		}
	}

	/**
	 * Get User information from Database
	 *
	 */
	public SCAccount getSCArtistFromDatabase(String id){
		SCAccount scAccount;
		ArtistSCDatabaseTable artistSCDatabaseTable = ArtistSCDatabaseTable.getInstance(UserLoginActivity.getActivity());
		scAccount = artistSCDatabaseTable.getArtist(id);
		return scAccount;
	}
	public void logout() {
		// TODO Auto-generated method stub
//		t  = null;
		guest = null;
		currentUser = null;
		States.loginState = NOT_LOGGED_IN;
		SCLoginDatabaseTable db = SCLoginDatabaseTable.getInstance(UserLoginActivity.getActivity());
		
		db.clearTable();
		SongController.getInstance().clear();
		
		
	}

	public boolean isLogin(){
		return false;
	}
	
	/**
	 * Get infomation of user to a bundle
	 * @param user current user
	 * @return a bundle
	 */
	public Bundle getBundle (User user){
		
		Bundle bundle = new Bundle();
		
		if (user == null) return bundle;
		bundle.putString(ID, user.getId());
		bundle.putString(USERNAME, user.getUsername());
		bundle.putString(AVATAR_URL, user.getAvatarUrl());
		bundle.putString(CITY, user.getCity());
		bundle.putString(COUNTRY, user.getCountry());
		//bundle.putString(DESCRIPTION, user.getDescription());
		bundle.putInt(FOLLOWERS_COUNT, user.getFollowersCount());
		bundle.putInt(FOLLOWINGS_COUNT, user.getFollowingCount());
		bundle.putString(FULLNAME, user.getFullName());
		//bundle.putBoolean(ONLINE, user.isOnline());
		bundle.putInt(PLAYLIST_COUNT, user.getPlaylistCount());
		bundle.putString(PERMALINK, user.getPermalink());
		bundle.putString(PERMALINK_URL, user.getPermalinkUrl());
		bundle.putBoolean(PRIMARY_EMAIL_CONFIRMED, user.isPrimaryEmailConfirmed());
		bundle.putInt(PRIVATE_PLAYLISTS_COUNT, user.getPlaylistCount());
		bundle.putInt(PRIVATE_TRACK_COUNT, user.getPrivateTracksCount());
		//bundle.putInt(PUBLIC_FAVORITES_COUNT,user.getPublicFavoriteCount());
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
	

	


	/**
	 * like a song 
	 * @param song song want to be like
	 * @return false if not login, true if sucess
	 */
	public boolean likeSong(Song song){
		/*
		 * Havent login
		 */
		return true;
	}
	
	private class likeSongBackground extends AsyncTask<Song, String,Boolean>{

		//private Song song;
		
		
		@Override
		protected Boolean doInBackground(Song...song) {

			return true;
		}
		
	}
	

	/**
	 * check if this user like the given song or not
	 * @param songID id of Song want to be check
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
	


	
	/**
	 * Get the Following of current user
	 * @return list of following user
	 * @throws IOException
	 * @throws JSONException
	 */
	public ArrayList<User> getFollowingUsers(int offset) throws IOException, JSONException {
		
		// TODO Auto-generated method stub

		
		return null;
	}
	
	public ArrayList<User> getSCUserSearch(String query ,int page) throws IOException, JSONException {
		
		// TODO Auto-generated method stub

		return null;
	}
	
	/**
	 * Get the Followers of current user
	 * @return List of followers
	 * @throws IOException
	 * @throws JSONException
	 */
	public ArrayList<User> getFollowerUsers(int offset) throws IOException, JSONException {
		// TODO Auto-generated method stub
	
		
		return null;
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

		
		
	}

	/**
	 * Unfollow an user on soundcloud
	 * @param soundCloudAccount
	 */
	public void unFollow(SCAccount soundCloudAccount) {
		// TODO Auto-generated method stub

		
	}
	
	/**
	 * Get info of soundclouduser by id
	 * @param params
	 * @return
	 * @throws IOException 
	 * @throws JSONException 
	 */
	public SCAccount getUserbyId(String params) throws IOException, JSONException{
		return null;
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
