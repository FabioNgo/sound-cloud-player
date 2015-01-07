package ngo.music.soundcloudplayer.general;

import java.io.IOException;

import ngo.music.soundcloudplayer.boundary.MainActivity;
import ngo.music.soundcloudplayer.controller.SongController;
import ngo.music.soundcloudplayer.service.MusicPlayerService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class MusicPlayerBroadcastReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		Log.i("intent action = ", action);
		// long id = intent.getLongExtra("id", -1);
		if ("dismiss".equals(action)) {
			MusicPlayerService.getInstance().pause();
			MusicPlayerService.getInstance().cancelNoti();
			try {
				SongController.getInstance().storePlayingSong();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e("store",e.getMessage());
			}
		}
		
		
	}
}