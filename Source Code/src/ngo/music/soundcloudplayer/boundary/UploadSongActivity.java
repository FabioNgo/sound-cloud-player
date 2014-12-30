package ngo.music.soundcloudplayer.boundary;

import java.io.File;
import java.io.IOException;
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
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class UploadSongActivity extends Activity implements Constants {

	private static final int AUDIO = 0;
	private static final int AVATAR = 1;
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
	    
	    configLayout();
	    // TODO Auto-generated method stub
	}
	
	
	private void configLayout(){
		
		// Get the width and length of the screen
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		screenHeight = displayMetrics.heightPixels;
		screenWidth = displayMetrics.widthPixels;
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
				type =  AUDIO;
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
				String rootDir = Environment.getExternalStorageDirectory().getAbsolutePath(); 
				type = AVATAR;
				showDialog(rootDir);
				
				
			}
		});
		
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
				Song song = new Song();
				song.setTitle(title);
				song.setDescription(desc);
				song.setTagList(tag);
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
				
			}
		});
	}
	
	private class BrowserAudioFileFragment extends DialogFragment {
	
		String currentDir;
		String fileSelected = "";
		HashMap<String, Boolean> files = null;
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
	        dialog.setTitle("CHOOSE YOUR AUDIO FILE");
	        
	        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        return dialog;
	    }
	    
	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	        // Inflate the layout to use as dialog or embedded fragment
	    	
	    	
	    	files = getListAudioFiles(currentDir);
	    	
	    
	    	View rootView = inflater.inflate(R.layout.file_list_view, container,false);
	    	
	    	final FileAdapter adapter = new  FileAdapter(getApplicationContext(),R.layout.file_list_view, files);
	    	
			//adapter.setNotifyOnChange(true);
			ListView filesList = (ListView) rootView.findViewById(R.id.file_list);
			//System.out.println ("CHANGED");
			//adapter.notifyDataSetChanged(); 
			filesList.setAdapter(adapter);
			filesList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View arg1, int position,long id) {
					// TODO Auto-generated method stub
					
					String folderSelected = (String) adapter.getItem(position);
					//System.out.println (folderSelected);
					/*
					 * if is the file
					 */
					if (files.get(folderSelected)){
						
						audioFileDir = folderSelected;
						uploadLink.setText(folderSelected);
						
					}else{
						BrowserAudioFileFragment newFragment = new BrowserAudioFileFragment(folderSelected);
						newFragment.show(getFragmentManager(), "dialog");
					}
					getDialog().dismiss();
					
					
					
				}
			});
			
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
	        return rootView;
	    }
	    
	   
	  
	}
	 private HashMap<String,Boolean> getListAudioFiles(String relativeDir) {
			// TODO Auto-generated method stub
	    	/*
	    	 * True : is file
	    	 * false: is folder
	    	 */
			HashMap<String, Boolean> listFiles = new HashMap<String,Boolean>();
			File dir = new File(relativeDir);
			File[] directoryListing  = dir.listFiles();
			for (File f : directoryListing){
				if (f.getName().endsWith(".mp3") ||f.getName().endsWith(".wav")){
					listFiles.put(f.getAbsolutePath(), true);
				}
				if (f.isDirectory()){
					listFiles.put(f.getAbsolutePath(), false);
				}
			}
			return listFiles; 
		}
	    
		private class BrowserAvatarFileFragment extends DialogFragment {
			
			String currentDir;
			String fileSelected = "";
			HashMap<String, Boolean> imageFiles = null;
			//HashMap<String, Boolean> files = null;
			public BrowserAvatarFileFragment(String relativeDir) {
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
		        dialog.setTitle("CHOOSE YOUR IMAGE FILE");
		        
		        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		        return dialog;
		    }
		    
		    @Override
		    public View onCreateView(LayoutInflater inflater, ViewGroup container,
		            Bundle savedInstanceState) {
		        // Inflate the layout to use as dialog or embedded fragment
		    	imageFiles = getListImageFiles(currentDir);
		    	View rootView = inflater.inflate(R.layout.file_list_view, container,false);
		    	
		    	final ImageFileAdapter adapter = new  ImageFileAdapter(getApplicationContext(),R.layout.file_list_view, imageFiles);
		    	
				//adapter.setNotifyOnChange(true);
				ListView filesList = (ListView) rootView.findViewById(R.id.file_list);
				//System.out.println ("CHANGED");
				//adapter.notifyDataSetChanged(); 
				filesList.setAdapter(adapter);
				filesList.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View arg1, int position,long id) {
						// TODO Auto-generated method stub
						String folderSelected = (String) adapter.getItem(position);
						/*
						 * if is the file
						 */
						if (imageFiles.get(folderSelected)){
							BitmapDrawable img = new BitmapDrawable(folderSelected);
							avatarFileDir = folderSelected;
							avatarImg.setImageDrawable(img);
							
						}else{
							BrowserAvatarFileFragment newFragment = new BrowserAvatarFileFragment(folderSelected);
							newFragment.show(getFragmentManager(), "dialog");
						}
						getDialog().dismiss();
						
						
						
					}
				});
				
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
									
						BrowserAvatarFileFragment newFragment = new BrowserAvatarFileFragment(parrentDir);
						newFragment.show(getFragmentManager(), "dialog");
						getDialog().dismiss();
					}
				});
		        return rootView;
		    }
		    
		   
		  
		}
	    private HashMap<String,Boolean> getListImageFiles(String relativeDir) {
	    	// TODO Auto-generated method stub
	    	/*
	    	 * True : is file
	    	 * false: is folder
	    	 */
			HashMap<String, Boolean> listFiles = new HashMap<String,Boolean>();
			File dir = new File(relativeDir);
			File[] directoryListing  = dir.listFiles();
			for (File f : directoryListing){

				if (f.getName().endsWith(".jpg") || f.getName().endsWith(".png") || f.getName().endsWith("jpeg")){
					
					listFiles.put(f.getAbsolutePath(), true);
				}
				if (f.isDirectory()){
					listFiles.put(f.getAbsolutePath(), false);
				}
			}
			return listFiles; 
		}
	public void showDialog(String rootDir) {
	    FragmentManager fragmentManager = getFragmentManager();
	    if (type == AUDIO){
	    	BrowserAudioFileFragment newFragment = new BrowserAudioFileFragment(rootDir);
	    
	   // if (mIsLargeLayout) {
	        // The device is using a large layout, so show the fragment as a dialog
	        newFragment.show(fragmentManager, "dialog");
	    } else if (type == AVATAR){
	    	BrowserAvatarFileFragment avatarFragment = new BrowserAvatarFileFragment(rootDir);
		    
	 	   // if (mIsLargeLayout) {
	 	        // The device is using a large layout, so show the fragment as a dialog
	 	        avatarFragment.show(fragmentManager, "dialog");
	    }
	        
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

}
