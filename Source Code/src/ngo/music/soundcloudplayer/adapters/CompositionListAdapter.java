package ngo.music.soundcloudplayer.adapters;

import java.util.ArrayList;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.ViewHolder.CompositionViewHolder;
import ngo.music.soundcloudplayer.boundary.MusicPlayerMainActivity;
import ngo.music.soundcloudplayer.boundary.fragments.ListItemsInCompositionListFragment;
import ngo.music.soundcloudplayer.controller.CategoryController;
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
	int type = -1;
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

	public static CompositionListAdapter createNewInstance(int type) {
		// TODO Auto-generated method stub
		switch (type) {
		case PLAYLIST:
			return new PlaylistAdapter(MusicPlayerMainActivity.getActivity()
					.getApplicationContext(), R.layout.list_view);
		case ALBUM:
			return new AlbumAdapter(MusicPlayerMainActivity.getActivity()
					.getApplicationContext(), R.layout.list_view);
		default:
			return null;
		}

	}

	public static CompositionListAdapter getInstance(int type) {

		switch (type) {
		case PLAYLIST:
			if(PlaylistAdapter.instance == null){
				createNewInstance(type);
			}
			return PlaylistAdapter.instance;
		case ALBUM:
			if(AlbumAdapter.instance == null){
				createNewInstance(type);
			}
			return AlbumAdapter.instance;
		default:
			return null;
		}
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

			holder = new CompositionViewHolder(NUM_ITEM_IN_ONE_CATEGORY, v);
			v.setTag(holder);
		} else {
			holder = (CompositionViewHolder) v.getTag();
		}

		setLayoutInformation(holder, categories.get(position), v);

		return v;
	}

	/**
	 * @param position
	 * @param v
	 */
	public void setLayoutInformation(final CompositionViewHolder holder,
			String catString, View v) {

		String[] catData = getSongsTitleFromCat(catString);

		/*
		 * Set song titles
		 */
		for (int i = 0; i < holder.items.length; i++) {
			if (i == 0) {
				holder.items[i].setText(catData[0]); // playlist name
			} else {

				String content = "" + i + ". " + catData[i];
				holder.items[i].setText(content);
			}

		}

		/**
		 * Menu
		 */
		holder.menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PopupMenu popup = new PopupMenu(MusicPlayerMainActivity
						.getActivity(), holder.menu);
				// Inflating the Popup using xml file
				popup.getMenuInflater().inflate(R.menu.composition_list_item_menu,
						popup.getMenu());

				// registering popup with OnMenuItemClickListener
				popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

					@Override
					public boolean onMenuItemClick(MenuItem arg0) {
						String cat = (String) holder.items[0].getText();
						// TODO Auto-generated method stub
						switch (arg0.getItemId()) {
						case R.id.composition_list_item_shows:
							
							ListItemsInCompositionListFragment.createInstance(
									getItemsFromCat(cat), cat,type).show(
									MusicPlayerMainActivity.getActivity()
											.getSupportFragmentManager(),
									"Show songs in cate");
							break;
						case R.id.composition_list_item_delete:
							CategoryController.getInstance(type).removeCategory(cat);
						default:
							break;
						}

						return false;
					}
				});

				popup.show(); // showing popup menu
			}
		});
	}

	private String[] getSongsTitleFromCat(String catString) {
		// TODO Auto-generated method stub
		String[] temp = catString.split(String.valueOf('\1'));
		String[] result = new String[holder.items.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = "";
			if (i < temp.length) {
				result[i] = temp[i];
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
	public String getWholeItem(int position) throws IndexOutOfBoundsException {
		return categories.get(position);
	}

	@Override
	public int getCount() {
		return categories.size();
	}

	public void update() {
		categories = getCategories();
		this.notifyDataSetChanged();
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	public int getAdapterType() {
		// TODO Auto-generated method stub
		return type;
	}

	

}
