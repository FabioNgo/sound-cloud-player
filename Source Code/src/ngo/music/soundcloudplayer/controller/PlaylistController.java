package ngo.music.soundcloudplayer.controller;

import java.io.IOException;

import ngo.music.soundcloudplayer.api.ApiWrapper;
import ngo.music.soundcloudplayer.api.Stream;
import ngo.music.soundcloudplayer.api.Token;
import ngo.music.soundcloudplayer.general.Constants;

public class PlaylistController implements Constants {

	private static PlaylistController  instance = null;
	
	private PlaylistController() {
		// TODO Auto-generated constructor stub
	}
	
	public PlaylistController getInstance (){
		if (instance == null){
			return new PlaylistController();
		}else{
			return instance;
		}
	}
	
	public Stream getPlaylistFromId(long id){
		Stream stream = null;
		SoundCloudUserController userController = SoundCloudUserController.getInstance();
		Token token = userController.getToken();
		ApiWrapper wrapper = new ApiWrapper(CLIENT_ID, CLIENT_SECRET, null, token);
		
		/*
		 * API URL OF THE SONG
		 */
		String uri = "http://api.soundcloud.com/tracks/" + id + "/stream";
		try {
			stream = wrapper.resolveStreamUrl(uri, true);
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return stream;
	}

}
