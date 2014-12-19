package ngo.music.soundcloudplayer.controller;

import ngo.music.soundcloudplayer.general.Contants;

public class UserControllerFactory {

	public UserControllerFactory() {
		// TODO Auto-generated constructor stub
	}
	
	public static UserController createUserController(int TYPE_USER_TAG){
		switch (TYPE_USER_TAG){
		case Contants.FACEBOOK_USER:
			return new FacebookUserController();
		case Contants.GOOGLE_PLUS_USER:
			return new GooglePlusUserController();
		case Contants.SOUNDCLOUD_USER:
			return  SoundCloudUserController.getInstance();
		default:
			return null;
		}
	}

}
