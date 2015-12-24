package ngo.music.player.adapters;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import ngo.music.player.Controller.MenuController;
import ngo.music.player.Model.Song;
import ngo.music.player.R;
import ngo.music.player.boundary.MusicPlayerMainActivity;
import ngo.music.player.helper.Constants;

public abstract class SongsInCateAdapter extends ArrayAdapter<Song> implements
		Constants.Models {
	public static SongsInCateAdapter instance = null;
	Context context;
	int resource;
	String cat;
	private View v;
	private int type = -1;
	private boolean canRemoveItem;
	private Song[] songs;

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
	 * @param type
	 *            in Constants.Models
	 * @param resource
	 * @param
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
		default:
			return null;
		}
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

	/**
	 * @return if the category can remove children item
	 */
	protected abstract boolean setCanRemoveItem();

	/**
	 * @return the type of category in Constant.Models
	 */
	protected abstract int setType();

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

		final Song song = songs[position];
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
//							((CategoryManager)ModelManager.getInstance(type))
//									.removeSongFromCate(song, cat);
							break;
						case R.id.song_cat_add:
							Song[] songs = new Song[1];
							songs[0] = song;
							MenuController.getInstance(songs).addToPlaylist();
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
		title.setText(song.getAttribute("title"));

		/*
		 * Set sub title
		 */
		TextView subtitle = (TextView) v.findViewById(R.id.song_cate_subtitle);
		subtitle.setText(song.getAttribute("artist") + " | " + song.getAttribute("artist"));

	}

	@Override
	public Song getItem(int position) {

		return songs[position];
	}

	@Override
	public int getCount() {
		return songs.length;
	}

	public void update() {
		// TODO Auto-generated method stub
		songs = CategoryListAdapter.getInstance(type).getSongsFromCat(cat);
		this.notifyDataSetChanged();
	}

}
