package ngo.music.soundcloudplayer.boundary;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.general.BasicFunctions;
import ngo.music.soundcloudplayer.general.Contants;
import ngo.music.soundcloudplayer.imageloader.ImageLoader;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class UserDisplayFragment extends Fragment implements Contants.UserContant {

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.user_profile_layout, container,false);
		Bundle bundle  = getActivity().getIntent().getBundleExtra(USER);
		
		String avatarUrl = bundle.getString(AVATAR_URL);
		String fullNameString = bundle.getString(FULLNAME);
		int numFollowers = bundle.getInt(FOLLOWERS_COUNT);
		
		/*
		 * Config FrameLayout
		 */
		FrameLayout frame =  (FrameLayout) rootView.findViewById(R.id.frame_avatar);
		// Get the width and length of the screen
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		int screenHeight = displayMetrics.heightPixels;
		int screenWidth = displayMetrics.widthPixels;
		int size = (int) (Math.min(screenWidth, screenHeight) * 0.3);
		//frame.getLayoutParams().width = size;
		frame.getLayoutParams().height = (int)(screenHeight * 0.3);
		
		/*
		 * load avatar
		 */
		ImageView avatar = (ImageView)rootView.findViewById(R.id.avatar_id);

		avatar.getLayoutParams().width = frame.getLayoutParams().width;
		avatar.getLayoutParams().height = frame.getLayoutParams().height;
		ImageLoader imgLoader = new ImageLoader(getActivity().getApplicationContext());

		// Loader image - will be shown before loading image
		int loader = R.drawable.image_not_found;
		imgLoader.DisplayImage(avatarUrl, loader, avatar);

		
		/*
		 * load fullname
		 * 
		 */
		TextView fullName = (TextView) rootView.findViewById(R.id.fullname);
		fullName.setText(fullNameString);
		
		
		/*
		 * load follower
		 */
		TextView follower = (TextView) rootView.findViewById(R.id.follower);
		System.out.println ("NUMBER OF TRACKS " +avatarUrl);
		follower.setText(String.valueOf(numFollowers)+ " followers");
		return rootView;
		
	
		
	}

	
	
	
}
