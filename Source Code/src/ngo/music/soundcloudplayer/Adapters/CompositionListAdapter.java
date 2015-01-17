package ngo.music.soundcloudplayer.Adapters;

import java.util.ArrayList;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.boundary.MusicPlayerMainActivity;
import ngo.music.soundcloudplayer.controller.SongController;
import ngo.music.soundcloudplayer.entity.OfflineSong;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.Constants;
import ngo.music.soundcloudplayer.service.MusicPlayerService;
import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.todddavies.components.progressbar.ProgressWheel;

public abstract class CompositionListAdapter extends ArrayAdapter<String>
		implements Constants.Categories {
	private View v;

	Context context;
	int resource;
	protected ArrayList<String> categories;
	CompositionViewHolder holder = null;

	public CompositionListAdapter(Context context, int resource) {
		super(context, resource);

		this.categories = getCategories();
		this.context = context;
		this.resource = resource;

	}

	/**
	 * get categories in list of item sets
	 * 
	 * @return categories
	 */
	protected abstract ArrayList<String> getCategories();

	/**
	 * get item from specific categories
	 * 
	 * @param cat
	 *            : category
	 * @return list of songs
	 */
	protected abstract ArrayList<Song> getItemsFromCat(String cat);

	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		v = convertView;
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.composition_view, parent, false);
			
			holder = new CompositionViewHolder(NUM_ITEM_IN_ONE_CATEGORY,v);
			v.setTag(holder);
		}else{
			holder = (CompositionViewHolder) v.getTag();
		}
		
		setLayoutInformation(holder,categories.get(position), v);

		return v;
	}

	/**
	 * @param position
	 * @param v
	 */
	public void setLayoutInformation(CompositionViewHolder holder,String catString, View v) {
		
		String[] catData = getSongsTitleFromCat(catString);

		/*
		 * Set song titles
		 */
		for (int i = 0; i < holder.items.length; i++) {
			if(i==0){
				holder.items[i].setText(catData[0]); //playlist name
			}else{

				String content = ""+i+". "+catData[i];
				holder.items[i].setText(content);
			}
			
		}
		

	}

	private String[] getSongsTitleFromCat(String catString) {
		// TODO Auto-generated method stub
		String[] temp = catString.split(String.valueOf('\1'));
		String[] result = new String[holder.items.length];
		for(int i=0;i<result.length;i++){
			result[i] = "";
			if(i<temp.length){
				result[i]=temp[i];
			}
		}
		return result;
	}
	/**
	 * get only title of category
	 */
	@Override
	public String getItem(int position) {
		String[] temp = categories.get(position).split(String.valueOf('\1'));
		return temp[0];
	}
	/**
	 * get title and content of a category
	 */
	public String getWholeItem(int position){
		return categories.get(position);
	}
	@Override
	public int getCount() {
		return categories.size();
	}
	public void update(){
		categories = getCategories();
		this.notifyDataSetChanged();
	}
	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}
	
	
}
