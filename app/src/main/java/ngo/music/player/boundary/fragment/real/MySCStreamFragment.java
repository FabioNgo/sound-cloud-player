package ngo.music.player.boundary.fragment.real;

import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import ngo.music.player.adapters.MySCStreamAdapter;
import ngo.music.player.boundary.MusicPlayerMainActivity;
import ngo.music.player.boundary.fragment.abstracts.NoRefreshListContentFragment;
import ngo.music.player.controller.SCUserController;
import ngo.music.player.controller.SongController;
import ngo.music.player.entity.Song;
import ngo.music.player.helper.Constants;
import ngo.music.player.R;

public class MySCStreamFragment extends NoRefreshListContentFragment implements
		Constants {


	public MySCStreamFragment() {
		// TODO Auto-generated constructor stub
		super();
		SCUserController soundCloudUserController = SCUserController
				.getInstance();
		// Token t = soundCloudUserController.getToken();

		// new loadSongBackground().execute();
	}

	@Override
	protected int getCategory() {
		// TODO Auto-generated method stub
		return STREAM;
	}

	@Override
	protected ArrayAdapter<?> getAdapter() {
		// TODO Auto-generated method stub

		ArrayList<Song> myStream = SongController.getInstance().getMyStream();
		return new MySCStreamAdapter(MusicPlayerMainActivity.getActivity()
				.getApplicationContext(), R.layout.list_view, myStream);
	}

	@Override
	protected boolean hasToolbar() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean hasLoadMore() {
		// TODO Auto-generated method stub
		return true;
	}
	// private class loadSongBackground extends AsyncTask<String , String,
	// String>{
	//
	// ArrayList<Song> myStream;
	// @Override
	// protected String doInBackground(String... params) {
	// // TODO Auto-generated method stub
	// SongController songController = SongController.getInstance();
	// try {
	// songController.loadMyStream();
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	//
	// return null;
	// }
	//
	// @Override
	// protected void onPostExecute(String result) {
	// // TODO Auto-generated method stub
	// if (myStream.size() == 0) {
	// /*
	// * Display the notice
	// */
	// TextView notification = (TextView) rootView.findViewById(R.id.notice);
	// notification.setVisibility(View.VISIBLE);
	// notification.setText("Do not have any song");
	//
	// }else{
	//
	// songsList.setAdapter(adapter);
	// }
	//
	// }
	//
	// }

	@Override
	protected void setUpToolBar(Toolbar toolbar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void updateToolbar(Toolbar toolbar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setUpLoadMore() {
		// TODO Auto-generated method stub
		
	}
}
