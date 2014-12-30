/**
 * 
 */
package ngo.music.soundcloudplayer.Adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.todddavies.components.progressbar.ProgressWheel;
import com.volley.api.AppController;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.controller.UpdateUiFromServiceController;
import ngo.music.soundcloudplayer.entity.FolderFile;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.service.MusicPlayerService;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author LEBAO_000
 *
 */
public class FileAdapter extends ArrayAdapter<String> {

	HashMap<String, Boolean> listFiles = new HashMap<String, Boolean>();
	ArrayList<String> linkOfFiles = new ArrayList<String>();
	/**
	 * @param context
	 * @param resource
	 * @param textViewResourceId
	 * @param objects
	 */
	public FileAdapter(Context context, int resource,HashMap<String, Boolean> dir) {
		super(context, resource);
		this.listFiles = dir;
		linkOfFiles = new ArrayList<String>(dir.keySet());
		
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
		String fileDir = linkOfFiles.get(position);
		
		/**
		 * Set avatar for song
		 */
		ImageView fileIcon = (ImageView) v.findViewById(R.id.file_icon);

		/*
		 * If is file
		 */
		if (listFiles.get(fileDir)){
			fileIcon.setImageResource(R.drawable.file_button);
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
		return linkOfFiles.get(position);
	}

	@Override
	public int getCount() {
		return linkOfFiles.size();
	}



}
