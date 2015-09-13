package ngo.music.player.controller;

import org.json.JSONException;

import java.io.IOException;

import ngo.music.player.entity.User;

public abstract class UserController {

	public UserController() {
		// TODO Auto-generated constructor stub
	}

	public abstract void login() throws IOException, JSONException;
	
	public abstract User validateLogin(String username, String password);
	
	public abstract void logout();
}
