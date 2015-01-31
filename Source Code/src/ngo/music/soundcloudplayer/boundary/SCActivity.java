package ngo.music.soundcloudplayer.boundary;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.R.id;
import ngo.music.soundcloudplayer.R.menu;
import ngo.music.soundcloudplayer.controller.SongController;
import ngo.music.soundcloudplayer.controller.SCUserController;
import ngo.music.soundcloudplayer.general.Constants;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class SCActivity extends MusicPlayerMainActivity {

	public SCActivity() {
		// TODO Auto-generated constructor stub
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    // TODO Auto-generated method stub
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.main_menu, menu);
		menu.setGroupVisible(R.id.search_group, true);
		
		SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			
			@Override
			public boolean onQueryTextSubmit(String query) {
				// TODO Auto-generated method stub
				//switchTab(Constants.SoundCloudExploreConstant.SEARCH);
				
				SongController.getInstance().clearSearch();
				MusicPlayerMainActivity.query = query;
				SCActivity.type = SCActivity.SOUNDCLOUD_SEARCH;
				Intent i = new Intent(getActivity(), SCActivity.class);
				SCUserController soundCloudUserController = SCUserController.getInstance();
				
				Bundle bundle = soundCloudUserController.getBundle(soundCloudUserController.getCurrentUser());
				i.putExtra(USER, bundle);
				//i.putExtra(ME_FAVORITES,stringResponse);
				SCActivity.getActivity().finish();
				startActivity(i);
				return false;
			}
			
			@Override
			public boolean onQueryTextChange(String arg0) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		
		
		return true;
	}

}
