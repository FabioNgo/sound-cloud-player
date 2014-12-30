package ngo.music.soundcloudplayer.Adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.boundary.MainActivity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageFileAdapter extends ArrayAdapter<String> {

	HashMap<String,Boolean > listFiles;
	ArrayList<String> imageFilesDir;
	public ImageFileAdapter(Context context, int resource, HashMap<String,Boolean> files) {
		super(context, resource);
		listFiles = files;
		this.imageFilesDir = new ArrayList<String>(files.keySet());
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v = convertView;
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.single_file_layout, null);
		}

		setLayoutInformation(position, v);
		return v;
	}
	private void setLayoutInformation(int position, View v) {
		// TODO Auto-generated method stub
		String fileDir = imageFilesDir.get(position);
		
		/**
		 * Set avatar for song
		 */
		ImageView fileIcon = (ImageView) v.findViewById(R.id.file_icon);
		fileIcon.getLayoutParams().height = MainActivity.screenWidth/10;
		fileIcon.getLayoutParams().width = MainActivity.screenWidth/10;
		/*
		 * If is file
		 */
		if (listFiles.get(fileDir)){
			BitmapDrawable img = new BitmapDrawable(fileDir);
			fileIcon.setImageDrawable(img);
		}else{
			fileIcon.setImageResource(R.drawable.folder_button);
		}

		/*
		 * Set title
		 */
		TextView fileName = (TextView) v.findViewById(R.id.file_name);
		 String temp [] = fileDir.split("/");
		 
		fileName.setText(temp[temp.length-1]);

		
	}

	@Override
	public String getItem(int position) {
		return imageFilesDir.get(position);
	}

	@Override
	public int getCount() {
		return imageFilesDir.size();
	}

}
