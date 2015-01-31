package ngo.music.soundcloudplayer.controller;

import ngo.music.soundcloudplayer.general.Constants;

public class UserControllerFactory {

	public UserControllerFactory() {
		// TODO Auto-generated constructor stub
	}
	
	public static UserController createUserController(int TYPE_USER_TAG){
		switch (TYPE_USER_TAG){
		case Constants.FACEBOOK_USER:
			return new FacebookUserController();
		case Constants.GOOGLE_PLUS_USER:
			return new GooglePlusUserController();
		case Constants.SOUNDCLOUD_USER:
			return  SCUserController.getInstance();
		default:
			return null;
		}
	}

}
