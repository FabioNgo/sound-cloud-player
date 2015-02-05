package ngo.music.soundcloudplayer.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.widget.Toast;
import ngo.music.soundcloudplayer.api.ApiWrapper;
import ngo.music.soundcloudplayer.api.Http;
import ngo.music.soundcloudplayer.api.Request;
import ngo.music.soundcloudplayer.boundary.MusicPlayerMainActivity;
import ngo.music.soundcloudplayer.entity.Category;
import ngo.music.soundcloudplayer.entity.OnlineSong;
import ngo.music.soundcloudplayer.entity.SCPlaylist;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.Constants;

public abstract class SCPlayListController extends CategoryController implements Constants.PlaylistConstant {



	@Override
	public void storeCategories() throws IOException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void addSongsToCategory(String categoryName, ArrayList<Song> songs) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeSongFromCate(Song song, String cate) {
		// TODO Auto-generated method stub
		
		for (Category category : categories) {
			if (category.getTitle().equals(cate)) {
				new removeSongFromSCPlaylistBackground(category, song).execute();
			}
		}
		
//		try {
//			//storeCategories();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}
	@Override
	public void removeCategory(String cate) {
		// TODO Auto-generated method stub
		for (Category category : categories) {
			if (category.getTitle().equals(cate)) {
				new removeSCPlaylistBackground(category).execute();
			}
		}
		
	}
	@Override
	public void updateTitle(String oldName, String newName) throws Exception {
		// TODO Auto-generated method stub
		if("".equals(newName)){
			throw new Exception("Playlist name cannot be empty");
		}
		for (Category category : categories) {
			if(category.getTitle().equals(newName)){
				throw new Exception("Playlist name exsited");
			}
		}
		for (Category category : categories) {
			if(category.getTitle().equals(oldName)){
				new updateTitleSCPlaylistBackground(category).execute(newName);
				break;
			}
		}
		storeCategories();
		UIController.getInstance().updateUiWhenDataChanged(TAG_DATA_CHANGED);
	}

	@Override
	public void createCategory(String name) throws Exception {
		for (Category category : categories) {
			if (category.getTitle().equals(name)) {
				throw new Exception("A playlist with the same name is existed");
			}
		}
	
		if (name.equals("")) {
			throw new Exception("A playlist cannot be created without a name");
		}
		new CreateSCPlaylistBackground(categories).execute(name);
		
		
	}

	private class removeSongFromSCPlaylistBackground extends AsyncTask<String, String, String>{

		OnlineSong song;
		SCPlaylist playlist;
		public removeSongFromSCPlaylistBackground(Category category, Song song) {
			// TODO Auto-generated constructor stub
			this.song = (OnlineSong) song;
			playlist = (SCPlaylist) category;
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			ApiWrapper wrapper = SCUserController.getInstance().getApiWrapper();
			try {
				HttpResponse resp = wrapper.put(Request.to(Constants.ME_PLAYLISTS + "/" + String.valueOf(playlist.getSoundcloudId()))
						.with("playlist[tracks][][id]", Integer.parseInt(this.song.getId())));
				if (resp.getStatusLine().toString().contains("200")){
					return "Remove successfully";
				}
				return "Remove unsuccessfully";
				
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if (result == null){
				Toast.makeText(MusicPlayerMainActivity.getActivity(), "Cannot remove playlist", Toast.LENGTH_LONG).show();
			}else{
				Toast.makeText(MusicPlayerMainActivity.getActivity(), result, Toast.LENGTH_LONG).show();
			}
			UIController.getInstance().updateUiWhenDataChanged(TAG_ITEM_CHANGED);
			
		}
		
	}
	
	private class removeSCPlaylistBackground extends AsyncTask<String, String, String>{

		
		SCPlaylist playlist;
		public removeSCPlaylistBackground(Category category) {
			// TODO Auto-generated constructor stub
			playlist = (SCPlaylist) category;
		}

		

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			ApiWrapper wrapper = SCUserController.getInstance().getApiWrapper();
			try {
				HttpResponse resp = wrapper.put(Request.to(Constants.ME_PLAYLISTS + "/" + String.valueOf(playlist.getSoundcloudId())));
				if (resp.getStatusLine().toString().contains("200")){
					return "Remove successfully";
				}
				return "Remove unsuccessfully";
				
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if (result == null){
				Toast.makeText(MusicPlayerMainActivity.getActivity(), "Cannot change playlist title", Toast.LENGTH_LONG).show();
			}else{
				Toast.makeText(MusicPlayerMainActivity.getActivity(), result, Toast.LENGTH_LONG).show();
			}
			UIController.getInstance().updateUiWhenDataChanged(TAG_ITEM_CHANGED);
			super.onPostExecute(result);
		}
		
	}
	
	private class updateTitleSCPlaylistBackground extends AsyncTask<String, String, String>{

		
		SCPlaylist playlist;
		public updateTitleSCPlaylistBackground(Category category) {
			// TODO Auto-generated constructor stub
			playlist = (SCPlaylist) category;
		}

		

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			ApiWrapper wrapper = SCUserController.getInstance().getApiWrapper();
			try {
				HttpResponse resp = wrapper.put(Request.to(Constants.ME_PLAYLISTS + "/" + String.valueOf(playlist.getSoundcloudId()))
														.with("playlist[title]",params[0]));
				if (resp.getStatusLine().toString().contains("200")){
					return "Remove successfully";
				}
				return "Remove unsuccessfully";
				
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if (result == null){
				Toast.makeText(MusicPlayerMainActivity.getActivity(), "Cannot remove playlist", Toast.LENGTH_LONG).show();
			}else{
				Toast.makeText(MusicPlayerMainActivity.getActivity(), result, Toast.LENGTH_LONG).show();
			}
			UIController.getInstance().updateUiWhenDataChanged(TAG_ITEM_CHANGED);
			super.onPostExecute(result);
		}
		
		
	}
	
	private class CreateSCPlaylistBackground extends AsyncTask<String, String, SCPlaylist>{

		ArrayList<Category> categories;
		
		public CreateSCPlaylistBackground(ArrayList<Category> categories) {
			this.categories = categories;
			// TODO Auto-generated constructor stub
		}

		@Override
		protected SCPlaylist doInBackground(String... params) {
			// TODO Auto-generated method stub
			//SCPlaylist playlist = new SCPlaylist(params[0]);
			ApiWrapper wrapper = SCUserController.getInstance().getApiWrapper();
			try {
				HttpResponse resp = wrapper.post((Request.to(Constants.ME_PLAYLISTS))
														.with("playlist[title]",params[0]));
				if (resp.getStatusLine().toString().contains("201")){
					String me = Http.getString(resp);
					JSONObject jObject = new JSONObject(me);
					return addPlaylistInfomation(jObject);
				}
				return null;
				
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				
				e.printStackTrace();
				return null;
			}
			
		}
		
		@Override
		protected void onPostExecute(SCPlaylist result) {
			// TODO Auto-generated method stub
			
			if (result == null){
				Toast.makeText(MusicPlayerMainActivity.getActivity(), "Cannot create playlist", Toast.LENGTH_LONG).show();
			}else{
				
				Toast.makeText(MusicPlayerMainActivity.getActivity(), "Create successfully", Toast.LENGTH_LONG).show();
				categories.add(result);
				System.out.println ("PLAYLIST CREATED = " + TAG_ITEM_CHANGED);
				UIController.getInstance().updateUiWhenDataChanged(TAG_ITEM_CHANGED);

				
			}
			
		}
		
		
	}
	
	protected SCPlaylist addPlaylistInfomation(JSONObject jObject)
			throws JSONException {
		SCPlaylist playlist = new SCPlaylist("");
		playlist.setTitle(jObject.getString(PLAYLIST_TITLE));
		playlist.setId(jObject.getString(PLAYLIST_ID));

		playlist.setUserId(jObject.getInt(PLAYLIST_CREATOR_ID));
		playlist.setDuration(jObject.getLong(PLAYLIST_DURATION));
		playlist.setDescription(jObject.getString(PLAYLIST_DESCRIPTION));
		playlist.setPermalinkUrl(jObject.getString(PLAYLIST_PERMALINK_URL));
		return playlist;
	}



}
