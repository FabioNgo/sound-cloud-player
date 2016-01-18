package ngo.music.player.Controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import ngo.music.player.helper.Constants;
import ngo.music.player.helper.States;
import ngo.music.player.service.MusicPlayerService;

public class IncomingCallController extends BroadcastReceiver implements Constants.MusicService {
	@Override
	public void onReceive(Context context, Intent intent) {
		// TELEPHONY MANAGER class object to register one listner
		TelephonyManager tmgr = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		//Create Listner
		MyPhoneStateListener PhoneListener = new MyPhoneStateListener();

		// Register listener for LISTEN_CALL_STATE
		tmgr.listen(PhoneListener, PhoneStateListener.LISTEN_CALL_STATE);
		
		
		
	}
	class MyPhoneStateListener extends PhoneStateListener{
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			Log.i("call state", String.valueOf(state));
			switch (state){
				case TelephonyManager.CALL_STATE_RINGING:
					if(States.musicPlayerState == MUSIC_PLAYING){

						MusicPlayerService.getInstance().pause();
						States.musicPlayerState = MUSIC_ON_PHONE;
					}
					break;
				case TelephonyManager.CALL_STATE_IDLE:
					if(States.musicPlayerState == MUSIC_ON_PHONE){

						MusicPlayerService.getInstance().playCurrentSong();
						States.musicPlayerState = MUSIC_PLAYING;
					}
					break;
				default:
					break;
			}
		}
	}
}