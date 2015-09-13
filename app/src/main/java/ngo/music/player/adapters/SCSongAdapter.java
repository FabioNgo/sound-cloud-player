package ngo.music.player.adapters;

import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.ShareActionProvider;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import ngo.music.player.boundary.MusicPlayerMainActivity;
import ngo.music.player.controller.SongController;
import ngo.music.player.entity.Song;
import ngo.music.player.helper.Constants;
import ngo.music.player.R;

public abstract class SCSongAdapter extends LiteListSongAdapter implements
		Constants {

	long enqueue = 0;
//	private SCSongViewHolder viewHolder = null;
	DownloadManager dm = null;

	private static final String ROOT_DIRECTORY = "/SoundCloudApp";

	public static SCSongAdapter instance = null;

	private ShareActionProvider mShareActionProvider;

	protected SCSongAdapter(Context context, int resource,
			ArrayList<Song> songs2) {
		super(context, resource);
		songs = songs2;

		// TODO Auto-generated constructor stub
	}
	@Override
	public int getSongMenuId() {
		// TODO Auto-generated method stub
		return R.menu.song_list_menu;
	}
	

//	/**
//	 * config avatar of Song
//	 * 
//	 * @param viewHolder2
//	 *            rootView
//	 * @param song
//	 *            song want to get ava
//	 * @return ImageView
//	 */
//	private NetworkImageView configLayoutAvatar(SCSongViewHolder viewHolder,
//			Song song) {
//		/**
//		 * Set avatar for song
//		 */
//
//		ImageLoader mImageLoader = AppController.getInstance().getImageLoader();
//
//		if (song.getArtworkUrl() != null) {
//			viewHolder.avatar.setImageUrl(song.getArtworkUrl(), mImageLoader);
//		}
//		//notifyDataSetChanged();
//		return viewHolder.avatar;
//	}

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
	
	

}
