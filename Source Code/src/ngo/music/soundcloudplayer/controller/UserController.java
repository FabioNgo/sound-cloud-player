package ngo.music.soundcloudplayer.controller;

import java.net.URI;

import ngo.music.soundcloudplayer.entity.User;

public abstract class UserController {

	public UserController() {
		// TODO Auto-generated constructor stub
	}

	public abstract void login();
	
	public abstract User validateLogin(String username, String password);
	
	public abstract void logout();
}
