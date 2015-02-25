package ngo.music.soundcloudplayer.adapters;

import java.io.IOException;
import java.util.ArrayList;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.ViewHolder.SCSongViewHolder;
import ngo.music.soundcloudplayer.api.ApiWrapper;
import ngo.music.soundcloudplayer.boundary.MusicPlayerMainActivity;
import ngo.music.soundcloudplayer.boundary.fragment.abstracts.CategoryAddingFragment;
import ngo.music.soundcloudplayer.boundary.fragment.real.SCPlaylistAddingFragment;
import ngo.music.soundcloudplayer.controller.SCUserController;
import ngo.music.soundcloudplayer.controller.SongController;
import ngo.music.soundcloudplayer.entity.SCSong;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.Constants;
import ngo.music.soundcloudplayer.service.MusicPlayerService;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.volley.api.AppController;

public abstract class ListSongAdapter extends LiteListSongAdapter implements
		Constants {

	protected ApiWrapper wrapper;
	long enqueue = 0;
	private SCSongViewHolder viewHolder = null;
	DownloadManager dm = null;

	private static final String ROOT_DIRECTORY = "/SoundCloudApp";

	public static ListSongAdapter instance = null;
	protected ArrayList<Song> songs;

	private ShareActionProvider mShareActionProvider;

	protected ListSongAdapter(Context context, int resource,
			ArrayList<Song> songs2, ApiWrapper wrapper) {
		super(context, resource);
		songs = songs2;
		this.wrapper = wrapper;

		// TODO Auto-generated constructor stub
	}

	// public static ListSongAdapter getInstance() {
	//
	// if (instance == null) {
	// instance = new
	// OnlineSongAdapter(MainActivity.getActivity().getApplicationContext(),
	// R.layout.tab_songs_view, songs);
	// }
	// return instance;
	// }

	

	/**
	 * Set layout infomation
	 * 
	 * @param position
	 *            positon of song in list
	 * @param viewHolder2
	 * @param v
	 *            rootview
	 */
	private void setLayoutInfomation(int position, SCSongViewHolder viewHolder) {
		final Song song = songs.get(position);
		//System.out.println ("SET LAYOUT = " + song.getId());
		NetworkImageView avatar = configLayoutAvatar(viewHolder, song);

		configTitleSong(viewHolder, song);

		configSongDetail(viewHolder, song, avatar);

		configPopupMenu(viewHolder, song);

		notifyDataSetChanged();
	}

	/**
	 * Config Song detail like playback count, likes count
	 * 
	 * @param viewHolder2
	 *            rootView
	 * @param song
	 *            song want to get detail
	 * @param avatar
	 *            avatar of song
	 */
	private void configSongDetail(SCSongViewHolder viewHolder, final Song song,
			NetworkImageView avatar) {

		/*
		 * Initial controller
		 */
		SCUserController userController = SCUserController.getInstance();

		/*
		 * Set song detail
		 */

		viewHolder.songDetail.getLayoutParams().height = MusicPlayerMainActivity.screenHeight / 20;

		/*
		 * Like this song
		 */

		viewHolder.likeCount.setText(((SCSong) song).getLikeCountString());

		viewHolder.likeCountLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (SCUserController.getInstance().likeSong(song)) {
					notifyDataSetChanged();
					Toast.makeText(getContext(), "You liked this song",
							Toast.LENGTH_LONG).show();
					// new updateFavoriteCounts(song, likeCount).execute();
				} else {
					Toast.makeText(getContext(),
							"You need to log in to like this song",
							Toast.LENGTH_LONG).show();
				}
			}
		});

		// ImageView likeIcon = (ImageView)
		// v.findViewById(R.id.likes_count_img);
		/*
		 * if not login and not like
		 */
		// if (userController.isLogin()){
		// //HttpResponse resp = wrapper.get(Request.to(ME_FAVORITE));
		// //String responseString =
		// if (userController.isLiked(song.getSoundcloudId())){
		// likeIcon.setImageResource(R.drawable.like_button);
		// }else{
		// likeIcon.setImageResource(R.drawable.love);
		// }
		// }else{
		// likeIcon.setImageResource(R.drawable.like_button);
		// }

		viewHolder.playBack.setText(((SCSong) song)
				.getPlaybackCountString());

		/*
		 * Download Song
		 */

		viewHolder.download.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new downloadSongBackground().execute(song);
			}
		});

		/*
		 * Share song
		 */

		viewHolder.share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				//System.out.println("SHARE");
				// MenuItem item = menu.findItem(R.id.menu_item_share);
				// mShareActionProvider = (ShareActionProvider)
				// item.getActionProvider();
				Intent sharingIntent = new Intent(
						android.content.Intent.ACTION_SEND);
				sharingIntent.setType("text/plain");
				// if (song instanceof OnlineSong){
				String shareBody = ((SCSong) song).getPermalinkUrl();
				String shareSubject = ((SCSong) song).getTitle();
				//System.out.println(shareBody);
				sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
						shareSubject);
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,
						shareBody);
				MusicPlayerMainActivity.getActivity().startActivity(
						Intent.createChooser(sharingIntent, "Share via "));
				// }

				return;
			}
		});

	}

	/**
	 * config Title of Song like Title, Author, Gerne
	 * 
	 * @param viewHolder2
	 *            RootView
	 * @param song
	 *            the song want to get info
	 */
	private void configTitleSong(SCSongViewHolder viewHolder, Song song) {
		/*
		 * Set title
		 */

		viewHolder.title.setText(song.getTitle());

		/*
		 * Set sub title
		 */

		viewHolder.subtitle.setText(song.getArtist());

		/*
		 * Set gerne
		 */

		if (song.getGenre() != null) {
			viewHolder.gerne.setText("#" + song.getGenre());
		}
	}

	/**
	 * Config popup Menu
	 * 
	 * @param viewHolder2
	 * @param song
	 */
	private void configPopupMenu(final SCSongViewHolder viewHolder,
			final Song song) {

		viewHolder.menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PopupMenu popup = new PopupMenu(MusicPlayerMainActivity
						.getActivity(), viewHolder.menu);
				// Inflating the Popup using xml file
				popup.getMenuInflater().inflate(R.menu.song_list_menu,
						popup.getMenu());

				// registering popup with OnMenuItemClickListener
				popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

					@Override
					public boolean onMenuItemClick(MenuItem arg0) {
						// TODO Auto-generated method stub
						

						return false;
					}
				});

				popup.show(); // showing popup menu
			}
		});
	}

	/**
	 * config avatar of Song
	 * 
	 * @param viewHolder2
	 *            rootView
	 * @param song
	 *            song want to get ava
	 * @return ImageView
	 */
	private NetworkImageView configLayoutAvatar(SCSongViewHolder viewHolder,
			Song song) {
		/**
		 * Set avatar for song
		 */

		ImageLoader mImageLoader = AppController.getInstance().getImageLoader();

		if (song.getArtworkUrl() != null) {
			viewHolder.avatar.setImageUrl(song.getArtworkUrl(), mImageLoader);
		}
		//notifyDataSetChanged();
		return viewHolder.avatar;
	}

	@Override
	public Song getItem(int position) {
		return songs.get(position);
	}

	@Override
	public int getCount() {
		return songs.size();
	}

	public ArrayList<Song> getSongs() {
		return songs;
	}

	/**
	 * Download song running in background
	 * 
	 * @author LEBAO_000
	 *
	 */
	private class downloadSongBackground extends
			AsyncTask<Song, String, String> {

		String result = "";

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			Toast.makeText(MusicPlayerMainActivity.getActivity(),
					"Start download...", Toast.LENGTH_LONG).show();
		}

		@Override
		protected String doInBackground(Song... params) {

			BroadcastReceiver receiver = new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
					String action = intent.getAction();
					if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
						long downloadId = intent.getLongExtra(
								DownloadManager.EXTRA_DOWNLOAD_ID, 0);
						Query query = new Query();

						query.setFilterById(enqueue);
						Cursor c = dm.query(query);
						if (c.moveToFirst()) {
							int columnIndex = c
									.getColumnIndex(DownloadManager.COLUMN_STATUS);
							if (DownloadManager.STATUS_SUCCESSFUL == c
									.getInt(columnIndex)) {

							}
						}
					}
				}

			};

			dm = (DownloadManager) MusicPlayerMainActivity.getActivity()
					.getSystemService(MusicPlayerMainActivity.DOWNLOAD_SERVICE);
			android.app.DownloadManager.Request request;

			try {
				request = new android.app.DownloadManager.Request(
						Uri.parse(params[0].getLink()));
				request.setTitle(params[0].getTitle());

				// OutputStream output = new FileOutputStream(outputName);

				// android.app.DownloadManager.Request downloadManager = new
				// DownloadManager.Request(uri);
				request.setDestinationInExternalPublicDir(ROOT_DIRECTORY,
						params[0].getTitle() + ".mp3");
				request.setNotificationVisibility(android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
				request.setMimeType("audio/mpeg");
				enqueue = dm.enqueue(request);
				result = "Download successfully";
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub

			Intent i = new Intent();
			i.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
			Toast.makeText(MusicPlayerMainActivity.getActivity(), result,
					Toast.LENGTH_LONG).show();
			// MusicPlayerMainActivity.getActivity().startActivity(i);

		}

	}

	private void setShareIntent(Intent shareIntent) {
		if (mShareActionProvider != null) {
			mShareActionProvider.setShareIntent(shareIntent);
		}
	}

	public void update(int mCategory) {
		// TODO Auto-generated method stub
		songs = SongController.getInstance().getOnlineSongs(mCategory);
		//System.out.println ("LIST SONG ADAPTER = " + songs.get(0).getId());
		//notifyDataSetChanged();
	}
	@Override
	public boolean onMenuItemClick(MenuItem arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getItemId()) {
		case R.id.list_addQueue:
			MusicPlayerService.getInstance().addSongToQueue(
					song);
			break;
		case R.id.list_playNext:
			MusicPlayerService.getInstance().addToNext(song);
			break;
		case R.id.list_addToPlaylist:
			ArrayList<Song> songs = new ArrayList<Song>();
			songs.add(song);
			CategoryAddingFragment playlistAddingFragment = new SCPlaylistAddingFragment(
					songs);
			playlistAddingFragment.show(MusicPlayerMainActivity
					.getActivity().getSupportFragmentManager(),
					"New Playlist");
			break;
		case R.id.list_delete:
			SongController.getInstance().deleteSong(song);
			break;
		default:
			break;
		}
		return false;
	}
	

}
