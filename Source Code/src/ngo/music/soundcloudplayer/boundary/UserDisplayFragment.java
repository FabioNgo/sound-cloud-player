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
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UserDisplayFragment extends Fragment implements Contants.UserContant, Contants.UIContant {

	int layoutWidth;
	int layoutHeight;
	
	public UserDisplayFragment(){
		super();
		layoutWidth = MainActivity.screenWidth;
		layoutHeight = MainActivity.screenHeight;
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.user_profile_layout, container,false);
		Bundle bundle  = getActivity().getIntent().getBundleExtra(USER);
		Bundle bundle2 = getArguments();
		
		String avatarUrl = bundle.getString(AVATAR_URL);
		String fullNameString = bundle.getString(FULLNAME);
		int numFollowers = bundle.getInt(FOLLOWERS_COUNT);
	
		
		//layoutWidth = bundle2.getInt(LAYOUT_WIDTH);
		
		
		/*
		 * Config FrameLayout
		 */
		RelativeLayout frame =  (RelativeLayout) rootView.findViewById(R.id.frame_avatar);
		// Get the width and length of the screen
		
		
		frame.getLayoutParams().width = layoutWidth;
		frame.getLayoutParams().height = (int)(layoutHeight * 0.2);
		
		/*
		 * load avatar
		 */
		FrameLayout avatar = (FrameLayout)rootView.findViewById(R.id.avatar_layout);
		//avatar.setPadding(10, frame.getLayoutParams().height * 0.2,null,null);
		
		//BasicFunctions.ResizeImageView(layoutWidth, height, imageView);
		avatar.getLayoutParams().height = (int) (frame.getLayoutParams().height * 0.5);
		avatar.getLayoutParams().width = avatar.getLayoutParams().height;
		
		ImageLoader imgLoader = new ImageLoader(getActivity().getApplicationContext());

		// Loader image - will be shown before loading image
		int loader = R.drawable.image_not_found;
		
		ImageView avatarCore = (ImageView)rootView.findViewById(R.id.avatar_image_id);
		
		imgLoader.DisplayImage(avatarUrl, loader,avatarCore);
		
		
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
