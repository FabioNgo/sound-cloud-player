package ngo.music.soundcloudplayer.boundary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.adapters.ListSongAdapter;
import ngo.music.soundcloudplayer.adapters.OfflineSongAdapter;
import ngo.music.soundcloudplayer.controller.SongController;
import ngo.music.soundcloudplayer.controller.SCUserController;
import ngo.music.soundcloudplayer.entity.SCSong;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.Constants;
import ngo.music.soundcloudplayer.service.MusicPlayerService;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class UploadSongActivity extends Activity implements Constants {

	
	private static final int PICK_IMAGE = 1;
	protected static final int PICK_AUDIO = 0;
	private boolean mIsLargeLayout;
	String audioFileDir = "";
	String avatarFileDir = "";
	EditText uploadLink;
	ImageView avatarImg;
	EditText titleEditText;
	EditText descEditText;
	EditText tagEditText;
	RadioGroup privacy;
	
	
	File audioFile = null;
	File avatarFile = null;
	/*
	 * 0 : LOAD AUDIO
	 * 1: LOAD IMAGE
	 */
	int type = -1;
	private int screenHeight;
	private int screenWidth;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	   
	    setContentView(R.layout.upload_song_layout);
	    mIsLargeLayout = getResources().getBoolean(R.bool.large_layout);
	    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
	    configLayout();
	    // TODO Auto-generated method stub
	}
	
	
	private void configLayout(){
		// Get the width and length of the screen
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		screenHeight = displayMetrics.heightPixels;
		screenWidth = displayMetrics.widthPixels;
		
		configFormLayout();
		configButtonActivity();
	}


	/**
	 * Config Edittext, Avatar, Search File
	 */
	private void configFormLayout() {
		
		/*
		 * UPLOAD LINK
		 */
		uploadLink = (EditText) findViewById(R.id.upload_link);
		uploadLink.getLayoutParams().width = MusicPlayerMainActivity.screenWidth/2;
		Button uploadSongButton = (Button) findViewById(R.id.upload_button);
		uploadSongButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String rootDir = Environment.getExternalStorageDirectory().getAbsolutePath();
				Intent intent = new Intent();
				intent.setType("audio/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_AUDIO);
				
				//showDialog(rootDir);
				//uploadLink.setText(audioFileDir);
				
			}
		});
		
		titleEditText = (EditText) findViewById(R.id.upload_title_song);
		descEditText = (EditText) findViewById(R.id.upload_description_song);
		tagEditText = (EditText) findViewById(R.id.upload_tag_song);
		privacy = (RadioGroup) findViewById(R.id.upload_privacy);
		/*
		 * UPLOAD AVATAR
		 */
		avatarImg = (ImageView) findViewById(R.id.avatar_upload_image);
		avatarImg.getLayoutParams().width = screenWidth/3;
		avatarImg.getLayoutParams().height = avatarImg.getLayoutParams().width;
		Button uploadAvatarButton = (Button) findViewById(R.id.upload_avatar_button);
		uploadAvatarButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				//intent.setAction(Intent.)
				startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
				
			}
		});
	}


	/**
	 * Config Cancel and OK Button
	 */
	private void configButtonActivity() {
		/*
		 * CANCEL BUTTON
		 */
		Button cancelButton = (Button) findViewById(R.id.upload_cancel_button);
		cancelButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//MusicPlayerMainActivity.isExplore = false;
				Intent i = new Intent(getApplicationContext(), MusicPlayerMainActivity.class);
				SCUserController soundCloudUserController = SCUserController.getInstance();
				
				Bundle bundle;
				try {
					bundle = soundCloudUserController.getBundle(soundCloudUserController.getUser());
					i.putExtra(UserContant.USER, bundle);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				startActivity(i);
			}
		});
		
		/*
		 * SUBMIT BUTTON
		 */
		Button submitButton = (Button) findViewById(R.id.upload_submit_button);
		submitButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				String fileDir = uploadLink.getText().toString();
//				File audioFile = new File(fileDir);
//				String title = titleEditText.getText().toString();
//				String desc = descEditText.getText().toString();
//				String tag = tagEditText.getText().toString();
//				String privacyStr = "";
//				;
//				switch (privacy.getCheckedRadioButtonId()){
//				case R.id.public_radio_button:
//					//if (privacyBoolean){
//						privacyStr = "public";
//				
//					break;
//				case R.id.private_radio_button:
//				//	if (privacyBoolean){
//						privacyStr = "private";
//					//}
//				}
//				
//				File avatarFile = new File(avatarFileDir);
//				
//				/*
//				 * Set data to song
//				 */
//				Song song = new Song();
//				song.setTitle(title);
//				song.setDescription(desc);
//				song.setGerne(tag);
//				song.setPrivacy(privacyStr);
//				song.setAssetData(audioFile);
//				song.setArtworkData(avatarFile);
//				
//				SongController songController = SongController.getInstance();
//				try {
//					songController.uploadSong(song, audioFile, avatarFile);
//				} catch (ClassNotFoundException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				new uploadSongBackground().execute();
			}
		});
	}
	
	
	/**
	 * To get image selected from gallery
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if(data != null && data.getData() != null && resultCode == Activity.RESULT_OK) {
	    	if (requestCode == PICK_IMAGE) { 
		        Uri _uri = data.getData();
		        avatarFileDir = _uri.getPath();
		        
		        InputStream stream = null;
				try {
					stream = getContentResolver().openInputStream(_uri);
					   Bitmap bitmap = BitmapFactory.decodeStream(stream);
			            stream.close();
			            avatarImg.setImageBitmap(bitmap);
			            avatarFile = new File(avatarFileDir);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
	    	
	    	if (requestCode == PICK_AUDIO){
		        Uri _uri = data.getData();
		        String uri = _uri.getPath();
		    	
				audioFile =  new File(uri);
				audioFileDir = uri;
	    	}
	
	       
	    }
	    super.onActivityResult(requestCode, resultCode, data);
	}
	
	private class uploadSongBackground extends AsyncTask<File, String, String>{

		ProgressDialog pDialog;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			
			pDialog = new ProgressDialog(UploadSongActivity.this);
			pDialog.setMessage("Uploading Song......");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			
			pDialog.show();
		}
		@Override
		protected String doInBackground(File... params) {
			// TODO Auto-generated method stub
			
			String title = titleEditText.getText().toString();
			String desc = descEditText.getText().toString();
			String tag = tagEditText.getText().toString();
			String privacyStr = "";
			;
			switch (privacy.getCheckedRadioButtonId()){
			case R.id.public_radio_button:
				//if (privacyBoolean){
					privacyStr = "public";
			
				break;
			case R.id.private_radio_button:
			//	if (privacyBoolean){
					privacyStr = "private";
				//}
			}
			
			
			
			/*
			 * Set data to song
			 */
			SCSong song = new SCSong("", title, "", "", "");
			song.setTitle(title);
			song.setDescription(desc);
			song.setGenre(tag);
			song.setPrivacy(privacyStr);
			song.setAssetData(audioFile);
			song.setArtworkData(avatarFile);
			
			SongController songController = SongController.getInstance();
//			try {
//				songController.uploadSong(song, audioFile, avatarFile);
//			} catch (ClassNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			


			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			pDialog.dismiss();
			Toast.makeText(UploadSongActivity.this, "Upload sucessfully", Toast.LENGTH_LONG).show();
		}
	}

}
