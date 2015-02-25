package ngo.music.soundcloudplayer.boundary.fragment.real;

import java.util.ArrayList;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.adapters.OfflineSongAdapter;
import ngo.music.soundcloudplayer.asynctask.UpdateNewSongBackgroundTask;
import ngo.music.soundcloudplayer.boundary.fragment.abstracts.ListContentFragment;
import ngo.music.soundcloudplayer.boundary.fragment.abstracts.RefreshListContentFragment;
import ngo.music.soundcloudplayer.controller.UIController;
import ngo.music.soundcloudplayer.entity.Song;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class OfflineSongsFragment extends RefreshListContentFragment {

	public ArrayAdapter<?> getAdapter() {
		// TODO Auto-generated method stub
		return OfflineSongAdapter.getInstance();
	}

	@Override
	protected int getCategory() {
		// TODO Auto-generated method stub
		return OFFLINE;
	}

	

}
