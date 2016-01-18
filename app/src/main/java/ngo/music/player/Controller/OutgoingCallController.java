package ngo.music.player.Controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import ngo.music.player.helper.Constants;
import ngo.music.player.helper.States;
import ngo.music.player.service.MusicPlayerService;

public class OutgoingCallController extends BroadcastReceiver implements Constants.MusicService {
	@Override
	public void onReceive(Context context, Intent intent) {
		// TELEPHONY MANAGER class object to register one listner

		if(States.musicPlayerState == MUSIC_NEW_SONG){

			MusicPlayerService.getInstance().pause();
			States.musicPlayerState = MUSIC_ON_PHONE;
			return;
		}
		if(States.musicPlayerState == MUSIC_ON_PHONE){

			MusicPlayerService.getInstance().playCurrentSong();
			States.musicPlayerState = MUSIC_RESUME;
			return;
		}
		
		
		
	}
}