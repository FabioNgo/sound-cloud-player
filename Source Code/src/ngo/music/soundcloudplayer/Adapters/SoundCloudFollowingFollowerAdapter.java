package ngo.music.soundcloudplayer.Adapters;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.volley.api.AppController;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.boundary.MainActivity;
import ngo.music.soundcloudplayer.controller.SoundCloudUserController;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.entity.SoundCloudAccount;
import ngo.music.soundcloudplayer.entity.User;
import ngo.music.soundcloudplayer.general.BasicFunctions;
import android.content.Context;
import android.os.AsyncTask;
import android.provider.DocumentsContract.Root;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SoundCloudFollowingFollowerAdapter  extends ArrayAdapter<User>{

	
	private ArrayList<User> users;
	private SoundCloudAccount soundCloudAccount;
	TextView userFollow;
	boolean isFollowing = false;
	View v;
	public SoundCloudFollowingFollowerAdapter(Context context, int resource,ArrayList<User> users) {
		
		super(context, resource);
		this.users = users; 
		
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//	System.out.println ("ADAPTER + " + users.size());
		v = convertView;
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.user_layout_adapter, null);
		}
		
		soundCloudAccount = (SoundCloudAccount) users.get(position);
		//System.out.println (soundCloudAccount.getAvatarUrl());
		/**
		 * Set avatar for user
		 */
		NetworkImageView avatar = (NetworkImageView) v.findViewById(R.id.user_image);
		ImageLoader mImageLoader = AppController.getInstance().getImageLoader(); 
		avatar.setDefaultImageResId(R.drawable.ic_launcher);
		
		BasicFunctions.setImageViewSize(MainActivity.screenHeight/5, MainActivity.screenHeight/5, avatar);

		avatar.setImageUrl(soundCloudAccount.getAvatarUrl(), mImageLoader);
		notifyDataSetChanged();
		/*
		 * Set User Full Name
		 */
		TextView userFullName = (TextView) v.findViewById(R.id.user_fullname);
		userFullName.setText(soundCloudAccount.getFullName());
		notifyDataSetChanged();
		/*
		 * Set Country of user
		 */
		TextView userCountry = (TextView) v.findViewById(R.id.user_country);
		//userCountry.setText(soundCloudAccount.getCountry() + " / " + soundCloudAccount.getCity());
		
		userCountry.setText(String.valueOf(soundCloudAccount.getCity() +  "  " + soundCloudAccount.getCountry()));
		notifyDataSetChanged();
		
		
		/*
		 * Set number of follower
		 * Display the number of people who is follwing this user
		 */
		TextView userFollower = (TextView) v.findViewById(R.id.follwer_count_id);
		userFollower.setText(String.valueOf(soundCloudAccount.getNumFollowerString()) + " followers");
		notifyDataSetChanged();
		
		/*
		 * Follow/Unfollow
		 */
		userFollow = (TextView) v.findViewById(R.id.follow_text);
		//new checkFollowingBackground().execute();
		
		
		notifyDataSetChanged();
		return v;
	}
	
	@Override
	public User getItem(int position) {
		return users.get(position);
	}
	@Override
	public int getCount() {
		return users.size();
	}
	
	public ArrayList<User> getUsers(){
		return users;
	}
	

	private class checkFollowingBackground extends AsyncTask<String, String, String>{
		
		
		
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			userFollow = (TextView) v.findViewById(R.id.follow_text);
		}
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			SoundCloudUserController userController = SoundCloudUserController.getInstance();
			isFollowing = userController.isFollowing(soundCloudAccount);
			return null;
				
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if (isFollowing){
				userFollow.setText("UNFOLLOW");
			}else{
				userFollow.setText("FOLLOW");
			}
			
			userFollow.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					SoundCloudUserController userController = SoundCloudUserController.getInstance();
					if (isFollowing){
						userController.follow(soundCloudAccount);
						Toast.makeText(getContext(), "You unfollowed "+ soundCloudAccount.getFullName(), Toast.LENGTH_LONG).show();
					}else{
						userController.unFollow(soundCloudAccount);
						Toast.makeText(getContext(), "You unfollowed "+ soundCloudAccount.getFullName(), Toast.LENGTH_LONG).show();
					}
				}
			});
			
			
		}
	
		
		
	}

}
