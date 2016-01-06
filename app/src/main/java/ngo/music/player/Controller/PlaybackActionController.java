package ngo.music.player.Controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ngo.music.player.helper.Constants;
import ngo.music.player.service.MusicPlayerService;

public class PlaybackActionController extends BroadcastReceiver implements Constants.MusicService {
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		// long id = intent.getLongExtra("id", -1);
		if (NOTI_ACTION_PLAY_PAUSE.equals(action)) {
			MusicPlayerService.getInstance().startPause();
			return;
		}
		if (NOTI_ACTION_NEXT.equals(action)) {
			MusicPlayerService.getInstance().playNextSong();
			return;
		}
		if (NOTI_ACTION_PREV.equals(action)) {
			MusicPlayerService.getInstance().playPreviousSong();
			return;
		}
		if (NOTI_ACTION_CANCEL.equals(action)) {
			MusicPlayerService.getInstance().release();
			return;
		}
		
		
		
	}
}