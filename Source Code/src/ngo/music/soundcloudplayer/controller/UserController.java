package ngo.music.soundcloudplayer.controller;

import java.io.IOException;
import java.net.URI;

import org.json.JSONException;

import ngo.music.soundcloudplayer.entity.User;

public abstract class UserController {

	public UserController() {
		// TODO Auto-generated constructor stub
	}

	public abstract void login() throws IOException, JSONException;
	
	public abstract User validateLogin(String username, String password);
	
	public abstract void logout();
}
