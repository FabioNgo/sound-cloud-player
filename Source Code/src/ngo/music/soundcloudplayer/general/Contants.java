package ngo.music.soundcloudplayer.general;

import java.net.URI;

/**
 * Store the contants of app
 * @author LEBAO_000
 *
 */
public class Contants {

	public static final String CLIENT_ID ="2ae2cf32fdd87967a353b7e718d7f3cd";
	public static final String CLIENT_SECRET = "acfe1a475e4d004214fed3326a01eb79";
	public static final URI REDIRECT_URI     = URI.create("http://localhost:8000");
	
	public static final int FACEBOOK_USER = 0;
	public static final int GOOGLE_PLUS_USER = 1;
	public static final int SOUNDCLOUD_USER = 2;
}
