package ngo.music.soundcloudplayer.boundary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.Adapters.FileAdapter;
import ngo.music.soundcloudplayer.Adapters.ImageFileAdapter;
import ngo.music.soundcloudplayer.Adapters.OfflineSongAdapter;
import ngo.music.soundcloudplayer.Adapters.SoundCloudExploreAdapter;
import ngo.music.soundcloudplayer.controller.SongController;
import ngo.music.soundcloudplayer.controller.SoundCloudUserController;
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
	private boolean mIsLargeLayout;
	String audioFileDir = "";
	String avatarFileDir = "";
	EditText uploadLink;
	ImageView avatarImg;
	EditText titleEditText;
	EditText descEditText;
	EditText tagEditText;
	RadioGroup privacy;
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
		uploadLink.getLayoutParams().width = MainActivity.screenWidth/2;
		Button uploadSongButton = (Button) findViewById(R.id.upload_button);
		uploadSongButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String rootDir = Environment.getExternalStorageDirectory().getAbsolutePath();
				
				showDialog(rootDir);
				uploadLink.setText(audioFileDir);
				
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
				MainActivity.isExplore = false;
				Intent i = new Intent(getApplicationContext(), MainActivity.class);
				SoundCloudUserController soundCloudUserController = SoundCloudUserController.getInstance();
				
				Bundle bundle = soundCloudUserController.getBundle(soundCloudUserController.getCurrentUser());
				i.putExtra(UserContant.USER, bundle);
				
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
	
	private class BrowserAudioFileFragment extends DialogFragment {
	
		String currentDir;
		String fileSelected = "";
		
		public BrowserAudioFileFragment(String relativeDir) {
			// TODO Auto-generated constructor stub
			super();
			this.currentDir = relativeDir;
		}
	    @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	    	// The only reason you might override this method when using onCreateView() is
	        // to modify any dialog characteristics. For example, the dialog includes a
	        // title by default, but your custom layout might not need it. So here you can
	        // remove the dialog title, but you must call the superclass to get the Dialog.
	        Dialog dialog = super.onCreateDialog(savedInstanceState);
	        dialog.setTitle("\t\t\tCHOOSE YOUR AUDIO FILE\t\t\t");
	        
	        //dialog.getWindow().getAttributes().width = screenWidth;
	        getWindow().setLayout(screenWidth, screenHeight);
	        
	        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        return dialog;
	    }
	    
	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	        // Inflate the layout to use as dialog or embedded fragment
	    	
	    	
	    	final HashMap<String, Boolean>  listFiles = getListFiles(currentDir);
	    	
	    	
	    
	    	View rootView = inflater.inflate(R.layout.file_list_view, container,false);
	    	
	    	configDialogListView(listFiles, rootView);
		
			configDialogButton(rootView);
	        return rootView;
	    }
		/**
		 * @param listFiles
		 * @param rootView
		 */
		private void configDialogListView(
				final HashMap<String, Boolean> listFiles, View rootView) {
			final FileAdapter adapter = new  FileAdapter(getApplicationContext(),R.layout.file_list_view, listFiles);
	    	
	    	
			//adapter.setNotifyOnChange(true);
			final ListView filesListView = (ListView) rootView.findViewById(R.id.file_list);
			
			//System.out.println ("CHANGED");
			//adapter.notifyDataSetChanged(); 
			filesListView.setAdapter(adapter);
			filesListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View arg1, int position,long id) {
					// TODO Auto-generated method stub
					
					String folderSelected = (String) adapter.getItem(position);
					//System.out.println (folderSelected);
					/*
					 * if is the file
					 */
					if (listFiles.get(folderSelected)){
						
						audioFileDir = folderSelected;
						uploadLink.setText(folderSelected);
						
					}else{
						BrowserAudioFileFragment newFragment = new BrowserAudioFileFragment(folderSelected);
						newFragment.show(getFragmentManager(), "dialog");
					}
					getDialog().dismiss();
					
					
					
				}
			});
		}
		/**
		 * @param rootView
		 */
		private void configDialogButton(View rootView) {
			Button cancelButton = (Button) rootView.findViewById(R.id.cancel_button);
			cancelButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					getDialog().dismiss();
				}
			});
			
			Button backButton = (Button) rootView.findViewById(R.id.back_button);
			backButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String parrentDir = new File(currentDir).getParent();
					BrowserAudioFileFragment newFragment;					
					newFragment = new BrowserAudioFileFragment(parrentDir);
					newFragment.show(getFragmentManager(), "dialog");
					getDialog().dismiss();
				}
			});
		}
	    
	   
	  
	}
	 private HashMap<String,Boolean> getListFiles(String relativeDir) {
		 	
		 	HashMap<String, Boolean> listFiles = new HashMap<String,Boolean>();
			// TODO Auto-generated method stub
	    	/*
	    	 * True : is file
	    	 * false: is folder
	    	 */
			
			File dir = new File(relativeDir);
			File[] directoryListing  = dir.listFiles();
			for (File f : directoryListing){
			
					if (f.isDirectory()){
						listFiles.put(f.getAbsolutePath(), false);
					}else{
						if (f.getName().endsWith(".mp3") ||f.getName().endsWith(".wav")){
							listFiles.put(f.getAbsolutePath(), true);
						}
					}
				
			
			}
			
			
			return listFiles; 
	}
	    	   
	  
	
	public void showDialog(String rootDir) {
	    FragmentManager fragmentManager = getFragmentManager();
	    
	    	BrowserAudioFileFragment newFragment = new BrowserAudioFileFragment(rootDir);
	    
	   // if (mIsLargeLayout) {
	        // The device is using a large layout, so show the fragment as a dialog
	        newFragment.show(fragmentManager, "dialog");

	        
			
	     
	        
//	    } else {
//	        // The device is smaller, so show the fragment fullscreen
//	        FragmentTransaction transaction = fragmentManager.beginTransaction();
//	        // For a little polish, specify a transition animation
//	        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//	        // To make it fullscreen, use the 'content' root view as the container
//	        // for the fragment, which is always the root view for the activity
//	        transaction.add(android.R.id.content, newFragment)
//	                   .addToBackStack(null).commit();
//	    }
	}
	
	/**
	 * To get image selected from gallery
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if(requestCode == PICK_IMAGE && data != null && data.getData() != null && resultCode == Activity.RESULT_OK) {
	        Uri _uri = data.getData();

	        InputStream stream = null;
			try {
				stream = getContentResolver().openInputStream(_uri);
				   Bitmap bitmap = BitmapFactory.decodeStream(stream);
		            stream.close();
		            avatarImg.setImageBitmap(bitmap);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
			String fileDir = uploadLink.getText().toString();
			File audioFile = new File(fileDir);
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
			
			File avatarFile = new File(avatarFileDir);
			
			/*
			 * Set data to song
			 */
			Song song = new Song();
			song.setTitle(title);
			song.setDescription(desc);
			song.setGerne(tag);
			song.setPrivacy(privacyStr);
			song.setAssetData(audioFile);
			song.setArtworkData(avatarFile);
			
			SongController songController = SongController.getInstance();
			try {
				songController.uploadSong(song, audioFile, avatarFile);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			


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
