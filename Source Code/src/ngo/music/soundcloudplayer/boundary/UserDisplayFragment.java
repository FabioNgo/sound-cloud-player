package ngo.music.soundcloudplayer.boundary;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.controller.SoundCloudUserController;
import ngo.music.soundcloudplayer.database.DatabaseHandler;
import ngo.music.soundcloudplayer.general.CircularImageView;
import ngo.music.soundcloudplayer.general.Contants;
import ngo.music.soundcloudplayer.imageloader.ImageLoader;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UserDisplayFragment extends Fragment implements Contants.UserContant, Contants.UIContant {

	int layoutWidth;
	int layoutHeight;
	View rootView;
	
	public UserDisplayFragment(){
		super();
		layoutWidth = MainActivity.screenWidth;
		layoutHeight = MainActivity.screenHeight;
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.user_profile_layout, container,false);
		
		/*
		 * Get info from actvity
		 */
		Bundle bundle  = getActivity().getIntent().getBundleExtra(USER);		
		String avatarUrl = bundle.getString(AVATAR_URL);
		String fullNameString = bundle.getString(FULLNAME);
		int numFollowers = bundle.getInt(FOLLOWERS_COUNT);

		configUserLayout(avatarUrl, fullNameString, numFollowers);
		
		
		
		TextView myMusic = (TextView) rootView.findViewById(R.id.my_music_id);
		TextView logOut = (TextView) rootView.findViewById(R.id.logout_id);
		logOut.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SoundCloudUserController soundCloudUserController = SoundCloudUserController.getInstance();
				soundCloudUserController.logout();
				DatabaseHandler databaseHandler = DatabaseHandler.getInstance(getActivity());
				databaseHandler.refreshDatabase();
				Intent loginAct = new Intent(getActivity(), LoginActivity.class);
				startActivity(loginAct);
			}
		});
		
		return rootView;
		
	
		
	}

	/**
	 * @param background.rootView
	 * @param avatarUrl
	 * @param fullNameString
	 * @param numFollowers
	 */
	private void configUserLayout(String avatarUrl,
			String fullNameString, int numFollowers) {
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
		CircularImageView avatar = (CircularImageView)rootView.findViewById(R.id.avatar_image_id);
		//avatar.setPadding(10, frame.getLayoutParams().height * 0.2,null,null);
		
		//BasicFunctions.ResizeImageView(layoutWidth, height, imageView);
		avatar.getLayoutParams().height = (int) (frame.getLayoutParams().height *0.5 );
		avatar.getLayoutParams().width = avatar.getLayoutParams().height;
		
		ImageLoader imgLoader = new ImageLoader(getActivity().getApplicationContext());

		// Loader image - will be shown before loading image
		int loader = R.drawable.image_not_found;
		
		imgLoader.DisplayImage(avatarUrl, loader,avatar);

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
	}

	
	
	
}
