package ngo.music.soundcloudplayer.Adapters;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.volley.api.AppController;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.boundary.MainActivity;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.entity.SoundCloudAccount;
import ngo.music.soundcloudplayer.entity.User;
import android.content.Context;
import android.provider.DocumentsContract.Root;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SoundCloudFollowingAdapter  extends ArrayAdapter<User>{

	
	private ArrayList<User> users;
	public SoundCloudFollowingAdapter(Context context, int resource,ArrayList<User> users) {
		super(context, resource);
		this.users = users; 
		
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//	System.out.println ("ADAPTER + " + users.size());
		View v = convertView;
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.user_layout_adapter, null);
		}
		
		User soundCloudAccount = users.get(position);
		//System.out.println (soundCloudAccount.getAvatarUrl());
		/**
		 * Set avatar for user
		 */
		NetworkImageView avatar = (NetworkImageView) v.findViewById(R.id.user_image);
		ImageLoader mImageLoader = AppController.getInstance().getImageLoader(); 
		avatar.setDefaultImageResId(R.drawable.ic_launcher);
		avatar.setMinimumHeight(MainActivity.screenHeight/5);
		avatar.setMinimumWidth(MainActivity.screenHeight/5);
		avatar.setMaxHeight(MainActivity.screenHeight/5);
		avatar.setMaxWidth(MainActivity.screenHeight/5);
		avatar.setImageUrl(soundCloudAccount.getAvatarUrl(), mImageLoader);
		
		/*
		 * Set User Full Name
		 */
		TextView userFullName = (TextView) v.findViewById(R.id.user_fullname);
		userFullName.setText(soundCloudAccount.getFullName());
		
		/*
		 * Set Country of user
		 */
//		TextView userCountry = (TextView) v.findViewById(R.id.user_country);
//		//userCountry.setText(soundCloudAccount.getCountry() + " / " + soundCloudAccount.getCity());
//		userCountry.setText(String.valueOf(soundCloudAccount.getId()));
//		
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

}
