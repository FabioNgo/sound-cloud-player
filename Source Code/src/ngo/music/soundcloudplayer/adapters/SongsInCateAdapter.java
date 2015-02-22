package ngo.music.soundcloudplayer.adapters;

import java.util.ArrayList;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.boundary.MusicPlayerMainActivity;
import ngo.music.soundcloudplayer.boundary.fragments.CategoryAddingFragment;
import ngo.music.soundcloudplayer.boundary.fragments.PlaylistAddingFragment;
import ngo.music.soundcloudplayer.controller.CategoryController;
import ngo.music.soundcloudplayer.controller.PlaylistController;
import ngo.music.soundcloudplayer.controller.SongController;
import ngo.music.soundcloudplayer.entity.OfflineSong;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.Constants;
import ngo.music.soundcloudplayer.service.MusicPlayerService;
import android.content.Context;
import android.support.v4.app.FragmentManager;
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

public abstract class SongsInCateAdapter extends ArrayAdapter<Song> implements
		Constants.Categories {
	private View v;
	private int type = -1;
	Context context;
	int resource;
	private boolean canRemoveItem;
	String cat;

	public static SongsInCateAdapter instance = null;
	private ArrayList<Song> songs;

	public SongsInCateAdapter(Context context, int resource,
			 String cat) {
		// TODO Auto-generated constructor stub
		super(context, resource);
		this.context = context;
		this.resource = resource;

		type = setType();
		canRemoveItem = setCanRemoveItem();
		this.cat = cat;
		songs = CategoryListAdapter.getInstance(type).getSongsFromCat(cat);
		
	}
	/**
	 * 
	 * @return if the category can remove children item
	 */
	protected abstract boolean setCanRemoveItem();
	
	/**
	 * 
	 * @return the type of category in Constant.Categories
	 */
	protected abstract int setType();
	
	
	/**
	 * 
	 * @param type
	 *            in Constants.Categories
	 * @param resource
	 * @param objects
	 * @param cate
	 * @return
	 */
	public static SongsInCateAdapter createInstance(int type, int resource,
			 String cate) {
		switch (type) {
		case PLAYLIST:
			return new SongsInPlaylistAdapter(
					MusicPlayerMainActivity.getActivity(), resource,
					cate);
		case ALBUM:
			return new SongsInAlbumsAdapter(
					MusicPlayerMainActivity.getActivity(), resource,
					cate);
		case ARTIST:
			return new SongsInArtistsAdapter(
					MusicPlayerMainActivity.getActivity(), resource,
					cate);
		case SC_PLAYLIST:
			return new SongsInMySCPlaylistAdapter(
					MusicPlayerMainActivity.getActivity(), resource,
					cate);
		case SC_SEARCH_PLAYLIST:
			return new SongsInSCSearchPlaylistAdapter(
					MusicPlayerMainActivity.getActivity(), resource,
					cate);
		default:
			return null;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		v = convertView;
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.song_in_cate, parent, false);

		}

		setLayoutInformation(position, v);
		return v;
	}

	/**
	 * @param position
	 * @param v
	 */
	private void setLayoutInformation(int position, View v) {

		final Song song = songs.get(position);
		/**
		 * Set avatar for song
		 */
		// ImageView avatar = (ImageView)
		// v.findViewById(R.id.lite_player_image);
		//
		// ImageLoader mImageLoader =
		// AppController.getInstance().getImageLoader();
		// avatar.setMinimumHeight(MainActivity.screenHeight/5);
		// avatar.setMinimumWidth(MainActivity.screenHeight/5);

		// avatar.setImageUrl(song.getArtworkUrl(), mImageLoader);
		/**
		 * set menu
		 */
		final ImageView menu = (ImageView) v.findViewById(R.id.song_cate_menu);
		menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PopupMenu popup = new PopupMenu(MusicPlayerMainActivity
						.getActivity(), menu);
				// Inflating the Popup using xml file
				popup.getMenuInflater().inflate(R.menu.song_in_cate_menu,
						popup.getMenu());
				if (!canRemoveItem) {
					popup.getMenu().findItem(R.id.song_cat_remove)
							.setVisible(false);
				}
				// registering popup with OnMenuItemClickListener
				popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

					@Override
					public boolean onMenuItemClick(MenuItem arg0) {
						// TODO Auto-generated method stub
						switch (arg0.getItemId()) {
						case R.id.song_cat_remove:
							CategoryController.getInstance(type)
									.removeSongFromCate(song, cat);
							break;
						case R.id.song_cat_add:
							ArrayList<Song> songs = new ArrayList<Song>();
							songs.add(song);
							CategoryAddingFragment playlistAddingFragment = new PlaylistAddingFragment(songs);
							playlistAddingFragment.show(MusicPlayerMainActivity.getActivity().getSupportFragmentManager(), "New Playlist");
							break;
							
						default:
							break;
						}

						return false;
					}
				});

				popup.show(); // showing popup menu
			}
		});
		/*
		 * Set title
		 */
		TextView title = (TextView) v.findViewById(R.id.song_cate_title);
		title.setText(song.getTitle());

		/*
		 * Set sub title
		 */
		TextView subtitle = (TextView) v.findViewById(R.id.song_cate_subtitle);
		subtitle.setText(song.getArtist() + " | " + song.getAlbum());

	}

	@Override
	public Song getItem(int position) {
		
		return songs.get(position);
	}

	@Override
	public int getCount() {
		return songs.size();
	}

	public static SongsInCateAdapter getInstance(int type) {
		// TODO Auto-generated method stub
		switch (type) {
		case PLAYLIST:
			
			return SongsInPlaylistAdapter.instance;

		default:
			break;
		}
		return null;
	}

	public void update() {
		// TODO Auto-generated method stub
		songs = CategoryListAdapter.getInstance(type).getSongsFromCat(cat);
		this.notifyDataSetChanged();
	}

}
